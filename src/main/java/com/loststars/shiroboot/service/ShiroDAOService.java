package com.loststars.shiroboot.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loststars.shiroboot.dao.ShiroDAO;
import com.loststars.shiroboot.model.User;

@Service
public class ShiroDAOService {

    @Autowired
    private ShiroDAO shiroDAO;
    
    public User getUser(String name) {
        User user = null;
        user = shiroDAO.getUser(name);
        return user;
    }
    
    public Set<String> listRoles(String name) {
        List<String> roles = shiroDAO.listRoles(name);
        if (roles == null) return new HashSet<String>();
        return new HashSet<String>(roles);
    }
    
    public Set<String> listPermissions() {
        List<String> permissions = shiroDAO.listAllPermissions();
        if (permissions == null) return new HashSet<String>();
        return new HashSet<String>(permissions);
    }
    
    public Set<String> listPermissions(String name) {
        List<String> permissions = shiroDAO.listPermissions(name);
        if (permissions == null) return new HashSet<String>();
        return new HashSet<String>(permissions);
    }
    
    public void addUser(User user) {
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, 2).toString();
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        shiroDAO.addUser(user);
    }
}
