package com.admin.service.Imp;

import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.CoinLog;
import com.admin.domain.entity.GameInfo;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.notification.Notification;
import com.admin.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
public class GameServiceImp implements GameService {

    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private Notification notification;


    @Override
    public ResponseResult findGamePage(QueryParamsVo queryParamsVo) {
        String gameName = queryParamsVo.getAttribute("gameName");
        Integer pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        Integer pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();

        Criteria criteria = new Criteria();
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
    public ResponseResult downlineGame() {

        //发起修改关闭游戏通知
//        notification
        
        return null;
    }
}
