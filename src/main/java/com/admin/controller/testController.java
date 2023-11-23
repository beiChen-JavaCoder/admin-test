package com.admin.controller;

import com.admin.annotation.Log;
import com.admin.domain.ResponseResult;
import com.admin.enums.BusinessType;
import com.admin.enums.OperatorType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Xqf
 * @version 1.0
 */
@RestController

@RequestMapping("/test")
public class testController {

    @Log(title = "测试类",businessType = BusinessType.OTHER,operatorType = OperatorType.TEST)
    @GetMapping
    public ResponseResult test() {
        return ResponseResult.okResult(200, "测试通过");
    }

}
