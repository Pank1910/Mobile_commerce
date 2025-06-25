package com.anhnlp.k22411csampleproject;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.models.Message;
import com.anhnlp.models.TelephonyInfor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendSMSActivity extends AppCompatActivity {

    TextView txtTelephonyName;
    TextView txtTelephonyNumber;
    EditText edtBody;
    ImageView imgSendSms1;
    ImageView imgSendSms2;
    TelephonyInfor ti;
    private static final int REQUEST_SEND_SMS = 3;
    private static final int REQUEST_RECEIVE_SMS = 4;

    private void requestReceiveSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                REQUEST_RECEIVE_SMS);
    }

    private boolean checkReceiveSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_smsactivity);

        // Kiểm tra và yêu cầu quyền RECEIVE_SMS
        if (!checkReceiveSmsPermission()) {
            requestReceiveSmsPermission();
        }

        addViews();
        addEvents();
    }

    private void addViews() {
        txtTelephonyName = findViewById(R.id.txtTelephonyName);
        txtTelephonyNumber = findViewById(R.id.txtTelephonyNumber);
        edtBody = findViewById(R.id.edtBody);
        imgSendSms1 = findViewById(R.id.imgSendSms1);
        imgSendSms2 = findViewById(R.id.imgSendSms2);

        Intent intent = getIntent();
        ti = (TelephonyInfor) intent.getSerializableExtra("TI");
        if (ti != null) {
            txtTelephonyName.setText(ti.getName());
            txtTelephonyNumber.setText(ti.getPhone());
        }
    }

    private void addEvents() {
        imgSendSms1.setOnClickListener(view -> {
            // Kiểm tra quyền trước khi gửi SMS
            if (checkSmsPermission()) {
                sendSms(String.valueOf(ti), edtBody.getText().toString());
            } else {
                requestSmsPermission();
            }
        });

        imgSendSms2.setOnClickListener(view -> {
            // Kiểm tra quyền trước khi gửi SMS với PendingIntent
            if (checkSmsPermission()) {
                sendSmsPendingIntent(ti, edtBody.getText().toString());
            } else {
                requestSmsPermission();
            }
        });
    }

    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                REQUEST_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền gửi SMS đã được cấp.", Toast.LENGTH_SHORT).show();
                sendSms(String.valueOf(ti), edtBody.getText().toString());
            } else {
                Toast.makeText(this, "Quyền gửi SMS bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_RECEIVE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền nhận SMS đã được cấp.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Quyền nhận SMS bị từ chối.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Gửi tin nhắn thành công đến " + phoneNumber, Toast.LENGTH_SHORT).show();
            Log.d("SendSMSActivity", "SMS sent to " + phoneNumber + ": " + message);

            // Đẩy dữ liệu tin nhắn lên Firebase
            Message msg = new Message(phoneNumber, message, System.currentTimeMillis());
            pushToFirebase(msg);
        } catch (Exception e) {
            Log.e("SendSMSActivity", "Failed to send SMS: " + e.getMessage());
            Toast.makeText(this, "Lỗi khi gửi tin nhắn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void pushToFirebase(Message message) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = database.getReference("Messages");
        String messageId = messagesRef.push().getKey();
        if (messageId != null) {
            messagesRef.child(messageId).setValue(message)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("SendSMSActivity", "Message pushed to Firebase: " + messageId);
                        Toast.makeText(this, "Đã lưu tin nhắn lên Firebase", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("SendSMSActivity", "Failed to push message to Firebase: " + e.getMessage());
                        Toast.makeText(this, "Lỗi khi lưu tin nhắn lên Firebase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("SendSMSActivity", "Failed to generate message ID");
            Toast.makeText(this, "Lỗi khi tạo ID tin nhắn", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSmsPendingIntent(TelephonyInfor ti, String content) {
        if (ti == null || ti.getPhone() == null || content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại và nội dung tin nhắn.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            Intent msgSent = new Intent("ACTION_MSG_SENT");
            PendingIntent pendingMsgSent = PendingIntent.getBroadcast(this, 0, msgSent, PendingIntent.FLAG_IMMUTABLE);

            // Đăng ký BroadcastReceiver
            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int result = getResultCode();
                    String msg = result == Activity.RESULT_OK ? "Gửi SMS thành công" : "Gửi SMS thất bại";
                    Toast.makeText(SendSMSActivity.this, msg, Toast.LENGTH_LONG).show();
                    // Hủy đăng ký BroadcastReceiver sau khi nhận kết quả
                    unregisterReceiver(this);
                }
            }, new IntentFilter("ACTION_MSG_SENT"), Context.RECEIVER_NOT_EXPORTED);

            // Gửi SMS
            smsManager.sendTextMessage(ti.getPhone(), null, content, pendingMsgSent, null);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi gửi SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver nếu cần
    }
}