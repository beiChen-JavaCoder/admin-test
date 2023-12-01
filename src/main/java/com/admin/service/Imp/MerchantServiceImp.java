package com.admin.service.Imp;

import com.admin.component.IdManager;
import com.admin.config.MongoUtil;
import com.admin.domain.ResponseResult;
import com.admin.domain.dto.MerchantDto;
import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.User;
import com.admin.domain.vo.MerchantVo;
import com.admin.domain.vo.PageVo;
import com.admin.enums.MerchantTypeEnum;
import com.admin.exception.SystemException;
import com.admin.notification.Notification;
import com.admin.service.MerchantService;
import com.admin.service.UserService;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson2.JSONObject;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    private Notification notification;
    @Autowired
    private MongoUtil mongoUtil;
    @Autowired
    private IdManager merchantIdManager;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserService userService;

    @Override
    public ResponseResult<PageVo> findMerchantPage(MerchantVo merchantVo, Integer pageNum, Integer pageSize) {
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        // 创建查询条件
        List<Criteria> criteriaList = new ArrayList<>();
        if (StringUtils.hasText(merchantVo.getMerchantName())) {
            criteriaList.add(Criteria.where("name").regex(merchantVo.getMerchantName()));
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
        List<MerchantEntity> Merchants = gameTemplate.find(query, MerchantEntity.class);

        // 统计总数
        long total = gameTemplate.count(Query.of(query).limit(-1).skip(-1), MerchantEntity.class);
        //封装返回结果
        PageVo pageVo = new PageVo();
        pageVo.setTotal(total);
        pageVo.setRows(Merchants);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addMerchant(MerchantEntity merchantEntity) {
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        Long userId = SecurityUtils.getUserId();
        log.info("用戶id：" + userId + "正在操作商戶");
        //判断商户名不能为空或已存在
        if (!StringUtils.hasText(merchantEntity.getMerchantName())) {
            return ResponseResult.errorResult(500, "商户名不能为空");
        }
        if (!StringUtils.hasText(merchantEntity.getRatio() + "")) {
            return ResponseResult.errorResult(500, "提现比例不能为空");
        }
        long count = gameTemplate
                .count(Query
                        .query(Criteria
                                .where("name")
                                .is(merchantEntity.getMerchantName())), MerchantEntity.class);
        if (count != 0) {
            return ResponseResult.errorResult(500, "商户名已被使用");
        }

        merchantEntity.setId(merchantIdManager.getMaxMerchantId().incrementAndGet());
        MerchantEntity merchant = gameTemplate.insert(merchantEntity);
//        mongoUtil.closeGameClient();
        if (merchant != null) {
            MerchantDto merchantDto = new MerchantDto();
            merchantDto.setType(MerchantTypeEnum.LIST.getType());
            String result = notification.notificationMerchant(merchantDto);
            if ("0".equals(result)) {
                log.info("用戶id：" + userId + "请求发送成功");
                return ResponseResult.okResult(200, "请求发送成功");
            } else {
                log.info("用戶id：" + userId + "请求发送失败");
                return ResponseResult.errorResult(500, "请求发送失败");
            }

        } else {
            log.info("用戶id：" + userId + "新增商户失败，原因：数据库插入失败");
            return ResponseResult.okResult(200, "新增商户失败");
        }

//    private void inorder(MerchantVo merchantVo) {
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
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        Long userId = SecurityUtils.getUserId();

        Query query = new Query(Criteria.where("_id").in(ids));
        long deletedCount = gameTemplate.remove(query, MerchantEntity.class).getDeletedCount();
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setType(MerchantTypeEnum.LIST.getType());
        String remsg = notification.notificationMerchant(merchantDto);

        if (deletedCount > 0 && "0".equals(remsg)) {
            //判斷商戶已被刪除，并且對用戶信息進行更新
            Update update = new Update();
            update.set("merchantEnt_id", "");
            UpdateResult updateResult = mongoTemplate
                    .updateMulti(Query.query(Criteria.where("merchantEnt_id").in(ids)),
                            update, User.class);
            if (!updateResult.wasAcknowledged() && updateResult.getMatchedCount() > 0) {
                return ResponseResult.errorResult(500, "刪除用戶綁定商戶信息");
            }

            //更新用户绑定的商户信息
            boolean re = userService.unbindMerchant(ids);


            log.info("用户id：" + userId + "删除了商户" + merchantDto);
            log.info("删除成功", "并且发送通知");

            return ResponseResult.okResult(200, "删除商户成功");
        }
        log.info("用户id：" + userId + "删除商户失败,失败原因，删除结果：" + deletedCount + "通知回调：" + remsg + "删除对象：" + merchantDto);
        return ResponseResult.errorResult(500, "删除商户失败");
    }

    @Override
    public MerchantEntity findMerchantByUserId(Long userId) {

        Query query = Query.query(Criteria.where("_id").is(userId));
        Long merchantEntId = mongoTemplate.findOne(query, User.class).getMerchantEntId();
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        return
                gameTemplate.findOne(Query.query(Criteria
                        .where("_id").is(merchantEntId)), MerchantEntity.class);
    }

    @Override
    public ResponseResult findMerchantByUserId() {

        Long userId = SecurityUtils.getUserId();
        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        User user = mongoTemplate.findById(userId, User.class);
        MerchantEntity merchant = gameTemplate
                .findOne(Query.query(Criteria
                        .where("_id").is(user.getMerchantEntId())), MerchantEntity.class);

        if (merchant == null) {
            return ResponseResult.errorResult(500, "当前用户未绑定商户");
        }
        return ResponseResult.okResult(merchant);
    }

    @Override
    public ResponseResult updateMerchantByid(MerchantEntity merchant) {

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        MerchantEntity save = gameTemplate.save(merchant);
        if (save == null) {
            ResponseResult.errorResult(500, "更新失败");
        }
        return ResponseResult.okResult(200, "更新成功");


    }

    @Override
    public MerchantEntity findMerchantById(Long merchantId) {

        MongoTemplate gameTemplate = mongoUtil.getGameTemplate();
        return gameTemplate.findById(merchantId, MerchantEntity.class);


    }


}
