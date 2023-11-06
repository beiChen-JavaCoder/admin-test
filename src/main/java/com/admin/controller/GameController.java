package com.admin.controller;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.BloodPoolControl;
import com.admin.service.BloodPoolControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/game/control")
public class GameController {

    @Autowired
    private BloodPoolControlService bloodPoolControlService;


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
    public ResponseResult getScore() {

        return bloodPoolControlService.getScore();

    }


}
