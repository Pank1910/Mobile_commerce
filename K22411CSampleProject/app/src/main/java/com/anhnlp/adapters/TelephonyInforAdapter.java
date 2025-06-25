package com.anhnlp.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anhnlp.k22411csampleproject.SendSMSActivity;
import com.anhnlp.k22411csampleproject.TelephonyActivity;
import com.anhnlp.models.TelephonyInfor;
import com.anhnlp.k22411csampleproject.R;

import java.util.List;

public class TelephonyInforAdapter extends ArrayAdapter<TelephonyInfor> {
    private Activity context;
    private int resource;
    private List<TelephonyInfor> contactList;

    public TelephonyInforAdapter(@NonNull Activity context, int resource, @NonNull List<TelephonyInfor> contactList) {
        super(context, resource, contactList);
        this.context = context;
        this.resource = resource;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView txtTelephonyName = convertView.findViewById(R.id.txtTelephonyName);
        TextView txtTelephonyNumber = convertView.findViewById(R.id.txtTelephonyNumber);
        ImageView imgDirectCall = convertView.findViewById(R.id.imgDirectCall);
        ImageView imgDialUp = convertView.findViewById(R.id.imgDialUp);
        ImageView imgSendSms = convertView.findViewById(R.id.imgSendSms);

        TelephonyInfor ti = getItem(position);
        if (ti != null) {
            txtTelephonyName.setText(ti.getName());
            txtTelephonyNumber.setText(ti.getPhone());
        }

        imgDirectCall.setOnClickListener(v -> ((TelephonyActivity) context).directCall(ti));
        imgDialUp.setOnClickListener(v -> ((TelephonyActivity) context).dialupCall(ti));
        imgSendSms.setOnClickListener(v -> {
            Intent intent = new Intent(context, SendSMSActivity.class);
            intent.putExtra("TI", ti);
            context.startActivity(intent);
        });

        return convertView;
    }
}