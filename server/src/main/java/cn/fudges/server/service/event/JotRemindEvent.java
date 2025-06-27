package cn.fudges.server.service.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author 王平远
 * @since 2025/6/27
 */
@Getter
@Setter
public class JotRemindEvent extends ApplicationEvent {

    private Long jotRecordId;

    public JotRemindEvent(Object source) {
        super(source);
    }
}
