package com.ean.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ean.usercenter.common.BaseResponse;
import com.ean.usercenter.common.ErrorCode;
import com.ean.usercenter.common.ResultUtils;
import com.ean.usercenter.exception.BusinessException;
import com.ean.usercenter.model.domain.User;
import com.ean.usercenter.model.request.LoginRequest;
import com.ean.usercenter.model.request.RegisterRequest;
import com.ean.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ean.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.ean.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @description:业务逻辑层
 * @author:Povlean
 */
@RestController
@Slf4j
@RequestMapping("/user")
@CrossOrigin(origins = {"http://127.0.0.1:5173","http://localhost:8080"}, allowCredentials = "true")
public class UserController {
    @Autowired
    private UserService userService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody RegisterRequest registerRequest){
        if(registerRequest == null){
            System.out.println("registerRequest is null");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 获取封装对象中的属性值
        String userAccount = registerRequest.getUserAccount();
        String userPassword = registerRequest.getUserPassword();
        String checkPassword = registerRequest.getCheckPassword();
        // 注册成功后获取userId
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        // 判断请求体是否为空
        if(request == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        if(loginRequest == null){
            System.out.println("loginRequest is null");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 获取封装对象中的属性值
        String userAccount = loginRequest.getUserAccount();
        String userPassword = loginRequest.getUserPassword();
        if(userAccount == null || userPassword == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 注册成功后获取userId
        User user = userService.userLogin(userAccount, userPassword, request);
        if(user == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(user);
    }

    /**
    * @description:获取用户的登录状态
    * @author:Povlean
    * @date:2022/11/30 15:07
    * @param:* @param request
    * @return:* @return User
    */
    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 在返回登录态之前，再次查询一次数据库，保证数据更新。
        Long userId = currentUser.getId();
        // 返回脱敏之后的user信息
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
    * @description:更新用户信息
    * @author:Povlean
    * @date:2023/2/4 20:17
    * @param:* @param user
    * @return:* @return BaseResponse<Integer>
    */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody User user,HttpServletRequest request) {
        // 1. 校验参数是否为空
        if(user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取登录态中的user信息
        User loginUser = userService.getCurrentUser(request);
        // 返回修改信息数量
        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    /**
    * @description:查询用户
    * @author:Povlean
    * @date:2022/11/23 15:12
    * @param:* @param username
    * @return:* @return List<User>
    */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username,HttpServletRequest request){
        if(!isAdmin(request)){
            System.out.println("用户权限低");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 构造sql条件语句
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(username != null,User::getUsername,username);
        List<User> users = userService.list(queryWrapper);
        // 处理集合中的userPassword属性
        // java 8 特性，下面代码相当于遍历集合
        List<User> userList = users.stream().map((user) -> {
            user.setUserPassword(null);
            return user;
        }).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    /**
    * @description:删除用户
    * @author:Povlean
    * @date:2022/11/23 15:17
    * @param:* @param id
    * @return:* @return boolean
    */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){
            System.out.println("用户权限低");
            return ResultUtils.error(ErrorCode.NO_AUTH);
        }
        if(id <= 0){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        boolean flag = userService.removeById(id);
        return ResultUtils.success(flag);
    }

    @PutMapping("/updateEmployee")
    public User updateEmployee(@RequestBody User user ,HttpServletRequest request){
        if(!isAdmin(request)) {
            System.out.println("用户权限低");
            return null;
        }
        if(user.getId() <= 0 && user.getUserStatus() != 0) {
            System.out.println("id输入有误");
            return null;
        }
        userService.updateById(user);
        return user;
    }

    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList) {
        System.out.println("do searchByTags");
        if(CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<User> userList = userService.searchUsersByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum ,HttpServletRequest request){
        User loginUser = userService.getCurrentUser(request);
        String redisKey = String.format("yupao:user:recommend:%s", loginUser.getId());
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，就直接读缓存
        Page<User> userPage = (Page<User>)valueOperations.get(redisKey);
        if(userPage != null) {
            return ResultUtils.success(userPage);
        }
        // 无缓存的情况下，使用分页查询查库
        // 因为浏览器能够承载的数据条数是有限的
        // 所以需要分页查询，将数据逐条显示
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 写缓存
        try {
            valueOperations.set(redisKey,userPage,30 * 1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error",e);
        }
        return ResultUtils.success(userPage);
    }

    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num,HttpServletRequest request) {
        if (num <= 0 || num > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        List<User> matchUsers = userService.matchUsers(num,loginUser);
        return ResultUtils.success(matchUsers);
    }

    public boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user == null || user.getUserRole() != ADMIN_ROLE){
            return false;
        }
        return true;
    }

}
