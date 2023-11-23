package com.admin.service.Imp;

import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Player;
import com.admin.domain.entity.SysOperLog;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.SysOperLogService;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
public class SysOperLogServiceImp implements SysOperLogService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;

    @Override
    public void insertOperlog(SysOperLog operLog) {
        //给予原子id
        operLog.setOperId(idManager.getMaxoperId().incrementAndGet());
        //操作时间
        operLog.setOperTime(new Date());
        mongoTemplate.insert(operLog);


    }

    @Override
    public ResponseResult findPageOperLog(QueryParamsVo queryParamsVo) {
        int pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        int pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));

        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
//        if (!(playerRechargeVo.getId() == null)) {
//            criteriaList.add(Criteria.where("_id").is(playerRechargeVo.getId()));
//        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        // 封装分页条件
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("operTime"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);
        // 执行查询操作
        List<SysOperLog> operLogs = mongoTemplate.find(query, SysOperLog.class);
        // 统计总数
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), SysOperLog.class);

        //组装响应数据
        PageVo pageVo = new PageVo(operLogs, total);
        return ResponseResult.okResult(pageVo);

    }
}
