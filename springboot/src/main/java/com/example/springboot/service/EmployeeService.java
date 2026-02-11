package com.example.springboot.service;

import com.example.springboot.entity.Employee;
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

    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll();
        return PageInfo.of(list);
    }

    public List<Employee> selectList(Employee employee) {
        System.out.println(employee);
        return null;
    }


    public void add(Employee employee) {
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }
}
