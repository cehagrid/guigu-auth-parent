package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.exception.GuiguException;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper,SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> findMenuNodes() {
        //调用SysMenuMapper中的获取所有的方法
        List<SysMenu> sysMenuList = baseMapper.selectList(new QueryWrapper<SysMenu>().eq("status", 1));

        //通过MenuHelper工具类将菜单集合转换为菜单树
        List<SysMenu> sysMenusTree = MenuHelper.buildTree(sysMenuList);
        return sysMenusTree;
    }

    @Override
    public void deleteById(Long id) {
        //根据id查询当前节点是否有子节点
        Integer count = baseMapper.selectCount(new QueryWrapper<SysMenu>().eq("parent_id",id));
        if(count > 0){
            //抛出异常
            throw new GuiguException(ResultCodeEnum.NODE_ERROR);
        }else{
            //删除该节点
            baseMapper.deleteById(id);
        }
    }

    @Override
    public List<SysMenu> getRoleMenuList(Long roleId) {
        //获取所有的权限菜单
        List<SysMenu> sysMenuList = baseMapper.selectList(new QueryWrapper<SysMenu>().eq("status",1));

        //根据角色id查询中间表获得角色已分配的权限菜单
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        //获取所有的角色已分配的权限菜单
        List<Long> sysRoleMenuIds = sysRoleMenus.stream().map(SysRoleMenu -> SysRoleMenu.getMenuId()).collect(Collectors.toList());
        //遍历所有的菜单
        for(SysMenu sysMenu: sysMenuList){
            //获取当前权菜单的id
            Long id = sysMenu.getId();
            //判断sysRoleMenuIds中是否包含当前id
            if(sysRoleMenuIds.contains(id)){
                //设置当前菜单被选中
                sysMenu.setSelect(true);
            }else{
                sysMenu.setSelect(false);
            }
        }

        //将权限菜单转换为菜单树
        List<SysMenu> sysMenuTree = MenuHelper.buildTree(sysMenuList);
        return sysMenuTree;
    }

    @Override
    public void assignMenu(AssignMenuVo assignMenuVo) {
        //获取角色id
        Long roleId = assignMenuVo.getRoleId();
        //根据角色id删除中间表之前分配的权限
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        //获取所有分配的菜单id
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        if (menuIdList != null && menuIdList.size() > 0) {
            //遍历所有的菜单id
            for (Long menuId : menuIdList){
                //创建SysRoleMenu对象
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                //设置角色id
                sysRoleMenu.setRoleId(roleId);
                //设置菜单id
                sysRoleMenu.setMenuId(menuId);
                //向中间表中插入数据
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }


}
