package com.admin.service.Imp;


import com.admin.domain.entity.RoleMenu;
import com.admin.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author xqf
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addRoleMenuBatch(List<RoleMenu> roleMenuList) {

        mongoTemplate.insertAll(roleMenuList);


    }
}
