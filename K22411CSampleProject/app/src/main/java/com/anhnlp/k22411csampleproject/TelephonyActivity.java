package com.anhnlp.k22411csampleproject;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.adapters.TelephonyInforAdapter;
import com.anhnlp.models.TelephonyInfor;
import com.anhnlp.k22411csampleproject.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelephonyActivity extends AppCompatActivity {
    ListView lvTelephony;
    TelephonyInforAdapter adapter;
    List<TelephonyInfor> allContacts;
    private static final List<String> VIETTEL_PREFIXES = Arrays.asList("032", "033", "034", "035", "036", "037", "038", "039", "096", "097", "098", "086");
    private static final List<String> MOBIFONE_PREFIXES = Arrays.asList("070", "077", "078", "079", "089", "090", "093");
    private static final int REQUEST_WRITE_CONTACTS = 100;

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
        allContacts = new ArrayList<>();
        getAllContacts();
        addEvents();
        checkAndAddSampleContacts();
    }

    private void checkAndAddSampleContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, REQUEST_WRITE_CONTACTS);
        } else {
            addSampleContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_CONTACTS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addSampleContacts();
        } else {
            Toast.makeText(this, "Cần quyền để thêm liên hệ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSampleContacts() {
        List<TelephonyInfor> sampleContacts = new ArrayList<>();
        sampleContacts.add(new TelephonyInfor("Viettel Contact 1", "0981234567"));
        sampleContacts.add(new TelephonyInfor("Viettel Contact 2", "0977654321"));
        sampleContacts.add(new TelephonyInfor("MobiFone Contact 1", "0901234567"));
        sampleContacts.add(new TelephonyInfor("MobiFone Contact 2", "0939876543"));
        sampleContacts.add(new TelephonyInfor("Vinaphone Contact", "0912345678"));
        sampleContacts.add(new TelephonyInfor("Vietnamobile Contact", "0923456789"));

        for (TelephonyInfor contact : sampleContacts) {
            addContactToDevice(contact.getName(), contact.getPhone());
        }
        getAllContacts(); // Cập nhật lại danh bạ sau khi thêm
    }

    private void addContactToDevice(String name, String phone) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Đã thêm liên hệ: " + name, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi thêm liên hệ: " + name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_telephony, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        adapter.clear();
        if (id == R.id.menu_filter_viettel) {
            filterContactsByNetwork(VIETTEL_PREFIXES);
            return true;
        } else if (id == R.id.menu_filter_mobifone) {
            filterContactsByNetwork(MOBIFONE_PREFIXES);
            return true;
        } else if (id == R.id.menu_filter_other) {
            filterOtherNetworks();
            return true;
        } else if (id == R.id.menu_filter_all) {
            adapter.addAll(allContacts);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterContactsByNetwork(List<String> prefixes) {
        for (TelephonyInfor ti : allContacts) {
            String phone = ti.getPhone().replaceAll("[^0-9]", "");
            if (phone.length() >= 10) {
                String prefix = phone.substring(0, 3);
                if (prefixes.contains(prefix)) {
                    adapter.add(ti);
                }
            }
        }
    }

    private void filterOtherNetworks() {
        for (TelephonyInfor ti : allContacts) {
            String phone = ti.getPhone().replaceAll("[^0-9]", "");
            if (phone.length() >= 10) {
                String prefix = phone.substring(0, 3);
                if (!VIETTEL_PREFIXES.contains(prefix) && !MOBIFONE_PREFIXES.contains(prefix)) {
                    adapter.add(ti);
                }
            }
        }
    }

    private void addEvents() {
        lvTelephony.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TelephonyInfor ti = adapter.getItem(position);
                makeAPhoneCall(ti);
            }
        });
    }

    private void makeAPhoneCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);
    }

    private void getAllContacts() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        adapter.clear();
        allContacts.clear();
        while (cursor.moveToNext()) {
            int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameIndex);
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phone = cursor.getString(phoneIndex);
            TelephonyInfor ti = new TelephonyInfor();
            ti.setName(name);
            ti.setPhone(phone);
            allContacts.add(ti);
            adapter.add(ti);
        }
        cursor.close();
    }

    private void addViews() {
        lvTelephony = findViewById(R.id.lvTelephonyInfor);
        adapter = new TelephonyInforAdapter(this, R.layout.item_telephony_infor);
        lvTelephony.setAdapter(adapter);
    }

    public void directCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(uri);
        startActivity(intent);
    }

    public void dialupCall(TelephonyInfor ti) {
        Uri uri = Uri.parse("tel:" + ti.getPhone());
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(uri);
        startActivity(intent);
    }
}