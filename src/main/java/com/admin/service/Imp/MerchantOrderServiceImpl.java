package com.admin.service.Imp;

import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.MerchantOrderEntity;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantOrderVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.MerchantOrderTypeEnum;
import com.admin.enums.MerchantTypeEnum;
import com.admin.enums.OrderAccountTypeEnum;
import com.admin.notification.Notification;
import com.admin.service.MerchantOrderService;
import com.admin.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Xqf
 * @version 1.0
 */
@Service

public class MerchantOrderServiceImpl implements MerchantOrderService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private Notification notification;


    @Override
    public ResponseResult<PageVo> findOrderPage(MerchantOrderVo merchantOrderVo) {

        ArrayList<Criteria> criteriaList = new ArrayList<>();
        //创建查询条件
        if (!(merchantOrderVo.getRid() == null)) {
            criteriaList.add(Criteria.where("rid").is(merchantOrderVo.getRid()));
        }
        //创建商户id查询条件
        Long userId = SecurityUtils.getUserId();
        Long merchantEntId = Objects.requireNonNull(mongoTemplate.findById(userId, User.class), "用户不存在！").getMerchantEntId();
        criteriaList.add(Criteria.where(("merchantId")).is(merchantEntId));
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
        long total = mongoTemplate.count(query,MerchantOrderEntity.class);
        merchantOrders.forEach(merchantOrder -> {
            Integer status = merchantOrder.getStatus();
            int accountType = merchantOrder.getAccountType();
            //判断账户类型
            if (accountType == OrderAccountTypeEnum.WECHAT.getType()) {
                merchantOrder.setSAccountType(OrderAccountTypeEnum.WECHAT.getMsg());
            } else if (accountType == OrderAccountTypeEnum.ALIPAY.getType()) {
                merchantOrder.setSAccountType(OrderAccountTypeEnum.ALIPAY.getMsg());
            } else {
                merchantOrder.setSAccountType(OrderAccountTypeEnum.Bank.getMsg());

            }
            //判断用户状态
            if (status == MerchantOrderTypeEnum.UNTREATED.getType()) {
                merchantOrder.setSStatus(MerchantOrderTypeEnum.UNTREATED.getMsg());
            } else if (status == MerchantOrderTypeEnum.processed.getType()) {
                merchantOrder.setSStatus(MerchantOrderTypeEnum.processed.getMsg());

            } else if (status == MerchantOrderTypeEnum.REFUSE.getType()) {
                merchantOrder.setSStatus(MerchantOrderTypeEnum.REFUSE.getMsg());

            } else if (status == MerchantOrderTypeEnum.PROCESSING_TIMEOUT.getType()) {
                merchantOrder.setSStatus(MerchantOrderTypeEnum.PROCESSING_TIMEOUT.getMsg());

            }
            //转换时间戳
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            merchantOrder.setTimeOutDate(dateFormat.format(new Date(merchantOrder.getTimeOut())));
            merchantOrder.setCreateTimeDate(dateFormat.format(new Date(merchantOrder.getCreateTime())));
        });
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(merchantOrders);
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public ResponseResult update(MerchantOrderEntity merchantOrder) {

        if (!(merchantOrder.getStatus() == MerchantOrderTypeEnum.UNTREATED.getType())) {
            return ResponseResult.errorResult(500, "订单已处理或已拒绝");
        }

        if (mongoTemplate.save(merchantOrder) != null) {
            //封装通知信息
            MerchantDto merchantDto = new MerchantDto();
            merchantDto.setType(MerchantTypeEnum.CASH.getType());
            merchantDto.setMerchantId(merchantOrder.getMerchantId());
            merchantDto.setChangeNum(merchantOrder.getNum());
            merchantDto.setUserId(merchantOrder.getRid());
            merchantDto.setOderId(merchantOrder.getId());
//            String reCode = notification.notificationMerchant(merchantDto);
//            if ("1".equals(reCode)) {
//                //返回1通知失败
//                return ResponseResult.errorResult(500, " 通知失败");
//
//            }
            return ResponseResult.okResult(200, "审核提交完成");
        }
        return ResponseResult.errorResult(500, "审核提交失败");


    }
}
