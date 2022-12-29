package com.czy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czy.reggie.common.R;
import com.czy.reggie.entity.Employee;
import com.czy.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //前端发送请求json格式，@RequestBody接收json数据，封装成employee对象
    //HttpServletRequest是用来当登陆成功，把员工id存在session
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.将页面提交的密码进行加密
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());//使用工具类的md5加密方法

        //2.根据页面提交的username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        //3.判断是否查询到，如果没查询号返回登陆失败结果
        if(emp==null){
            return R.error("登录失败");
        }

        //4.如果查询到，比较密码是否正确
        if (!emp.getPassword().equals(password)){//密码错误
            return R.error("登录失败");
        }

        //5.查看员工状态是否禁用
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }

        //6.员工id存入session，返回成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //要清除session中存储的登录的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}",employee);

        //设置初始密码123456，需要加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置创建时间、更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //设置员工的创建人、最后更新人
        //从Session获取的是key-value，value就是id
//        Long empId =(Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        //调用service方法
        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件，模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件,根据修改时间排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //执行查询,page()中自动封装了records、total等
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }

    /**
     * 修改
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    //把发过来的请求id、status封装成employee
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
            log.info("employee:{}",employee);
//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable  Long id){
        log.info("根据id查询");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
