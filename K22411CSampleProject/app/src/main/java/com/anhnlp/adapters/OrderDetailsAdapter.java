package com.anhnlp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anhnlp.k22411csampleproject.R;
import com.anhnlp.models.OrderDetailsViewer;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private ArrayList<OrderDetailsViewer> detailsList;

    public OrderDetailsAdapter() {
        this.detailsList = new ArrayList<>();
    }

    public void setDetailsList(ArrayList<OrderDetailsViewer> detailsList) {
        this.detailsList = detailsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailsViewer od = detailsList.get(position);
        holder.txtProductId.setText("Product ID: " + od.getProductId());
        holder.txtQuantity.setText("Quantity: " + od.getQuantity());
        holder.txtPrice.setText("Price: " + od.getPrice() + " VND");
        holder.txtDiscount.setText("Discount: " + od.getDiscount() + "%");
        holder.txtVAT.setText("VAT: " + od.getVAT() + "%");
        holder.txtTotalValue.setText("Total Value: " + od.getTotalValue() + " VND");
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductId, txtQuantity, txtPrice, txtDiscount, txtVAT, txtTotalValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductId = itemView.findViewById(R.id.txtProductId);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDiscount = itemView.findViewById(R.id.txtDiscount);
            txtVAT = itemView.findViewById(R.id.txtVAT);
            txtTotalValue = itemView.findViewById(R.id.txtTotalValue);
        }
    }
}