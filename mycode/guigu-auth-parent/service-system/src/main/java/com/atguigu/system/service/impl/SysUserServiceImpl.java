package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.helper.RouterHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

@Autowired
private SysMenuMapper sysMenuMapper;

    @Override
    public IPage<SysUser> getPageList(Page<SysUser> sysUserPage, SysUserQueryVo userQueryVo) {
        return baseMapper.getPageList(sysUserPage,userQueryVo);
    }

    @Override
    public SysUser getSysUserByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username",username));
    }

//    @Override
//    public Map<String, Object> getMenuListByUserId(Long id) {
//        //调用当前类中根据用户id查询用户权限菜单的方法
//        List<SysMenu> menuList = getUserMenuByUserId(id);
//        //将权限菜单转化为菜单树
//        List<SysMenu> sysMenuTree = MenuHelper.buildTree(menuList);
//        //将权限菜单树转化为路由
//        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenuTree);
//        //创建一个返回的Map
//        Map<String,Object> returnMap = new HashMap<>();
//        //将路由器放到Map中
//        returnMap.put("routers",routerVos);
//        //调用根据用户id获取用户的按钮权限标识符的方法获取按钮权限标识符
//        List<String> userBtnPermsByUserId = getUserBtnPermsByUserId(id);
//        //将用户的按钮权限标识符放到map中
//        returnMap.put("buttons",userBtnPermsByUserId);
//        //返回Map
//        return returnMap;
//    }

    @Override
    public Map<String, Object> getMenuListByUserId(Long id) {
        //调用当前类中根据用户id查询用户权限菜单的方法
        List<SysMenu> menuList = getUserMenuByUserId(id);
        //将权限菜单转化为菜单树
        List<SysMenu> sysMenuTree = MenuHelper.buildTree(menuList);
        //将权限菜单树转化为路由
        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenuTree);
        //创建一个返回的Map
        Map<String,Object> returnMap = new HashMap<>();
        //将路由器放到Map中
        returnMap.put("routers",routerVos);
        //调用根据用户id获取用户的按钮权限标识符的方法获取按钮权限标识符
        List<String> userBtnPermsByUserId = getUserBtnPermsByUserId(id);
        //将用户的按钮权限标识符放到map中
        returnMap.put("buttons",userBtnPermsByUserId);
        //返回Map
        return returnMap;
    }

    @Override
    public List<String> getUserBtnPermsByUserId(Long id) {
        //调用当前类中国根据用户id查询用户权限菜单的方法
        List<SysMenu> menuList = getUserMenuByUserId(id);
        //创建一个保存用户的权限按钮标识符的List
        List<String> userBtnPerms = new ArrayList<>();
        //遍历所有的权限菜单
        for (SysMenu sysMenu : menuList){
            if (sysMenu.getType() == 2){
                //证明当前节点是按钮
                String perms = sysMenu.getPerms();
                if(perms != null && perms != ""){
                    //将标识符添加到userBtnPerms
                    userBtnPerms.add(perms);
                }
            }
        }
        return userBtnPerms;
    }

    //根据用户id获取用户权限菜单的方法
    List<SysMenu> getUserMenuByUserId(Long userId) {
        List<SysMenu> menuList = null;
        //判断当前用户是否是超级管理员
        if(userId.longValue() == 1){
            //证明是系统管理员,获取所有的菜单
            menuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status",1).orderByAsc("sort_value"));
        }else{
            //根据用户id查询用户的权限菜单
            menuList = sysMenuMapper.getUserMenuListByUserId(userId);
        }
        return menuList;
    }
}
