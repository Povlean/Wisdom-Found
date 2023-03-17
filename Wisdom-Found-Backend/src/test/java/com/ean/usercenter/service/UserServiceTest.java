package com.ean.usercenter.service;

import com.ean.usercenter.model.domain.User;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
* @description:用户功能测试
* @author:Povlean
* @date:2022/11/21 8:20
* @param:* @param null
* @return:
*/
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser(){
        User user = new User();
        user.setUsername("Demo");
        user.setUserAccount("111111");
        user.setAvatarUrl("https://cn.bing.com/images/search?q=%E8%8F%85%E7%94%B0%E5%B0%86%E6%99%96%E5%A4%B4%E5%83%8F&FORM=IQFRBA&id=9225C0B966C875A3B5B164882D04D97FCBDC6F4B");
        user.setUserPassword("lhj2001124");
        user.setPhone("17780510628");
        user.setEmail("1927079760@qq.com");
        boolean flag = userService.save(user);
        System.out.println(user);
        Assertions.assertTrue(flag);
    }

    @Test
    public void testRegister(){
        String userAccount = "admin";
        String userPassword = "123456789l";
        String checkPassword = "123456789l";
        long userId = userService.userRegister(userAccount, userPassword, checkPassword);
        System.out.println(userId);
    }

    /*
        测试账户：jmring23
        测试密码：123456789l
        admin 123456789l
     */

    /*@Test
    public void testLogin(){
        String userAccount = "jmring23";
        String userPassword = "123456789l";
        User user = userService.userLogin(userAccount, userPassword,null);
        System.out.println(user.toString());
    }*/

    @Test
    public void testSearchUsersByTags(){
        List<String> tagList = Arrays.asList("java","python");
        List<User> userList = userService.searchUsersByTags(tagList);
        Assert.assertNotNull(userList);
    }

}