package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;

    /*
        测试查询所有
    */
    @Test
    public void testList(){
        //测试查询所有
        List<SysRole> list = sysRoleService.list();
        list.forEach(System.out::println);
        //测试批量删除
        List<Long> ids = new ArrayList<>();
        ids.add(11L);
        ids.add(12L);
        boolean b = sysRoleService.removeByIds(ids);
        System.out.println(b?"删除成功":"删除失败");
    }
}
