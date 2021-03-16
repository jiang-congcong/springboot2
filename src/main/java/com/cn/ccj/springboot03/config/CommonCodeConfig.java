package com.cn.ccj.springboot03.config;

import com.cn.ccj.springboot03.util.CommonCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jiangcongcong
 * @date 2021/3/16 16:04
 * 公共编码类配置类
 */
@Configuration
public class CommonCodeConfig {

    @Bean
    public CommonCode commonCode(){
        return new CommonCode();
    }

}
