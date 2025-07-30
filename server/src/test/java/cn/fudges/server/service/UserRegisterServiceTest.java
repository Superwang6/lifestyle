package cn.fudges.server.service;

import cn.fudges.server.entity.UserBase;
import cn.fudges.server.request.UserPasswordRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@SpringBootTest
class UserRegisterServiceTest {

    @Autowired
    private UserRegisterService userRegisterService;

    @Test
    void registerUserPassword() {
        UserPasswordRegisterRequest request = new UserPasswordRegisterRequest();
        request.setMobilePhone("test");
        request.setPassword("202cb962ac59075b964b07152d234b70");
        request.setName("test11");
        request.setLength(3);
        UserBase userBase = userRegisterService.registerUserPassword(request);
        System.out.println(userBase);
    }

}