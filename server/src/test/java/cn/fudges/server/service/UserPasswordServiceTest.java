package cn.fudges.server.service;

import cn.fudges.server.request.UserPasswordRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 王平远
 * @since 2025/7/23
 */
@SpringBootTest
class UserPasswordServiceTest {

    @Autowired
    private UserPasswordService userPasswordService;

    @Test
    void modifyUserPassword() {
        UserPasswordRequest request = new UserPasswordRequest();
        request.setUserId(2L);
        request.setPassword("202cb962ac59075b964b07152d234b70");
        request.setOldPassword("202cb962ac59075b964b07152d234b70");
        request.setLength(3);
        Boolean b = userPasswordService.modifyUserPassword(request);
        System.out.println(b);
    }
}