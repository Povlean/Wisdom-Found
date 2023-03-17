package com.ean.usercenter.once;

import com.ean.usercenter.mapper.UserMapper;
import com.ean.usercenter.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.Resource;

/**
 * @description:TODO
 * @author:Povlean
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    // 定时任务调度
    // @Scheduled(initialDelay = 5 * 1000,fixedRate = Long.MAX_VALUE)
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int NUM = 1000;
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
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
