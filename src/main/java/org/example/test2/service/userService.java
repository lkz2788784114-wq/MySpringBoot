package org.example.test2.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.test2.entity.User;
import org.example.test2.mapper.userMapper;
import org.example.test2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class userService {
    @Autowired
    private userMapper userMapper;
    public List<User> getAllUser() {
        return userMapper.selectList(null);
    }
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new IllegalArgumentException("Id: " + id + " not exist");
        }
        return user;
    }
    public void  insertUser(User user) {
        if(user.getName()==null||user.getPassword()==null){
            throw new IllegalArgumentException("名字，密码不为空！");
        }
        if(userMapper.selectById(user.getId())!=null){
            throw new IllegalArgumentException("用户已经存在");
        }
        userMapper.insert(user);
    }
    public void  updateUser(User user) {
        if(userMapper.selectById(user.getId())==null){
            throw new IllegalArgumentException("Id: " + user.getId() + " not exist");
        }
        userMapper.updateById(user);
    }
    public void deleteUser(Integer id) {
        if(userMapper.selectById(id)==null){
            throw new IllegalArgumentException("Id: " + id + " not exist");
        }
        userMapper.deleteById(id);
    }


    public String userLogin(User user) {
        if(user.getName()==null||user.getPassword()==null){
            throw new IllegalArgumentException("账号密码不能为空");
        }
        LambdaQueryWrapper<User>  wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, user.getName()).eq(User::getPassword, user.getPassword());
        User user1 = userMapper.selectOne(wrapper);
        if(user1==null){
            throw new IllegalArgumentException("账号密码错误！");
        }
        Map<String, Object> claims = new java.util.HashMap<>();
        claims.put("id", user1.getId());
        claims.put("name",user1.getName());

        // 呼叫我们刚才写的机器造 Token
        String token = JwtUtils.genToken(claims);

        return token;
    }
    public void userLogout(Integer id) {
       User user =  userMapper.selectById(id);
       if(user==null){
           throw new IllegalArgumentException("Id: " + id + " not exist");
       }
    }
}
