package cn.fudges.server.push;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 王平远
 * @since 2025/6/24
 */
@SpringBootTest
public class UniPushTest {

    @Test
    public void push() {
        Map<String,Object> param = new HashMap<>();
        param.put("title", "测试推送");
        param.put("content", "测试推送内容");
        String s = HttpUtil.post("https://fc-mp-821da366-0775-451d-a73a-1118c8627b10.next.bspapp.com/sendUniPush", param);
        System.out.println(s);
    }
}
