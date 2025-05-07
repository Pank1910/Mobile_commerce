package com.anhnlp.k22411csampleproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmployeeManagementActivity extends AppCompatActivity {

    // Khai báo các thành phần UI
    private EditText edtEmployeeId, edtEmployeeName, edtEmployeePhone, edtEmployeeEmail, edtEmployeePosition, edtEmployeeSalary;
    private Button btnAdd, btnClear, btnSearch;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_management);

        // Ánh xạ các thành phần UI
        initializeViews();

        // Thiết lập các sự kiện
        setupEvents();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        // Ánh xạ các EditText
        edtEmployeeId = findViewById(R.id.edtEmployeeId);
        edtEmployeeName = findViewById(R.id.edtEmployeeName);
        edtEmployeePhone = findViewById(R.id.edtEmployeePhone);
        edtEmployeeEmail = findViewById(R.id.edtEmployeeEmail);
        edtEmployeePosition = findViewById(R.id.edtEmployeePosition);
        edtEmployeeSalary = findViewById(R.id.edtEmployeeSalary);

        // Ánh xạ các Button
        btnAdd = findViewById(R.id.btnAdd);
        btnClear = findViewById(R.id.btnClear);
        btnSearch = findViewById(R.id.btnSearch);

        // Ánh xạ tìm kiếm
        edtSearch = findViewById(R.id.edtSearch);
    }

    private void setupEvents() {
        // Sự kiện nút Thêm
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra dữ liệu nhập vào
                if (validateInputs()) {
                    // Thực hiện thêm nhân viên
                    addEmployee();
                }
            }
        });

        // Sự kiện nút Xóa trống
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearInputs();
            }
        });

        // Sự kiện nút Tìm kiếm
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = edtSearch.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    // Thực hiện tìm kiếm nhân viên
                    searchEmployee(searchText);
                } else {
                    Toast.makeText(EmployeeManagementActivity.this,
                            "Vui lòng nhập từ khóa tìm kiếm",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Phương thức kiểm tra dữ liệu nhập vào
    private boolean validateInputs() {
        // Kiểm tra mã nhân viên
        if (edtEmployeeId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_SHORT).show();
            edtEmployeeId.requestFocus();
            return false;
        }

        // Kiểm tra tên nhân viên
        if (edtEmployeeName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên nhân viên", Toast.LENGTH_SHORT).show();
            edtEmployeeName.requestFocus();
            return false;
        }

        // Kiểm tra số điện thoại
        String phoneNumber = edtEmployeePhone.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            edtEmployeePhone.requestFocus();
            return false;
        }

        // Kiểm tra email
        String email = edtEmployeeEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            edtEmployeeEmail.requestFocus();
            return false;
        }

        // Kiểm tra chức vụ
        if (edtEmployeePosition.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập chức vụ", Toast.LENGTH_SHORT).show();
            edtEmployeePosition.requestFocus();
            return false;
        }

        // Kiểm tra lương
        String salary = edtEmployeeSalary.getText().toString().trim();
        if (salary.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lương", Toast.LENGTH_SHORT).show();
            edtEmployeeSalary.requestFocus();
            return false;
        }

        return true;
    }

    // Phương thức thêm nhân viên
    private void addEmployee() {
        // Lấy thông tin từ các trường nhập liệu
        String id = edtEmployeeId.getText().toString().trim();
        String name = edtEmployeeName.getText().toString().trim();
        String phone = edtEmployeePhone.getText().toString().trim();
        String email = edtEmployeeEmail.getText().toString().trim();
        String position = edtEmployeePosition.getText().toString().trim();
        String salary = edtEmployeeSalary.getText().toString().trim();

        // TODO: Thêm nhân viên vào cơ sở dữ liệu hoặc danh sách

        // Hiển thị thông báo thành công
        Toast.makeText(this, "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();

        // Xóa trống các trường nhập liệu
        clearInputs();
    }

    // Phương thức xóa trống các trường nhập liệu
    private void clearInputs() {
        edtEmployeeId.setText("");
        edtEmployeeName.setText("");
        edtEmployeePhone.setText("");
        edtEmployeeEmail.setText("");
        edtEmployeePosition.setText("");
        edtEmployeeSalary.setText("");
        edtEmployeeId.requestFocus();
    }

    // Phương thức tìm kiếm nhân viên
    private void searchEmployee(String keyword) {
        // TODO: Thực hiện tìm kiếm nhân viên theo từ khóa

        // Hiển thị thông báo kết quả tìm kiếm
        Toast.makeText(this, "Đang tìm kiếm: " + keyword, Toast.LENGTH_SHORT).show();
    }
}