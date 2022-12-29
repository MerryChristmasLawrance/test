package com.czy.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.reggie.entity.DishFlavor;
import com.czy.reggie.mapper.DishFlavorMapper;
import com.czy.reggie.service.DishFlavorService;
import com.czy.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
