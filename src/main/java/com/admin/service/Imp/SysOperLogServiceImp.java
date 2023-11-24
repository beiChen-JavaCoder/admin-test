package com.admin.service.Imp;

import com.admin.annotation.Log;
import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysOperLog;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.enums.BusinessType;
import com.admin.service.SysOperLogService;
import com.admin.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
@Slf4j
public class SysOperLogServiceImp implements SysOperLogService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;

    @Override
    public void insertOperlog(SysOperLog operLog) {
        //给予原子id
        long id = idManager.getMaxoperId().incrementAndGet();
        operLog.setOperId(id);
        //操作时间
        operLog.setOperTime(new Date());
        mongoTemplate.insert(operLog);


    }

    @Override
    public ResponseResult findPageOperLog(QueryParamsVo queryParamsVo) {

        Integer pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        Integer pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));
        JSONArray dateRange = (JSONArray) queryParamsVo.getObject("dateRange");
        //时间区间
        Date end = new Date();
        Date first = new Date();


        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.isNotEmpty((queryParamsVo.getAttribute("businessType")))) {
            criteriaList.add(Criteria.where("businessType").is(Integer.valueOf(queryParamsVo.getAttribute("businessType"))));
        }
        if (StringUtils.isNotEmpty(queryParamsVo.getAttribute("operIp"))) {
            criteriaList.add(Criteria.where("operIp").is(queryParamsVo.getAttribute("operIp")));
        }
        if (StringUtils.isNotEmpty(queryParamsVo.getAttribute("operName"))) {
            criteriaList.add(Criteria.where("operName").is(queryParamsVo.getAttribute("operName")));
        }
        if (StringUtils.isNotEmpty(queryParamsVo.getAttribute("status"))) {
            criteriaList.add(Criteria.where("status").is(Integer.valueOf(queryParamsVo.getAttribute("status"))));
        }
        if (StringUtils.isNotEmpty(queryParamsVo.getAttribute("title"))) {
            criteriaList.add(Criteria.where("title").is(queryParamsVo.getAttribute("title")));
        }
        if (!dateRange.isEmpty()) {

            String pattern = "yyyy-MM-dd HH:mm:ss";

            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                first = sdf.parse(String.valueOf(dateRange.get(0)));
                end = sdf.parse(String.valueOf(dateRange.get(1)));
            } catch (ParseException e) {
                e.getMessage();
            }
            criteriaList.add(Criteria.where("operTime").gte(first).lte(end));
        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        // 封装分页条件
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "_id"));
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
    @Override
    public ResponseResult cleanOperLog() {

        mongoTemplate.dropCollection(SysOperLog.class);

        return ResponseResult.okResult(200, "清空成功");
    }

    @Override
    public ResponseResult deleteOperLogByIds(Long[] operIds) {

        boolean wassed = mongoTemplate
                .remove(Query.query(Criteria.where("_id").in(operIds)),SysOperLog.class).wasAcknowledged();
        if (wassed) {
            return  ResponseResult.okResult(200,"删除成功");
        }
        return ResponseResult.errorResult(500,"删除失败");


    }

}
