package com.czy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czy.reggie.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrdersService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
