package com.admin.domain.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

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
public class User implements Serializable {
    @MongoId
    private String id;
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
    private String type;
    //账号状态（0正常 1停用）
    private String status;
    //创建人的用户id
    private String createBy;
    //创建时间
    private Date createTime;
    //更新人
    private String updateBy;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

    //关联角色id数组，非user表字段
    private String[] roleIds;

}
