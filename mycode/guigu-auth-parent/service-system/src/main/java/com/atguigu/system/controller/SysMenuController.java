package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @ApiOperation("获取菜单树")
    @GetMapping("/findMenuNodes")
    public Result findMenuNodes(){
        List<SysMenu> sysMenuList = sysMenuService.findMenuNodes();
        return Result.ok(sysMenuList);
    }

    @ApiOperation("添加子节点")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        //设置更新日期
        sysMenu.setUpdateTime(null);
        //调用SysMenuService中保存的方法
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation("删除节点")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        //调用SysMenuService中根据id删除菜单的方法
        sysMenuService.deleteById(id);
        return Result.ok();
    }

    @ApiOperation("根据id查询节点")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        //调用SysMenuService中根据id查询的方法
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }

    @ApiOperation("更改节点")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu){
        //设置更新日期
        sysMenu.setUpdateTime(null);
        //调用SysMenuService中根据id更新的方法
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation("根据角色id查询角色的菜单")
    @GetMapping("getRoleMenuList/{roleId}")
    public Result getRoleMenuList(@PathVariable Long roleId){
        //调用SysMenuService中根据角色id获取角色权限菜单的方法
        List<SysMenu> sysMenuList = sysMenuService.getRoleMenuList(roleId);
        return Result.ok(sysMenuList);
    }

    @ApiOperation("给角色分配权限")
    @PostMapping("/assignMenu")
    public Result assignMenu(@RequestBody AssignMenuVo assignMenuVo){
        //调用SysMenuService中给角色分配权限的方法
        sysMenuService.assignMenu(assignMenuVo);
        return Result.ok();
    }

}
