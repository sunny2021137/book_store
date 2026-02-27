package com.example.springboot.controller;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Department;
import com.example.springboot.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/department")
public class DepartmentController {
    @Resource
    private DepartmentService departmentService;

    //   /selectById/1
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }

    //  查詢所有的數據：路徑/department/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(Department department){
        List<Department> list = departmentService.selectAll(department);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            Department department,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){

        PageInfo<Department> pageInfo = departmentService.selectPage(department, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    //  RequestBody將前端傳來的jason轉成java對象或數組
    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result update(@PathVariable Integer id) {
        departmentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result update(@RequestBody List<Integer> ids) {
        departmentService.deleteBatch(ids);
        return Result.success();
    }

}
