package com.admin.service;

import com.admin.domain.entity.Menu;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
public interface MenuService {

    /**
     * 根据id查Perms权限
     * @param Id
     * @return
     */
    public List<String> selectPermsByUserId(Long Id);
    /**
     * 根据用户id查询用户菜单权限树
     *
     */
    public List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> findMenuList(Menu menu);

    void addMenu(Menu menu);

    void updateById(Menu menu);

    Menu findById(Long menuId);

    boolean hasChild(Long menuId);

    void removeById(Long menuId);

    List<Long> findMenuListByRoleId(Long roleId);

    /**
     * 根据菜单ids获取权限字符
     * @param menuIds
     * @return
     */
    List<String> findMenuPamersList(Long[] menuIds);
}
