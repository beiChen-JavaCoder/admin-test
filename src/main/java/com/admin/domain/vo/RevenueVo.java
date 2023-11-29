package com.admin.domain.vo;

import cn.hutool.core.date.DateRange;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xqf
 * @version 1.0
 * 税收Vo
 */
@Data
public class RevenueVo extends BaseVo {

    private String id;
    private String  sectionTime;
    private Long num;
    private Map<String,Object> searchTime;


}
