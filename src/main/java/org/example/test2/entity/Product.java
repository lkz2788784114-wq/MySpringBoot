package org.example.test2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private String productName;
    private Double price;
    private Integer stock;
    private Integer view;
    @TableLogic(value = "1",delval = "0")
    private Integer status;


}
