package com.admin.domain.vo;

import com.admin.domain.entity.MerchantEntity;
import com.admin.domain.entity.Role;
import com.admin.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAndRoleIdsVo {
    private User user;
    private MerchantEntity merchantEntity;
    private List<Role> roles;
    private List<Long> roleIds;




}