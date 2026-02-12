package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }

    public List<Employee> selectList(Employee employee) {
        System.out.println(employee);
        return null;
    }


    public void add(Employee employee) {
        // 檢查帳號是否註冊過
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee != null) {
            throw new CustomException("500", "帳號已存在，請更換別的帳號");
        }
        // 密碼沒填，預設123
        if(StrUtil.isBlank(employee.getPassword())) {
            employee.setPassword("123");
        }
        // 名字沒填，預設成和帳號名一樣
        if(StrUtil.isBlank(employee.getName())) {
           employee.setName(employee.getUsername());
        }
        // 角色設置成EMP
        employee.setRole("EMP");
        // inset
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids){
            this.deleteById(id);
        }
    }

    public Employee login(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee == null) {
            throw new CustomException("500", "帳號不存在");
        }
        String password = employee.getPassword();
        if(!dbEmployee.getPassword().equals(password)){
            throw new CustomException("500", "帳號或密碼錯誤");
        }
        return dbEmployee;
    }

    public void register(Employee employee) {
        this.add(employee);
    }
}
