package com.czy.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.reggie.Exception.CustomException;
import com.czy.reggie.dto.SetmealDto;
import com.czy.reggie.entity.Setmeal;
import com.czy.reggie.entity.SetmealDish;
import com.czy.reggie.mapper.SetmealMapper;
import com.czy.reggie.service.SetmealDishService;
import com.czy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐基本信息到setmeal
        this.save(setmealDto);

        //保存套餐菜品到setmealDish，还需要给他setmeal的id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可用删除 select count(*) from setmeal where id in (ids) and status=1
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids).eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);//查询该条件的count
        if (count>0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖，不能删除");
        }

        //如果可以删除，先删除套餐表的数据
        this.removeByIds(ids);

        //删除关系表中的数据delete from setmeal_dish where setmeal_id in (ids)
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId,ids);

        setmealDishService.remove(queryWrapper2);
    }
}
