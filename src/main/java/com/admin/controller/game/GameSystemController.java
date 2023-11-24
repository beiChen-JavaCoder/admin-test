package com.admin.controller.game;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.enums.BusinessType;
import com.admin.notification.Notification;
import com.admin.service.GameService;
import com.admin.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/system/game")
@Slf4j
public class GameSystemController {


    @Autowired
    private GameService gameService;

    /**
     * 删库（游戏）接口
     * @return
     */
    @Log(title = "关闭游戏",businessType = BusinessType.DELETE)
    @GetMapping("{gameId}")
    public ResponseResult downlineGame(@PathVariable Long gameId) {


        log.info("管理员:1正在对游戏"+gameId.toString()+"执行关闭操作,请谨慎操作");
        if (SecurityUtils.isAdmin()) {
        return gameService.turnGame(gameId);
        }
        return ResponseResult.okResult();
    }
    @PostMapping
    public ResponseResult getList(@RequestBody QueryParamsVo queryParamsVo){


        return gameService.findGamePage(queryParamsVo);


    }


}
