package com.admin.controller.game;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Robot;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.GameControlVo;
import com.admin.domain.vo.RobotBeanVo;
import com.admin.enums.BusinessType;
import com.admin.enums.GameControlTypeEnum;
import com.admin.service.BloodPoolControlService;
import com.admin.service.RobotService;
import com.admin.service.UserControlService;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController
@RequestMapping("/game")
@Api("游戏控制前端控制器")
@Slf4j
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
    @ApiOperation("金币控制台列表")
    public ResponseResult getGame() {

        return bloodPoolControlService.getScore();

    }

    @Log(title = "修改金币控制台", businessType = BusinessType.UPDATE)
    @PutMapping("/blood")
    @ApiOperation("修改金币控制台")
    public ResponseResult updateGame(@RequestBody GameControlVo gameControlVo) {
        ResponseResult responseResult = checkGoldParameter(gameControlVo);

        if (responseResult.getCode() == 500) {
            return responseResult;
        }
        return bloodPoolControlService.updateGame(gameControlVo);

    }


    @GetMapping("/blood/refresh")
    @ApiOperation("定时刷新当前金币")
    public ResponseResult refreshScore() {
        return bloodPoolControlService.refreshScore();
    }

    @GetMapping("/p2p")
    @ApiOperation("点控控制台列表")
    public ResponseResult getPtp() {
        return userControlService.userControlList();
    }

    @Log(title = "更改点控控制台", businessType = BusinessType.UPDATE)
    @PutMapping("/p2p")
    @ApiOperation("更改点控控制台")
    public ResponseResult updateGameUser(@RequestBody UserControl userControl) {
        return userControlService.updateUserControl(userControl);

    }

    @Log(title = "添加点控控制", businessType = BusinessType.INSERT)
    @PostMapping("/p2p")
    @ApiOperation("添加点控控制")
    public ResponseResult addUserControl(@RequestBody UserControl userControl) {

        return userControlService.addUserControl(userControl);

    }

    @GetMapping("/robot")
    @ApiOperation("机器人控制台列表")
    public ResponseResult getRobotControl() {
        return robotService.robotControlList();
    }




    @Log(title = "上传机器人", businessType = BusinessType.INSERT)
    @PostMapping("/robot")
    @ApiOperation("上传机器人（robotName）")
    public ResponseResult uploadFile(@RequestParam("file") MultipartFile file) {


        return robotService.importRobot(file);

    }
    @Log(title = "更改机器人控制", businessType = BusinessType.UPDATE)
    @PutMapping("/robot")
    @ApiOperation("更改机器人控制")
    public ResponseResult updateRobot(@RequestBody RobotBeanVo robotVo) {

        return robotService.updateRobot(robotVo);

    }

    @GetMapping("/robot/list")
    @ApiOperation("机器人名称列表")
    public ResponseResult getRobotList(Integer pageNum, Integer pageSize) {
        return robotService.findRobotPage(pageNum, pageSize);
    }
    @Log(title = "删除机器人", businessType = BusinessType.DELETE)
    @DeleteMapping("/robot/list/{robotId}")
    @ApiOperation("删除机器人")
    public ResponseResult delRobot(@PathVariable Long robotId) {
        return robotService.delRobot(robotId);
    }



    @Log(title = "修改机器人信息", businessType = BusinessType.INSERT)
    @PostMapping("/robot/update")
    @ApiOperation("修改机器人信息")
    public ResponseResult updateRobotName(@RequestBody Robot robot) {
        return robotService.updateRobotName(robot);
    }


    /**
     * 金币控制参数校验
     *
     * @param gameControlVo
     * @return
     */
    private ResponseResult checkGoldParameter(GameControlVo gameControlVo) {
        String regex = new String();
        Pattern pattern = null;
        Matcher matcher = null;
        //校验控制台
        //匹配1-10000 触发率（万分比）
        regex = "^(?:[1-9]\\d{0,3}|10000)$";
        pattern = Pattern.compile(regex);
        if (gameControlVo.getType().equals(GameControlTypeEnum.bigVomitControl.getType())) {
            JSONObject game = (JSONObject) gameControlVo.getGame();
            JSONObject bigEatControl = (JSONObject) game.get("bigVomitControl");
            String limitScore = String.valueOf(bigEatControl.get("ratio"));
            matcher = pattern.matcher(limitScore);
        } else if (gameControlVo.getType().equals(GameControlTypeEnum.vomitControl.getType())) {
            JSONObject game = (JSONObject) gameControlVo.getGame();
            JSONObject bigEatControl = (JSONObject) game.get("vomitControl");
            String limitScore = String.valueOf(bigEatControl.get("ratio"));
            matcher = pattern.matcher(limitScore);
        } else if (gameControlVo.getType().equals(GameControlTypeEnum.bigEatControl.getType())) {
            JSONObject game = (JSONObject) gameControlVo.getGame();
            JSONObject bigEatControl = (JSONObject) game.get("bigEatControl");
            String limitScore = String.valueOf(bigEatControl.get("ratio"));
            matcher = pattern.matcher(limitScore);
        } else if (gameControlVo.getType().equals(GameControlTypeEnum.eatControl.getType())) {
            JSONObject game = (JSONObject) gameControlVo.getGame();
            JSONObject bigEatControl = (JSONObject) game.get("eatControl");
            String limitScore = String.valueOf(bigEatControl.get("ratio"));
            matcher = pattern.matcher(limitScore);
        } else {
            //当type为0是默认基础金币执行
            String score = gameControlVo.getScore();
            if (score == null) {
                return ResponseResult.errorResult(500, "任务金币不能为空");
            }
            regex = "^-?\\d{1,13}$";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(gameControlVo.getScore());
        }
        if (!matcher.matches()) {
            return ResponseResult.errorResult(500, "请输入合规分数");
        }
        return ResponseResult.okResult(200, "验证通过");
    }

}
