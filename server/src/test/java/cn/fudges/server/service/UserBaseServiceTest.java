package cn.fudges.server.service;

import cn.fudges.server.business.user.service.UserBaseService;
import cn.fudges.server.business.user.request.UserBaseRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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