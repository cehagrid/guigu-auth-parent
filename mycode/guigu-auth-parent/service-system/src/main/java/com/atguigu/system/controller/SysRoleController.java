package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.exception.GuiguException;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/findAll")
    public List<SysRole> findAll() {
//        try {
//            int i = 10 / 0;
//        } catch (Exception e) {
//            throw new GuiguException(2023,"出现了数学异常");
//        }
        List<SysRole> sysRoleList = sysRoleService.list();
        return sysRoleList;
    }

    @ApiOperation("查询所有角色")
    @GetMapping("/getSysRoles")
    public Result getSysRoles() {
        //调用SysRoleService中查询所有的方法
        List<SysRole> sysRoleList = sysRoleService.list();
        return Result.ok(sysRoleList);
    }

  /*  @ApiOperation(value = "新增角色")
    @PostMapping("/save")
    public Result save(@RequestBody @Validated SysRole role) {
        sysRoleService.save(role);
        return Result.ok();
    }*/

    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result addSysRole(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询角色")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        //调用SYSRoleService中根据id查询的方法
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @ApiOperation("根据id更新角色信息")
    @PostMapping("/updateById")
    public Result updateById(@RequestBody SysRole sysRole) {
        //重置更新时间
        //方式1:
//        sysRole.setUpdateTime(new Date());
        //方式2:
        sysRole.setUpdateTime(null);
        boolean b = sysRoleService.updateById(sysRole);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Long id) {
        boolean b = sysRoleService.removeById(id);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Long> ids) {
        //调用SysRoleService中批量删除的方法
        boolean b = sysRoleService.removeByIds(ids);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("分页及带条件的查询,自定义sql语句的方法")
    @GetMapping("/{current}/{size}")
    public Result getPage(
            @ApiParam(name = "current", value = "当前页", required = true)
            @PathVariable Long current,
            @ApiParam(name = "size", value = "每页显示的条数", required = true)
            @PathVariable Long size,
            @ApiParam(name = "roleQueryVo", value = "查询条件", required = false)
            SysRoleQueryVo roleQueryVo) {
        //创建一个Page对象
        Page<SysRole> sysRolePage = new Page<>(current, size);
        //调用SysRoleService中分页及带条件查询的方法
        IPage<SysRole> iPage = sysRoleService.findPage(sysRolePage, roleQueryVo);
        return Result.ok(iPage);
    }



    @ApiOperation("分页及带条件的查询")
    @GetMapping("/getPageByMyBatisPlus/{current}/{size}")
    public Result getPageByMyBatisPlus(@PathVariable Long current , @PathVariable Long size , SysRoleQueryVo roleQueryVo) {
        //创建一个Page对象
        Page<SysRole> sysRolePage = new Page<>(current, size);
        //创建QueryWrapper对象
        QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
        //获取查询条件
        String roleName = roleQueryVo.getRoleName();
        //判断查询条件是否为空
        if (roleName != null && roleName != "") {
            //封装查询条件
            sysRoleQueryWrapper.like("role_name", roleName);
        }
        //调用SysRoleService中自带的分页的方法
        IPage<SysRole> iPage = sysRoleService.page(sysRolePage,sysRoleQueryWrapper);
        return Result.ok(iPage);
    }

    @ApiOperation("根据用户id查询用户已分配的角色")
    @GetMapping("/getRolesByUserId/{userId}")
    public Result getRolesByUserId(@PathVariable Long userId){
        //调用SysRoleService中根据用户ID查询用户已分配角色的方法
        Map<String ,Object> returnMap = sysRoleService.getRolesByUserId(userId);
        return Result.ok(returnMap);
    }

    @ApiOperation("给用户分配角色")
    @PostMapping("/assignRoles")
    public Result assignRoles(@RequestBody AssignRoleVo assignRoleVo){
        //调用给用户分配角色的SysRoleService
        sysRoleService.assignRoles(assignRoleVo);
        return Result.ok();
    }
}