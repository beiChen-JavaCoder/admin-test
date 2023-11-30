package com.admin.domain.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户表(User)表实体类
 *
 * @author xqf
 * @since 2022-02-03 16:25:40
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("sys_user")
@ApiModel("后台用户信息实体类")
public class User implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.annotation.Id
    private Long id;
    //用户名
    @Field("user_name")
    private String userName;
    @Field("nick_name")
    //昵称
    private String nickName;
    //密码
    @Field("password")
    private String password;
    //用户类型：0代表普通用户，1代表管理员
    @Field("type")
    private String type;
    //账号状态（0正常 1停用）
    private String status;
    //创建人的用户id
    @Field("createBy")
    private Long createBy;
    //创建时间
    private Date createTime;
    //更新人
    @Field("updateBy")
    private Long updateBy;
    //更新时间
    @Field("updateTime")
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    @Field("delFlag")
    private Integer delFlag;
    //关联角色id数组，非user表字段
    @Transient
    private Long[] roleIds;
    //权限验证角色数组，非user表字段
    @Transient
    private List<String> roles;
    //关联绑定商户
    @Field("merchantEnt_id")
    @Indexed(unique = true)
    private Long merchantEntId;


}
