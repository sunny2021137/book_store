package com.example.springboot.controller;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    //   /selectById/1
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    //  查詢所有的數據：路徑/admin/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin){
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            Admin admin,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){

        PageInfo<Admin> pageInfo = adminService.selectPage(admin, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    //  RequestBody將前端傳來的jason轉成java對象或數組
    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }

    @DeleteMapping("/deleteById/{id}")
    public Result update(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result update(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success();
    }

    //   /selectPathVariable/1/101
    @GetMapping("/selectPathVariable/{id}/{no}")
    public Result selectPathVariable(@PathVariable Integer id, @PathVariable String no){
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }
    //    /selectParam?id=1&no=101
    @GetMapping("/selectParam")
    public Result selectParam(@RequestParam Integer id, @RequestParam(required = false) String no){
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

//    參數是一個entity，可以接受多個參數，作為entity裡的變數(不必每個entity裡的變數都傳，entity裡不包含的會被忽略)
    @GetMapping("/selectList")
    public Result selectList(Admin admin){
        List<Admin> list = adminService.selectList(admin);
        return Result.success(list);
    }


}
