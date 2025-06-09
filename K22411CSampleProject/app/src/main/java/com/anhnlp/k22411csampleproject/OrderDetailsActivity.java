package com.anhnlp.k22411csampleproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.connectors.OrderDetailsConnector;
import com.anhnlp.connectors.SQLiteConnector;
import com.anhnlp.models.OrderDetailsViewer;

public class OrderDetailsActivity extends AppCompatActivity {

    TextView txtOrderIdLabel, txtOrderId;
    TextView txtProductIdLabel, txtProductId;
    TextView txtQuantityLabel, txtQuantity;
    TextView txtPriceLabel, txtPrice;
    TextView txtDiscountLabel, txtDiscount;
    TextView txtVATLabel, txtVAT;
    TextView txtTotalValueLabel, txtTotalValue;

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
        txtOrderIdLabel = findViewById(R.id.txtOrderIdLabel);
        txtOrderId = findViewById(R.id.txtOrderId);
        txtProductIdLabel = findViewById(R.id.txtProductIdLabel);
        txtProductId = findViewById(R.id.txtProductId);
        txtQuantityLabel = findViewById(R.id.txtQuantityLabel);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtPriceLabel = findViewById(R.id.txtPriceLabel);
        txtPrice = findViewById(R.id.txtPrice);
        txtDiscountLabel = findViewById(R.id.txtDiscountLabel);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtVATLabel = findViewById(R.id.txtVATLabel);
        txtVAT = findViewById(R.id.txtVAT);
        txtTotalValueLabel = findViewById(R.id.txtTotalValueLabel);
        txtTotalValue = findViewById(R.id.txtTotalValue);

        int orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId != -1) {
            SQLiteConnector connector = new SQLiteConnector(this);
            OrderDetailsConnector odc = new OrderDetailsConnector();
            OrderDetailsViewer details = odc.getOrderDetails(connector.openDatabase(), orderId);
            if (details != null) {
                txtOrderId.setText(String.valueOf(details.getOrderId()));
                txtProductId.setText(String.valueOf(details.getProductId()));
                txtQuantity.setText(String.valueOf(details.getQuantity()));
                txtPrice.setText(String.valueOf(details.getPrice()) + " VND");
                txtDiscount.setText(String.valueOf(details.getDiscount()) + "%");
                txtVAT.setText(String.valueOf(details.getVAT()) + "%");
                txtTotalValue.setText(String.valueOf(details.getTotalValue()) + " VND");
            }
        }
    }
}