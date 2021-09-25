package com.nd.xcw.tmall.controller;

import com.nd.xcw.tmall.pojo.Product;
import com.nd.xcw.tmall.pojo.PropertyValue;
import com.nd.xcw.tmall.service.ProductService;
import com.nd.xcw.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;

    @GetMapping("admin_propertyValues/{pid}")
    public String edit(Model model,@PathVariable("pid") int pid) {
        Product p = productService.get(pid);
        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(p.getId());

        model.addAttribute("p", p);
        model.addAttribute("pvs", pvs);
        return "admin/editPropertyValue";
    }
    @PostMapping("admin_propertyValues")
    @ResponseBody
    public String update(PropertyValue pv) {
        propertyValueService.update(pv);
        return "success";
    }
}
