package com.anhnlp.models;

import java.util.ArrayList;

public class ListEmployee {
//    Biến lưu danh sách employee
    private ArrayList<Employee> employees;
//  private ArrayList<Object> employees; là biến lưu danh sách nào cũng dc => ko nên
//    1 biến sinh ra phải cấp phát ô nhớ => khởi tạo

//  chuột phải => generate => constructor ko đối số
    public ListEmployee() {
        employees=new ArrayList<>();
    }
//  phải thông qua getter & setter để thay đổi thông tin
//  hàm truy suất getter
    public ArrayList<Employee> getEmployees() {
        return employees;
    }
//  hàm truy suất setter để thay đổi giá trị
    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }
    public void gen_dataset()
    {
        Employee e1=new Employee();
        e1.setId(1);
        e1.setName("John");
        e1.setEmaill("john@gmail.com");
        e1.setPhone("0878825252");
        e1.setUsername("john");
        e1.setPassword("123");
        employees.add(e1);

        Employee e2=new Employee();
        e2.setId(2);
        e2.setName("Tom");
        e2.setEmaill("tom@gmail.com");
        e2.setPhone("0878825242");
        e2.setUsername("tom");
        e2.setPassword("456");
        employees.add(e2);

        Employee e3=new Employee();
        e3.setId(3);
        e3.setName("Peter");
        e3.setEmaill("peter@gmail.com");
        e3.setPhone("0878825262");
        e3.setUsername("peter");
        e3.setPassword("789");
        employees.add(e3);
    }
}
