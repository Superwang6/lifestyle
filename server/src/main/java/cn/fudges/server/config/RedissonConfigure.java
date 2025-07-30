package cn.fudges.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 王平远
 * @since 2025/3/14
 */
@Configuration
public class RedissonConfigure {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Value("${spring.data.redis.database}")
    private Integer database;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.redisson.connect-timeout}")
    private Integer connectTimeout;

    @Value("${spring.data.redis.redisson.connection-pool-size}")
    private Integer connectionPoolSize;

    @Bean
    public RedissonClient redisson(ObjectMapper objectMapper) {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setPassword(password)
                .setDatabase(database)
                .setConnectTimeout(connectTimeout)
                .setConnectionPoolSize(connectionPoolSize);
        config.setCodec(new JsonJacksonCodec(objectMapper));
        return Redisson.create(config);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.modules(new JavaTimeModule());
    }

}
