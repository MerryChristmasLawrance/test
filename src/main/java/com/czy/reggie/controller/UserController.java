package com.czy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czy.reggie.common.R;
import com.czy.reggie.entity.User;
import com.czy.reggie.service.UserService;
import com.czy.reggie.utils.SMSUtils;
import com.czy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用短信服务
            SMSUtils.sendMessage("程子毅的博客","SMS_265005863",phone,code);

            //将验证码保存到session
            session.setAttribute(phone,code);

            return R.success("发送验证码短信成功");
        }
        return R.error("发送验证码短信失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //session中获取code
        Object codeInSession = session.getAttribute(phone);

        //进行验证码比对，页面提交的验证码和session中的验证码比较
        if (codeInSession!=null && code.equals(codeInSession)){
            //比对成功，查询当前手机是否新用户，如果是新用户自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if (user==null){
                //说明是新用户，自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //把userid存进session
            session.setAttribute("user",user.getId());
            //不管新老用户都返回
            return R.success(user);
        }

        return R.error("登录失败");
    }
}
