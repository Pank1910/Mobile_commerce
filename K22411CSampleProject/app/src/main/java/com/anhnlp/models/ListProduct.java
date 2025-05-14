package com.anhnlp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ListProduct implements Serializable {
    private ArrayList<Product> products;

    public ListProduct() {
        products = new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public void generate_sample_dataset() {
        Random random = new Random();
        String[] productPrefixes = {"Laptop", "Phone", "Shirt", "Book", "Toy"};
        for (int i = 1; i <= 100; i++) {
            int id = i;
            String name = productPrefixes[random.nextInt(productPrefixes.length)] + " " + i;
            int quantity = random.nextInt(100) + 1; // Random quantity from 1 to 100
            double price = 10.0 + random.nextDouble() * 990.0; // Random price from 10 to 1000
            int cate_id = random.nextInt(5) + 1; // Random category ID from 1 to 5
            String description = "Description for " + name;
            Product p = new Product(id, name, quantity, price, cate_id, description);
            addProduct(p);
        }
    }
}