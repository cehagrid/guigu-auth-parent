package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysUser;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    //根据用户名查询数据库实现用户认证
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用SysUserService中根据用户名查询SysUser对象的方法
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //判断用户是否被禁用
        if(sysUser.getStatus() == 0){
            throw new RuntimeException("该用户已被禁用");
        }
        return new CustomUser(sysUser, Collections.emptyList());
    }
}
