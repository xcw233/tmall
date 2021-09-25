package com.nd.xcw.tmall.controller;

import java.util.List;

import com.nd.xcw.tmall.pojo.Category;
import com.nd.xcw.tmall.service.ProductService;
import com.nd.xcw.tmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nd.xcw.tmall.pojo.Product;
import com.nd.xcw.tmall.service.CategoryService;

@Controller
@RequestMapping("")
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @PostMapping("admin_products")
    public String add(Model model, Product p) {
        productService.add(p);
        return "redirect:/admin_products/"+p.getCid();
    }

    @DeleteMapping("admin_products/{id}")
    public String delete(@PathVariable("id") int id) {
        Product p = productService.get(id);
        productService.delete(id);
        return "redirect:/admin_products/"+p.getCid();
    }

    @GetMapping("admin_products/{cid}/{id}")
    public String edit(Model model,@PathVariable("id") int id) {
        Product p = productService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p", p);
        return "admin/editProduct";
    }

    @PutMapping("admin_products")
    public String update(Product p) {
        productService.update(p);
        return "redirect:/admin_products/"+p.getCid();
    }

    @GetMapping("admin_products/{cid}")
    public String list(@PathVariable("cid") int cid, Model model, Page page) {
        Category c = categoryService.get(cid);

        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Product> ps = productService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        model.addAttribute("ps", ps);
        model.addAttribute("c", c);
        model.addAttribute("page", page);

        return "admin/listProduct";
    }
}
