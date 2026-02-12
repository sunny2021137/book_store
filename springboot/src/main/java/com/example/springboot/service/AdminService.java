package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public List<Admin> selectList(Admin admin) {
        System.out.println(admin);
        return null;
    }


    public void add(Admin admin) {
        // 檢查帳號是否註冊過
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin != null) {
            throw new CustomException("500", "帳號已存在，請更換別的帳號");
        }
        // 密碼沒填，預設123
        if(StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("admin");
        }
        // 名字沒填，預設成和帳號名一樣
        if(StrUtil.isBlank(admin.getName())) {
           admin.setName(admin.getUsername());
        }
        // 角色設置成管理員
        admin.setRole("ADMIN");
        // inset
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for(Integer id: ids){
            this.deleteById(id);
        }
    }

    public Admin login(Account admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin == null) {
            throw new CustomException("500", "帳號不存在");
        }
        String password = admin.getPassword();
        if(!dbAdmin.getPassword().equals(password)){
            throw new CustomException("500", "帳號或密碼錯誤");
        }
        return dbAdmin;
    }

}
