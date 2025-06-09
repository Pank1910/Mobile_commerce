package com.anhnlp.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anhnlp.models.OrderDetailsViewer;

import java.util.ArrayList;

public class OrderDetailsConnector {
    public ArrayList<OrderDetailsViewer> getOrderDetails(SQLiteDatabase database, int orderId) {
        ArrayList<OrderDetailsViewer> detailsList = new ArrayList<>();

        String sql = "SELECT od.Id, od.OrderId, od.ProductId, od.Quantity, od.Price, od.Discount, od.VAT, " +
                "((od.Price * od.Quantity - (od.Discount / 100.0) * od.Quantity * od.Price) * (1 + od.VAT / 100.0)) AS TotalValue " +
                "FROM OrderDetails od WHERE od.OrderId = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(orderId)});
        while (cursor.moveToNext()) {
            OrderDetailsViewer details = new OrderDetailsViewer();
            details.setId(cursor.getInt(0));
            details.setOrderId(cursor.getInt(1));
            details.setProductId(cursor.getInt(2));
            details.setQuantity(cursor.getInt(3));
            details.setPrice(cursor.getDouble(4));
            details.setDiscount(cursor.getDouble(5));
            details.setVAT(cursor.getDouble(6));
            details.setTotalValue(cursor.getDouble(7));
            detailsList.add(details);
        }
        cursor.close();
        return detailsList;
    }
}