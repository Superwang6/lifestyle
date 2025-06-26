package cn.fudges.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 王平远
 * @since 2025/6/26
 */
@SpringBootTest
class JotRecordServiceTest {

    @Autowired
    private JotRecordService jotRecordService;

    @Test
    void jotRemindJob() {
        jotRecordService.jotRemindJob();
    }
}