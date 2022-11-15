package com.txy.reggie.dto;


import com.txy.reggie.entity.Setmeal;
import com.txy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
