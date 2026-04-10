package org.example.test2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.test2.entity.Product;
@Mapper
public interface productMapper extends BaseMapper<Product> {
}
