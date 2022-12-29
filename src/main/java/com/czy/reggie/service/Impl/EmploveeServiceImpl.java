package com.czy.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.reggie.entity.Employee;
import com.czy.reggie.mapper.EmployeeMapper;
import com.czy.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmploveeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
