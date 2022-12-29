package com.czy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.reggie.common.R;
import com.czy.reggie.dto.DishDto;
import com.czy.reggie.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    //根据菜品id查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    ////根据菜品id修改菜品信息和口味信息
    public void updateByIdWithFlavor(DishDto dishDto);
}
