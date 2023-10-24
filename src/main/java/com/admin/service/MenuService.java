package com.admin.service;

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
    public List<String> selectPermsByUserId(String Id);


}
