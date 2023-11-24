package com.admin.controller.system;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Menu;
import com.admin.domain.vo.MenuTreeVo;
import com.admin.domain.vo.MenuVo;
import com.admin.domain.vo.RoleMenuTreeSelectVo;
import com.admin.enums.BusinessType;
import com.admin.service.MenuService;
import com.admin.utils.BeanCopyUtils;
import com.admin.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;



    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.findMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        System.out.println(options.toString());
        return ResponseResult.okResult(options);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.findMenuList(new Menu());
        List<Long> checkedKeys = menuService.findMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        System.out.println(vo.toString());
        return ResponseResult.okResult(vo);

    }



    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.findMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }
    @Log(title = "新增菜单", businessType = BusinessType.INSERT)
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu)
    {
        menuService.addMenu(menu);
        return ResponseResult.okResult();
    }
    /**
     * 修改菜单
     */
    @PutMapping
    @Log(title = "修改菜单", businessType = BusinessType.UPDATE)
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }
    /**
     * 根据菜单编号获取详细信息
     */
    @GetMapping(value = "/{menuId}")
    public ResponseResult getInfo(@PathVariable Long menuId)
    {
        return ResponseResult.okResult(menuService.findById(menuId));
    }
    /**
     * 删除菜单
     */
    @Log(title = "删除菜单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChild(menuId)) {
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }
}