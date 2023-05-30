package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "登录接口")
@RequestMapping("/admin/system/index/")
@RestController
public class IndexController {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    SysUserService sysUserService;
//    @ApiOperation("登录")
//    @PostMapping("/login")
//    public Result login(){
//        //需要返回的数据
//        //创建一个map
//        Map map = new HashMap<>();
//        map.put("token","admin-token");
//        return Result.ok(map);
//    }

    @ApiOperation("连接数据库的登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo){
        //获取用户名和密码
        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        //验证用户名和密码是否为空
        if(username == null || username == "" || password == null || password == ""){
            return Result.build(null, ResultCodeEnum.USERNAME_PASSWORD_NOT_EMPTY);
        }
        //根据用户名查询数据库得到SysUser对象
        SysUser sysUser = sysUserService.getSysUserByUsername(username);
        //判断是否为null
        if(sysUser == null){
            //该用户不存在
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }
        //验证用户输入的密码是否正确
        if(!MD5.encrypt(password).equalsIgnoreCase(sysUser.getPassword())){
            //密码错误
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }
        //判断用户是否被锁定
        if (sysUser.getStatus() == 0){
            //用户被锁定
            return Result.build(null,ResultCodeEnum.ACCOUNT_STOP);
        }

        //使用UUID生成一个随机字符串为token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        //创建一个map
        Map map = new HashMap<>();
        map.put("token",token);

        //将用户信息保存到redis中,并设置它的有效时间为两个小时
        redisTemplate.boundValueOps(token).set(sysUser,2, TimeUnit.HOURS);
        return Result.ok(map);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
//    public Result info(){
//        //需要返回的数据：{"code":200,"data":{"roles":["admin"],"introduction":"I am a super administrator","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif","name":"Super Admin"}}
//        //创建一个Map
//        Map map = new HashMap<>();
//        //设置头像地址
////        map.put("avatar","https://img.soogif.com/cU0COGpwtMdSaInr4LfpeTU0UnjOfCrY.gif");
////        map.put("avatar","http://n.sinaimg.cn/sinacn/w400h221/20171209/471d-fypnsip3380210.gif");
//        map.put("avatar","https://pic2.zhimg.com/v2-6b8326957d90ef3a43be93cadd9c9b39_b.webp");
//        //设置用户名
//        map.put("name","超级管理员");
//        return Result.ok(map);
//    }
    public Result info(HttpServletRequest request){
        //获取请求头中的token
        String token = request.getHeader("token");
        //从Redis中获取用户信息
        SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
//        Map map = new HashMap();
        //调用SysUserService中根据用户id获取权限菜单的方法
        Map<String,Object> map = sysUserService.getMenuListByUserId(sysUser.getId());
        //设置头像地址
        map.put("avatar",sysUser.getHeadUrl());
        //设置用户名
        map.put("name",sysUser.getUsername());
        return Result.ok(map);
    }


    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
