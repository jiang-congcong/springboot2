package com.cn.ccj.springboot03.base;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base")
public class baseController {

    @RequestMapping("/get")
    public String getBaseString(){
        return "base";
    }
}
