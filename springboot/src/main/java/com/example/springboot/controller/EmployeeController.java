package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Department;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.DepartmentService;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.IO.print;

@RestController
@RequestMapping ("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;
    @Resource
    private DepartmentService departmentService;

    //   /selectById/1
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id){
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    //  查詢所有的數據：路徑/employee/selectAll
    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            Employee employee,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){

        PageInfo<Employee> pageInfo = employeeService.selectPage(employee, pageNum, pageSize);
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

    @DeleteMapping("/deleteBatch")
    public Result update(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
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

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 查員工資料
        List<Employee> employeeList = employeeService.selectAll(null);

        // 1. writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 2. 設置欄位別名，只導出有別名的
        writer.addHeaderAlias("username", "帳號");
        writer.addHeaderAlias("name", "名稱");
        writer.addHeaderAlias("sex", "性別");
        writer.addHeaderAlias("no", "工號");
        writer.addHeaderAlias("age", "年齡");
        writer.addHeaderAlias("description", "介紹");
        writer.addHeaderAlias("departmentName", "部門");

        writer.setOnlyAlias(true);
        // 3. 資料放入writer
        writer.write(employeeList, true);

        // 4.1 瀏覽器響應格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        // 4.2 表頭設定文件名
        String fileName = URLEncoder.encode("員工信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 5. flush(os)
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        // 6. close
        writer.close();
    }

    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception{
        // 1. 取得inputStream
        InputStream inputStream = file.getInputStream();
        // 2. 構建Excel Reader
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 3. 設定別名
        reader.addHeaderAlias("帳號", "username");
        reader.addHeaderAlias("名稱", "name");
        reader.addHeaderAlias("性別", "sex");
        reader.addHeaderAlias("工號", "no");
        reader.addHeaderAlias("年齡", "age");
        reader.addHeaderAlias("介紹", "description");
        reader.addHeaderAlias("部門", "departmentName");
        // 4. 讀取
        List<Employee> employeeList = reader.readAll(Employee.class);

        // 建立部門名稱和id對應Map
        List<Department> departmentList = departmentService.selectAll(null);
        Map<String, Integer> deptMap = departmentList.stream()
                .collect(Collectors.toMap(Department::getName, Department::getId, (v1, v2) -> v1));

        // 5. 更新資料庫
        for(Employee employee: employeeList){
            // 設定部門id
            Integer departmentId = deptMap.get(employee.getDepartmentName());
            employee.setDepartmentId(departmentId);
            // insert
            employeeService.add(employee);
        }
        return Result.success();
    }

}
