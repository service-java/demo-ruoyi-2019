package com.ruoyi.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.framework.mybatisplus.CommonMetaObjectHandler;

/**
 * @author sushengbuyu
 * @date 2019/1/24 16:14
 */
@Configuration
@MapperScan("com.ruoyi.*.mapper")
public class MybatisPlusConfig {

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new CommonMetaObjectHandler();
    }
}
