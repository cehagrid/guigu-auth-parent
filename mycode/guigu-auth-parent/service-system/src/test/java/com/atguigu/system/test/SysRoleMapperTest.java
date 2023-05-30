package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /*
        测试查询所有
    */
    @Test
    public void testSelectList(){
        //调用SysRoleMapper中查询所有的方法
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
        sysRoleList.forEach(System.out::println);
    }

    /*
        测试添加
    */
    @Test
    public void testInsert(){
        //创建SysRole对象
        SysRole sysRole = new SysRole("潘金莲2", "nj", "技师");
        //调用SysRoleMapper中插入的方法
        int insert = sysRoleMapper.insert(sysRole);
        System.out.println(insert>0?"插入成功":"插入失败");
        //获取sysRole对象的id值
        System.out.println("sysRole.getId() = " + sysRole.getId());
    }

    /*
        测试查询一个
    */
    @Test
    public void testSelectById(){
        SysRole sysRole = sysRoleMapper.selectById(9);
        System.out.println("sysRole = " + sysRole);
    }

    /*
        测试更新
    */
    @Test
    public void testUpdate(){
        SysRole sysRole = new SysRole();
        //设置id
        sysRole.setId(9L);
        //设置更新的属性
        sysRole.setRoleName("武大");
        sysRole.setRoleCode("csy");
        sysRole.setDescription("大郎，该吃药了");
        //调用更新的方法
        int i = sysRoleMapper.updateById(sysRole);
        System.out.println(i>0?"更新成功":"更新失败");
    }

    /*
        测试删除，是逻辑删除
    */
    @Test
    public void testDelete(){
        //调用SysRoleMapper中删除的方法
        int i = sysRoleMapper.deleteById(9);
    }

    /*
        测试带条件的查询
    */
    @Test
    public void testQueryWrapper(){
        //创建QueryWrapper对象
        QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
        //封装查询条件
        //查询role_code为nj并且description为技师的数据
//        sysRoleQueryWrapper.eq("role_code","nj").eq("description","技师");
        //查询role_code为nj或者role_code为csy的数据
//        sysRoleQueryWrapper.eq("role_code","nj").or().eq("role_code","csy");
        //查询role_name中包含 管理 的数据
//        sysRoleQueryWrapper.like("role_name","管理");
        //查询role_name以 系统 开头的数据
//        sysRoleQueryWrapper.likeRight("role_name","系统");
        //查询role_name以 莲 结尾的数据
//        sysRoleQueryWrapper.likeLeft("role_name","莲");
        //查询id值大于等于8的数据
        sysRoleQueryWrapper.ge("id",8L);
        //指定查询的字段
        sysRoleQueryWrapper.select("id","role_name","role_code");
        //调用SysRoleMapper中的selectList方法
        List<SysRole> sysRoles = sysRoleMapper.selectList(sysRoleQueryWrapper);
        sysRoles.forEach(System.out::println);
    }

    /*
        测试使用LambdaQueryWrapper测试待条件的查询
    */
    @Test
    public void testLambdaQueryWrapper(){
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getDescription,"大郎，该吃药了");
        List<SysRole> sysRoles = sysRoleMapper.selectList(sysRoleLambdaQueryWrapper);
        sysRoles.forEach(System.out::println);
    }
    
    /*
        测试分页
    */
    @Test
    public void testPage(){
        //创建Page对象
        Page<SysRole> page = new Page<>(3, 2);
        //调用SysRoleMapper中分页的方法
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(page, null);
        //获取当前页
        System.out.println("sysRolePage.getCurrent() = " + sysRolePage.getCurrent());
        //获取每页显示的条数
        System.out.println("sysRolePage.getSize() = " + sysRolePage.getSize());
        //获取总页数
        System.out.println("sysRolePage.getPages() = " + sysRolePage.getPages());
        //总记录数
        System.out.println("sysRolePage.getTotal() = " + sysRolePage.getTotal());
        System.out.println("当前页的数据是：");
        List<SysRole> records = sysRolePage.getRecords();
        for (SysRole record : records) {
            System.out.println("record = " + record);
        }

    }
}
