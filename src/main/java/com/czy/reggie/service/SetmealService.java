package com.czy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.reggie.dto.DishDto;
import com.czy.reggie.dto.SetmealDto;
import com.czy.reggie.entity.Setmeal;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
