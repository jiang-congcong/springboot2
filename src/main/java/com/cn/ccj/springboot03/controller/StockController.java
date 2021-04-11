package com.cn.ccj.springboot03.controller;

import com.cn.ccj.springboot03.entity.RequestInputObject;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/31 16:39
 */
@Controller
@RequestMapping("stockController")
@Api(value = "stockController",description = "库存操作类")
//@Transactional
public class StockController {

    @Autowired
    private IStockSV iStockSV;

    @Autowired
    private BaseUtils baseUtils;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @ApiOperation(value = "新建库存")
    @RequestMapping(method = RequestMethod.POST,value = "/insertStock")
    @ResponseBody
    public ResultOutputObject insertStock(@RequestBody RequestInputObject requestInputObject) throws Exception {
        Map paramsMap = requestInputObject.getParams();
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        String tableName = "t_goods_stock";
        String goodsId = (String)paramsMap.get("goodsId");
        String stockNum = (String)paramsMap.get("stockNum");
        String stockConsume = (String)paramsMap.get("stockConsume");
        String crtUserId = (String)paramsMap.get("crtUserId");
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
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String crtTime = formatter.format(date);

        if(null!=paramsMap&&paramsMap.size()>0){
            String stockId = baseUtils.getSequence(tableName);
            Map reqMap = new HashMap();
            reqMap.put("stockId",stockId);
            reqMap.put("goodsId",paramsMap.get("goodsId"));
            reqMap.put("stockNum",paramsMap.get("stockNum"));
            reqMap.put("stockConsume",paramsMap.get("stockConsume"));
            reqMap.put("crtUserId",paramsMap.get("crtUserId"));
            reqMap.put("crtTime",crtTime);
            reqMap.put("stockValid", CommonCode.getStockVaildEffective());//库存状态有效

            try {
                iStockSV.insertStock(reqMap);
                iStockSV.insertStockRelation(reqMap);
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

    @ApiOperation(value = "更新库存消耗量")
    @RequestMapping(method = RequestMethod.POST,value = "/updateStockConsume")
    @ResponseBody
    public ResultOutputObject updateStockConsume(@RequestBody RequestInputObject requestInputObject) throws GeneralException {
        int updateResult = 0;
        Map map = requestInputObject.getParams();
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        if (null == map || map.size() < 1) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "参数不能为空");
        }
        String goodsId = (String) map.get("goodsId");
        String stockConsume = (String) map.get("stockConsume");
        String operateType = (String) map.get("operateType");//库存操作类型 01-扣减  02-返销
        if (ObjectUtils.isEmpty(goodsId)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，商品id不能为空");
        }
        if (ObjectUtils.isEmpty(stockConsume)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，更新数量不能为空");
        }
        if (ObjectUtils.isEmpty(operateType)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，操作类型不能为空");
        }
        try{
            updateResult = iStockSV.updateStockConsume(map);
        }catch (Exception e){
            logger.error("更新库存失败：" + e.getMessage());
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存失败：" + e.getMessage());
        }
        if(updateResult<0){
            resultOutputObject.setRtnMsg("更新库存失败");
            resultOutputObject.setRtnCode(updateResult+"");
        }
        else {
            resultOutputObject.setRtnMsg("更新库存成功");
            resultOutputObject.setRtnCode(updateResult+"");
        }
        return resultOutputObject;
    }

    @ApiOperation(value = "更新库存原始量")
    @RequestMapping(method = RequestMethod.POST,value = "/updateStockNum")
    @ResponseBody
    public ResultOutputObject updateStockNum(@RequestBody RequestInputObject requestInputObject) throws GeneralException {
        int updateResult = 0;
        Map map = requestInputObject.getParams();
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        if (null == map || map.size() < 1) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "参数不能为空");
        }
        String goodsId = (String) map.get("goodsId");
        String stockNum = (String) map.get("stockNum");
        String operateType = (String) map.get("operateType");//库存操作类型 01-扣减  02-返销
        if (ObjectUtils.isEmpty(goodsId)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，商品id不能为空");
        }
        if (ObjectUtils.isEmpty(stockNum)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，更新数量不能为空");
        }
        if (ObjectUtils.isEmpty(operateType)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存时，操作类型不能为空");
        }
        try{
            updateResult = iStockSV.updateStockConsume(map);
        }catch (Exception e){
            logger.error("更新库存失败：" + e.getMessage());
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "更新库存失败：" + e.getMessage());
        }
        if(updateResult<0){
            resultOutputObject.setRtnMsg("更新库存失败");
            resultOutputObject.setRtnCode(updateResult+"");
        }
        else {
            resultOutputObject.setRtnMsg("更新库存成功");
            resultOutputObject.setRtnCode(updateResult+"");
        }
        return resultOutputObject;
    }

    @ApiOperation(value = "新建订单")
    @RequestMapping(method = RequestMethod.POST,value = "/insertOrder")
    @ResponseBody
    public ResultOutputObject insertOrder(@RequestBody RequestInputObject requestInputObject) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        Map paramsMap = requestInputObject.getParams();
        String userId = (String)paramsMap.get("userId");
        String goodsId = (String)paramsMap.get("goodsId");
        String operateNum = (String)paramsMap.get("operateNum");
        String operateType = (String)paramsMap.get("operateType");
        String orderId = baseUtils.getSequence("t_order_details");
        if(ObjectUtils.isEmpty(userId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"用户id不能为空");
        }
        if(ObjectUtils.isEmpty(goodsId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"商品id不能为空");
        }
        if(ObjectUtils.isEmpty(operateNum)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"操作数量不能为空");
        }
        if(ObjectUtils.isEmpty(operateType)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"操作类型不能为空");
        }
        String valid = CommonCode.getStockVaildEffective();//状态 01 有效
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String crtTime = formatter.format(date);
        Map reqMap = new HashMap();
        reqMap.put("userId",userId);
        reqMap.put("orderId",orderId);
        reqMap.put("goodsId",goodsId);
        reqMap.put("operateNum",operateNum);
        reqMap.put("operateType",operateType);
        reqMap.put("valid",valid);
        reqMap.put("crtTime",crtTime);
        try{
            iStockSV.insertOrderDetails(reqMap); //新建订单详情
            iStockSV.insertUserOrderRelation(reqMap);//新建用户与订单关系
        }catch (Exception e){
            logger.error("新建订单失败："+e.getMessage());
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"新建订单失败："+e.getMessage());
        }
        resultOutputObject.setRtnCode("0");
        resultOutputObject.setRtnMsg("创建订单成功");
        return resultOutputObject;
    }



    //悲观锁 synchronized关键字锁住方法，单线程执行，缺点效率低
    //注：synchronized关键字不要与@Transactional事务注解一起使用，会出问题，高并发场景下会出现超卖现象，单线程结束事务还未结束
    @ApiOperation(value = "秒杀-悲观锁")
    @RequestMapping(method = RequestMethod.POST,value = "/seckillByPessmisticLocking")
    @ResponseBody
    public synchronized ResultOutputObject seckillByPessmisticLocking(@RequestBody RequestInputObject requestInputObject) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();

        Map mapParams = requestInputObject.getParams();
        String goodsId = (String)mapParams.get("goodsId");
        String operateNum = (String)mapParams.get("operateNum");
        String userId = (String)mapParams.get("userId");
        String operateType = (String)mapParams.get("operateType");
        mapParams.put("stockConsume",operateNum);
        if(ObjectUtils.isEmpty(userId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"用户id不能为空");
        }
        if(ObjectUtils.isEmpty(goodsId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"商品id不能为空");
        }
        if(ObjectUtils.isEmpty(operateNum)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"操作数量不能为空");
        }
        if(ObjectUtils.isEmpty(operateType)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "操作类型不能为空");
        }
        mapParams.put("stockConsume",operateNum);
        try {
            updateStockConsume(requestInputObject);//扣库存
            insertOrder(requestInputObject);//创建订单
        }catch (Exception e){
            logger.error("秒杀异常："+e);
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"秒杀异常："+e);
        }
        return resultOutputObject;
    }

    //乐观锁 利用库存表version字段 和数据库实物的隔离性 来解决高并发引起的超卖
    @ApiOperation(value = "秒杀-乐观锁")
    @RequestMapping(method = RequestMethod.POST,value = "/seckillByOptimisticLocking")
    @ResponseBody
    public ResultOutputObject seckillByOptimisticLocking(@RequestBody RequestInputObject requestInputObject) throws Exception {
        ResultOutputObject resultOutputObject = new ResultOutputObject();

        Map mapParams = requestInputObject.getParams();
        String goodsId = (String)mapParams.get("goodsId");
        String operateNum = (String)mapParams.get("operateNum");
        String userId = (String)mapParams.get("userId");
        String operateType = (String)mapParams.get("operateType");
        mapParams.put("stockConsume",operateNum);
        if(ObjectUtils.isEmpty(userId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"用户id不能为空");
        }
        if(ObjectUtils.isEmpty(goodsId)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"商品id不能为空");
        }
        if(ObjectUtils.isEmpty(operateNum)){
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"操作数量不能为空");
        }
        if(ObjectUtils.isEmpty(operateType)) {
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(), "操作类型不能为空");
        }
        mapParams.put("stockConsume",operateNum);
        mapParams.put("seckillByOptimisticLocking","1");//秒杀乐观锁
        try {
            updateStockConsume(requestInputObject);//扣库存
            insertOrder(requestInputObject);//创建订单
        }catch (Exception e){
            logger.error("秒杀异常："+e);
            throw new GeneralException(CommonCode.getDEFAUT_ERROR_CODE(),"秒杀异常："+e);
        }
        resultOutputObject.setRtnMsg("秒杀-乐观锁成功");
        resultOutputObject.setRtnCode("0");
        return resultOutputObject;
    }

}
