package com.adi.e_posyandu.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.adi.e_posyandu.R;

public class Bantuan extends AppCompatActivity {
    private ImageView btn_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan);
        btn_kembali = findViewById(R.id.btn_kembali);
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }
}