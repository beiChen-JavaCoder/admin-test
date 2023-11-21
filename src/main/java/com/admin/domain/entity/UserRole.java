package com.admin.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;

/**
 * @Author xqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("sys_user_role")
public class UserRole {
    /**
     * 用户ID
     */
    @Field("user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @Field("role_id")
    private Long roleId;
}