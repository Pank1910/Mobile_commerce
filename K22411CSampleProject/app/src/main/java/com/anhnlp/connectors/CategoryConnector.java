package com.anhnlp.connectors;

import com.anhnlp.models.Category;
import com.anhnlp.models.ListCategory;

import java.util.ArrayList;

public class CategoryConnector {
    ListCategory listCategory;

    public CategoryConnector() {
        listCategory = new ListCategory();
        listCategory.generate_sample_product_dataset();
    }

    public ArrayList<Category> get_all_categories() {
        if (listCategory == null) {
            listCategory = new ListCategory();
            listCategory.generate_sample_product_dataset();
        }
        return listCategory.getCategories();
    }

    public ArrayList<Category> get_categories_by_name_prefix(String prefix) {
        if (listCategory == null) {
            listCategory = new ListCategory();
            listCategory.generate_sample_product_dataset();
        }
        ArrayList<Category> results = new ArrayList<>();
        for (Category c : listCategory.getCategories()) {
            if (c.getName().startsWith(prefix)) {
                results.add(c);
            }
        }
        return results;
    }
}