package com.cn.ccj.springboot03.controller;

import com.cn.ccj.springboot03.entity.RequestInputObject;
import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IRegisterSV;
import com.cn.ccj.springboot03.redis.RedisOperate;
import com.cn.ccj.springboot03.utils.CommonCode;
import com.cn.ccj.springboot03.utils.SaltUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 19:56
 */
@RestController
@RequestMapping("/register")
@Api(value = "registerController",description = "用户注册类")
public class RegisterController {

    @Autowired
    private IRegisterSV iRegisterSV;

    @Autowired
    private CommonCode commonCode;

    public static Logger logger = LoggerFactory.getLogger(RedisOperate.class);

    @RequestMapping(method = RequestMethod.POST,value = "/register")
    @ApiOperation(value = "用户注册")
    public void register(@RequestBody RequestInputObject requestInputObject) throws GeneralException {
        Map params = requestInputObject.getParams();
        String username = (String)params.get("username");
        String password = (String)params.get("password");
        Map map = new HashMap();
        String salt = SaltUtils.getSalt(8);
        map.put("userName",username);
        Md5Hash md5Hash = new Md5Hash(password,salt,1024);
        String md5Password = md5Hash.toHex();
        map.put("password",md5Password);
        map.put("salt",salt);
        try {
            iRegisterSV.insertUserIdentity(map);
        }catch (Exception e){
            logger.error("保存用户信息失败:"+e.getMessage());
            throw new GeneralException(commonCode.DEFAUT_ERROR_CODE,"注册用户身份信息失败："+e.getMessage());
        }
    }
}
