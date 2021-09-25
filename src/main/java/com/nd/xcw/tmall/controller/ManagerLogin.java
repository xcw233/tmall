package com.nd.xcw.tmall.controller;

import com.nd.xcw.tmall.pojo.Manager;
import com.nd.xcw.tmall.pojo.User;
import com.nd.xcw.tmall.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class ManagerLogin {
    @Autowired
    ManagerService managerService;

    @RequestMapping("/managerLogin")
    public String login(String name, String password, Model model, HttpSession session)
    {
        name = HtmlUtils.htmlEscape(name);
        Manager manager = managerService.get(name, password);

        if(null == manager)
        {
            model.addAttribute("msg","账号密码错误");
            return "admin/login";
        }

        session.setAttribute("manager",manager);
        return "redirect:admin_categories";
    }

    @RequestMapping("/managerLogout")
    public String logout(HttpSession session)
    {
        session.removeAttribute("manager");
        return "admin/login";
    }
}
