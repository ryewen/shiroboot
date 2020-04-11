package com.loststars.shiroboot.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.loststars.shiroboot.service.ShiroDAOService;
import com.loststars.shiroboot.util.SpringContextUtils;

public class URLPathMatchingFilter extends PathMatchingFilter {

    private ShiroDAOService shiroDAOService;
    
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        if (shiroDAOService == null) {
            shiroDAOService = SpringContextUtils.getContext().getBean(ShiroDAOService.class);
        }
        String requestURI = getPathWithinApplication(request).substring(1);
        Subject subject = SecurityUtils.getSubject();
        if (! subject.isAuthenticated()) {
            WebUtils.issueRedirect(request, response, "/login");
            return false;
        }
        boolean needInterceptor = shiroDAOService.listPermissions().contains(requestURI);
        if (! needInterceptor) {
            return true;
        } else {
            boolean hasPermission = false;
            String userName = subject.getPrincipal().toString();
            hasPermission = shiroDAOService.listPermissions(userName).contains(requestURI);
            if (hasPermission) {
                return true;
            } else {
                WebUtils.issueRedirect(request, response, "/nopermission");
                return false;
            }
        }
    }
}
