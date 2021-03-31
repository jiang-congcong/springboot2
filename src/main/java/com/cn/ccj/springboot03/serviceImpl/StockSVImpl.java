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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

}
