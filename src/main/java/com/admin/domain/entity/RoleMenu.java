package com.admin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * @Author xqf
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("sys_role_menu")
public class RoleMenu {
    @MongoId
    private Long id;
    /**
     * 角色ID
     */
    @Field("role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Field("menu_id")
    private Long menuId;
}