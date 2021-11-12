package com.adi.e_posyandu.activity.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.fragment.LoginFragment;
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

public class UbahProfile extends AppCompatActivity {
    private ImageView btn_kembali;
    private TextInputLayout l_email, l_username, l_nama, l_phone;
    private EditText edt_email, edt_username, edt_nama, edt_phone;
    private LinearLayout btn_simpan;
    private ProgressBar progress;
    private TextView txt_simpan;
    private String email, username, nama, phone;
    private StringRequest updateProfile;
    private SharedPreferences session_data;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profile);
        session_data = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
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
                setUpdateProfile();
            }
        });
    }

    private void setUpdateProfile() {
        progress.setVisibility(View.VISIBLE);
        txt_simpan.setVisibility(View.GONE);

        updateProfile = new StringRequest(Request.Method.POST, URLServer.POSTPROFILE, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    User postUser = new User();
                    postUser.setNama(data.getString("nama"));
                    postUser.setUsername(data.getString("username"));
                    postUser.setEmail(data.getString("email"));
                    postUser.setNo_hp(data.getString("no_hp"));

                    editor = session_data.edit();
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.apply();
                    super.onBackPressed();
                    Toast.makeText(this, "Update Profile Success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            progress.setVisibility(View.GONE);
            txt_simpan.setVisibility(View.VISIBLE);
        }, error -> {
            progress.setVisibility(View.GONE);
            txt_simpan.setVisibility(View.VISIBLE);
            error.printStackTrace();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String id = String.valueOf(session_data.getInt("id", 0));
                HashMap<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("no_hp", phone);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(updateProfile);
    }

    private void setTExt() {
        email = edt_email.getText().toString().trim();
        username = edt_username.getText().toString().trim();
        nama = edt_nama.getText().toString().trim();
        phone = edt_phone.getText().toString().trim();
    }

    private void cekvalidasi() {
        setTExt();
        edt_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nama.isEmpty()) {
                    l_nama.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.isEmpty()) {
                    l_email.setErrorEnabled(false);
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    l_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.isEmpty()) {
                    l_username.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (phone.isEmpty()) {
                    l_phone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean validasi() {
        setTExt();
        if (nama.isEmpty()) {
            l_nama.setErrorEnabled(true);
            l_nama.setError("Kolom nama tidak boleh kosong!");
            return false;
        }
        if (email.isEmpty()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Kolom email tidak boleh kosong!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            l_email.setErrorEnabled(true);
            l_email.setError("Format email salah!. Contoh: gunakan @example.com");
            return false;
        }
        if (username.isEmpty()) {
            l_username.setErrorEnabled(true);
            l_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }
        if (phone.isEmpty()) {
            l_phone.setErrorEnabled(true);
            l_phone.setError("Kolom phone tidak boleh kosong!");
            return false;
        }
        return true;
    }

    private void init() {
        btn_kembali = findViewById(R.id.btn_kembali);
        l_email = findViewById(R.id.l_email);
        l_username = findViewById(R.id.l_username);
        l_nama = findViewById(R.id.l_nama);
        l_phone = findViewById(R.id.l_phone);
        edt_email = findViewById(R.id.edt_email);
        edt_phone = findViewById(R.id.edt_phone);
        edt_username = findViewById(R.id.edt_username);
        edt_nama = findViewById(R.id.edt_nama);
        btn_simpan = findViewById(R.id.btn_simpan);
        txt_simpan = findViewById(R.id.txt_simpan);
        progress = findViewById(R.id.progress);

        edt_nama.setText(session_data.getString("nama", ""));
        edt_email.setText(session_data.getString("email", ""));
        edt_username.setText(session_data.getString("username", ""));
        edt_phone.setText(session_data.getString("no_hp", ""));
    }
}