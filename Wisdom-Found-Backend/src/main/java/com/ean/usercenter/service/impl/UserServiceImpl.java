package com.ean.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ean.usercenter.common.ErrorCode;
import com.ean.usercenter.constant.UserConstant;
import com.ean.usercenter.exception.BusinessException;
import com.ean.usercenter.model.domain.User;
import com.ean.usercenter.service.UserService;
import com.ean.usercenter.mapper.UserMapper;
import com.ean.usercenter.utils.AlgorithmUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.method.MethodDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static com.ean.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.ean.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Asphyxia
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-20 11:52:25
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserMapper userMapper;
    /**
    * @description:实现注册逻辑
    * @author:Povlean
    * @date:2022/11/21 10:35
    * @param:* @param userAccount
     * @param userPassword
     * @param checkPassword
    * @return:* @return long
    */
    private static final String SALT = "ean";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {

        // 账户和密码，以及确认密码不为空
        if(userAccount == null || userPassword == null || checkPassword == null){
            System.out.println("账号密码为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }

        // 账号和密码不小于规范长度
        if(userAccount.length() < 4 || userPassword.length() < 6){
            System.out.println("账号和密码长度不足");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账户过短");
        }

        // 两次输入的密码不同
        if(userPassword.length() != checkPassword.length() || !userPassword.equals(checkPassword)){
            System.out.println("请确认密码");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }

        // String validPattern = "^[^0-9][\\\\w_]{4,12}$+";
        // Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        // // 校验账户格式
        // if(!matcher.find()){
        //     System.out.println("格式不匹配");
        //     return -1;
        // }

        // 条件构造语句，查询数据库中是否有重复的账户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,userAccount);
        User user = this.getOne(queryWrapper);

        // 若查询到的user不为空，则创建的账号有重复，返回-1
        if(user != null){
            System.out.println("账号有重复");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号有重复");
        }

        // 使用MD5加密密码

        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());

        // 将账户添加到数据库中
        User createUser = new User();
        createUser.setUserAccount(userAccount);
        createUser.setUserPassword(encryptPassword);
        boolean result = this.save(createUser);
        if(!result){
            System.out.println("保存失败");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"保存失败");
        }
        // 最后返回用户id值
        return createUser.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 账户和密码，以及确认密码不为空
        if(userAccount == null || userPassword == null){
            // System.out.println("账号密码为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账户或密码为空");
        }

        // 账号和密码不小于规范长度
        if(userAccount.length() < 4 || userPassword.length() < 6){
            // System.out.println("账号和密码长度不足");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号和密码长度不足");
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((userPassword + SALT).getBytes());
        // 条件语句，与数据库中的数据比较账号密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount,userAccount);
        queryWrapper.eq(User::getUserPassword,encryptPassword);
        // 返回User对象
        User user = this.getOne(queryWrapper);
        // 在数据库中没有该对象，返回null
        if(user == null){
            // log.info("Login failed,please check your account or password!");
            return null;
        }
        // 用户信息脱敏
        User safetyUser = getSafetyUser(user);
        // 记录用户登录状态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }
    @Override
    public User getSafetyUser(User user){
        if(user == null){
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setUserStatus(0);
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setIsDelete(user.getIsDelete());
        safetyUser.setTags(user.getTags());
        return safetyUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        // 从请求体中拿不到登录态时，返回-1
        if(request.getSession().getAttribute(USER_LOGIN_STATE) == null){
            return -1;
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
    * @description:通过标签查询用户
    * @author:Povlean
    * @date:2022/12/18 19:19
    * @param:* @param tagListNames
    * @return:* @return List<User>
    */
    @Deprecated
    public List<User> searchUsersBySQL(List<String> tagListNames) {
        if (CollectionUtils.isEmpty(tagListNames)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        for (String tagName : tagListNames) {
            queryWrapper = queryWrapper.like(User::getTags,tagName);
        }
        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public List<User> searchUsersByTags(List<String> tagListNames) {
        if (CollectionUtils.isEmpty(tagListNames)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1.先查询所有的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        Gson gson = new Gson();
        // 2.在内存中判断是否包含要求的标签
        return userList.stream().filter((user) -> {
            String tagsStr = user.getTags();
            Set<String> tempTagNameSet = gson.fromJson(tagsStr,new TypeToken<Set<String>>(){}.getType());
            tempTagNameSet = Optional.ofNullable(tempTagNameSet).orElse(new HashSet<>());
            for(String tagName : tagListNames) {
                if(!tempTagNameSet.contains(tagName)) {
                    return false;
                }
            }
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());

    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User userObj = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null || userObj.getUserRole() != UserConstant.ADMIN_ROLE) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser.getUserRole() == 1;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = (User)request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return user;
    }

    @Override
    public int updateUser(User user, User loginUser) {
        Long userId = user.getId();
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 校验权限
        // 2.1 管理员可以更新任意信息
        // 2.2 用户只能更新自己的信息
        if (!isAdmin(loginUser) && userId != loginUser.getId()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User oldUser = this.getById(user.getId());
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        // 3. 触发更新
        return this.baseMapper.updateById(user);
    }

    @Override
    public List<User> matchUsers(long num, User loginUser) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tags");
        queryWrapper.isNotNull("tags");
        List<User> userList = this.list(queryWrapper);
        String tags = loginUser.getTags();
        Gson gson = new Gson();
        List<String> tagList = gson.fromJson(tags, new TypeToken<List<String>>() {
        }.getType());
        // 用户列表的下标 => 相似度
        List<Pair<User, Long>> list = new ArrayList<>();
        // 依次计算所有用户和当前用户的相似度
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            String userTags = user.getTags();
            // 无标签或者为当前用户自己
            if (StringUtils.isBlank(userTags) || user.getId().equals(loginUser.getId())) {
                continue;
            }
            List<String> userTagList = gson.fromJson(userTags, new TypeToken<List<String>>() {
            }.getType());
            // 计算分数
            long distance = AlgorithmUtils.minDistance(tagList, userTagList);
            list.add(new Pair<>(user, distance));
        }
        // 按编辑距离由小到大排序
        List<Pair<User, Long>> topUserPairList = list.stream()
                .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
                .limit(num)
                .collect(Collectors.toList());
        // 原本顺序的 userId 列表
        List<Long> userIdList = topUserPairList.stream().map(pair -> pair.getKey().getId()).collect(Collectors.toList());
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.in("id", userIdList);
        // 1, 3, 2
        // User1、User2、User3
        // 1 => User1, 2 => User2, 3 => User3
        Map<Long, List<User>> userIdUserListMap = this.list(userQueryWrapper)
                .stream()
                .map(user -> getSafetyUser(user))
                .collect(Collectors.groupingBy(User::getId));
        List<User> finalUserList = new ArrayList<>();
        for (Long userId : userIdList) {
            finalUserList.add(userIdUserListMap.get(userId).get(0));
        }
        return finalUserList;
    }

}




