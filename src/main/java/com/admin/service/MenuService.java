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

    void insertMenu(Menu menu);
}
