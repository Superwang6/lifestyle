package cn.fudges.server.common.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/8/5
 */
@Data
public class DeadLetter<T> {

    private String originQueue;

    private T message;

    private LocalDateTime createTime;

    private Integer retryCount;
}
