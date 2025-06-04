package com.anhnlp.k22411csampleproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.anhnlp.connectors.EmployeeConnector;
import com.anhnlp.connectors.SQLiteConnector;
import com.anhnlp.models.Employee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {

    EditText edtUserName;
    EditText edtPassword;
    CheckBox chkSaveLogin;
    Button btnLogin;
    ImageView imgExit;

    String DATABASE_NAME="SalesDatabase.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;

    private long lastBackPressedTime = 0;
    private static final long BACK_PRESS_THRESHOLD = 1200; // 1.2 giây

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressedTime <= BACK_PRESS_THRESHOLD) {
            // Hiển thị dialog xác nhận thoát
            do_exit(null);
        } else {
            lastBackPressedTime = currentTime;
            Toast.makeText(this, "Nhấn lần nữa để thoát", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
//        Tất cả câu lệnh liên quan giao diện phải viết sau contentview
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        processCopy();

    }


    private void addViews() {
        edtUserName=findViewById(R.id.edtUserName);
        edtPassword=findViewById(R.id.edtPassword);
        chkSaveLogin=findViewById(R.id.chkSaveLoginInfo);
    }

    public void do_login(View view) {

        String usr=edtUserName.getText().toString();
        String pwd=edtPassword.getText().toString();
        EmployeeConnector ec=new EmployeeConnector();

//        SQLiteConnector sqLiteConnector=new SQLiteConnector(this);
//        sqLiteConnector.openDatabase();
//        Employee emp=ec.login(sqLiteConnector.getDatabase(),usr,pwd);

        Employee emp = ec.login(new SQLiteConnector(this).openDatabase(),usr,pwd);

        if(emp!=null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,
                    "Login failed - please check your account again!",
                    Toast.LENGTH_LONG).show();
        }


//        Đối số thứ nhất là màn hình hiện tại.this, đối số thứ 2 là màn hình muôn mở.class
//        Intent intent=new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    public void do_exit(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        Resources res=getResources();

//        Thiết lập tiêu đề
        builder.setTitle(res.getText(R.string.confirm_exit_title));
//        Nội dung cửa sổ
        builder.setMessage(res.getText(R.string.confirm_exit_message));
//        Biểu tượng
        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        Thiết lập tương tác yes
        builder.setPositiveButton(res.getText(R.string.confirm_exit_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                System.exit(0);
                finish();
            }
        });
        builder.setNegativeButton(res.getText(R.string.confirm_exit_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public void saveLoginInformation()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        String usr=edtUserName.getText().toString();
        String pwd=edtPassword.getText().toString();
        boolean isSave=chkSaveLogin.isChecked();
        editor.putString("USERNAME",usr);
        editor.putString("PASSWORD",pwd);
        editor.putBoolean("SAVED",isSave);
        editor.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLoginInformation();
    }

    public void restoreLoginInformation()
    {
        SharedPreferences preferences=getSharedPreferences("LOGIN_INFORMATION", MODE_PRIVATE);
        String usr=preferences.getString("USERNAME","");
        String pwd=preferences.getString("PASSWORD","");
        boolean isSave=preferences.getBoolean("SAVED",true);
        if(isSave)
        {
            edtUserName.setText(usr);
            edtPassword.setText(pwd);
            chkSaveLogin.setChecked(isSave);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreLoginInformation();
    }

    private void processCopy() {
        //private app (copy dữ liệu từ phần mềm voòa phần cứng)
        File dbFile = getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists())
        {
            try
            {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset()
    {
        try {
            InputStream myInput;

            myInput = getAssets().open(DATABASE_NAME);


            // Path to the just created empty db
            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}