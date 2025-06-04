package com.anhnlp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anhnlp.k22411csampleproject.R;
import com.anhnlp.models.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    Activity context;
    int resource;
    public ProductAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context=context;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
//        nhân bản giao diện theo từng cái vị trí position mà dữ liệu được duyệt qua
        View item=inflater.inflate(this.resource,null);
//        lúc này toàn bộ view nằm trong cái layout resource(item_advanced_product)
//        sẽ được mô hình hóa hướng đối tượng và được quản lý bởi bến item
//        tức là item là Tổng tài view
//        như vậy muốn truy xuất tới các view con trong nó thì phải thông qua biến item
        ImageView imgProduct=item.findViewById(R.id.imgProduct);
        TextView txtProductId=item.findViewById(R.id.txtProductId);
        TextView txtProductName=item.findViewById(R.id.txtProductName);
        TextView txtProductQuantity=item.findViewById(R.id.txtProductQuantity);
        TextView txtProductPrice=item.findViewById(R.id.txtProductPrice);
        ImageView imgCart=item.findViewById(R.id.imgCart);

//        Lấy mô hình ối tượng đang đực nhân bản ở vị trí position (đối số 1)
        Product p=getItem(position);
//        Rải dữ liệu con của Product lên giao diện trong item
        imgProduct.setImageResource(p.getImage_id());
        txtProductId.setText(p.getId()+"");
        txtProductName.setText(p.getName());
        txtProductQuantity.setText(p.getQuantity()+"");
        txtProductPrice.setText(p.getPrice()+"(VND)");
//        xử lý bấm vào nút mua tính sau


        return item;
    }
}
