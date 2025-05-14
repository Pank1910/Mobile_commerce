package com.anhnlp.connectors;

import com.anhnlp.models.Product;
import com.anhnlp.models.ListProduct;

import java.util.ArrayList;

public class ProductConnector {
    ListProduct listProduct;

    public ProductConnector() {
        listProduct = new ListProduct();
        listProduct.generate_sample_dataset();
    }

    public ArrayList<Product> get_all_products() {
        if (listProduct == null) {
            listProduct = new ListProduct();
            listProduct.generate_sample_dataset();
        }
        return listProduct.getProducts();
    }

    public ArrayList<Product> get_products_by_category(int cate_id) {
        if (listProduct == null) {
            listProduct = new ListProduct();
            listProduct.generate_sample_dataset();
        }
        ArrayList<Product> results = new ArrayList<>();
        for (Product p : listProduct.getProducts()) {
            if (p.getCate_id() == cate_id) {
                results.add(p);
            }
        }
        return results;
    }
}