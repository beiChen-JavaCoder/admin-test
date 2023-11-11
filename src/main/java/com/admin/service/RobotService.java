package com.admin.service;

import com.admin.domain.ResponseResult;
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
     * 查询机器人详情信息
     * @return
     */
    ResponseResult robotList();

    /**
     * 修改机器人控制请求
     * @param robot
     * @return
     */
    ResponseResult updateRobot(RobotBeanVo robot);
}
