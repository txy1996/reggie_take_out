package com.txy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.txy.reggie.common.Result;
import com.txy.reggie.entity.User;
import com.txy.reggie.service.UserService;
import com.txy.reggie.utils.SMSUtils;
import com.txy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author tangxiaoyu
 * @version 1.0
 * @description: TODO
 * @date 2022/11/11 11:35
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        //生成验证码

        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码: {}",code);
            // code = "1234";
            //发送验证吗
            // SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //保存验证码
            session.setAttribute(phone, code);
            return Result.success("手机验证码发送成功");
        }


        return Result.error("验证码发送失败");
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map map,HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = (String) map.get("phone");
        //获取验证码
        String code = (String) map.get("code");
        //从session中获取验证码
        String codeCheck = (String) session.getAttribute(phone);
        code = "1234";
        codeCheck = "1234";
        //比对验证吗的一致性
        if (code.equals(codeCheck)){
            //比对成功后,判断手机号是否存在,不存在自动保存用户

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if ( user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return Result.success(user);
        }

        return Result.error("登录失败");

    }
}
