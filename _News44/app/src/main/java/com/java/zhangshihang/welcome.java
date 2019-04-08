package com.java.zhangshihang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
    public  void onclick2(View view) {
        try {


            Intent it = new Intent(welcome.this, MainActivity.class);
            startActivity(it);
            finish();


        } catch (Exception ex) {
            // 显示异常信息
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
