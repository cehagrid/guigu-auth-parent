package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysRoleMapperTest2 {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Test
    public void testSelectList(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        sysRoles.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
        int insert = sysRoleMapper.insert(new SysRole("武松", "wdt", "杀人者打虎武松也"));
        System.out.println(insert);
    }

    @Test
    public void testUpdate(){
        SysRole sysRole = new SysRole("鲁智深", "ltx", "倒拔垂杨柳");
        sysRole.setId(9L);
        int result = sysRoleMapper.updateById(sysRole);
        System.out.println(result>0?"更新成功":"更新失败");
    }

    @Test
    public void testDelete(){
        int i = sysRoleMapper.deleteById(1);
        System.out.println(i>0?"删除成功":"删除失败");
    }



}
