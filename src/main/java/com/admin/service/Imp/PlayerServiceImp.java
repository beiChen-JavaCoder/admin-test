package com.admin.service.Imp;

import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.CoinLog;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.Player;
import com.admin.domain.entity.User;
import com.admin.domain.vo.*;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.notification.Notification;
import com.admin.service.PlayerService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class PlayerServiceImp implements PlayerService {

    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Notification notification;

    @Override
    public ResponseResult<PageVo> findPlayerRechargePage(PlayerRechargeVo playerRechargeVo, Integer pageNum, Integer pageSize) {

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();

        Long merchantEntId = SecurityUtils.getLoginUser().getUser().getMerchantEntId();


        if ((merchantEntId == null || merchantEntId == 0) && !SecurityUtils.isAdmin()) {
            return ResponseResult.errorResult(500, "用户未绑定商户，请联系管理员");
        }

        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();

        //筛选绑定了商户渠道的玩家
        if (merchantEntId!=null&&merchantEntId != 0L ){
            Integer channel = gameTemplate.findById(merchantEntId, MerchantEntity.class).getChannel();
            criteriaList.add(Criteria.where("channel").is(channel));
        }
        if (!(playerRechargeVo.getId() == null)) {
            criteriaList.add(Criteria.where("_id").is(playerRechargeVo.getId()));
        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        //只查询同渠道的玩家列表
        criteriaList.add(Criteria.where("chann").is(playerRechargeVo.getId()));

        // 封装分页条件
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("_id"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);
        // 执行查询操作
        List<Player> roleInfos = gameTemplate.find(query, Player.class);
        // 统计总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), Player.class);

        // 封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(roleInfos);

        log.info("查询玩家待充值列表：" + roleInfos);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateRoleGold(RechargeVo rechargeVo) {

        //获取登录用户查询绑定的商户
        Long userId = SecurityUtils.getUserId();
        MerchantDto merchantDto = new MerchantDto();
        Long merchantEntId = mongoTemplate.findById(userId, User.class).getMerchantEntId();
        merchantDto.setMerchantId(merchantEntId);
        merchantDto.setType(MerchantTypeEnum.CHARGE.getType());
        merchantDto.setChangeNum(rechargeVo.getNum());
        merchantDto.setUserId(rechargeVo.getRid());
        String reRecharge = notification.notificationMerchant(merchantDto);
        try {
            if (String.valueOf(AppHttpCodeEnum.NOTIFICATION_SUCCESS.getCode()).equals(reRecharge)) {
                return ResponseResult.okResult(200, "充值成功");
            } else {

                return ResponseResult.errorResult(500, "充值失败,游戏异常");
            }
        } finally {
            log.info("商户" + merchantEntId + "对玩家" + merchantDto.getUserId() + "充值" + merchantDto.getChangeNum());
        }
    }

    @Override
    public ResponseResult<PageVo> findPlayerPage(@RequestBody PlayerVo queryParamsVo) {
        Integer pageNum = queryParamsVo.getPageNum();
        Integer pageSize = queryParamsVo.getPageSize();

        List<Criteria> criteriaList = new ArrayList<>();
        //添加搜索条件
        if (queryParamsVo.getRid() > 0) {
            criteriaList.add(Criteria.where("_id").is(queryParamsVo.getRid()));
        }
        if (queryParamsVo.getIsOnline() != null) {
            criteriaList.add(Criteria.where("isOnline").is(queryParamsVo.getIsOnline()));
        }
        JSONObject gold = queryParamsVo.getGold();
        if (gold.get("min") != null && gold.get("max") != null) {
            Long min = Long.valueOf(String.valueOf(gold.get("min")));
            Long max = Long.valueOf(String.valueOf(gold.get("max")));
            criteriaList.add(Criteria.where("gold").gte(min).lte(max));
        }
        String[] lastloginTime = queryParamsVo.getLastlogin_time();
        if (Arrays.toString(lastloginTime).isEmpty()) {
            Long min = Long.valueOf(String.valueOf(lastloginTime[0]));
            Long max = Long.valueOf(String.valueOf(lastloginTime[1]));
            criteriaList.add(Criteria.where("lastlogin_time").gte(min).lte(max));
        }
        JSONObject winningLosing = queryParamsVo.getWinningLosing();
        if (winningLosing.get("min") != null && winningLosing.get("max") != null) {
            Long min = Long.valueOf(String.valueOf(winningLosing.get("min")));
            Long max = Long.valueOf(String.valueOf(winningLosing.get("max")));
            criteriaList.add(Criteria.where("winningLosing").gte(min).lte(max));
        }
        JSONObject recharge = queryParamsVo.getRecharge();
        if (recharge.get("min") != null && recharge.get("max") != null) {
            Long min = Long.valueOf(String.valueOf(recharge.get("min")));
            Long max = Long.valueOf(String.valueOf(recharge.get("max")));
            criteriaList.add(Criteria.where("recharge").gte(min).lte(max));
        }

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        // 创建查询条件
//        List<Criteria> criteriaList = new ArrayList<>();
//        if (!(playerRechargeVo.getId() ==null)) {
//            criteriaList.add(Criteria.where("_id").is(playerRechargeVo.getId()));
//        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        // 封装分页条件
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("_id"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);
        // 执行查询操作
        List<Player> playerList = gameTemplate.find(query, Player.class);
        // 统计总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), Player.class);

        // 封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(playerList);

        log.info("查询玩家列表列表：" + playerList);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult findFlowPage(QueryParamsVo queryParamsVo) {
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        //分页参数
        int pageNum = Integer.parseInt(queryParamsVo.getAttribute("pageNum"));
        int pageSize = Integer.parseInt(queryParamsVo.getAttribute("pageSize"));
        ArrayList<Criteria> criteriaList = new ArrayList<>();
        Criteria criteria = new Criteria();


        //添加搜索条件
        //玩家id
        String rid = queryParamsVo.getAttribute("rid");
        if (StringUtils.hasText(rid)) {
            Long id = Long.valueOf(rid);
            criteriaList.add(Criteria.where("rid").is(id));
        }
        //游戏id
        if (StringUtils.hasText(queryParamsVo.getAttribute("gameId"))) {
            Long gameId = Long.valueOf(queryParamsVo.getAttribute("gameId"));
            criteriaList.add(Criteria.where("gameid").is(gameId));
        }
        //排除掉充值增加，税收扣除，提现扣除等日志
        criteriaList.add(Criteria.where("key").ne("充值增加"));
        criteriaList.add(Criteria.where("key").ne("税收扣除"));
        criteriaList.add(Criteria.where("key").ne("提现扣除"));

        //添加时间区间搜索条件
        if (queryParamsVo.getObject("sectionTime") != null) {
            JSONArray sectionTime = (JSONArray) queryParamsVo.getObject("sectionTime");
            ArrayList<String> strings = new ArrayList<>();
            for (Object s : sectionTime) {

                // 创建 DateTimeFormatter 对象，指定日期时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                // 将时间字符串解析为 LocalDateTime 对象
                LocalDateTime utcDateTime = LocalDateTime.parse(String.valueOf(s), formatter);

                // 获取中国时区
                ZoneId chinaZone = ZoneId.of("Asia/Shanghai");

                // 将 LocalDateTime 转换为 ZonedDateTime 对象，并应用中国时区
                ZonedDateTime chinaDateTime = utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(chinaZone);

                // 格式化为中国时区的时间字符串
                String chinaTimeString = chinaDateTime.format(formatter);


                strings.add(chinaTimeString);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
            try {
                //解析查询日期为时间戳判断范围
                LocalDateTime startDate = LocalDateTime.parse(strings.get(0), formatter);
                LocalDateTime endDate = LocalDateTime.parse(strings.get(1), formatter);
                long startStamp = startDate.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                long endStamp = endDate.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                criteriaList.add(Criteria.where("time_stamp").gte(startStamp).lte(endStamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ;


        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }
        //封装分页参数
        //创建分页请求和排序，默认按time_stamp升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "time_stamp"));
        //创建查询对象
        Query query = Query.query(criteria).with(pageable);
        List<CoinLog> coinLogs = gameTemplate.find(query, CoinLog.class);

        //对返回对象进行处理
        List<FlowVo> flowVos = BeanCopyUtils.copyBeanList(coinLogs, FlowVo.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 遍历 coinLogs 和 revenueVos
        for (int i = 0; i < coinLogs.size() && i < flowVos.size(); i++) {
            CoinLog coinLog = coinLogs.get(i);
            FlowVo flowVo = flowVos.get(i);

            // 将 coinLog 的 timeStamp 复制到 revenue 的 sectionTime
            flowVo.setCreateTime(dateFormat.format(new Date(coinLog.getTimeStamp())));
        }
        //查询总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), CoinLog.class);
        //封装数据返回
        PageVo pageVo = new PageVo();
        pageVo.setRows(flowVos);
        pageVo.setTotal(total);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult findRevenuePage(RevenueVo revenueVo) {

        int pageNum = revenueVo.getPageNum();
        int pageSize = revenueVo.getPageSize();


        ArrayList<Criteria> criteriaList = new ArrayList<>();

        Criteria criteria = new Criteria();
        //过滤税收
        criteria.and("key").is("税收扣除");

        //添加时间区间搜索条件
        JSONArray searchTime = (JSONArray) revenueVo.getSearchTime().get("searchTime");
        //添加时间区间搜索条件
        if (searchTime != null) {

            ArrayList<String> dates = new ArrayList<>();
            for (Object s : searchTime) {
                // 创建 DateTimeFormatter 对象，指定日期时间格式
                DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                // 将时间字符串解析为 LocalDateTime 对象
                LocalDateTime utcDateTime = LocalDateTime.parse(String.valueOf(s), formatter);

                // 获取中国时区
                ZoneId chinaZone = ZoneId.of("Asia/Shanghai");

                // 将 LocalDateTime 转换为 ZonedDateTime 对象，并应用中国时区
                ZonedDateTime chinaDateTime = utcDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(chinaZone);

                // 格式化为中国时区的时间字符串
                String chinaTimeString = chinaDateTime.format(formatter);
                dates.add(chinaTimeString);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

            try {
                //解析查询日期为时间戳判断范围
                LocalDateTime startDate = LocalDateTime.parse(dates.get(0), formatter);
                LocalDateTime endDate = LocalDateTime.parse(dates.get(1), formatter);
                long startStamp = startDate.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                long endStamp = endDate.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
                criteriaList.add(Criteria.where("time_stamp").gte(startStamp).lte(endStamp));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }
        //封装分页参数
        //创建分页请求和排序，默认按time_stamp升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "time_stamp"));
        //创建查询对象
        Query query = Query.query(criteria).with(pageable);
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        List<CoinLog> coinLogs = gameTemplate.find(query, CoinLog.class);
        List<RevenueVo> revenueVos = BeanCopyUtils.copyBeanList(coinLogs, RevenueVo.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 定义日期格式
        // 遍历 coinLogs 和 revenueVos
        for (int i = 0; i < coinLogs.size() && i < revenueVos.size(); i++) {
            CoinLog coinLog = coinLogs.get(i);
            RevenueVo revenue = revenueVos.get(i);

            // 将 coinLog 的 timeStamp 复制到 revenue 的 sectionTime
            revenue.setSectionTime(dateFormat.format(new Date(coinLog.getTimeStamp())));
        }
        //查询总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), CoinLog.class);

        //封装数据返
        PageVo pageVo = new PageVo();
        pageVo.setRows(revenueVos);
        pageVo.setTotal(total);

        return ResponseResult.okResult(pageVo);
    }

}
