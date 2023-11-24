package com.admin.service.Imp;

import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysLogininfor;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.SysLogininforService;
import com.admin.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.client.result.DeleteResult;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@Service
public class SysLogininforServiceImp implements SysLogininforService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;


    @Override
    public ResponseResult findLogininforPage(QueryParamsVo queryParamsVo) {
        Integer pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        Integer pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));
        String ipaddr = queryParamsVo.getAttribute("ipaddr");
        String userName = queryParamsVo.getAttribute("userName");
        String status = queryParamsVo.getAttribute("status");
        JSONArray dateRange = (JSONArray) queryParamsVo.getObject("dateRange");

        //创建查询参数
        ArrayList<Criteria> criteriaList = new ArrayList<>();
        Criteria criteria = new Criteria();

        //组装参数
        if (StringUtils.isNotEmpty(ipaddr)) {
            criteriaList.add(Criteria.where(("ipaddr")).is(String.valueOf(ipaddr)));
        }
        if (StringUtils.isNotEmpty(status)) {
            criteriaList.add(Criteria.where(("status")).is(String.valueOf(status)));
        }
        if (StringUtils.isNotEmpty(userName)) {
            criteriaList.add(Criteria.where(("userName")).is(String.valueOf(userName)));
        }
        if (!dateRange.isEmpty()) {

            String pattern = "yyyy-MM-dd HH:mm:ss";
            Date first = new Date();
            Date end = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                first = sdf.parse(String.valueOf(dateRange.get(0)));
                end = sdf.parse(String.valueOf(dateRange.get(1)));
            } catch (ParseException e) {
                e.getMessage();
            }
            criteriaList.add(Criteria.where("loginTime").gte(first).lte(end));
        }

        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        //组装分页参数
        Pageable pageable = PageRequest
                .of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "_id"));

        //查询总参数
        Query query = Query.query(criteria).with(pageable);
        //执行查询
        List<SysLogininfor> logininfors = mongoTemplate.find(query, SysLogininfor.class);
        //查询总数
        long count = mongoTemplate.count(query, SysLogininfor.class);
        PageVo pageVo = new PageVo();
        pageVo.setTotal(count);
        pageVo.setRows(logininfors);


        return ResponseResult.okResult(pageVo);
    }

    @Override
    public void insertLogininfor(SysLogininfor logininfor) {
        logininfor.setInfoId(idManager.getMaxLogininforId().incrementAndGet());
        logininfor.setLoginTime(new Date());
        mongoTemplate.insert(logininfor);


    }

    @Override
    public ResponseResult deleteLogininforByIds(Long[] infoIds) {


        DeleteResult result = mongoTemplate
                .remove(Query.query(Criteria.where("_id")
                        .in(infoIds)), SysLogininfor.class);
        if (result.wasAcknowledged() && result.getDeletedCount() > 0) {
            return ResponseResult.okResult(200, "删除成功");
        }
        return ResponseResult.errorResult(500, "删除失败");


    }

    @Override
    public ResponseResult cleanLogininfor() {

        try {
            mongoTemplate.dropCollection(SysLogininfor.class);
        } catch (Exception e) {

            return ResponseResult.errorResult(500, "清空出现异常,请联系管理员");

        }

        return ResponseResult.okResult(200, "清空成功");
    }


}
