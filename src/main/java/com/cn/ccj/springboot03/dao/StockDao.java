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

    //新建库存
    public void insertStock(Map map);

    //保存库存与商品关系
    public void insertStockRelation(Map map);

    //查询库存
    public List<Map<String,Object>> selectStockByGoodsId(String goodsId);

    //更新库存
    public int updateStock(Map map);

    //秒杀高并发场景下更新库存
    public int updateKillStock(Map<String,Object> map);

    //创建订单
    public void insertOrderDetails(Map map);

    //创建订单与用户关系
    public void insertUserOrderRelation(Map map);

}
