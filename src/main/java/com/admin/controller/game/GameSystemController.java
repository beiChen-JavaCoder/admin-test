package com.admin.controller.game;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.enums.BusinessType;
import com.admin.notification.Notification;
import com.admin.service.GameService;
import com.admin.utils.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/system/game")
@PreAuthorize("@ss.hasRole('admin')")
public class GameSystemController {


    @Autowired
    private GameService gameService;

    /**
     * 删库（游戏）接口
     * @return
     */
    @Log(title = "关闭游戏",businessType = BusinessType.UPDATE)
    @GetMapping("{gameId}")
    public ResponseResult turnGame(@PathVariable Long gameId) {

        if (SecurityUtils.isAdmin()) {
        return gameService.turnGame(gameId);
        }
        return ResponseResult.okResult(500,"无权限操作,请联系管理员");
    }
    @PostMapping
    public ResponseResult getList(@RequestBody QueryParamsVo queryParamsVo){


        return gameService.findGamePage(queryParamsVo);


    }
    @Log(title = "删除游戏",businessType = BusinessType.DELETE)
    @ApiOperation("删除游戏")
    @DeleteMapping()
    public ResponseResult deletGame(@PathVariable Long gameId){
        return gameService.deletGame(gameId);
    }


}
