package com.czy.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.reggie.entity.SetmealDish;
import com.czy.reggie.mapper.SetmealDishMapper;
import com.czy.reggie.mapper.SetmealMapper;
import com.czy.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
