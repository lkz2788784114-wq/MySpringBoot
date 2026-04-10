package org.example.test2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 🚀 企业级 Redis 容灾工具类
 */
@Component // 极其关键：把它交给 Spring 管理，这样你在别的地方才能 @Autowired 注入它
public class RedisUtils {

    // 引入大厂标配的日志系统
    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ============================ 基础操作 ============================

    /**
     * 极速递增 (INCR)
     * @param key 键
     * @param delta 要增加几 (大于0)
     * @return 增加后的最新值
     */
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        try {
            // opsForValue().increment 底层调用的就是 Redis 的 INCRBY 命令
            // 它是单线程原子操作，绝对不会出现并发问题！
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("Redis 递增异常! key: {}", key, e);
            return 0; // 兜底策略
        }
    }
    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis 设置过期时间异常! key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存 (支持传一个或多个 key)
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    // ============================ String (普通字符串/对象) ============================

    /**
     * 普通缓存获取
     */
    public Object get(String key) {
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis 获取缓存异常! key: {}", key, e);
            // 🛡️ 容灾降级：Redis 挂了，我们不抛异常导致白屏，而是返回 null
            // 让上层业务以为没查到缓存，老老实实去查 MySQL
            return null;
        }
    }

    /**
     * 普通缓存放入
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis 写入缓存异常! key: {}", key, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("Redis 写入带过期的缓存异常! key: {}", key, e);
            return false;
        }
    }

    // ============================ Hash (哈希字典) ============================

    /**
     * HashGet 取出字典里的某个特定字段
     */
    public Object hget(String key, String item) {
        try {
            return redisTemplate.opsForHash().get(key, item);
        } catch (Exception e) {
            log.error("Redis 获取 Hash 异常! key: {}, item: {}", key, item, e);
            return null;
        }
    }

    /**
     * HashSet 往字典里存入一个字段
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("Redis 写入 Hash 异常! key: {}, item: {}", key, item, e);
            return false;
        }
    }
}