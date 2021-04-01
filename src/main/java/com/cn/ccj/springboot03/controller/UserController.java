package com.cn.ccj.springboot03.controller;

import com.cn.ccj.springboot03.entity.RequestInputObject;
import com.cn.ccj.springboot03.entity.ResultOutputObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 18:46
 * 用户控制
 */
@Controller
@RequestMapping("userController")
@Api(value = "userController",description = "用户登录类")
public class UserController {

    @ApiOperation(value = "用户登录")
    @RequestMapping(method = RequestMethod.POST,value = "/login")
    @ResponseBody
    public String login(@RequestBody RequestInputObject requestInputObject) throws Exception {
        String resultStr = "";
        ResultOutputObject resultOutputObject = new ResultOutputObject();
        Map params = requestInputObject.getParams();
        String username = (String)params.get("username");
        String password = (String)params.get("password");
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username,password));
            //return "redirct:/index.html";
            //resultOutputObject.setBeans(resultList);
            resultOutputObject.setRtnMsg("认证成功");
            resultOutputObject.setRtnCode("0");
            System.out.println("认证成功");
            //resultStr = "index.html";
            resultStr = "认证成功";
        }catch (UnknownAccountException e){
            resultOutputObject.setRtnMsg("用户名错误");
            resultOutputObject.setRtnCode("-9999");
            System.out.println("用户名错误！");
            //throw new GeneralException("-9999","用户名错误"+e.getMessage());
            resultStr = "用户名错误";
        }catch (IncorrectCredentialsException e){
            resultOutputObject.setRtnMsg("密码错误");
            resultOutputObject.setRtnCode("-9999");
            System.out.println("密码错误！");
            //throw new GeneralException("-9999","密码错误"+e.getMessage());
            resultStr = "密码错误";
        }
        //return resultOutputObject;
        return resultStr;
    }

    @ApiOperation(value = "用户注销")
    @RequestMapping(method = RequestMethod.POST,value = "/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "注销成功";
    }
}
