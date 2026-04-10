package org.example.test2.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.test2.entity.Product;
import org.example.test2.mapper.productMapper;
import org.example.test2.mapper.userMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class productService {
    @Autowired
    private productMapper mapper;
    public List<Product> selectAll() {
        return mapper.selectList(null);
    }
    public Product selectById(Integer id) {
           Product product = mapper.selectById(id);
           if(product == null) {
               throw new IllegalArgumentException("没有找到商品");
           }
           return product;
    }
    public void insert(Product product) {
        if(product.getPrice()<=0||product.getStock()<0) {
            throw new IllegalArgumentException("商品价格应该大于0，库存应该大于等于0");
        }
        mapper.insert(product);
        if (mapper.selectById(product.getProductId())==null) {
            throw new IllegalArgumentException("商品添加失败，已经存在该商品");
        }
    }
    public void update(Product product) {
        if(mapper.selectById(product.getProductId())==null) {
            throw new IllegalArgumentException("请输入修改商品的ID/ID未找到");
        }
        if(product.getPrice()<=0||product.getStock()<0) {
            throw new IllegalArgumentException("商品价格应该大于0，库存应该大于等于0");
        }
        mapper.updateById(product);
    }
    public void delete(Integer id) {
        if(mapper.selectById(id)==null) {
            throw new IllegalArgumentException("商品不存在");
        }
        mapper.deleteById(id);
    }
    public List<Product> selectAllProduct() {
        return mapper.selectList(null);
    }
}
