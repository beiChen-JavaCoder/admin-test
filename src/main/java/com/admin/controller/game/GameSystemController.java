package com.admin.controller.game;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;
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
    @GetMapping("{gameIds}")
    public ResponseResult downlineGame(@PathVariable List<Long> gameIds) {

        log.info("管理员:1正在对游戏"+gameIds.toString()+"执行下线操作,请谨慎操作");
        if (SecurityUtils.isAdmin()) {
        return gameService.downlineGame();
        }
        return ResponseResult.okResult();
    }
    @PostMapping
    public ResponseResult getList(@RequestBody QueryParamsVo queryParamsVo){


        return gameService.findGamePage(queryParamsVo);


    }


}
