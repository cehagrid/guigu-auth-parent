package com.atguigu.system.config;

import com.atguigu.system.filter.TokenAuthenticationFilter;
import com.atguigu.system.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication//声明当前类是一个配置类
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨域请求csrf
        http.csrf().disable();
        //设置哪些请求地址不需要认证,哪些地址需要认证
        http.authorizeRequests().antMatchers("/admin/system/index/login","/admin/system/index/info","/admin/system/index/logout").permitAll()
                .anyRequest().authenticated();
        //添加过滤器
        http.addFilter(new TokenLoginFilter(authenticationManager(),redisTemplate));
        //设置TokenAuthenticationFilter过滤器在UsernamePasswordAuthenticationFilter之前执行
        http.addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico","/swagger-resources/**", "/webjars/**", "/v2/**", "/doc.html");
    }
}
