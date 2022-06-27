package com.liuao.reggie.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liuao.reggie.common.R;
import com.liuao.reggie.entity.User;
import com.liuao.reggie.service.UserService;
import com.liuao.reggie.utils.SendEmail;
import com.liuao.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping("/sendMsg")
    public R<String> sendMsgApi(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if(StringUtils.isNotBlank(phone)){
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            session.setAttribute(phone, code);
            SendEmail.sendEmailCode(phone, code);
        }
        return R.success("获取验证码成功");
    }

    @PostMapping("/login")
    public R<User> loginApi(@RequestBody JSONObject json, HttpSession session){
        String phone = json.getString("phone");
        String code = json.getString("code");

        if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code) && code.equals(session.getAttribute(phone))){
            // 判断是否新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("userId", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
