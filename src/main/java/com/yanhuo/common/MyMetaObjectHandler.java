package com.yanhuo.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime",new Date());
        metaObject.setValue("createUser",BaseContext.getId());
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("updateUser",BaseContext.getId());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",new Date());
        metaObject.setValue("updateUser",BaseContext.getId());
    }
}
