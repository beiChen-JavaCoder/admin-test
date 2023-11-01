package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.MerchantBean;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.component.IdManager;
import com.admin.service.MerchantService;
import com.admin.utils.BeanCopyUtils;
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
@Slf4j
@Service
public class MerchantServiceImp implements MerchantService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager merchantIdManager;

    @Override
    public ResponseResult<PageVo> findMerchantPage(MerchantVo merchantVo, Integer pageNum, Integer pageSize) {

        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.hasText(merchantVo.getName())) {
            criteriaList.add(Criteria.where("name").regex(merchantVo.getName()));
        }
        Criteria criteria = new Criteria();
        if (!criteriaList.isEmpty()) {
            criteria.andOperator(criteriaList.toArray(new Criteria[0]));
        }

        //封装分页条件
        //创建分页请求和排序，默认按_id升序
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("id"));
        // 创建查询对象
        Query query = Query.query(criteria).with(pageable);

        List<MerchantEntity> Merchants = mongoTemplate.find(query, MerchantEntity.class);

        // 统计总数
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), MerchantEntity.class);
        //封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(Merchants);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addMerchant(MerchantEntity merchantEntity ) {

        //判断商户名不能为空或已存在
        if (!StringUtils.hasText(merchantEntity.getMerchantName())) {
            return ResponseResult.errorResult(500, "商户名不能为空");
        }
        if (!StringUtils.hasText(merchantEntity.getRatio()+"")) {
            return ResponseResult.errorResult(500, "提现比例不能为空");
        }
        long count = mongoTemplate
                .count(Query
                        .query(Criteria
                                .where("name")
                                .is(merchantEntity.getMerchantName())), MerchantEntity.class);
        if (count!=0) {
            return ResponseResult.errorResult(500, "商户名已被使用");
        }


        merchantEntity.setId(merchantIdManager.getMaxMerchantId().incrementAndGet());
        if (mongoTemplate.insert(merchantEntity) != null) {

//            String result = Notification.notificationMerchant(new MerchantDto(MerchantTypeEnum.LIST.getType()));
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(500, "添加商户失败");
        }
    }

    private void inorder(MerchantVo merchantVo) {
//        MerchantOrderEntity merchantOrder = new MerchantOrderEntity();
//        merchantOrder.setNum(merchantVo.getNum());
//        merchantOrder.setRid(merchantVo.getId());
//        merchantOrder.setCreateTime(DateUtil.date());
//        merchantOrder.setPayeeAccount(merchantOrder.getPayeeAccount());
//        merchantOrder.setPayeeNickname(merchantVo.getName());
//        merchantOrder.setMerchantId(merchantVo.getId());
//
//        PageVo pageVo = new PageVo();
//        MerchantOrderEntity reMerchantOrder = mongoTemplate.insert(merchantOrder);
//        if (reMerchantOrder != null) {
//            MerchantDto merchantDto = new MerchantDto();
//            //todo 封装提现的用户对象
//            merchantDto.setUserId(new Long("1111"));
//            merchantDto.setChangeNum(merchantVo.getNum());
//            merchantDto.setType(MerchantTypeEnum.CASH.getType());
//            Notification.notificationMerchant(merchantDto);
//            //订单生成成功返回订单id
//            pageVo.setRows(new ArrayList(merchantDto.getMerchantId()));
//        } else {
//            log.error("订单生成失败");
//        }
    }

    @Override
    public ResponseResult removeMerchantById(List<Long> ids) {

        Query query = new Query(Criteria.where("_id").in(ids));
        if (mongoTemplate.remove(query, MerchantBean.class).getDeletedCount() > 1) {
            try {
//                Assert.isTrue("0".equals(Notification.notificationMerchant(new MerchantDto(1))));
            } catch (Exception e) {
                throw new SystemException(AppHttpCodeEnum.NOTIFICATION_NO);
            }
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.MERCHANT_REMOVE_NO);
        }

    }

    @Override
    public MerchantEntity findMerchantByUserId(Long userId) {
        Query query = Query.query(Criteria.where("_id").is(userId));
        Long merchantEntId = mongoTemplate.findOne(query, User.class).getMerchantEntId();

        return mongoTemplate
                .findOne(Query.query(Criteria
                        .where("_id").is(merchantEntId)), MerchantEntity.class);
    }


}
