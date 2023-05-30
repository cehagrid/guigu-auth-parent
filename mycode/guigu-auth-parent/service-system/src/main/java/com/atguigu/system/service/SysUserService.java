package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {
    /**
     * 根据用户id获取用户登录信息
     * @param userId
     * @return
     */
//    Map<String,Object> getUserInfoByUserId(Long userId);
    /**
     * 根据用户id获取用户按钮权限标识符
     * @param id
     * @return
     */
//    List<String> getUserBtnPermsByUserId(Long id);
    IPage<SysUser> getPageList(Page<SysUser> sysUserPage, SysUserQueryVo userQueryVo);

    SysUser getSysUserByUsername(String username);

    Map<String, Object> getMenuListByUserId(Long id);

    List<String> getUserBtnPermsByUserId(Long id);
}
