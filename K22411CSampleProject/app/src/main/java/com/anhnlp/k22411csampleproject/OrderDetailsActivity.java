package com.anhnlp.k22411csampleproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anhnlp.adapters.OrderDetailsAdapter;
import com.anhnlp.connectors.OrderDetailsConnector;
import com.anhnlp.connectors.SQLiteConnector;
import com.anhnlp.models.OrderDetailsViewer;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    RecyclerView rvOrderDetails;
    OrderDetailsAdapter adapter;
    TextView txtOrderIdHeader, txtTotalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
    }

    private void addViews() {
        rvOrderDetails = findViewById(R.id.rvOrderDetails);
        txtOrderIdHeader = findViewById(R.id.txtOrderIdHeader);
        txtTotalValue = findViewById(R.id.txtTotalValue);

        rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailsAdapter();
        rvOrderDetails.setAdapter(adapter);

        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            txtOrderIdHeader.setText("Order ID: " + orderId);
            SQLiteConnector connector = new SQLiteConnector(this);
            OrderDetailsConnector odc = new OrderDetailsConnector();
            ArrayList<OrderDetailsViewer> detailsList = odc.getOrderDetails(connector.openDatabase(), orderId);
            adapter.setDetailsList(detailsList);

            // Tính và hiển thị tổng TotalValue
            double totalValue = detailsList.stream().mapToDouble(OrderDetailsViewer::getTotalValue).sum();
            txtTotalValue.setText("Total Value: " + String.valueOf(totalValue) + " VND");
        }
    }
}