package com.anhnlp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class ListCategory implements Serializable {
    private ArrayList<Category> categories;

    public ListCategory() {
        categories = new ArrayList<>();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category c) {
        categories.add(c);
    }

    public void generate_sample_dataset() {
        Random random = new Random();
        String[] categoryPrefixes = {"Electronics", "Clothing", "Books", "Home", "Toys"};
        for (int i = 1; i <= 50; i++) {
            int id = i;
            String name = categoryPrefixes[random.nextInt(categoryPrefixes.length)] + " " + i;
            Category c = new Category(id, name);
            addCategory(c);
        }
    }
}