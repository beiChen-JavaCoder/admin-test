package com.admin.controller.system;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/dict/data")
@PreAuthorize("@ss.hasRole('admin')")
public class SysDictDataController

{
//
//    @Autowired
//    private SysDictTypeService dictTypeService;
//
//    /**
//     * 根据字典类型查询字典数据信息
//     */
//    @GetMapping(value = "/type/{dictType}")
//    public ResponseResult dictType(@PathVariable String dictType)
//    {
//        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
//        if (StringUtils.isNull(data))
//        {
//            data = new ArrayList<SysDictData>();
//        }
//        return success(data);
//    }


}