package com.cn.ccj.springboot03.controller;

import com.cn.ccj.springboot03.entity.ResultOutputObject;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IStockSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.utils.BaseUtils;
import com.cn.ccj.springboot03.utils.CommonCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/31 16:39
 */
@Controller
@RequestMapping("stockController")
@Api(value = "stockController",description = "库存操作类")
public class StockController {

    @Autowired
    private IStockSV iStockSV;

    @Autowired
    private BaseUtils baseUtils;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @ApiOperation(value = "新建库存")
    @RequestMapping(method = RequestMethod.POST,value = "/insertStock")
    @ResponseBody
    public ResultOutputObject insertStock(Map paramsMap) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        String tableName = "t_goods_stock";
        String goodsId = (String)paramsMap.get("goodsId");
        String stockNum = (String)paramsMap.get("stockNum");
        String stockConsume = (String)paramsMap.get("stockConsume");
        String crtUserId = (String)paramsMap.get("crtUserId");
        String crtTime = (String)paramsMap.get("crtTime");
        if(ObjectUtils.isEmpty(goodsId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"商品id不能为空");
        }
        if(ObjectUtils.isEmpty(stockNum)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"库存数量不能为空");
        }
        if(ObjectUtils.isEmpty(stockConsume)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"库存消耗量不能为空");
        }
        if(ObjectUtils.isEmpty(crtUserId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"创建用户id不能为空");
        }
        if(ObjectUtils.isEmpty(crtTime)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"创建时间不能为空");
        }


        if(null!=paramsMap&&paramsMap.size()>0){
            String stockId = baseUtils.getSequence(tableName);
            Map reqMap = new HashMap();
            reqMap.put("stockId",stockId);
            reqMap.put("goodsId",paramsMap.get("goodsId"));
            reqMap.put("stockNum",paramsMap.get("stockNum"));
            reqMap.put("stockConsume",paramsMap.get("stockConsume"));
            reqMap.put("crtUserId",paramsMap.get("crtUserId"));
            reqMap.put("crtTime",paramsMap.get("crtTime"));
            reqMap.put("stockVaild", CommonCode.getStockVaildEffective());//库存状态有效

            try {
                iStockSV.insertStockRelation(reqMap);
                iStockSV.insertStock(reqMap);
            }catch (Exception e){
                logger.error("新建库存失败："+e.getMessage());
                throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建库存失败："+e.getMessage());
            }

        }else {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建库存参数不足");
        }
        resultOutputObject.setRtnCode("0");
        resultOutputObject.setRtnMsg("新建库存成功");

        return resultOutputObject;
    }

}
