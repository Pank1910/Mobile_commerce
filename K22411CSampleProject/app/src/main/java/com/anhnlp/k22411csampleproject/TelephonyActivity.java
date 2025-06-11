package com.anhnlp.k22411csampleproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.models.TelephonyInfor;

public class TelephonyActivity extends AppCompatActivity {
    ListView lvTelephony;
    ArrayAdapter<TelephonyInfor> adapter;
    private static final int REQUEST_CALL_PHONE = 1;
    private TelephonyInfor pendingCall; // Lưu thông tin cuộc gọi khi chờ quyền

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_telephony);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        // Kiểm tra quyền READ_CONTACTS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    2); // REQUEST_READ_CONTACTS
        } else {
            getAllContacts();
        }
        addEvents();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingCall != null) {
                    makeAPhoneCall(pendingCall);
                    pendingCall = null;
                }
            } else {
                Toast.makeText(this, "Permission denied. Cannot make a phone call.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) { // REQUEST_READ_CONTACTS
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAllContacts();
            } else {
                Toast.makeText(this, "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addEvents() {
        lvTelephony.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                TelephonyInfor ti = adapter.getItem(i);
                initiatePhoneCall(ti); // Gọi hàm mới để xử lý quyền
            }
        });
    }

    private void initiatePhoneCall(TelephonyInfor ti) {
        pendingCall = ti; // Lưu thông tin để sử dụng sau khi quyền được cấp
        // Kiểm tra quyền CALL_PHONE
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền nếu chưa được cấp
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_PHONE);
        } else {
            // Quyền đã được cấp, thực hiện cuộc gọi
            makeAPhoneCall(ti);
        }
    }

    private void makeAPhoneCall(TelephonyInfor ti) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            Toast.makeText(this, "This device does not support phone calls.", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneNumber = ti.getPhone().replaceAll("[^0-9+]", "");
        if (phoneNumber.isEmpty() || phoneNumber.length() < 7) { // Kiểm tra độ dài tối thiểu
            Toast.makeText(this, "Invalid phone number: " + ti.getPhone(), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("TelephonyActivity", "Calling number: " + phoneNumber); // Log để kiểm tra
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Failed to make call: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void getAllContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        adapter.clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursor.getString(nameIndex); // Get Name
                int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String phone = cursor.getString(phoneIndex); // Get Phone Number

                TelephonyInfor ti = new TelephonyInfor();
                ti.setName(name);
                ti.setPhone(phone);
                adapter.add(ti);
            }
            cursor.close();
        }
    }

    private void addViews() {
        lvTelephony = findViewById(R.id.lvTelephonyInfor);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvTelephony.setAdapter(adapter);
    }
}