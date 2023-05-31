package com.atguigu.system.filter;

import com.atguigu.common.result.Result;
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.custom.CustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//自定义过滤器实现用户认证
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter{

    private RedisTemplate redisTemplate;
    //
    public TokenLoginFilter(AuthenticationManager authenticationManager,RedisTemplate redisTemplate){
        //设置认证管理器
        this.setAuthenticationManager(authenticationManager);
        //设置登录的请求地址和请求方式
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            //获取LoginVo对象
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(), LoginVo.class);
            //创建一个需要认证的对象
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginVo.getUsername(),loginVo.getPassword());
            //委托认证管理器getAuthenticationManager查询数据库进行认证
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //认证成功的方法
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //获取CustomUser对象
        CustomUser customUser = (CustomUser) authResult.getPrincipal();
        //从customUser对象中获取SysUser对象
        SysUser sysUser = customUser.getSysUser();
        //使用UUID生成一个随机字符串作为token
        String token = UUID.randomUUID().toString().replaceAll("-","");
        //将用户信息保存到Redis中,并设置它的有效时长为2个小时
        redisTemplate.boundValueOps(token).set(sysUser,2, TimeUnit.HOURS);
        //创建一个Map
        Map map = new HashMap<>();
        map.put("token",token);
        //将map响应到前端
        ResponseUtil.out(response,Result.ok(map));
    }

    //认证失败的方法
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //将异常信息响应到前端
        ResponseUtil.out(response,Result.build(null,444,failed.getMessage()));
    }
}
