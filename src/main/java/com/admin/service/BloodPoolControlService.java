package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.entity.UserControl;
import com.admin.domain.vo.GameControlVo;

/**
 * @author Xqf
 * @version 1.0
 */
public interface BloodPoolControlService {
    /**
     * 获取血池列表
     * @return
     */
    ResponseResult getScore();

    /**
     * 更新血池分数
     * @param game
     * @return
     */
    ResponseResult updateGame(GameControlVo game);

    /**
     * 定时器刷新分数
     * @return
     */
    ResponseResult refreshScore();

}
