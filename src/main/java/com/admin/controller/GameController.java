package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Robot;
import com.admin.domain.entity.RobotBean;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.GameControlVo;
import com.admin.domain.vo.RobotBeanVo;
import com.admin.service.BloodPoolControlService;
import com.admin.service.RobotService;
import com.admin.service.UserControlService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/game")
@Api("游戏控制前端控制器")
public class GameController {

    @Autowired
    private BloodPoolControlService bloodPoolControlService;
    @Autowired
    private UserControlService userControlService;
    @Autowired
    private RobotService robotService;

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

    @GetMapping("/blood/refresh")
    public ResponseResult refreshScore(){
        return bloodPoolControlService.refreshScore();
    }

    @GetMapping("/p2p")
    public ResponseResult getPtp() {
        return userControlService.userControlList();
    }

    @PutMapping("/p2p")
    public ResponseResult updateGameUser(@RequestBody UserControl userControl) {
        return userControlService.updateUserControl(userControl);

    }

    @PostMapping("/p2p")
    public ResponseResult addUserControl(@RequestBody UserControl userControl) {

        return userControlService.addUserControl(userControl);

    }

    @GetMapping("/robot")
    public ResponseResult getRobot() {
        return robotService.robotList();
    }

    @PostMapping("/robot")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println(file);
        System.out.println(file);
        return robotService.importRobot(file);

    }

    @PutMapping("/robot")
    public ResponseResult updateRobot(@RequestBody RobotBeanVo robotVo) {

        return robotService.updateRobot(robotVo);

    }


}
