package com.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * 角色信息表(Role)表实体类
 *
 * @author makejava
 * @since 2022-08-09 22:36:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("sys_role")
public class Role {
    //角色ID@TableId
    private Long id;

    @Field("role_name")
    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;

    //角色状态（0正常 1停用）
    private String status;
    //删除标志（0代表存在 2代表删除）
    @Field("del_flag")
    private String delFlag;
    @Field("create_by")
    private Long createBy;
    @Field("create_time")
    private Date createTime;
    @Field("update_by")
    private Long updateBy;
    @Field("update_time")
    private Date updateTime;
    //备注
    private String remark;
    //关联菜单id数组，不是表中的字段  用来接收参数使用
    private Long[] menuIds;

}
