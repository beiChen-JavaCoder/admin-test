package com.admin.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端参数Vo
 */
@Data
@AllArgsConstructor
public class QueryParamsVo {
    private Map<String, String> attributes;
    private Map<String, Object> objects;

    public QueryParamsVo() {
        this.attributes = new HashMap<>();
        this.objects = new HashMap<>();
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void addObject(String key, Object value) {
        objects.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Object getObject(String key) {
        return objects.get(key);
    }

    // 可以根据需要添加其他方法或自定义逻辑

}
