package com.adi.e_posyandu.activity.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UbahPassword extends AppCompatActivity {

    private ImageView btn_kembali;
    private TextInputLayout l_password, l_konf_pass;
    private EditText edt_password, edt_konf_pass;
    private LinearLayout btn_simpan;
    private ProgressBar progress;
    private TextView text_simpan;
    private String password, konf_password;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest updatePassword;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        init();
        setButton();
        cekvalidasi();
    }

    private void setButton() {
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
        btn_simpan.setOnClickListener(v -> {
            if (validasi()) {
                setUpdatePassword();
            }
        });
    }

    private void setUpdatePassword(){
        progress.setVisibility(View.VISIBLE);
        text_simpan.setVisibility(View.GONE);

        updatePassword = new StringRequest(Request.Method.POST, URLServer.POSTPASSWORD, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    editor = preferences.edit();
                    editor.remove(String.valueOf(preferences.getInt("id", 0)));
                    editor.remove(preferences.getString("nama", ""));
                    editor.clear();
                    editor.apply();

                    Toast.makeText(this, "Update Password Success!", Toast.LENGTH_LONG).show();
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
            text_simpan.setVisibility(View.VISIBLE);
        }, error -> {
            progress.setVisibility(View.GONE);
            text_simpan.setVisibility(View.VISIBLE);
            error.printStackTrace();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id  = String.valueOf(preferences.getInt("id", 0));
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("password", password);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(updatePassword);
    }

    private void setText() {
        password = edt_password.getText().toString().trim();
        konf_password = edt_konf_pass.getText().toString().trim();
    }

    private void cekvalidasi(){
        setText();
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.isEmpty()) {
                    l_password.setErrorEnabled(false);
                } else if (password.length() > 7) {
                    l_password.setErrorEnabled(false);
                } else if (PASSWORD_FORMAT.matcher(password).matches()) {
                    l_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_konf_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (konf_password.isEmpty()) {
                    l_konf_pass.setErrorEnabled(false);
                } else if (konf_password.length() > 7) {
                    l_konf_pass.setErrorEnabled(false);
                } else if (PASSWORD_FORMAT.matcher(konf_password).matches()) {
                    l_konf_pass.setErrorEnabled(false);
                } else if (konf_password.matches(password)) {
                    l_konf_pass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validasi() {
        setText();
        if (password.isEmpty()) {
            l_password.setErrorEnabled(true);
            l_password.setError("Kolom password tidak boleh kosong!");
            return false;
        } else if (password.length() < 6) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(password).matches()) {
            l_password.setErrorEnabled(true);
            l_password.setError("Password sangat lemah!. Contoh: @Jad123");
            return false;
        }
        if (konf_password.isEmpty()) {
            l_konf_pass.setErrorEnabled(true);
            l_konf_pass.setError("Kolom konfirmasi password tidak boleh kosong!");
            return false;
        } else if (konf_password.length() < 6) {
            l_konf_pass.setErrorEnabled(true);
            l_konf_pass.setError("Konfirmasi password tidak boleh kurang dari 6 karakter!");
            return false;
        } else if (!PASSWORD_FORMAT.matcher(konf_password).matches()) {
            l_konf_pass.setErrorEnabled(true);
            l_konf_pass.setError("Konfirmasi password sangat lemah!");
            return false;
        } else if (!konf_password.matches(password)) {
            l_konf_pass.setErrorEnabled(true);
            l_konf_pass.setError("Konfirmasi password tidak sama dengan password!");
            return false;
        }
        return true;
    }

    private void init() {
        btn_kembali = findViewById(R.id.btn_kembali);
        l_password = findViewById(R.id.l_password);
        l_konf_pass = findViewById(R.id.l_konf_pass);
        edt_password = findViewById(R.id.edt_password);
        edt_konf_pass = findViewById(R.id.edt_konf_pass);
        btn_simpan = findViewById(R.id.btn_simpan);
        progress = findViewById(R.id.progress);
        text_simpan = findViewById(R.id.text_simpan);
    }
}