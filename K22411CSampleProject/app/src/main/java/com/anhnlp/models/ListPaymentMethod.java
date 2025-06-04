package com.anhnlp.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ListPaymentMethod {
    ArrayList<PaymentMethod> paymentMethods;
    public ListPaymentMethod()
    {
        paymentMethods=new ArrayList<>();
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(ArrayList<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public void gen_payments_method()
    {
        paymentMethods.add(new PaymentMethod(1,"Banking Account","Chuyển khoản ngân hàng"));
        paymentMethods.add(new PaymentMethod(2,"MOMO","Thanh toán ví MOMO"));
        paymentMethods.add(new PaymentMethod(3,"CASH","Thanh toán tiền mặt"));
        paymentMethods.add(new PaymentMethod(4,"COD","Nhận hàng rồi thanh toán"));

    }

    // Thêm phương thức để lấy danh sách PaymentMethod từ database
    public ListPaymentMethod getAllPaymentMethods(SQLiteDatabase database) {
        paymentMethods.clear(); // Xóa danh sách hiện tại để tránh trùng lặp
        Cursor cursor = database.rawQuery("SELECT * FROM PaymentMethod", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("Id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                paymentMethods.add(new PaymentMethod(id, name, description));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return this;
    }
}
