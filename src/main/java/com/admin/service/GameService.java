package com.admin.service;

import com.admin.domain.ResponseResult;
import com.admin.domain.vo.QueryParamsVo;

/**
 * @author Xqf
 * @version 1.0
 */

public interface GameService {
    /**
     * 游戏列表
     * @return
     */
    ResponseResult findGamePage(QueryParamsVo queryParamsVo);

    ResponseResult downlineGame();

}
