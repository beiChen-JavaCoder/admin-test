package com.admin.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static cn.hutool.crypto.digest.DigestAlgorithm.MD5;

@Slf4j
public class SignUtil {

    //签名key
    private static final String SIGN_KEY = "9876543210";

    public static void main(String[] args) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("d", "4");
        paramMap.put("b", "2");
        paramMap.put("c", "3");
        paramMap.put("a", "1");
        log.info(getPostData(paramMap));
    }

    /**
     *
     * @param paramMap
     * @return
     */
    public static Map<String, Object> switchMap(Map<String, String[]> paramMap) {
        Map<String, Object> resultMap = new HashMap<>();
        for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
            resultMap.put(param.getKey(), param.getValue()[0]);
        }
        return resultMap;
    }

    /**
     *
     * @param paramMap
     * @return
     */
//    public static void isSignEquals(Map<String, Object> paramMap) {
//        String sign = (String)paramMap.get("sign");
//        String md5Str = getSign(paramMap);
//        if(!sign.equals(md5Str)) {
//            throw new HfbException(ResultCodeEnum.SIGN_ERROR);
//        }
//    }

    /**
     * 加密
     * @param paramMap
     * @return
     */
    public static String getSign(Map<String, Object> paramMap) {
        if(paramMap.containsKey("sign")) {
            paramMap.remove("sign");
        }
        TreeMap<String, Object> sorted = new TreeMap<>(paramMap);
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, Object> param : sorted.entrySet()) {
            str.append(param.getValue()).append("|");
        }
        str.append(SIGN_KEY);
        String md5Str = SecureUtil.md5(str.toString());
        return md5Str;
    }

    /**
     *
     * @param paramMap
     * @return
     */
    public static String getPostData(Map<String, Object> paramMap) {
        TreeMap<String, Object> sortedParamMap = new TreeMap<>(paramMap);
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, Object> param : sortedParamMap.entrySet()) {
            paramStr.append(param.getKey()).append("=")
                    .append(param.getValue()).append("&");
        }
        String md5Str = getSign(paramMap);
        paramStr.append("sign=").append(md5Str);
        String postData = paramStr.toString().trim();
        log.info("加密后参数：" + postData);
        return postData;
    }

    public static String sendRequest(Map<String, Object> paramMap, String url){
        String result = "";
        try {
            String postdata = getPostData(paramMap);
            log.info(String.format("--> 发送请求到游戏：post data %1s", postdata));
            byte[] reqData = postdata.getBytes("utf-8");
            byte[] respdata = HttpUtils.doPost(url,reqData);
            result = new String(respdata);
            log.info(String.format("--> 游戏应答结果：result data %1s", result));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
