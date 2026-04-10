package org.example.test2.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.test2.common.Result;
import org.example.test2.entity.Product;
import org.example.test2.service.productService;
import org.example.test2.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "商品模块")
@RestController
@RequestMapping("/api/users")
public class productController {
    @Autowired
    private productService service;
    @Autowired
    private RedisUtils redisUtils;
    @ApiOperation("上架商品")
    @PostMapping("/add")
    public Result<String> add(@RequestBody Product product) {
        service.insert(product);
        String redisKey = "product" + product.getProductId();
        redisUtils.set(redisKey, product,60*60);
        return Result.success("商品上架成功");
    }
    @ApiOperation("查询商品")
    @GetMapping("/get")
    public Result<Product> get(@RequestParam("product_Id") Integer product_Id) {
           Product redisProduct =( Product) redisUtils.get("product" + product_Id);
           if (redisProduct != null) {
               return Result.success(redisProduct);
           }
           return Result.success(service.selectById(product_Id));
    }
    @ApiOperation("修改商品")
    @PutMapping("/put")
    public Result<String> update(@RequestBody Product product) {
        service.update(product);
        String redisKey = "product" + product.getProductId();
        redisUtils.set(redisKey, product,60*60);
        return Result.success("修改成功");
    }
    @ApiOperation("删除商品")
    @DeleteMapping("/del/{id}")
    public Result<String> deleteById(@RequestParam("product_Id") Integer id) {
        service.delete(id);
        String redisKey = "product" + id;
        redisUtils.del(redisKey);
        return Result.success("删除成功");
    }
    @ApiOperation("view")
    @GetMapping("/view/{id}")
    public Result<String> view(@PathVariable("id") Integer id) {
       String redisView =  "productView:"+service.selectById(id).getProductName();
         return Result.success( "当前商品浏览量："+redisUtils.increment(redisView,1));
    }
    @ApiOperation("查询所有商品列表")
    @GetMapping("/getAll")
    public Result<List<Product>> getAll() {
        String redisKey = "products:all";
        List<Product> products = service.selectAll();
        redisUtils.set(redisKey,products,60*60);
        return Result.success(products);
    }


}
