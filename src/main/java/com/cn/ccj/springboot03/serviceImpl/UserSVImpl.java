package com.cn.ccj.springboot03.serviceImpl;

import com.cn.ccj.springboot03.dao.UserDao;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IUserSV;
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
 * @date 2021/3/28 16:19
 */
@Service
public class UserSVImpl implements IUserSV {

    @Autowired
    private UserDao userDao;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @Override
    public List<Map<String,Object>> selectUserByUserName(String userName) throws Exception {
        List<Map<String,Object>> resultList = new ArrayList<>();
        try {
            resultList = userDao.selectUserByUserName(userName);
        }catch (Exception e){
            logger.error("查询用户身份密码失败："+e.getMessage());
            throw new GeneralException(CommonCode.DEFAUT_ERROR_CODE,"查询用户身份密码失败"+e.getMessage());
        }
        return resultList;
    }
}
