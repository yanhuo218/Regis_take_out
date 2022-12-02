package com.yanhuo.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalException {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        log.info(message);
        if (message.contains("Duplicate entry")) {
            String[] messages = message.split(" ");
            return R.error(messages[2] + "已存在");
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> customException(CustomException ex) {
        return R.error(ex.getMessage());
    }
}
