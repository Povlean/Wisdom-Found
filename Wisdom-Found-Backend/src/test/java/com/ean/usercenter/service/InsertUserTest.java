package com.ean.usercenter.service;

import com.ean.usercenter.mapper.UserMapper;
import com.ean.usercenter.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:TODO
 * @author:Povlean
 */
@SpringBootTest
public class InsertUserTest {

    @Resource
    private UserService userService;

    /**
    * @description:TODO
    * @author:Povlean
    * @date:2023/2/7 15:44
    * @param:
    * @return:
    */
    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int NUM = 100000;
        // 分十组
        for (int i = 0; i < 10; i++) {

        }
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            User user = new User();
            user.setUsername("测试");
            user.setUserAccount("fakeData");
            user.setAvatarUrl("https://tupian.qqw21.com/article/UploadPic/2020-6/20206921521371271.jpg");
            user.setGender(0);
            user.setUserPassword("fake123456");
            user.setPhone("13678786969");
            user.setEmail("123@qq.com");
            user.setUserStatus(0);
            user.setTags("[]");
            user.setProfile("1111111");
            user.setUserRole(0);
            userList.add(user);
        }
        userService.saveBatch(userList,100);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
