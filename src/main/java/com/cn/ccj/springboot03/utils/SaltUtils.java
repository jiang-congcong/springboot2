package com.cn.ccj.springboot03.utils;

import java.util.Random;

/**
 * @author jiangcongcong
 * @date 2021/3/25 19:47
 * 随机盐生成类
 */
public class SaltUtils {

    public static String getSalt(int length){
        String salt = "";
        char[] chars = "!@#$%^&*()_+1234567890-=QAZWSXEDCRFVTGBYHNUJMIK<OL>P:?qwertyuiopasdfghjklzxcvbnm".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<length ;i++){
            char getChar = chars[new Random().nextInt(chars.length)];
            stringBuilder.append(getChar);
        }
        salt = stringBuilder.toString();
        return salt;
    }

//    public static void main(String[] args){
//        System.out.println(getSalt(9));
//    }

}
