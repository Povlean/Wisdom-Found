package com.ean.usercenter.service;

import com.ean.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Asphyxia
* @description 针对表【user】的数据库操作Service
* @createDate 2022-11-20 11:52:25
*/
public interface UserService extends IService<User> {
    long userRegister(String userAccount, String userPassword, String checkPassword);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getSafetyUser(User user);

    Integer userLogout(HttpServletRequest request);

    List<User> searchUsersByTags(List<String> tagListNames);

    /**
    * @description:是否为管理员
    * @author:Povlean
    * @date:2023/2/4 20:52
    * @param:* @param request
    * @return:* @return boolean
    */
    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    /**
    * @description:获取当前用户
    * @author:Povlean
    * @date:2023/2/4 20:53
    * @param:* @param request
    * @return:* @return User
    */
    User getCurrentUser(HttpServletRequest request);

    /**
    * @description:更新用户信息
    * @author:Povlean
    * @date:2023/2/4 20:53
    * @param:* @param user
     * @param loginUser
    * @return:* @return int
    */
    int updateUser(User user,User loginUser);

    List<User> matchUsers(long num, User loginUser);

}
