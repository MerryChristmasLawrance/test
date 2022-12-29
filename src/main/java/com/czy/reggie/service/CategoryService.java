package com.czy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.reggie.entity.Category;
import org.springframework.stereotype.Service;


public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
