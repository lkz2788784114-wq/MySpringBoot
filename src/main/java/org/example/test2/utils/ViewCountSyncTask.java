package org.example.test2.utils;

import org.example.test2.entity.Product;
import org.example.test2.mapper.productMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component // 必须交给 Spring 管理
public class ViewCountSyncTask {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private productMapper productMapper; // 你的 MyBatis-Plus Mapper

    // 🔑 魔法注解：Cron 表达式
    // "0 0 2 * * ?" 的意思是：每天凌晨 2 点 0 分 0 秒准时执行！
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncViewCountToMySQL() {
        System.out.println("🚨 经理开始半夜巡逻：同步 Redis 浏览量到 MySQL...");

        // 1. 去 Redis 里拿到最新的浏览量
        // (实际大厂开发中会用 keys() 查出所有商品的 key 批量处理，这里简化为一个商品 id=1)
        Object viewsObj = redisUtils.get("product:viewCount:1");

        if (viewsObj != null) {
            long totalViews = Long.parseLong(viewsObj.toString());

            // 2. 把这个庞大的数字，一次性 Update 到 MySQL 数据库里
            // 相当于执行: UPDATE product SET view_count = #{totalViews} WHERE id = 1
            Product p = new Product();
            p.setProductId(1);
            p.setView(  (int) totalViews);
            productMapper.updateById(p);

            System.out.println("✅ 同步完成！ID为1的商品最新浏览量已落库：" + totalViews);
        }
    }
}