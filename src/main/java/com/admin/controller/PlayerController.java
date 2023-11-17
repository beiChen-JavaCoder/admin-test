package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.PlayerRechargeVo;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.service.PlayerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * @version 1.0
 */
@Api("玩家管理")
@RestController()
@RequestMapping("/player")
public class PlayerController {


@Autowired
private PlayerService playerService;

    @PostMapping("/list")
    public ResponseResult getList( @RequestBody QueryParamsVo queryParamsVo){
        return playerService.findPlayerPage(queryParamsVo) ;
    }




}
