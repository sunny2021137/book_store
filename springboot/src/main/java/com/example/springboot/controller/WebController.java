package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("EMP".equals(account.getRole())) {
            dbAccount = employeeService.login(account);
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return Result.success();
    }

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
