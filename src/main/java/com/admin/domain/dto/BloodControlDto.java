package com.admin.domain.dto;

import com.admin.domain.entity.BloodPoolControl;
import lombok.Data;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
public class BloodControlDto {

    /** 修改血池时本值为空 */
    BloodPoolControl.UpdateType type;
    /** 游戏编号 */
    long gameId;
    /** 修改血池分数 */
    long score;
    /** 开始控制的分数 */
    int limitScore;
    /** 控制概率 */
    int ratio;

}
