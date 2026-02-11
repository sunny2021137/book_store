package com.example.springboot.controller;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

    //   /selectById/1
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    //  查詢所有的數據：路徑/employee/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Employee> list = employeeService.selectAll();
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Employee> pageInfo = employeeService.selectPage(pageNum, pageSize);
        return Result.success(pageInfo);

    }

    //  RequestBody將前端傳來的jason轉成java對象或數組
    @PostMapping("/add")
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result update(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success();
    }

    //   /selectPathVariable/1/101
    @GetMapping("/selectPathVariable/{id}/{no}")
    public Result selectPathVariable(@PathVariable Integer id, @PathVariable String no){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }
    //    /selectParam?id=1&no=101
    @GetMapping("/selectParam")
    public Result selectParam(@RequestParam Integer id, @RequestParam(required = false) String no){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

//    參數是一個entity，可以接受多個參數，作為entity裡的變數(不必每個entity裡的變數都傳，entity裡不包含的會被忽略)
    @GetMapping("/selectList")
    public Result selectList(Employee employee){
        List<Employee> list = employeeService.selectList(employee);
        return Result.success(list);
    }


}
