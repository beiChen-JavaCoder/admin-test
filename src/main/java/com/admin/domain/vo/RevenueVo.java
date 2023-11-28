package com.admin.domain.vo;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;

/**
 * @author Xqf
 * @version 1.0
 * 税收Vo
 */
@Data
public class RevenueVo extends BaseVo {

    private String id;
    private String sectionTime;
    private Long num;
    private HashMap<String, Object> searchTime;


}
