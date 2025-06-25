package com.anhnlp.k22411csampleproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class AsyncTaskActivity extends AppCompatActivity {
    EditText edtNumberOfButton;
    Button btnDrawButton;
    TextView txtPercent;
    ProgressBar progressBarPercent;
    LinearLayout linearLayoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_async_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addViews();
        addEvents();
    }

    private void addEvents() {
        btnDrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawButtonRealtime();
            }
        });
    }
    private void drawButtonRealtime() {
        int n=Integer.parseInt(edtNumberOfButton.getText().toString());
        MyDrawButtonTask task=new MyDrawButtonTask();
        task.execute(n); //truyền 1 đối số thì doInBackGround nận 1 mảng có 1 phần tử là n
        // nếu ta gọi task.execute(n1,n2,n3); thì doInBackground nhận 1 mảng có 3 phần tử

    }

    private void addViews() {
        edtNumberOfButton=findViewById(R.id.edtNumberOfCustomer);
        btnDrawButton=findViewById(R.id.btnDrawButton);
        txtPercent=findViewById(R.id.txtPercent);
        progressBarPercent=findViewById(R.id.progressBarPercent);
        linearLayoutButton=findViewById(R.id.linearLayoutButton);

    }

    class MyDrawButtonTask extends AsyncTask<Integer,Integer,Double>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtPercent.setText("0%");
            progressBarPercent.setProgress(0);
            linearLayoutButton.removeAllViews();
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            txtPercent.setText("100%");
            progressBarPercent.setProgress(100);
            AlertDialog.Builder builder=new AlertDialog.Builder(AsyncTaskActivity.this);
            builder.setMessage("Giá trị trung bình ="+aDouble);
            builder.create().show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // hàm này tự động kích hoạt khi publishProgress được gọi
            int percent=values[0];
            int value=values[1];
            txtPercent.setText(percent+"%");
            progressBarPercent.setProgress(percent);
            Button btn=new Button(AsyncTaskActivity.this);
            btn.setText(value+"");
            btn.setWidth(300);
            btn.setHeight(10);
            linearLayoutButton.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn.setTextColor(Color.RED);
                }
            });
            btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    btn.setVisibility(View.GONE);
                    return false;
                }
            });
        }

        @Override
        protected Double doInBackground(Integer... integers) {
            int n=integers[0];
            double avg=0;
            Random random=new Random();
            for(int i=1;i<=n;i++)
            {
                int percent=i*100/n;
                int value= random.nextInt(100);
                // Đẩy dữ liệu qua onProgressUpdate để vẽ giao diện thời gian thực
                publishProgress(percent,value); //thứ tự do ta quết định
                SystemClock.sleep(100); //tạm dừng 0.1 giây để các tiến trình khác có thể thực thi
                                            // đôi khi ta tấy nó mượt hơn khi nhường người khác
                avg=avg+value;
            }
            return avg/n;
        }
    }
}