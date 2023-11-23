package com.admin.service.Imp;

import com.admin.component.IdManager;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.SysLogininfor;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.SysLogininforService;
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
public class SysLogininforServiceImp implements SysLogininforService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;


    @Override
    public ResponseResult findLogininforPage(QueryParamsVo queryParamsVo) {
        Integer pageNum = Integer.valueOf(queryParamsVo.getAttribute("pageNum"));
        Integer pageSize = Integer.valueOf(queryParamsVo.getAttribute("pageSize"));


        //创建查询参数
        ArrayList<Criteria> criteriaList = new ArrayList<>();
        Criteria criteria = new Criteria();

        //组装参数
//        if (!(属性 == null)) {
//            criteriaList.add(Criteria.where(("字段")).is(属性));
//        }

        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        //组装分页参数
        Pageable pageable = PageRequest
                .of(pageNum-1,pageSize, Sort.by(Sort.Direction.DESC,"_id"));

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




}
