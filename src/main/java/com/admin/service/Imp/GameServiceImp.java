package com.admin.service.Imp;

import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.CoinLog;
import com.admin.domain.entity.GameInfo;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.notification.Notification;
import com.admin.service.GameService;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class GameServiceImp implements GameService {

    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private Notification notification;


    @Override
    public ResponseResult findGamePage(QueryParamsVo queryParamsVo) {


        Integer pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        Integer pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();

        Criteria criteria = new Criteria();
        ArrayList<Criteria> criteriaList = new ArrayList<>();
        //游戏名称
        if (StringUtils.hasText(queryParamsVo.getAttribute("gameName"))) {
            String gameName = queryParamsVo.getAttribute("gameName");
            criteriaList.add(Criteria.where("gameName").is(gameName));
        }
        //状态
        if (StringUtils.hasText(queryParamsVo.getAttribute("active"))) {
            Boolean active = Boolean.valueOf(queryParamsVo.getAttribute("active"));
            criteriaList.add(Criteria.where("IsActive").is(active));
        }
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("_id"));
        Query query = Query.query(criteria).with(pageable);

        List<GameInfo> gameInfos = gameTemplate.find(query, GameInfo.class);
        //查询总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), GameInfo.class);

        //封装返回对象
        PageVo pageVo = new PageVo();
        pageVo.setRows(gameInfos);
        pageVo.setTotal(total);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult turnGame(Long gameId) {

        //发起修改关闭游戏通知
        boolean remsg = notification.updateGame(gameId, 0);
        if (remsg) {
            log.info("关闭游戏id:" + gameId + "成功");
            return ResponseResult.okResult(200, "关闭游戏成功");
        }
        log.error("关闭游戏id:" + gameId + "成功(通知失败)");
        return ResponseResult.errorResult(500, "关闭游戏失败(通知失败)");


    }

    @Override
    public ResponseResult deletGame(Long gameId) {

        //发情删除游戏通知
        boolean reDelet = notification.updateGame(gameId, 1);

        if (reDelet) {
            log.info("关闭游戏id:" + gameId + "成功");
            return ResponseResult.okResult(200, "删除成功！");
        }
        log.error("关闭游戏id:" + gameId + "成功(通知失败)");
        return ResponseResult.errorResult(500, "删除失败");
    }
}
