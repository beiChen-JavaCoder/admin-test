package com.admin.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.List;

/**
 * 菜单权限表(Menu)表实体类
 *
 * @author makejava
 * @since 2022-08-09 23:47:50
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("sys_menu")
@Accessors(chain = true)
public class Menu  {
    //菜单ID
    @Field("_id")
    private Long id;
    //菜单名称
    @Field("menu_name")
    private String menuName;
    //父菜单ID
    @Field("parent_id")
    private Long parentId;
    //显示顺序
    @Field("order_num")
    private Integer orderNum;
    //路由地址
    private String path;
    //组件路径
    private String component;
    //是否为外链（0是 1否）
    @Field("is_frame")
    private Integer isFrame;
    //菜单类型（M目录 C菜单 F按钮）
    @Field("menu_type")
    private String menuType;
    //菜单状态（0显示 1隐藏）
    private String visible;
    //菜单状态（0正常 1停用）
    @Field("status")
    private String status;
    //权限标识
    private String perms;
    //菜单图标
    private String icon;
    //创建者
    @Field("create_by")
    private Long createBy;
    //创建时间
    @Field("create_time")
    private Date createTime;
    //更新者
    @Field("update_by")
    private Long updateBy;
    //更新时间
    @Field("update_time")
    private Date updateTime;
    //备注
    private String remark;
    @Field("del_flag")
    private String delFlag;

    private List<Menu> children;
}
