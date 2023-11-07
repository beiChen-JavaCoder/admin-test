package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.GameControlVo;

/**
 * @author Xqf
 * @version 1.0
 */
public interface BloodPoolControlService {
    ResponseResult getScore();

    ResponseResult updateGame(GameControlVo game);

}
