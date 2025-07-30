package cn.fudges.server.service;

import cn.fudges.server.entity.UserBase;
import cn.fudges.server.request.UserBaseRequest;
import cn.fudges.server.request.UserPasswordRegisterRequest;
import cn.fudges.server.request.UserPasswordRequest;
import cn.fudges.server.response.UserBaseResponse;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@SpringBootTest
class UserBaseServiceTest {

    @Autowired
    private UserBaseService userBaseService;


    @Test
    void detail() {
    }

    @Test
    void modifyUserBase() {
        UserBaseRequest request = new UserBaseRequest();
        request.setId(1L);
        request.setName("admin");
        Boolean b = userBaseService.modifyUserBase(request);
        System.out.println(b);
    }
}