package com.nd.xcw.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nd.xcw.tmall.pojo.Category;
import com.nd.xcw.tmall.service.CategoryService;
import com.nd.xcw.tmall.util.ImageUtil;
import com.nd.xcw.tmall.util.Page;
import com.nd.xcw.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
  
    @GetMapping("admin_categories")
    public String list(Model model, Page page){
        PageHelper.offsetPage(page.getStart(), page.getCount(),"id desc");
        List<Category> cs= categoryService.list();
        int total = (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs", cs);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }

    @PostMapping("admin_categories")
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.add(c);
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return "redirect:/admin_categories";
    }

    @DeleteMapping("admin_categories/{id}")
    public String delete(@PathVariable("id") int id,HttpSession session) throws IOException {
        categoryService.delete(id);

        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();

        return "redirect:/admin_categories";
    }

    @GetMapping("admin_categories/{id}")
    public String edit(@PathVariable("id") int id,Model model) throws IOException {
        Category c= categoryService.get(id);
        model.addAttribute("c", c);
        return "admin/editCategory";
    }

    @PutMapping("admin_categories")
    public String update(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.update(c);
        MultipartFile image = uploadedImageFile.getImage();
        if(null!=image &&!image.isEmpty()){
            File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,c.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }
        return "redirect:/admin_categories";
    }
}