package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.Robot;
import com.admin.domain.vo.QueryParamsVo;
import com.admin.domain.vo.RobotBeanVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Xqf
 * @version 1.0
 */
public interface RobotService {
    /**
     * 新增机器人
     * @param file
     * @return
     */
     ResponseResult importRobot(MultipartFile file);

    /**
     * 查询机器人控制台详情信息
     * @return
     */
    ResponseResult robotControlList();

    /**
     * 修改机器人控制请求
     * @param robot
     * @return
     */
    ResponseResult updateRobot(RobotBeanVo robot);

    /**
     * 获取机器人名称(分页)
     * @return
     */
    ResponseResult findRobotPage(Integer pageNum,Integer pageSize);

    /**
     * 修改机器人名称
     * @param robot
     * @return
     */
    ResponseResult updateRobotName(Robot robot);
}
