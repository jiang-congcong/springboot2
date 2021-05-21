package com.cn.ccj.springboot03.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangcongcong
 * @date 2021/5/21 10:51
 */

@ExcelTarget("excelUserIdentity")
@Data
public class ExcelUserIdentity implements Serializable {
    @Excel(name = "身份证号",width = 40,orderNum = "4")
    private String idCardNum;//身份证号
    @Excel(name = "籍贯",width = 50,orderNum = "5")
    private String nativePlace;//籍贯

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
}
