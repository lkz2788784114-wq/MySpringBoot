package org.example.test2.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.test2.common.Result;
import org.example.test2.entity.User;
import org.example.test2.service.userService;
import org.example.test2.utils.JwtUtils;
import org.example.test2.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Objects;

@Api(tags ="用户管理模块")
@RestController
@RequestMapping("/api/user")
public class userController {
    @Autowired
    private userService service;
    @Autowired
    private RedisUtils redisUtils;
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user) {
        String token = service.userLogin(user);
        String userId = JwtUtils.parseToken(token).get("id").toString();
        String RedisKey = "login:token:"+userId;
        redisUtils.set(RedisKey, token,3600*24);
        return Result.success(token);
    }
     @ApiOperation("踢人下线")
     @GetMapping("/logout/{id}")
    public Result<String> logout(@PathVariable Integer id) {
        String RedisKey = "login:token:"+id;
           String token  = String.valueOf(redisUtils.get(RedisKey));
       if (token == null) {
           throw new IllegalArgumentException("用户不在线");
       }
       redisUtils.del(RedisKey);
       return Result.success("踢人下线成功");
    }
    @ApiOperation("查询所有用户")
    @Cacheable(cacheNames = "user")
    @GetMapping("/getAllUser")
    public Result<List<User>> getAllUser() {
        return Result.success(service.getAllUser());
    }
    @ApiOperation("根据ID查询用户")
    @Cacheable(cacheNames = "user",key = "#id")
    @GetMapping("/getById/{id}")
public Result<User> getById(@PathVariable Integer id) {
        return Result.success(service.getUserById(id));
    }
    @ApiOperation("注册用户")
    @CachePut(cacheNames = "user", key = "#user.id")
    @PostMapping("/add")
    public Result<String> add(@RequestBody User user) {
        service.insertUser(user);
        return Result.success("add success!");
    }
    @ApiOperation("修改用户")
    @CachePut(cacheNames = "user",key  = "#user.id")
    @PutMapping("/Put")
    public Result<String> upDateUser(@RequestBody User user) {
         service.updateUser(user);
         return Result.success("Put user success!");
    }
    @ApiOperation("删除用户")
    @CacheEvict(cacheNames = "user",key = "#user.id" )
    @DeleteMapping("/del/{id}")
    public Result<String> del(@PathVariable Integer id) {
        service.deleteUser(id);
        return Result.success("delete success!");
    }

}
