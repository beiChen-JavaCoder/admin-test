package com.admin.controller;

import com.admin.domain.ResponseResult;
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


    @GetMapping
    public ResponseResult test() {


        return ResponseResult.okResult(200, "测试通过");
    }

}
