package com.atguigu.system.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    /**
     * 菜单树形数据
     * @return
     */
//    List<SysMenu> findNodes();
    List<SysMenu> findMenuNodes();
    void deleteById(Long id);
    List<SysMenu> getRoleMenuList(Long roleId);
    void assignMenu(AssignMenuVo assignMenuVo);
}
