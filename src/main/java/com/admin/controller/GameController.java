package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.BloodPoolControl;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.GameControlVo;
import com.admin.domain.vo.UserControlVo;
import com.admin.service.BloodPoolControlService;
import com.admin.service.UserControlService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/game/control")
public class GameController {

    @Autowired
    private BloodPoolControlService bloodPoolControlService;
    @Autowired
    private UserControlService userControlService;


//    @PutMapping("/blood")
//    public ResponseResult updateBlood(@RequestBody) {
//
////        bloodPoolControl.updateBlood();
//
//        return ResponseResult.okResult();
//
//
//    }

    @GetMapping("/blood")
    public ResponseResult getGame() {

        return bloodPoolControlService.getScore();

    }

    @PutMapping("/blood")
    public ResponseResult updateGame(@RequestBody GameControlVo gameControlVo) {
        return bloodPoolControlService.updateGame(gameControlVo);

    }

    @GetMapping("/p2p")
    public ResponseResult getPtp() {
        return userControlService.userControlList();
    }
    @PutMapping("/p2p")
    public ResponseResult updateGameUser(@RequestBody UserControl userControl) {
        return userControlService.updateUserControl(userControl);

    }

}
