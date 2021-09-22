package com.nd.xcw.tmall.comparator;
 
import com.nd.xcw.tmall.pojo.Product;

import java.util.Comparator;
 
public class ProductSaleCountComparator implements Comparator<Product>{
 
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount();
    }
 
}