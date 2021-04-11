package com.cn.ccj.springboot03.serviceImpl;

import com.cn.ccj.springboot03.dao.StockDao;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IStockSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.utils.CommonCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiangcongcong
 * @date 2021/3/31 14:24
 */
@Service
public class StockSVImpl implements IStockSV {

    @Autowired
    private StockDao stockDao;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @Override
    public int insertStock(Map map) throws GeneralException {
        if(null!=map&&map.size()>0){
            try {
                stockDao.insertStock(map);
            }catch (Exception e){
                logger.error("保存库存失败"+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"保存库存失败"+e.getMessage());
            }

        }
        return 1;
    }

    @Override
    public int insertStockRelation(Map map) throws GeneralException {
        if(null!=map&&map.size()>0){
            try {
                stockDao.insertStockRelation(map);
            }catch (Exception e){
                logger.error("保存商品与库存关系失败"+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"保存商品与库存关系失败"+e.getMessage());
            }

        }
        return 1;
    }

    @Override
    public List<Map<String, Object>> selectStock(String goodsId) throws GeneralException {
        List<Map<String,Object>> resultList = new ArrayList<>();
        if(null!=goodsId&&goodsId.length()>0){
            try {
                resultList = stockDao.selectStockByGoodsId(goodsId);
            }catch (Exception e){
                logger.error("查询库存失败"+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"查询库存失败"+e.getMessage());
            }

        }
        return resultList;
    }

    @Override
    public int updateStockConsume(Map map) throws GeneralException { //更新库存消耗量
        int result = 0;
        String goodsId = (String) map.get("goodsId");
        String stockConsume = (String) map.get("stockConsume");
        String operateType = (String) map.get("operateType");//库存操作类型 01-扣减  02-返销
        String updateStockConsume = stockConsume;//更新到数据库里的数量
        List<Map<String, Object>> selectStockList = selectStock(goodsId);
        if (null != selectStockList && selectStockList.size() > 0) {
            Map selectStockMapFirst = selectStockList.get(0);
            String stockId = (String) selectStockMapFirst.get("stockId");
            String version = String.valueOf(selectStockMapFirst.get("version"));

            String databaseStockConsume = String.valueOf(selectStockMapFirst.get("stockConsume"));//库存消耗量
            String databaseStockNum = String.valueOf(selectStockMapFirst.get("stockNum"));//原始库存量
            String remainderStock = String.valueOf(selectStockMapFirst.get("remainderStock"));//库存剩余量
            if (operateType.equals("01")) {
                if (Integer.parseInt(remainderStock) < Integer.parseInt(stockConsume)) {
                    throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "库存剩余不足！");
                }
                updateStockConsume = String.valueOf(Integer.parseInt(databaseStockConsume) + Integer.parseInt(stockConsume));
            } else if ("02".equals(operateType)) {
                if (Integer.parseInt(databaseStockConsume) < Integer.parseInt(stockConsume)) {
                    throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "返销数量大于库存消耗量！");
                }
                updateStockConsume = String.valueOf(Integer.parseInt(databaseStockConsume) - Integer.parseInt(stockConsume));
            } else {
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "操作类型不在枚举值内");
            }
            Map reqMap = new HashMap();
            reqMap.put("stockId", stockId);
            reqMap.put("stockConsume", updateStockConsume);
            reqMap.put("version",version);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String alterTime = formatter.format(date);
            reqMap.put("alterTime",alterTime);
            String seckillByOptimisticLocking = (String)map.get("seckillByOptimisticLocking");
            try {
                if("1".equals(seckillByOptimisticLocking)){
                    reqMap.put("stockConsume", stockConsume);//覆盖数据，利用数据库进行加减  从而加锁
                    result = stockDao.updateKillStock(reqMap);//秒杀高并发场景
                }else {
                    result = stockDao.updateStock(reqMap);
                }
            } catch (Exception e) {
                logger.error("更新库存失败：" + e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存失败：" + e.getMessage());
            }

        }
        else {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"此商品暂无库存");
        }
        return result;
    }

    @Override
    public int updateStockNum(Map map) throws GeneralException { //更新原始库存量
        int result = 0;
        String goodsId = (String) map.get("goodsId");
        String stockNum = (String) map.get("stockNum");
        String operateType = (String) map.get("operateType");//库存操作类型 01-扣减  02-返销
        String updateStockNum = stockNum;//更新到数据库里的数量
        List<Map<String, Object>> selectStockList = selectStock(goodsId);
        if (null != selectStockList && selectStockList.size() > 0) {
            Map selectStockMapFirst = selectStockList.get(0);
            String stockId = (String) selectStockMapFirst.get("stockId");
            String databaseStockConsume = String.valueOf(selectStockMapFirst.get("stockConsume"));//库存消耗量
            String databaseStockNum = String.valueOf(selectStockMapFirst.get("stockNum"));//原始库存量
            String remainderStock = String.valueOf(selectStockMapFirst.get("remainderStock"));//库存剩余量
            if (operateType.equals("01")) {
                if (Integer.parseInt(remainderStock) < Integer.parseInt(stockNum)) {
                    throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "库存剩余不足！");
                }
                updateStockNum = String.valueOf(Integer.parseInt(databaseStockNum) - Integer.parseInt(stockNum));
            } else if ("02".equals(operateType)) {
                updateStockNum = String.valueOf(Integer.parseInt(databaseStockNum) + Integer.parseInt(stockNum));
            } else {
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "操作类型不在枚举值内");
            }
            Map reqMap = new HashMap();
            reqMap.put("stockId", stockId);
            reqMap.put("stockNum", updateStockNum);
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String alterTime = formatter.format(date);
            reqMap.put("alterTime",alterTime);
            try {
                result = stockDao.updateStock(reqMap);
            } catch (Exception e) {
                logger.error("更新库存失败：" + e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存失败：" + e.getMessage());
            }

        } else {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"此商品暂无库存");
        }
        return result;
    }

    @Override
    public void insertOrderDetails(Map map) throws GeneralException {
        if(null!=map&&map.size()>0){
            try {
                stockDao.insertOrderDetails(map);
            }catch (Exception e){
                logger.error("新建订单失败："+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建订单失败："+e.getMessage());
            }

        }else{
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建订单数据不能为空");
        }
    }

    @Override
    public void insertUserOrderRelation(Map map) throws GeneralException {
        if(null!=map&&map.size()>0){
            try {
                stockDao.insertUserOrderRelation(map);
            }catch (Exception e){
                logger.error("新建用户与订单关系失败："+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建用户与关系订单失败："+e.getMessage());
            }

        }else{
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建用户与订单关系数据不能为空");
        }
    }

}
