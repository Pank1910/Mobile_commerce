package com.anhnlp.k22411c_firstdegree;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

//    KhAI BÁO CÁC BIẾN ĐỂ QUẢN LÝ CÁC Ô NH CỦA CÁC VIEW
    EditText edtCoefficientA;
    EditText edtCoefficientB;
    TextView txtResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Spinner spinnerLanguage = findViewById(R.id.spinnerLanguage);
        String[] languages = {"English", "Tiếng Việt", "French", "Spanish"};
        String[] langCodes = {"en", "vi", "fr", "es"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

// Optional: set selection to current language
        Locale current = getResources().getConfiguration().getLocales().get(0);
        int currentIndex = 0;
        for (int i = 0; i < langCodes.length; i++) {
            if (current.getLanguage().equals(langCodes[i])) {
                currentIndex = i;
                break;
            }
        }
        spinnerLanguage.setSelection(currentIndex);

// Set event
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstSelection) {
                    firstSelection = false; // Ignore first automatic selection
                    return;
                }
                setLocale(langCodes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addViews() {
        edtCoefficientA=findViewById(R.id.edtCoefficientA);
        edtCoefficientB=findViewById(R.id.edtCoefficientB);
        txtResult=findViewById(R.id.txtResult);

    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        resources.updateConfiguration(config, resources.getDisplayMetrics());
        recreate(); // Reload activity to apply language
    }

    public void do_solution(View view) {
//        Lấy hệ số a trên giao diện
        String hsa=edtCoefficientA.getText().toString();
        double a=Double.parseDouble(hsa);
//        Lấy hệ số b trên giao diện
        double b=Double.parseDouble(edtCoefficientB.getText().toString());

        if (a==0 && b==0)
        {
            txtResult.setText(getResources().getText(R.string.title_infinity));
        }
        else if(a==0 && b!=0)
        {
            txtResult.setText(getResources().getText(R.string.title_no_solution));
        }
        else
        {
            double x=-b/a;
            txtResult.setText("x="+x);
        }

    }

    public void do_next(View view) {
        edtCoefficientA.setText("");
        edtCoefficientB.setText("");
        txtResult.setText("");
        //di chuyển con trỏ nập liệu vào HSA để nhập cho lẹ
        edtCoefficientA.requestFocus();
    }

    public void do_exit(View view) {
        finish();
    }



}