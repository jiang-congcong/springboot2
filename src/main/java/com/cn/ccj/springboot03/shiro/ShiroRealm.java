package com.cn.ccj.springboot03.shiro;

import com.cn.ccj.springboot03.generalException.GeneralException;
import com.cn.ccj.springboot03.iservice.IUserSV;
import com.cn.ccj.springboot03.utils.CommonCode;
import io.lettuce.core.output.GenericMapOutput;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.elasticsearch.common.util.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jiangcongcong
 * @date 2021/3/25 11:10
 * 自定义realm
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    IUserSV iUserSV;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String principal = (String)authenticationToken.getPrincipal();//用户名
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        String credentials = new String((char[])authenticationToken.getCredentials());//密码
        List<Map<String,Object>> userList = new ArrayList<>();
        try {
            userList = iUserSV.selectUserByUserName(principal);
        }catch (Exception e){
            new GeneralException(CommonCode.DEFAUT_ERROR_CODE,"查询用户身份密码失败："+e.getMessage());
        }
        if(!ObjectUtils.isEmpty(userList)){
            for (Map map:userList){ //可能有多个用户名相同
                String userName = (String)map.get("userName");
                String password = (String)map.get("password");
                String salt = (String)map.get("salt");
                Md5Hash md5Hash = new Md5Hash(credentials,salt,1024);
                String md5Password = md5Hash.toHex();
                if(password.equals(md5Password)){
                    return new SimpleAuthenticationInfo(principal,credentials, ByteSource.Util.bytes(salt),this.getName());
                }
            }
        }

        return null;
    }
}
