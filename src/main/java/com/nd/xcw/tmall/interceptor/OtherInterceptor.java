package com.nd.xcw.tmall.interceptor;

import com.nd.xcw.tmall.pojo.Category;
import com.nd.xcw.tmall.pojo.OrderItem;
import com.nd.xcw.tmall.pojo.User;
import com.nd.xcw.tmall.service.CategoryService;
import com.nd.xcw.tmall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class OtherInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        获取分类信息放在搜索栏的下方
        List<Category> cs = categoryService.list();
        request.getSession().setAttribute("cs",cs);
//变形金刚链接 跳转到首页
        HttpSession session = request.getSession();
        String contextPath = session.getServletContext().getContextPath();
        request.getSession().setAttribute("contextPath",contextPath);
//        显示购物车中商品的数量
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user)
        {
            List<OrderItem> ois = orderItemService.listByUser(user.getId());
            for(OrderItem oi:ois)
            {
                cartTotalItemNumber += oi.getNumber();
            }
        }
        session.setAttribute("cartTotalItemNumber",cartTotalItemNumber);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
