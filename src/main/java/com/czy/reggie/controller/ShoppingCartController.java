package com.czy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czy.reggie.common.BaseContext;
import com.czy.reggie.common.R;
import com.czy.reggie.entity.ShoppingCart;
import com.czy.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车：{}",shoppingCart);
        //设置用户id，指定当前当前数据是哪个用户的购物车
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //在shopping_cart中查询当前菜品或套餐是否在购物车中,  判断shoppingCart中的是setmealId还是dishId
        //查询添加根据userId和dishId或setmealId
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){//说明当前是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //查询 select * from shopping_cart where user_id=? and dish_id/setmeal_id=?
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        //如果已经存在，就在原来数量上+1
        if (one!=null){
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            //如果不存在，添加到购物车，数量默认是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;//赋值给one，方便后面return
        }


        return R.success(one);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> subtract(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车：{}",shoppingCart);

        //设置用户id，指定当前是哪个用户的购物车
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //判断传过来的是dishId还是setmealId
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        if (dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        //在shopping_cart中查询，看有没有记录
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        //如果有，就把该菜品或套餐数量减1
        if (one!=null){
            one.setNumber(one.getNumber()-1);
            shoppingCartService.updateById(one);
        }else {
            one=shoppingCart;
        }

        return R.success(one);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        //delete * from shopping_cart where user_id=?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车");
    }
}
