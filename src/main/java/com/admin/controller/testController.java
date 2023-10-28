package com.admin.controller;

import com.admin.domain.vo.NotifyVo;
import com.admin.enums.MerchantTypeEnum;
import com.admin.task.ScheduledTask;
import com.admin.utils.SignUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xqf
 * @version 1.0
 */
public class testController {

    void test(HttpServletRequest request, HttpServletResponse response){

        Map<String, Object> paramMap = SignUtil.switchMap(request.getParameterMap());
        HashMap<String, Object> resultMap = new HashMap<>();
        //异步通知
        //threadPoolExecutor.submit(new NotifyThread((String)paramMap.get("notifyUrl"), paramMap));
        resultMap.put("type",MerchantTypeEnum.LIST.getType());
        ScheduledTask.queue.offer(new NotifyVo("http://192.168.10.62:9998//hall/merchant/merchantListChange", resultMap));
    }

}
