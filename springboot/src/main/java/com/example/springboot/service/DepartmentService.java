package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Department;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;

    public List<Department> selectAll(Department department) {
        return departmentMapper.selectAll(department);
    }

    public Department selectById(Integer id) {
        return departmentMapper.selectById(id);
    }

    public PageInfo<Department> selectPage(Department department, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(department);
        return PageInfo.of(list);
    }

    public List<Department> selectList(Department department) {
        System.out.println(department);
        return null;
    }


    public void add(Department department) {
        // inset
        departmentMapper.insert(department);
    }

    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids){
            this.deleteById(id);
        }
    }


}
