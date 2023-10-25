package com.admin.service.Imp;


import com.admin.constants.SystemConstants;
import com.admin.domain.entity.Menu;
import com.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 22:32:10
 */
@Service("menuService")
public class MenuServiceImpl  implements MenuService {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<String> selectPermsByUserId(String id) {


            Query query = new Query();
            query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
            query.addCriteria(Criteria.where("status").is(Integer.valueOf(SystemConstants.STATUS_NORMAL)));
            List<Menu> menus = mongoTemplate.find(query, Menu.class);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());

            return perms;
        }
        //否则返回所具有的权限

//    public List<String> findPermsByUserId(String Id){
//        MatchOperation match = match(Criteria.where("ur.user_id").is(userId)
//                .and("m.menu_type").in("C", "F")
//                .and("m.status").is(0)
//                .and("m.del_flag").is(0));
//
//        LookupOperation lookupRoleMenu = lookup("sys_role_menu", "role_id", "role_id", "roles");
//        LookupOperation lookupMenu = lookup("sys_menu", "id", "menu_id", "menus");
//
//        ProjectionOperation project = project().and("menus.perms").as("perms");
//
//        Aggregation aggregation = newAggregation(match, lookupRoleMenu, lookupMenu, project);
//
//        AggregationResults<UserRole> results = mongoTemplate.aggregate(aggregation, "sys_user_role", UserRole.class);
//        List<UserRole> documents = results.getMappedResults();
//
//        List<String> perms = new ArrayList<>();
//        for (UserRole document : documents) {
//            List<String> permsList = document.getList("perms", String.class);
//            perms.addAll(permsList);
//        }
//
//        return perms;
//    }
//
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(String userId) {

        List<Menu> menus = selectAllRouterMenu();

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,"0");
        return menuTree;
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
    Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(
                    Criteria.where("menu_type").in("C", "M")
                            .and("status").is(0)
                            .and("del_flag").is(0)
            ),
            Aggregation.project("id", "parent_id", "menu_name", "path", "component", "visible", "status", "is_frame", "menu_type", "icon", "order_num", "create_time")
                    .and(ConditionalOperators.Cond.when(Criteria.where("perms").is(null))
                            .then("")
                            .otherwiseValueOf("perms"))
                            .as("perms"));
            Aggregation.sort(Sort.by(Sort.Direction.ASC, "parent_id", "order_num"));

    AggregationResults<Menu> results = mongoTemplate.aggregate(aggregation, "sys_menu", Menu.class);
    return results.getMappedResults();
}
    private List<Menu> builderMenuTree(List<Menu> menus, String parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }
    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}

