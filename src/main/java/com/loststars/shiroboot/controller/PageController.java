package com.loststars.shiroboot.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PageController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("name") String name, @RequestParam("password") String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        try {
            subject.login(token);
            Session session = subject.getSession();
            System.out.println("session id: " + session.getId().toString());
            System.out.println("name: " + subject.getPrincipal());
            session.setAttribute("subject", subject);
            return "redirect:index";

        } catch (AuthenticationException e) {
            return "login";
        }
    }
    
    @RequestMapping(value = {"/addProduct", "/deleteProduct", "/addOrder", "/deleteOrder"})
    public ModelAndView action(HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        model.setViewName("action");
        model.addObject("action", request.getRequestURI());
        return model;
    }
    
    @RequestMapping("/nopermission")
    public String permission() {
        return "noPermission";
    }
}
