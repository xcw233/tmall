package com.nd.xcw.tmall.controller;

import java.util.List;

import com.nd.xcw.tmall.pojo.Category;
import com.nd.xcw.tmall.pojo.Property;
import com.nd.xcw.tmall.service.CategoryService;
import com.nd.xcw.tmall.service.PropertyService;
import com.nd.xcw.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @PostMapping("admin_properties")
    public String add(Model model, Property p) {
        propertyService.add(p);
        return "redirect:admin_properties/"+p.getCid();
    }

    @DeleteMapping("admin_properties/{id}")
    public String delete(@PathVariable("id") int id) {
        Property p = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:/admin_properties/"+p.getCid();
    }

    @GetMapping("admin_properties/{cid}/{id}")
    public String edit(Model model,@PathVariable("id") int id) {
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p", p);
        return "admin/editProperty";
    }

    @PutMapping("admin_properties")
    public String update(Property p, HttpSession session) {
        propertyService.update(p);
        String contextPath = session.getServletContext().getContextPath();
        return "redirect:/admin_properties/"+p.getCid();
    }

    @GetMapping("admin_properties/{cid}")
    public String list(@PathVariable("cid") int cid, Model model, Page page) {
        Category c = categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Property> ps = propertyService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        model.addAttribute("ps", ps);
        model.addAttribute("c", c);
        model.addAttribute("page", page);

        return "admin/listProperty";
    }
}
