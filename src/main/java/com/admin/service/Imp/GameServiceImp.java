package com.admin.service.Imp;

import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.CoinLog;
import com.admin.domain.entity.GameInfo;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.notification.Notification;
import com.admin.service.GameService;
import com.admin.utils.PageUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<JSONObject> gameList = notification.findGameList();
        if (gameList.isEmpty()) {
            return ResponseResult.errorResult(500, "游戏端异常");
        }
        PageUtils pageUtils = new PageUtils();
        List<JSONObject> games = pageUtils.getPages(gameList, pageNum, pageSize);

        List<JSONObject> reGames = games.stream()
                .filter(game -> {
                    String gameName = queryParamsVo.getAttribute("gameName");
                    String active = queryParamsVo.getAttribute("active");

                    boolean isGameNameMatched = gameName == null || gameName.isEmpty() || gameName.equals(game.getString("gameName"));
                    boolean isActiveMatched = active == null || active.equals(game.getString("active"));

                    return (isGameNameMatched && isActiveMatched) ||
                            (isGameNameMatched && active == null) ||
                            (gameName == null && isActiveMatched) ||
                            (gameName == null && active == null);
                })
                .collect(Collectors.toList());

        //封装返回对象
        PageVo pageVo = new PageVo();
        pageVo.setRows(reGames);
        pageVo.setTotal(Long.valueOf(gameList.size()));

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
