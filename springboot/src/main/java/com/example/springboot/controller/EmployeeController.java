package com.example.springboot.controller;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping ("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;

//    查詢所有的數據：路徑/employee/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Employee> list = employeeService.selectAll();
        return Result.success(list);
    }
}
