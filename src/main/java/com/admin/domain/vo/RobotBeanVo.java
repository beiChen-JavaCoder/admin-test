package com.admin.domain.vo;

/**
 * @author Xqf
 * @version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改机器人控制Vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RobotBeanVo {
    /**
     * 0:initScore 1:betScore 2:carryScore 3:betTime 4:betRatio
     */
    Integer type;

    Long min;

    Long max;

    Long gameId;

    int betRatio;
}
