package com.anhnlp.connectors;

import static android.icu.text.ListFormatter.Type.OR;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anhnlp.models.Employee;
import com.anhnlp.models.ListEmployee;

public class EmployeeConnector
{

    public Employee login(SQLiteDatabase database, String usr, String pwd)
    {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM Employee WHERE UserName = ? AND PassWord = ?",
        new String[]{usr,pwd});
        Employee emp=null;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String email = cursor.getString(2);
            String phone = cursor.getString(3);
            String username = cursor.getString(4);
            String password = cursor.getString(5);
            emp=new Employee();
            emp.setId(id);
            emp.setName(name);
            emp.setEmaill(email);
            emp.setPhone(phone);
            emp.setUsername(username);
            emp.setPassword(password);
        }
        cursor.close();

        return emp;
    }
//
    public Employee login(String usr,String pwd)
    {
        ListEmployee le=new ListEmployee();
        le.gen_dataset();
//      trong thực tế là nap từ csdl chứ k gen
//      duyệt từng employee trong list employee
        for (Employee emp : le.getEmployees())
        {
//            username ko phân biệt hoa thường nhưng mật khẩu phân biệt hoa thường
            if (emp.getUsername().equalsIgnoreCase(usr) && emp.getPassword().equals(pwd))
            {
                return emp;
            }
        }
        return null;
    }

}
