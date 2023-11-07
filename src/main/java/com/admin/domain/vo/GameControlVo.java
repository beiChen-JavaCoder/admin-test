package com.admin.domain.vo;

import com.admin.controller.GameController;
import com.admin.domain.entity.BloodPoolControl;
import lombok.Data;
import org.bson.json.JsonObject;

/**
 * @author Xqf
 * @version 1.0
 */
@Data
public class GameControlVo {

    private Integer type;
    private Object game;
    private Long score;
}
