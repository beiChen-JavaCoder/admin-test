package com.admin.service.Imp;


import com.admin.component.IdManager;
import com.admin.domain.entity.RoleMenu;
import com.admin.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;

/**
 * @Author xqf
 */
@Service
public class RoleMenuServiceImp implements RoleMenuService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;

    @Override
    public void addRoleMenuBatch(List<RoleMenu> roleMenuList) {
        roleMenuList.forEach(roleMenu->{
            roleMenu.setId(idManager.getMaxRoleMenuId().incrementAndGet());
        });
        mongoTemplate.insertAll(roleMenuList);


    }

    @Override
    public void removeRoleMenuByRoleId(Long id) {
        mongoTemplate.remove(Query.query(Criteria.where("role_id").is(id)), RoleMenu.class);
    }
}
