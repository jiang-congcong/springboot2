package com.cn.ccj.springboot03.config;

import com.cn.ccj.springboot03.utils.BaseUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangcongcong
 * @date 2021/3/31 19:21
 */
@Configuration
public class BaseUtilsConfig {

    @Bean
    public BaseUtils baseUtils(){
        return new BaseUtils();
    }

}
