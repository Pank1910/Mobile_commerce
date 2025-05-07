package com.anhnlp.connectors;

import com.anhnlp.models.Employee;
import com.anhnlp.models.ListEmployee;

public class EmployeeConnector
{
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
