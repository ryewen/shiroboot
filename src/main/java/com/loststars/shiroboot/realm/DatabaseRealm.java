package com.loststars.shiroboot.realm;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.loststars.shiroboot.model.User;
import com.loststars.shiroboot.service.ShiroDAOService;
import com.loststars.shiroboot.util.SpringContextUtils;

public class DatabaseRealm extends AuthorizingRealm {
    
    private ShiroDAOService service;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO Auto-generated method stub
        if (service == null) service = SpringContextUtils.getContext().getBean(ShiroDAOService.class);
        System.out.println("Permission");
        String name = (String) principals.getPrimaryPrincipal();
        Set<String> permissions = service.listPermissions(name);
        Set<String> roles = service.listRoles(name);
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        s.setStringPermissions(permissions);
        s.setRoles(roles);
        return s;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // TODO Auto-generated method stub
        if (service == null) service = SpringContextUtils.getContext().getBean(ShiroDAOService.class);
        System.out.println("Login");
        UsernamePasswordToken t = (UsernamePasswordToken) token;
        String name = t.getUsername();
        User user = service.getUser(name);
        if (user == null) throw new AuthenticationException();
        String passwordDB = user.getPassword();
        if (passwordDB == null) throw new AuthenticationException();
        return new SimpleAuthenticationInfo(name, passwordDB, ByteSource.Util.bytes(user.getSalt()), getName());
    }
}
