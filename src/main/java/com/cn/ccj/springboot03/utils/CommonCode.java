package com.cn.ccj.springboot03.utils;

/**
 * @author jiangcongcong
 * @date 2021/3/15 16:05
 * 公共编码类
 */
public class CommonCode {

    //默认异常编码
    public static String DEFAUT_ERROR_CODE = "-9999";
    private static String stockVaildEffective = "01";//有效库存
    private static String stockVaildIneffective = "00";//无效库存

    public static String getDEFAUT_ERROR_CODE(){
        return DEFAUT_ERROR_CODE;
    }

    public static String getStockVaildEffective(){
        return stockVaildEffective;
    }

    public static String getStockVaildIneffective(){
        return stockVaildIneffective;
    }

}
