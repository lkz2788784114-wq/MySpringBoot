package org.example.test2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用 Jackson2JsonRedisSerializer 来序列化 Value
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(new StringRedisSerializer()); // Key 通常用 String
        template.setValueSerializer(serializer);               // Value 自动转 JSON
        return template;
    }
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        // 1. 初始化默认配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        // 2. 配置序列化器为 JSON
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
        );

        // 3. (可选) 设置缓存过期时间，比如 1 小时
        config = config.entryTtl(Duration.ofHours(1));

        return config;
    }
}
