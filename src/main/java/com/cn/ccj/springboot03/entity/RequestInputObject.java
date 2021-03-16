package com.cn.ccj.springboot03.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求报文接口类封装
 */
public class RequestInputObject implements Serializable {

    private Map<String,Object> params = new HashMap<String,Object>();
    private String serviceCode;
    private List<Map<String,Object>> beans = new ArrayList<Map<String,Object>>();
    private Object object;
    private Map<String,Object> sysParams;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<Map<String, Object>> getBeans() {
        return beans;
    }

    public void setBeans(List<Map<String, Object>> beans) {
        this.beans = beans;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, Object> getSysParams() {
        return sysParams;
    }

    public void setSysParams(Map<String, Object> sysParams) {
        this.sysParams = sysParams;
    }

    @Override
    public String toString() {
        return "RequestInputObject{" +
                "params=" + params +
                ", serviceCode='" + serviceCode + '\'' +
                ", beans=" + beans +
                ", object=" + object +
                ", sysParams=" + sysParams +
                '}';
    }
}
