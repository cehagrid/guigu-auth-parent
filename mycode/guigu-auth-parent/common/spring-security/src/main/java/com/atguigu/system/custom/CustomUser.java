package com.atguigu.system.custom;

import com.atguigu.model.system.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

//自定义User类
public class CustomUser extends User {

    //设置一个属性数据封装数据库中的用户信息
    private SysUser sysUser;

    public CustomUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(),sysUser.getPassword(),authorities);
        //给当前对象的sysUser属性赋值
        this.sysUser = sysUser;
    }

    public SysUser getSysUser(){
        return sysUser;
    }

    public void setSysUser(SysUser sysUser){
        this.sysUser = sysUser;
    }
}
