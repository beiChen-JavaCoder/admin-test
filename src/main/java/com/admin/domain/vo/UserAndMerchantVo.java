package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndMerchantVo {

    private Long id;
    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;
    //用户类型：0代表普通用户，1代表管理员
    private String type;
    //账号状态（0正常 1停用）
    private String status;
    //创建人
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新人
    private Long updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //关联角色id数组，非user表字段
    private Long[] roleIds;
    /**
     * 商户名称
     */

    String MerchantName;
    /**
     * QQ号
     */

    long qq;
    /**
     * 微信账号
     */

    String wx;
    /**
     * 歪歪账号
     */

    String yy;
    /**
     * 提现比例(游戏币比例)
     */

    int ratio;





}
