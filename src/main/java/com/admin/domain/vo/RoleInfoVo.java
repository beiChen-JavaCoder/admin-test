package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfoVo {
    /**
     * 角色id
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 角色昵称
     */
    private String roleName;
    /**
     * 变更金币数量
     */
    private Integer num;
}
