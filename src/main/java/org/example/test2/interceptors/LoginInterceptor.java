package org.example.test2.interceptors;
import org.example.test2.utils.JwtUtils;
import org.example.test2.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头里获取 Token（约定好的名字叫 Authorization）
        String token = request.getHeader("Authorization");
        try {
            JwtUtils.parseToken(token);
            Map<String, Object> claims = JwtUtils.parseToken(token);
            String userId = claims.get("id").toString();
            String redisKey = "login:token:"+userId;
            Object userObj = redisUtils.get(redisKey);
            if (userObj == null||!userObj.equals(token)) {
                // Redis 里找不到，说明：
                // 1. 登录超时过期了
                // 2. 用户点过退出登录了
                // 3. 被管理员踢下线了
                response.setStatus(401);
                return false;
            }
            // 解析成功，说明是合法用户，放行！
            redisUtils.expire(redisKey, 3600);
            return true;
        } catch (Exception e) {
            // 解析失败（过期、篡改、或压根没传）
            // 设置响应状态码为 401（未授权）
            response.setStatus(401);
            return false;
        }
    }
}
