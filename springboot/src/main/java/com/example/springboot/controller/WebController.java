package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @GetMapping("/hello")
    public Result hello() {
        return Result.success("hello");
    }

    @GetMapping("/count")
    public Result count() {
        int num = 1/0;
        return Result.success(10);
    }
    @GetMapping("/custom")
    public Result custom() {
        throw new CustomException("400", "錯誤，禁止請求");
    }


}
