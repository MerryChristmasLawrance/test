package com.czy.reggie.dto;

import com.czy.reggie.entity.Setmeal;
import com.czy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
