package org.example.test2.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 极其机密的密钥（就像印钞机的防伪模板），绝不能泄露给前端！
    private static final String KEY = "MySecretKey_test";

    /**
     * 接收业务数据，生成 Token（发手环）
     */
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims) // 把用户的信息（比如用户ID、名字）存进手环里
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 设置手环过期时间：12小时后作废
                .sign(Algorithm.HMAC256(KEY)); // 用我们的机密密钥进行数字签名防伪
    }

    /**
     * 接收 Token，验证并解析数据（保安验手环）
     */
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY)) // 拿出我们的机密密钥去对比
                .build()
                .verify(token) // 验证 Token 是否被篡改、是否过期（如果有问题会直接抛出异常！）
                .getClaim("claims")
                .asMap(); // 把手环里的用户信息取出来返回
    }
}
