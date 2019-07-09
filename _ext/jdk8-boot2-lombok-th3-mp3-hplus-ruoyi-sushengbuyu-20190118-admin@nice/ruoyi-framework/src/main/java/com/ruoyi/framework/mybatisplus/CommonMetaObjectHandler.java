package com.ruoyi.framework.mybatisplus;

import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.base.BaseEntity;
import com.ruoyi.framework.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sushengbuyu
 * @date 2019/1/24 16:16
 */
@Slf4j
public class CommonMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("自动填充-新增");
        setInsertFieldValByName(BaseEntity.CREATE_BY, ShiroUtils.getLoginName(), metaObject);
        setInsertFieldValByName(BaseEntity.CREATE_TIME, new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("自动填充-更新");
        setUpdateFieldValByName(BaseEntity.UPDATE_BY, ShiroUtils.getLoginName(), metaObject);
        setUpdateFieldValByName(BaseEntity.UPDATE_TIME, new Date(), metaObject);
    }
}
