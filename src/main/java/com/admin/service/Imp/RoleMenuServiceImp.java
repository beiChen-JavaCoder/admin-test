package com.admin.service.Imp;


import com.admin.component.IdManager;
import com.admin.domain.entity.RoleMenu;
import com.admin.enums.AppHttpCodeEnum;
import com.admin.exception.SystemException;
import com.admin.service.RoleMenuService;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        roleMenuList.forEach(roleMenu -> {
            roleMenu.setId(idManager.getMaxRoleMenuId().incrementAndGet());
        });
        mongoTemplate.insertAll(roleMenuList);


    }

    @Override
    public void removeRoleMenuByRoleId(Long id) {
        mongoTemplate.remove(Query.query(Criteria.where("role_id").is(id)), RoleMenu.class);
    }

    @Override
    public Set<String> findMenuByRoleId(Long[] roleIds) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("role_id").in(roleIds)),
                Aggregation.lookup("sys_role_menu", "menu_id", "_id", "menus"),
                Aggregation.project("perms")

        );
        AggregationResults<HashMap> aggregate = mongoTemplate.aggregate(aggregation, "sys_role_menu", HashMap.class);

        System.out.println(aggregate);
        return null;
    }

    @Override
    public void removeRoleMenuByRoleIds(List<Long> ids) {


        DeleteResult remove = mongoTemplate.remove(Query.query(Criteria.where("role_id").in(ids)), RoleMenu.class);
        if (!remove.wasAcknowledged() && remove.getDeletedCount() > 0) {
            throw new SystemException(AppHttpCodeEnum.valueOf("批量删除角色菜单关联失败"));
        }


    }
}
