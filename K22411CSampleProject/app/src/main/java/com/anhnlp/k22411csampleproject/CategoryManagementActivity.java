package com.anhnlp.k22411csampleproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.connectors.CategoryConnector;
import com.anhnlp.models.Category;
import com.anhnlp.models.ListCategory;
import com.anhnlp.models.Product;

import java.util.List;

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

        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = adapterProduct.getItem(position);
                if (p != null) {
                    displayProductDetailActivity(p);
                } else {
                    Toast.makeText(CategoryManagementActivity.this, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
                }
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

//    private void addEvents() {
//        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
//                Product p = adapterProduct.getItem(i);
//                displayProductDetailActivity(p);
//            }
//        });
//    }

    private void displayProductDetailActivity(Product p) {
        Intent intent = new Intent(CategoryManagementActivity.this, ProductDetailActivity.class);
        intent.putExtra("SELECTED_PRODUCT", p);
        startActivity(intent);
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
        connector = new CategoryConnector();
        try {
            List<Category> products = connector.get_all_categories();
            if (products != null && !products.isEmpty()) {
                adapterCategory.addAll(products);
            } else {
                Toast.makeText(this, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lấy dữ liệu sản phẩm: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        lvCategory.setAdapter(adapterProduct);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_new_product) {
            Toast.makeText(CategoryManagementActivity.this, "Mở màn hình thêm mới sản phẩm", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CategoryManagementActivity.this, ProductDetailActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_product_report) {
            Toast.makeText(CategoryManagementActivity.this, "Xem báo cáo sản phẩm", Toast.LENGTH_LONG).show();
        } else if (item.getItemId() == R.id.menu_help) {
            Toast.makeText(CategoryManagementActivity.this, "Mở website hướng dẫn sử dụng", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}