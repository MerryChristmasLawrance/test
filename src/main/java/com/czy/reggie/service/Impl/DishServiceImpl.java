package com.czy.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.reggie.dto.DishDto;
import com.czy.reggie.entity.Dish;
import com.czy.reggie.entity.DishFlavor;
import com.czy.reggie.mapper.DishMapper;
import com.czy.reggie.service.DishFlavorService;
import com.czy.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service()
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto){
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        //保存菜品口味数据到菜品口味表DishFlavor，但DishFlavor中属性需要dishId，
        //上面this.save已经把菜品基本信息保存了，dish表就生成了dishId，那么就从dish表获取
        Long dishId = dishDto.getId();

        //遍历dishDto中的 flavors集合，为每个口味设定对应的 dishId
        List<DishFlavor> flavors = dishDto.getFlavors();
         flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据菜品id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //在dish表根据菜品id查询菜品信息
        Dish dish = this.getById(id);

        //在dishFlavor表根据菜品id查询口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        //查询得到口味集合
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        DishDto dishDto = new DishDto();
        dishDto.setFlavors(list);
        //对象拷贝，把菜品信息复制到dishDto
        BeanUtils.copyProperties(dish,dishDto);

        return dishDto;
    }

    /**
     *   根据菜品id修改菜品信息和口味信息
     * @param dishDto
     */
    @Override
    public void updateByIdWithFlavor(DishDto dishDto) {
//        //先把dish表基本信息更新
        this.updateById(dishDto);

        //删除当前菜品对应的口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

//        添加提交过来的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map((item)->{
            //为item即dishFlavor设置菜品id
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        //用save的原因是，不是直接update，而是把原来的dishFlavor记录删掉，再添加新的来达到修改目的
        dishFlavorService.saveBatch(flavors);
    }
}
