package org.example.test2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.test2.entity.User;
@Mapper
public interface userMapper extends BaseMapper<User> {
}
