package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PageVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.domain.vo.RechargeVo;
import com.admin.domain.vo.PlayerRechargeVo;
import com.admin.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("充值管理")
@RestController
@RequestMapping("/recharge")
public class RechargeController {

    @Autowired
    private PlayerService playerService;


    @GetMapping("/list")
    @ApiOperation(value = "角色列表")
    public ResponseResult<PageVo> getRoleList(PlayerRechargeVo playerRechargeVo,Integer pageNum,Integer pageSize) {

        return playerService.findPlayerRechargePage(playerRechargeVo,pageNum,pageSize);
    }

    @PostMapping("/updateGold")
    @ApiOperation("充值角色金币")
    public ResponseResult updateGold(@RequestBody RechargeVo rechargeVo) {


        return playerService.updateRoleGold(rechargeVo);

    }

}
