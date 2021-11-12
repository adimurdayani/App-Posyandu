package com.adi.e_posyandu.activity.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.activity.HomeActivity;
import com.adi.e_posyandu.activity.activity.LoginActivity;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.model.User;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    private View view;
    private LinearLayout btn_login;
    private TextView btn_register, text_login;
    private TextInputLayout l_username;
    private EditText edt_username;
    private TextInputLayout l_password;
    private EditText edt_password;
    private String username, password;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest login;
    private SharedPreferences session_data;
    private SharedPreferences.Editor editor;
    ProgressBar progressBar;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        init();
        setButton();
        cekvalidasi();
        return view;
    }


    private void setGetText() {
        username = edt_username.getText().toString().trim();
        password = edt_password.getText().toString().trim();
    }

    private void setButton() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.frm_layout_login, new RegisterFragment())
                        .commit();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    setLogin();
                }
            }
        });
    }

    private void setLogin() {
        progressBar.setVisibility(View.VISIBLE);
        text_login.setVisibility(View.GONE);

        login = new StringRequest(Request.Method.POST, URLServer.LOGIN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");

                    session_data = requireContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    editor = session_data.edit();
                    editor.putInt("id", data.getInt("id"));
                    editor.putString("nama", data.getString("nama"));
                    editor.putString("username", data.getString("username"));
                    editor.putString("email", data.getString("email"));
                    editor.putString("user_id", data.getString("user_id"));
                    editor.putString("token_id", data.getString("token_id"));
                    editor.putString("no_hp", data.getString("no_hp"));
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    startActivity(new Intent(getContext(), HomeActivity.class));
                    ((LoginActivity) requireContext()).finish();
                    Toast.makeText(getContext(), "Login success!", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
            text_login.setVisibility(View.VISIBLE);
        }, error -> {
            progressBar.setVisibility(View.GONE);
            text_login.setVisibility(View.VISIBLE);
            error.printStackTrace();
            Toast.makeText(requireActivity(), "Terjadi masalah data!", Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(login);
    }

    private void cekvalidasi() {
        setGetText();
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
    }

    private boolean validasi() {
        setGetText();
        if (username.isEmpty()) {
            l_username.setErrorEnabled(true);
            l_username.setError("Kolom username tidak boleh kosong!");
            return false;
        }
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
        return true;
    }

    private void init() {
        btn_register = view.findViewById(R.id.btn_register);
        btn_login = view.findViewById(R.id.btn_login);
        l_username = view.findViewById(R.id.l_username);
        edt_username = view.findViewById(R.id.edt_username);
        l_password = view.findViewById(R.id.l_password);
        edt_password = view.findViewById(R.id.edt_password);
        progressBar = view.findViewById(R.id.progress);
        text_login = view.findViewById(R.id.text_login);
    }
}
