package com.yanhuo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yanhuo.common.R;
import com.yanhuo.entity.User;
import com.yanhuo.service.UserService;
import com.yanhuo.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session, HttpServletRequest request) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        log.info("phone:{}", phone);
        log.info("code:{}", code);
        String codes = (String) session.getAttribute(phone);
        if (codes.equals(code)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (null == user) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            } else {
                if (user.getStatus() == 0) {
                    return R.error("登录失败,账号以封禁");
                }
            }
            request.getSession().setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("验证码错误");
    }

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        log.info("{}", user.getPhone());
        String phone = user.getPhone();
        if (null != phone) {
            String code = String.valueOf(ValidateCodeUtils.generateValidateCode(4));
            log.info("code:{}", code);//----------发短信操作
            session.setAttribute(phone, code);
            return R.success("OK");
        }
        return R.error("请输入正确的手机号");
    }

    @PostMapping("/loginout")
    public R<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("成功");
    }

}
