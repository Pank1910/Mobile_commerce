package com.anhnlp.connectors;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anhnlp.models.OrderDetailsViewer;

public class OrderDetailsConnector {
    public OrderDetailsViewer getOrderDetails(SQLiteDatabase database, int orderId) {
        OrderDetailsViewer details = null;

        String sql = "SELECT od.Id, od.OrderId, od.ProductId, od.Quantity, od.Price, od.Discount, od.VAT, " +
                "((od.Price * od.Quantity - (od.Discount / 100.0) * od.Quantity * od.Price) * (1 + od.VAT / 100.0)) AS TotalValue " +
                "FROM OrderDetails od WHERE od.OrderId = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            int orderIdValue = cursor.getInt(1);
            int productId = cursor.getInt(2);
            int quantity = cursor.getInt(3);
            double price = cursor.getDouble(4);
            double discount = cursor.getDouble(5);
            double vat = cursor.getDouble(6);
            double totalValue = cursor.getDouble(7);

            details = new OrderDetailsViewer();
            details.setId(id);
            details.setOrderId(orderIdValue);
            details.setProductId(productId);
            details.setQuantity(quantity);
            details.setPrice(price);
            details.setDiscount(discount);
            details.setVAT(vat);
            details.setTotalValue(totalValue);
        }
        cursor.close();
        return details;
    }
}