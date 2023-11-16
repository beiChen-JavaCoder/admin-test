package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.notification.Notification;
import com.admin.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/system/game")
@Slf4j
public class GameSystem {

    @Autowired
    private Notification notification;

    /**
     * 删库（游戏）接口
     * @return
     */
    @DeleteMapping
    public ResponseResult gameOut() {

        log.info("管理员:1正在执行删除游戏数据库操作,请谨慎操作");
//        if (SecurityUtils.isAdmin()) {
//            notification.gameOut();
//        }

        return ResponseResult.okResult();
    }


}
