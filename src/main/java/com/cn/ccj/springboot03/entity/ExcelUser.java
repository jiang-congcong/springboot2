package com.cn.ccj.springboot03.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

/**
 * @author jiangcongcong
 * @date 2021/5/21 10:47
 */
@ExcelTarget("user") //excel唯一标识实体类，个人感觉无实际意义
public class ExcelUser {
    @Excel(name = "用户id",width = 30,orderNum = "0")
    private String id;
    @Excel(name = "用户名",width = 20,orderNum = "1")
    private String name;
    @Excel(name = "用户性别",width = 15,orderNum = "3")
    private String gender;
    @Excel(name = "用户年龄",width = 15,orderNum = "2")
    private int age;
    @ExcelEntity
    private ExcelUserIdentity excelUserIdentity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ExcelUserIdentity getExcelUserIdentity() {
        return excelUserIdentity;
    }

    public void setExcelUserIdentity(ExcelUserIdentity excelUserIdentity) {
        this.excelUserIdentity = excelUserIdentity;
    }
}
