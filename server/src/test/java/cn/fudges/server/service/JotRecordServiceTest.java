package cn.fudges.server.service;

import cn.fudges.server.entity.JotRecord;
import cn.fudges.server.request.JotRecordRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    @Test
    void queryPage() {
        JotRecordRequest request = new JotRecordRequest();
        request.setPageNum(1);
        request.setPageSize(20);
        request.setUserId(1L);
        request.setTimeType(5);
        IPage<JotRecord> page = jotRecordService.queryPage(request);
        System.out.println(page);
    }

    @Test
    void addJotRecord() {
        JotRecordRequest request = new JotRecordRequest();
        request.setTitle("test1");
        request.setDescription("test1 description");
        request.setUserId(1L);
        request.setBookId(1L);
        request.setClassifyId(1L);
        request.setStatus(0);
        request.setRemindType(1);
        Boolean b = jotRecordService.addJotRecord(request);
        System.out.println(b);
    }

    @Test
    void modifyJotRecord() {
    }

    @Test
    void delete() {
    }
}