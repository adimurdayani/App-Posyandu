package com.adi.e_posyandu.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.api.URLServer;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private TextView nama, email,phone;
    private LinearLayout btn_ubahprofil, btn_ubahpassword, btn_bantuan, btn_tentang, btn_logout;
    private StringRequest logout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        init();
        setButton();
    }

    private void setButton() {
        btn_kembali.setOnClickListener(v -> {
            onBackPressed();
        });
        btn_ubahprofil.setOnClickListener(v -> {
            startActivity(new Intent(this, UbahProfile.class));
        });
        btn_ubahpassword.setOnClickListener(v -> {
            startActivity(new Intent(this, UbahPassword.class));
        });
        btn_bantuan.setOnClickListener(v -> {
            startActivity(new Intent(this, Bantuan.class));
        });
        btn_tentang.setOnClickListener(v -> {
            startActivity(new Intent(this, Tentang.class));
        });
        btn_logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Apakah anda yakin ingin keluar?");
            builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setLogout();
                }
            });
            builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        });
    }

    private void init() {
        btn_kembali = findViewById(R.id.btn_kembali);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        btn_ubahprofil = findViewById(R.id.btn_ubahprofil);
        btn_ubahpassword = findViewById(R.id.btn_ubahpassword);
        btn_bantuan = findViewById(R.id.btn_bantuan);
        btn_tentang = findViewById(R.id.btn_tentang);
        btn_logout = findViewById(R.id.btn_logout);
        phone = findViewById(R.id.phone);
        progress = findViewById(R.id.progress);

    }

    @Override
    protected void onResume() {
        nama.setText(preferences.getString("nama", ""));
        email.setText(preferences.getString("email", ""));
        phone.setText(preferences.getString("no_hp", ""));
        super.onResume();
    }

    private void setLogout() {
        progress.setVisibility(View.VISIBLE);

        logout = new StringRequest(Request.Method.GET, URLServer.LOGOUT, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    editor = preferences.edit();
                    editor.remove(String.valueOf(preferences.getInt("id", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();

                    Toast.makeText(this, "Logout Sukses!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            progress.setVisibility(View.GONE);
        }, error -> {
            progress.setVisibility(View.GONE);
            error.printStackTrace();
        });
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(logout);
    }
}