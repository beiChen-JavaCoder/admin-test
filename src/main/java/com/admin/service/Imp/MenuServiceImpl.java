package com.admin.service.Imp;


import cn.hutool.core.date.DateUtil;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.RoleMenu;
import com.admin.service.MenuService;
import com.admin.utils.SecurityUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 22:32:10
 */
@Service("menuService")
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;


    @Override
    public List<String> selectPermsByUserId(Long id) {

        //管理员权限
        if (SecurityUtils.isAdmin()) {
            Query query = new Query();
            query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
            query.addCriteria(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));
            query.with(Sort.by(Sort.Direction.ASC, "parent_id", "order_num"));
            List<Menu> menus = mongoTemplate.find(query, Menu.class);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());

            return perms;
        }
        //否则返回所具有的权限
        return findPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {

        List<Menu> menus = null;
        //判断是否是管理员
        if (SecurityUtils.isAdmin()) {
            //如果是 获取所有符合要求的Menu
            menus = selectAllRouterMenu();
        } else {
            //否则  获取当前用户所具有的Menu
            menus = findRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);

        return menuTree;
    }

    private List<Menu> findRouterMenuTreeByUserId(Long userId) {

        Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.lookup("sys_role_menu", "role_id", "role_id", "roleMenus"),
                Aggregation.unwind("roleMenus", true),
                Aggregation.lookup("sys_menu", "roleMenus.menu_id", "_id", "menus"),
                Aggregation.unwind("menus", true),
                Aggregation.match(Criteria.where("user_id").is(userId)
                        .and("menus.status").is("0")
                        .and("menus.menu_type").in("C", "M")
                        .and("menus.del_flag").is("0")
                ),
                Aggregation.project()
                        .and("menus._id").as("id")
                        .and("menus.parent_id").as("parent_id")
                        .and("menus.menu_name").as("menu_name")
                        .and("menus.path").as("path")
                        .and("menus.component").as("component")
                        .and("menus.visible").as("visible")
                        .and("menus.status").as("status")
                        .and(ConditionalOperators.ifNull("menus.perms").then("")).as("perms")
                        .and("menus.is_frame").as("is_frame")
                        .and("menus.menu_type").as("menu_type")
                        .and("menus.icon").as("icon")
                        .and("menus.order_num").as("order_num")
                        .and("menus.create_time").as("create_time")
                        .andExclude("_id"), // 排除"_id"字段
                group(fields().and("id").and("parent_id")
                        .and("menu_name")
                        .and("path")
                        .and("component")
                        .and("visible")
                        .and("status")
                        .and("perms")
                        .and("is_frame")
                        .and("menu_type")
                        .and("icon")
                        .and("order_num")
                        .and("create_time"))
                        .first("id").as("id")
                        .first("parent_id").as("parent_id")
                        .first("menu_name").as("menu_name")
                        .first("path").as("path")
                        .first("component").as("component")
                        .first("visible").as("visible")
                        .first("status").as("status")
                        .first("perms").as("perms")
                        .first("is_frame").as("is_frame")
                        .first("menu_type").as("menu_type")
                        .first("icon").as("icon")
                        .first("order_num").as("order_num")
                        .first("create_time").as("create_time"),
                sort(Sort.Direction.ASC, "parent_id", "order_num")

        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "sys_user_role", Document.class);
        List<Document> menuList = results.getMappedResults();
        ArrayList<Menu> reMenus = new ArrayList<>();
        menuList.stream().forEach(itme -> {
            Map map = itme.get("_id", Map.class);
            Menu menu = new Menu();
            menu.setId(Long.valueOf(String.valueOf(map.get("id"))));
            menu.setVisible(String.valueOf(map.get("visible")));
            menu.setCreateTime(new Date(String.valueOf(map.get("create_time"))));
            menu.setMenuType(String.valueOf(map.get("menu_type")));
            menu.setIcon(String.valueOf(map.get("icon")));
            menu.setIsFrame(Integer.valueOf(String.valueOf(map.get("is_frame"))));
            menu.setPath(String.valueOf(map.get("path")));
            menu.setComponent(String.valueOf(map.get("component")));
            menu.setParentId(Long.valueOf(String.valueOf(map.get("parent_id"))));
            menu.setOrderNum(Integer.valueOf(String.valueOf(map.get("order_num"))));
            menu.setPerms(String.valueOf(map.get("perms")));
            menu.setStatus(String.valueOf(map.get("status")));
            menu.setMenuName(String.valueOf(map.get("menu_name")));
            reMenus.add(menu);
        });

        return reMenus;
    }

    @Override
    public List<Menu> findMenuList(Menu menu) {
        Query query = new Query();

        if (StringUtils.hasText(menu.getMenuName())) {
            //添加模糊查询条件
            query.addCriteria(Criteria.where("menu_name").regex(menu.getMenuName()));
        }

        if (StringUtils.hasText(menu.getStatus())) {
            //添加等于查询条件
            query.addCriteria(Criteria.where("status").is(menu.getStatus()));
        }
        //添加排序条件 根据 parentId 和 orderNum 进行升序排序
        query.with(Sort.by(Sort.Order.asc("parent_id"), Sort.Order.asc("order_num")));
        //执行查询
        List<Menu> menus = mongoTemplate.find(query, Menu.class);
        return menus;


    }

    @Override
    public void addMenu(Menu menu) {

        menu.setId(idManager.getMaxMenuId().incrementAndGet());
        menu.setCreateTime(DateUtil.date());
        menu.setCreateBy(SecurityUtils.getUserId());
        menu.setDelFlag(0 + "");
        menu.setIsFrame(1);
        mongoTemplate.insert(menu);
    }

//    @Override
//    public Menu findById(Long menuId) {
//        return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(menuId)), Menu.class);
//    }

    @Override
    public void updateById(Menu menu) {

        mongoTemplate.save(menu);


    }

    @Override
    public Menu findById(Long menuId) {
        return mongoTemplate.findById(menuId, Menu.class);
    }

    @Override
    public boolean hasChild(Long menuId) {
        return mongoTemplate.count(Query.query(Criteria.where("parent_id").is(menuId)), Menu.class) != 0;
    }

    @Override
    public void removeById(Long menuId) {

        mongoTemplate.remove(Query.query(Criteria.where("_id").is(menuId)), Menu.class);
    }

    @Override
    public List<Long> findMenuListByRoleId(Long roleId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("role_id").is(roleId));
        query.with(Sort.by(Sort.Direction.ASC, "parent_id", "order_num"));
        List<RoleMenu> roleMenuList = mongoTemplate.find(query, RoleMenu.class);
        List<Long> menuIds = roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());

        return menuIds;

    }

    //    @Override
//    public List<Menu> selectMenuList(Menu menu) {
//
//        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//        //menuName模糊查询
//        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
//        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
//        //排序 parent_id和order_num
//        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
//        List<Menu> menus = list(queryWrapper);;
//        return menus;
//    }
//
//    @Override
//    public boolean hasChild(Long menuId) {
//        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Menu::getParentId,menuId);
//        return count(queryWrapper) != 0;
//    }
//
//    @Override
//    public List<Long> selectMenuListByRoleId(Long roleId) {
//        return getBaseMapper().selectMenuListByRoleId(roleId);
//    }
//
//    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
//        List<Menu> menuTree = menus.stream()
//                .filter(menu -> menu.getParentId().equals(parentId))
//                .map(menu -> menu.setChildren(getChildren(menu, menus)))
//                .collect(Collectors.toList());
//        return menuTree;
//    }
//
//    /**
//     * 获取存入参数的 子Menu集合
//     * @param menu
//     * @param menus
//     * @return
//     */
//    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
//        List<Menu> childrenList = menus.stream()
//                .filter(m -> m.getParentId().equals(menu.getId()))
//                .map(m->m.setChildren(getChildren(m,menus)))
//                .collect(Collectors.toList());
//        return childrenList;
//    }
    private List<Menu> selectAllRouterMenu() {
        Aggregation aggregation = newAggregation(
                Aggregation.match(
                        Criteria.where("menu_type").in("C", "M")
                                .and("status").is("0")
                                .and("del_flag").is("0")
                ),
                Aggregation.project(
                                "id", "parent_id", "menu_name", "path", "component", "visible", "status", "is_frame", "menu_type", "icon", "order_num", "create_time"
                        )
                        .and(ConditionalOperators.Cond.when(Criteria.where("perms").is(null))
                                .then("")
                                .otherwiseValueOf("perms"))
                        .as("perms"),
                Aggregation.sort(Sort.by(Sort.Direction.ASC, "parent_id", "order_num")));

        AggregationResults<Menu> results = mongoTemplate.aggregate(aggregation, "sys_menu", Menu.class);
        return results.getMappedResults();
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    private List<String> findPermsByUserId(Long userId) {

        Aggregation aggregation = Aggregation.newAggregation(

                Aggregation.lookup("sys_role_menu", "role_id", "role_id", "rm"),
                Aggregation.unwind("rm"),
                Aggregation.lookup("sys_menu", "rm.menu_id", "_id", "menus"),
                Aggregation.unwind("menus"),
                Aggregation.match(Criteria.where("user_id").is(userId)
                        .and("menus.status").is("0")
                        .and("menus.menu_type").in("C", "F")),
                group("menus.perms").addToSet("menus.perms").as("perms")
        );
        AggregationResults<Map> sysUserRole = mongoTemplate.aggregate(aggregation, "sys_user_role", Map.class);
        List<String> perms = sysUserRole.getMappedResults().stream().map(item -> {
            return String.valueOf(item.get("perms"));
        }).collect(Collectors.toList());
        log.info(String.valueOf(perms));


        return perms;
    }

}

