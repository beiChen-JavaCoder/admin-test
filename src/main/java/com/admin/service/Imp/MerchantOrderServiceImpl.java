package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;
import com.admin.service.MerchantOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
public class MerchantOrderServiceImpl implements MerchantOrderService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public ResponseResult<PageVo> findOrderPage(MerchantOrderVo merchantOrderVo) {

        ArrayList<Criteria> criteriaList = new ArrayList<>();
        //创建查询条件
        if (!StringUtils.hasText(merchantOrderVo.getRid() + "")) {
            criteriaList.add(Criteria.where("rid").regex(merchantOrderVo.getRid() + ""));
        }
//        if (StringUtils.hasText(merchantOrderVo.getVoucher())){
//            criteriaList.add(Criteria.where("voucher"))
//        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }
        // 创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(merchantOrderVo.getPageNum() - 1, merchantOrderVo.getPageSize(), Sort.by("id"));

        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);

        // 使用 MongoTemplate 执行查询：
        List<MerchantOrderEntity> merchantOrders = mongoTemplate.find(query, MerchantOrderEntity.class);

        //统计总数
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), User.class);
//        long total = (long) merchantOrders.size();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(merchantOrders);
        return ResponseResult.okResult(pageVo);

    }
}
