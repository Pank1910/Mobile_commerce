package com.anhnlp.k22411csampleproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.connectors.CategoryConnector;
import com.anhnlp.models.Category;
import com.anhnlp.models.ListCategory;
import com.anhnlp.models.Product;

public class CategoryManagementActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    ListView lvCategory;
    ArrayAdapter<Category> adapterCategory;
    CategoryConnector connector;
    ListCategory listCategory;
    ArrayAdapter<Product>adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        addEvents();
    }

    private void addEvents() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category c=adapterCategory.getItem(position);
                displayProductsByCategory(c);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        lvCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
//                Category selected = adapterCategory.getItem(i);
//                adapterCategory.remove(selected);
//                return false;
//            }
//        });
    }

    private void displayProductsByCategory(Category c) {
//        xóa dữ liêụ cũ trong listview đi
        adapterProduct.clear();
//        nạp mới lại dữ liệu cho adapter;
        adapterProduct.addAll(c.getProducts());
    }

    private void addViews() {
        spinnerCategory=findViewById(R.id.spinnerCategory);
        adapterCategory=new ArrayAdapter<>(
                CategoryManagementActivity.this,
                android.R.layout.simple_spinner_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        listCategory=new ListCategory();
        listCategory.generate_sample_product_dataset();
        adapterCategory.addAll(listCategory.getCategories());


        lvCategory = findViewById(R.id.lvCategory);
        adapterProduct = new ArrayAdapter<>(CategoryManagementActivity.this,
                android.R.layout.simple_list_item_1);
//        connector = new CategoryConnector();
//        adapterCategory.addAll(connector.get_all_categories());
        lvCategory.setAdapter(adapterProduct);
    }
}