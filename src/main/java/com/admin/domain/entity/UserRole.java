package com.admin.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author xqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("sys_user_role")
public class UserRole {

    /** 用户ID */
    private String userId;

    /** 角色ID */
    private String roleId;
}