package com.cn.ccj.springboot03.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/31 11:11
 */
@Mapper
public interface StockDao {

    public void insertStock(Map map);

    public void insertStockRelation(Map map);

    public List<Map<String,Object>> selectStockByGoodsId(String goodsId);

}
