package com.admin.service.Imp;


import cn.hutool.core.date.DateUtil;
import com.admin.component.IdManager;
import com.admin.constants.SystemConstants;
import com.admin.domain.entity.Menu;
import com.admin.domain.entity.UserRole;
import com.admin.service.MenuService;
import com.admin.utils.SecurityUtils;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;


/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-08-09 22:32:10
 */
@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdManager idManager;


    @Override
    public List<String> selectPermsByUserId(Long id) {

        //管理员权限
        Query query = new Query();
        query.addCriteria(Criteria.where("menu_type").in(SystemConstants.MENU, SystemConstants.BUTTON));
        query.addCriteria(Criteria.where("status").is(SystemConstants.STATUS_NORMAL));
        List<Menu> menus = mongoTemplate.find(query, Menu.class);
        List<String> perms = menus.stream()
                .map(Menu::getPerms)
                .collect(Collectors.toList());

        return perms;
    }
    //否则返回所具有的权限
//
    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {

        List<Menu> menus = selectAllRouterMenu();

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
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

        return mongoTemplate.find(query, Long.class, "sys_menu");

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
                Aggregation.project("id", "parent_id", "menu_name", "path", "component", "visible", "status", "is_frame", "menu_type", "icon", "order_num", "create_time")
                        .and(ConditionalOperators.Cond.when(Criteria.where("perms").is(null))
                                .then("")
                                .otherwiseValueOf("perms"))
                        .as("perms"));
        Aggregation.sort(Sort.by(Sort.Direction.ASC, "parent_id", "order_num"));

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
    public List<String> selectPermsByUserId(String userId) {
        // 创建查询条件
        Criteria criteria = new Criteria();
        criteria.and("user_id").is(userId)
                .and("menu_type").in("C", "F")
                .and("status").is(0)
                .and("del_flag").is(0);

        // 创建聚合管道
        Aggregation aggregation = Aggregation.newAggregation(
                // 使用match操作筛选符合条件的文档
                Aggregation.match(criteria),
                // 使用lookup操作进行关联查询，类似于SQL中的左连接
                Aggregation.lookup("sys_role_menu", "role_id", "role_id", "roleMenus"),
                Aggregation.unwind("roleMenus"), // 展开数组字段
                Aggregation.lookup("sys_menu", "menu_id", "id", "menus"),
                Aggregation.unwind("menus"), // 再次展开数组字段
                // 使用group操作进行分组统计，这里将perms字段聚合为一个列表
                Aggregation.group("menus.perms").addToSet("menus.perms").as("permsList")
        );
        // 执行聚合查询
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "sys_user_role", Document.class);

        // 处理查询结果
        List<String> permsList = new ArrayList<>();
        results.getMappedResults().forEach(document -> {
            List<String> perms = (List<String>) document.get("permsList");
            permsList.addAll(perms);
        });

        return permsList;
    }

}

