package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@Api("用户管理系统")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("分页及带条件的查询方法")
    @RequestMapping(value = "/{current}/{size}")
    public Result getPageList(@PathVariable Long current, @PathVariable Long size, SysUserQueryVo userQueryVo){
        //创建Page对象
        Page<SysUser> sysUserPage = new Page<>(current, size);
        //创建QueryWrapper对象
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        //获取查询条件
        String keyword = userQueryVo.getKeyword();
        String createTimeBegin = userQueryVo.getCreateTimeBegin();
        String createTimeEnd = userQueryVo.getCreateTimeEnd();
        //判断关键字是否为空
        if(keyword != null && keyword != ""){
//            sysUserQueryWrapper.like("username",keyword).or().like("name",keyword).or().like("phone",keyword);
            sysUserQueryWrapper.and(QueryWrapper->QueryWrapper.like("username",keyword).or().like("name",keyword).or().like("phone",keyword));
        }
        //判断开始时间和结束时间是否为空
        if(createTimeBegin!= null && createTimeBegin!= "" && createTimeEnd != null && createTimeEnd != ""){
            sysUserQueryWrapper.ge("create_time",createTimeBegin).le("create_time",createTimeEnd);
        }
        //调用sysUserService中的分页方法
        IPage<SysUser> iPage = sysUserService.page(sysUserPage,sysUserQueryWrapper);
        return Result.ok(iPage);
    }

    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser sysUser){
        //使用MD5加密
        sysUser.setPassword(MD5.encrypt(sysUser.getPassword()));
        //调用SysUserService中保存的方法
        boolean save = sysUserService.save(sysUser);
        if(save) {
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation("根据id删除用户")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        //调用SysUserService中根据id删除的方法
        boolean b = sysUserService.removeById(id);
        if(b){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id) {
        //根据sysUserService中方法查询
        SysUser sysUser = sysUserService.getById(id);
        return Result.ok(sysUser);
    }

    @ApiOperation("根据id更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        //更新时把日期也更新
        sysUser.setUpdateTime(null);
        //跟据sysUserService中根据用户id更新用户信息的方法
        boolean b = sysUserService.updateById(sysUser);
        if(b){
            return Result.ok();
        }else{
            return Result.fail();
        }
    }

    @ApiOperation("更新滑块")
    @GetMapping("/updateStatus/{userId}/{status}")
    public Result updateStatus(@PathVariable Long userId,@PathVariable Integer status){
        //创建SysUser对象
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setStatus(status);
        //调用sysUserService的更新的方法
        sysUserService.updateById(sysUser);
        return Result.ok();
    }
}
