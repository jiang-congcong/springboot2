package com.cn.ccj.springboot03.serviceImpl;

import com.cn.ccj.springboot03.dao.UserDao;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IRegisterSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.utils.CommonCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 20:01
 */
@Service
public class RegisterSVImpl implements IRegisterSV {

    @Autowired
    public UserDao userDao;

    @Autowired
    public CommonCode commonCode;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @Override
    public String insertUserIdentity(Map map) throws GeneralException {
        try {
            userDao.insertUserIdentity(map);
        }catch (Exception e){
            logger.error("保存用户身份信息失败！");
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"保存用户身份信息失败"+e.getMessage());
        }
        return "保存用户身份信息成功！";
    }
}
