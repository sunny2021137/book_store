package com.example.springboot.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.ArticleService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class WebController {

    @Resource
    private EmployeeService employeeService;
    @Resource
    private AdminService adminService;
    @Resource
    private ArticleService articleService;

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("EMP".equals(account.getRole())) {
            dbAccount = employeeService.login(account);
        } else {
            throw new CustomException("500", "非法職位");
        }
        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return Result.success();
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("EMP".equals(account.getRole())) {
            employeeService.updatePassword(account);
        } else {
            throw new CustomException("500", "非法職位");
        }
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

    @GetMapping("/barData")
    public Result getBarData(){
        // 取得部門數據
        List<Employee> employeeList = employeeService.selectAll(null);
        Map<String, Object> barMap = new HashMap<>(); //用object因為x軸y軸資料型別不同
        // x軸：不重複的部門名稱
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
        barMap.put("department", departmentNameSet);
        // y軸：部門員工數量
        List<Long> countList = new ArrayList<>();
        for(String departmentName: departmentNameSet){
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            countList.add(count);
        }
        barMap.put("count", countList);

        return Result.success(barMap);
    }

    @GetMapping("/pieData")
    public Result getPieData(){
        List<Map<String, Object>> pieList = new ArrayList<>();
        // 取得部門數據
        List<Employee> employeeList = employeeService.selectAll(null);
        // x軸：不重複的部門名稱
        Set<String> departmentNameSet = employeeList.stream().map(Employee::getDepartmentName).collect(Collectors.toSet());
        // y軸：部門員工數量
        for(String departmentName: departmentNameSet){
            long count = employeeList.stream().filter(employee -> employee.getDepartmentName().equals(departmentName)).count();
            Map<String, Object> map = new HashMap<>();
            map.put("value", count);
            map.put("name", departmentName);
            pieList.add(map);
        }

        return Result.success(pieList);
    }

    @GetMapping("/lineData")
    public Result getLineData(){
        Map<String, Object> lineMap = new HashMap<>(); //用object因為x軸y軸資料型別不同

        // x軸：近七日日期
        Date date = new Date(); // 今天
        DateTime startDate = DateUtil.offsetDay(date, -7); //前第七天
        List<DateTime> dateTimeList = DateUtil.rangeToList(startDate, date, DateField.DAY_OF_YEAR); //前七天日期list
        // 格式化日期
        List<String> dateFormatList = dateTimeList.stream().map(dateTime -> DateUtil.format(dateTime, "MM月dd日"))
                .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        lineMap.put("date", dateFormatList);

        // y軸：統計數量
        List<Integer> countList = new ArrayList<>();
        for(DateTime dateTime: dateTimeList) {
            // 轉成String，用於查詢
            String dateStr = DateUtil.formatDate(dateTime);
            Integer count = articleService.selectCountByDate(dateStr);
            countList.add(count);
        }
        lineMap.put("count", countList);

        return Result.success(lineMap);
    }

}
