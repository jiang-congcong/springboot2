package com.cn.ccj.springboot03.jsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author jiangcongcong
 * @date 2021/3/15 14:52
 * jsonUtil工具类
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T stringToObject(String json,Class<T> object) throws IOException {
        return objectMapper.readValue(json,object);
    }

}
