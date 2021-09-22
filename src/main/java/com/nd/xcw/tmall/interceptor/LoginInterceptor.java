package com.nd.xcw.tmall.interceptor;

import com.nd.xcw.tmall.pojo.Manager;
import com.nd.xcw.tmall.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        String[] noNeedAuthPage = new String[]{
            "home",
            "checkLogin",
            "register",
            "loginAjax",
            "login",
            "product",
            "category",
            "search"};
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri,contextPath);
        if(uri.startsWith("/fore"))
        {
            String method = StringUtils.substringAfterLast(uri,"/fore");
            if(!Arrays.asList(noNeedAuthPage).contains(method))
            {
                User user = (User) session.getAttribute("user");
                if(null == user)
                {
                    response.sendRedirect("loginPage");
                    return false;
                }
            }
        }else if(uri.startsWith("/admin"))
        {
            Manager manager = (Manager) session.getAttribute("manager");

            if(null == manager)
            {
                response.sendRedirect("managerLoginPage");
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
