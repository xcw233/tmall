package com.nd.xcw.tmall.comparator;
 
import com.nd.xcw.tmall.pojo.Product;

import java.util.Comparator;
 
public class ProductPriceComparator implements Comparator<Product>{
 
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice()-p2.getPromotePrice());
    }
 
}