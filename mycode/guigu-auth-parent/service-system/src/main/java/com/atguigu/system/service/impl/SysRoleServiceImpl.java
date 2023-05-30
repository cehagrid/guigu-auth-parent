package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserRoleMapper;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Resource SysUserRoleMapper sysUserRoleMapper;
    @Override
    public IPage<SysRole> findPage(Page<SysRole> sysRolePage, SysRoleQueryVo roleQueryVo) {
        IPage<SysRole> page = baseMapper.findPage(sysRolePage, roleQueryVo);
        return page;
    }

    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        //获取所有的角色
        List<SysRole> sysRoles = baseMapper.selectList(null);
        //根据用户id查询中间表获得已分配的角色
        //创建queryWrapper对象
        QueryWrapper<SysUserRole> sysUserRoleQueryWrapper = new QueryWrapper<>();
        //封装查询条件
        sysUserRoleQueryWrapper.eq("user_id",userId);
        //根据条件查询中间表获取SysUserRole对象
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.selectList(sysUserRoleQueryWrapper);
//        //创建一个保存用户已分配的角色的id的List
//        List<Long> userRoleIds = new ArrayList<>();
//        //遍历sysUserRoleList
//        for(SysUserRole sysUserRole :sysUserRoleList){
//            userRoleIds.add(sysUserRole.getRoleId());
//        }
        List<Long> userRoleIds = sysUserRoleList.stream().map(sysUserRole -> sysUserRole.getRoleId()).collect(Collectors.toList());
        //创建一个返回的Map
        Map<String, Object> returnMap = new HashMap();
        //将所有的角色对象放到Map中
        returnMap.put("allRoles",sysRoles);
        //将所有用户已分配的角色id放到map中
        returnMap.put("userRoleIds",userRoleIds);
        return returnMap;
    }

    @Override
    public void assignRoles(AssignRoleVo assignRoleVo) {
        //获取用户id
        Long userId = assignRoleVo.getUserId();
        //根据用户id删除中间表达中已分配的角色
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id",userId));
        //获取现在分配的角色id
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        if(roleIdList != null && roleIdList.size() > 0) {
            //遍历得到每一个角色id
            for (Long roleId : roleIdList) {
                //创建SYSUserRole对象
                SysUserRole sysUserRole = new SysUserRole();
                //设置用户id
                sysUserRole.setUserId(userId);
                //设置角色id
                sysUserRole.setRoleId(roleId);
                //调用SysUserRoleMapper中插入的方法
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }
}
