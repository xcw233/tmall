package com.nd.xcw.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.nd.xcw.tmall.comparator.*;
import com.nd.xcw.tmall.pojo.*;
import com.nd.xcw.tmall.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;

    @RequestMapping("forehome")
    public String home(Model model) {
        List<Category> cs= categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
         model.addAttribute("cs", cs);
        return "fore/home";
    }

    @RequestMapping("forelogin")
    public String login(String name, String password, Model model, HttpSession session)
    {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);

        if(null == user)
        {
            model.addAttribute("msg","账号密码错误");
            return "fore/login";
        }

        session.setAttribute("user",user);
        return "redirect:forehome";
    }

    @RequestMapping("foreregister")
    public String register(Model model,User user) {
        String name =  user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);

        if(exist){
            String m ="用户名已经被使用,不能使用";
            model.addAttribute("msg", m);
            model.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);

        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogout")
    public String logout( HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product( int pid, Model model) {
        Product p = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.list(p.getId(), ProductImageService.type_single);
        List<ProductImage> productDetailImages = productImageService.list(p.getId(), ProductImageService.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);

        List<PropertyValue> pvs = propertyValueService.list(p.getId());
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("reviews", reviews);
        model.addAttribute("p", p);
        model.addAttribute("pvs", pvs);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin( HttpSession session) {
        User user =(User)  session.getAttribute("user");
        if(null!=user)
            return "success";
        return "fail";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(null==user){
            return "fail";
        }
        session.setAttribute("user", user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid,String sort, Model model) {
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }

        model.addAttribute("c", c);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search( String keyword,Model model){

        PageHelper.offsetPage(0,20);
        List<Product> ps= productService.search(keyword);
        productService.setSaleAndReviewNumber(ps);
        model.addAttribute("ps",ps);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session) {
        Product p = productService.get(pid);
        int oiid = 0;

        User user =(User)  session.getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return "redirect:forebuy?oiid="+oiid;
    }

    @RequestMapping("forebuy")
    public String buy(Model model,String[] oiid, HttpSession session)
    {
        List<OrderItem> ois = new ArrayList<>();
        float total = 0f;
        for(String strid:oiid)
        {
            int id= Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }

        session.setAttribute("ois",ois);
        model.addAttribute("total",total);

        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num,Model model, HttpSession session)
    {
        Product p = productService.get(pid);
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());

        boolean found = false;
        for(OrderItem oi:ois)
        {
            if(oi.getPid().intValue() == p.getId())
            {
                oi.setNumber(oi.getNumber() + num);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }

        if(!found)
        {
            OrderItem oi = new OrderItem();
            oi.setNumber(num);
            oi.setUid(user.getId());
            oi.setPid(pid);
            orderItemService.add(oi);
        }

        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session)
    {
        User user =(User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        model.addAttribute("ois",ois);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(Model model, HttpSession session, int pid, int number){
        User user = (User) session.getAttribute("user");
        if(null == user)
            return "fail";

        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for(OrderItem oi:ois)
        {
            if(oi.getPid().intValue() == pid)
            {
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }
        }

        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(Model model, HttpSession session, int oiid)
    {
        User user = (User) session.getAttribute("user");
        if(null == user)
            return "fail";
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(Model model, Order order, HttpSession session)
    {
        User user = (User) session.getAttribute("user");
        String ordercode =new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);
        order.setCreateDate(new Date());
        order.setStatus(OrderService.waitPay);
        order.setUid(user.getId());
        order.setOrderCode(ordercode);
        List<OrderItem> ois =(List<OrderItem>) session.getAttribute("ois");

        float total = orderService.add(order, ois);
        return "redirect:forealipay?oid="+order.getId()+"&total="+total;
    }

    @RequestMapping("forepayed")
    public String payed(int oid,float total, Model model)
    {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitDelivery);
        o.setPayDate(new Date());
        orderService.update(o);
        model.addAttribute("o",o);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought(HttpSession session, Model model)
    {
        User user = (User) session.getAttribute("user");

        List<Order> os = orderService.list(user.getId(), OrderService.delete);
        orderItemService.fill(os);

        model.addAttribute("os",os);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay(int oid, Model model)
    {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        model.addAttribute("o",order);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(int oid)
    {
        Order order = orderService.get(oid);
        order.setConfirmDate(new Date());
        order.setStatus(OrderService.waitReview);
        orderService.update(order);
        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(int oid)
    {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.delete);
        orderService.update(order);
        return "success";
    }

    @RequestMapping("forereview")
    public String review(int oid, Model model)
    {
        Order order = orderService.get(oid);
        orderItemService.fill(order);
        Product p = order.getOrderItems().get(0).getProduct();
        List<Review> rs = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p",p);
        model.addAttribute("o",order);
        model.addAttribute("reviews",rs);
        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doreview(@RequestParam("oid") int oid,@RequestParam("pid") int pid, String content,Model model,HttpSession session)
    {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.finish);
        orderService.update(order);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user =(User) session.getAttribute("user");
        Review review = new Review();
        review.setPid(p.getId());
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewService.add(review);

        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
}
