package com.cn.ccj.springboot03.iservice;

import com.cn.ccj.springboot03.generalException.GeneralException;
import io.lettuce.core.output.GenericMapOutput;

import java.util.*;

/**
 * @author jiangcongcong
 * @date 2021/3/31 14:22
 */
public interface IStockSV {

    public int insertStock(Map map) throws GeneralException;

    public int insertStockRelation(Map map) throws GeneralException;

    public List<Map<String,Object>> selectStock(String goodsId) throws GeneralException;

    //更新消耗库存量stock_cosume
    public int updateStockConsume(Map map) throws GeneralException;

    //更新原始库存数量stock_num
    public int updateStockNum(Map map) throws GeneralException;

    public void insertOrderDetails(Map map) throws GeneralException;

    public void insertUserOrderRelation(Map map) throws GeneralException;

}
