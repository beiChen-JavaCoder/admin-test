package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Menu;
import com.admin.domain.vo.MenuVo;
import com.admin.service.MenuService;
import com.admin.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.findMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu)
    {
        menuService.insertMenu(menu);
        return ResponseResult.okResult();
    }


}