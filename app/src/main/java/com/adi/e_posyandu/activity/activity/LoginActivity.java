package com.adi.e_posyandu.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction().replace(R.id.frm_layout_login, new LoginFragment()).commit();
        init();
    }

    public void init(){

    }
}