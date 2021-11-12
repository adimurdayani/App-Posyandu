package com.adi.e_posyandu.activity.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.adi.e_posyandu.R;
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

public class RegisterFragment extends Fragment {

    private View view;
    private LinearLayout btn_register;
    private TextView btn_login;
    private ImageView btn_back;
    private TextInputLayout l_nama;
    private EditText edt_nama;
    private TextInputLayout l_email;
    private EditText edt_email;
    private TextInputLayout l_username;
    private EditText edt_username;
    private TextInputLayout l_password;
    private EditText edt_password;
    private TextInputLayout l_konf_pass;
    private EditText edt_konf_pass;
    public static final Pattern PASSWORD_FORMAT = Pattern.compile("^" +
            "(?=.*[1-9])" + //harus menggunakan satu angka
            "(?=.*[a-z])" + //harus menggunakan abjad
            "(?=.*[A-Z])" + //harus menggunakan huruf kapital
            "(?=.*[@#$%^&+=])" + //harus menggunakan sepesial karakter
            "(?=\\S+$)" + // tidak menggunakan spasi
            ".{6,}" + //harus lebih dari 6 karakter
            "$"
    );
    private StringRequest kirimDataUser;
    private ArrayList<User> dataUserRegis;
    private String nama, email, username, password, konf_password, token;
    ProgressBar progressBar;
    private TextView token_id, txt_register;

    public RegisterFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment, container, false);
        init();
        cekvalidasi();
        return view;
    }

    private void setGetText() {
        nama = edt_nama.getText().toString().trim();
        email = edt_email.getText().toString().trim();
        username = edt_username.getText().toString().trim();
        password = edt_password.getText().toString().trim();
        konf_password = edt_konf_pass.getText().toString().trim();
        token = token_id.getText().toString().trim();
    }

    public void init() {
        btn_register = view.findViewById(R.id.btn_register);
        btn_login = view.findViewById(R.id.btn_login);
        btn_back = view.findViewById(R.id.btn_back);
        l_nama = view.findViewById(R.id.l_nama);
        edt_nama = view.findViewById(R.id.edt_nama);
        l_email = view.findViewById(R.id.l_email);
        edt_email = view.findViewById(R.id.edt_email);
        l_username = view.findViewById(R.id.l_username);
        edt_username = view.findViewById(R.id.edt_username);
        l_password = view.findViewById(R.id.l_password);
        edt_password = view.findViewById(R.id.edt_password);
        l_konf_pass = view.findViewById(R.id.l_konf_pass);
        edt_konf_pass = view.findViewById(R.id.edt_konf_pass);
        token_id = view.findViewById(R.id.token_id);
        txt_register = view.findViewById(R.id.txt_register);
        progressBar = view.findViewById(R.id.progress);

        token_id.setText(FirebaseInstanceId.getInstance().getToken());
        setButton();
    }

    private void setButton() {

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.frm_layout_login, new LoginFragment())
                        .commit();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validasi()) {
                    registerUser();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                fragmentManager.beginTransaction()
                        .replace(R.id.frm_layout_login, new LoginFragment())
                        .commit();
            }
        });
    }

    private void registerUser() {
        dataUserRegis = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        txt_register.setVisibility(View.GONE);

        kirimDataUser = new StringRequest(Request.Method.POST, URLServer.REGISTER, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONObject data = object.getJSONObject("data");
                    User postUser = new User();
                    postUser.setNama(data.getString("nama"));
                    postUser.setUsername(data.getString("username"));
                    postUser.setEmail(data.getString("email"));
                    postUser.setPassword(data.getString("password"));
                    postUser.setToken_id(data.getString("token_id"));

                    FragmentManager manager = getFragmentManager();
                    assert manager != null;
                    manager.beginTransaction().replace(R.id.frm_layout_login, new LoginFragment())
                            .commit();
                    Toast.makeText(getContext(), "Register success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
            txt_register.setVisibility(View.VISIBLE);
        }, error -> {
            progressBar.setVisibility(View.GONE);
            txt_register.setVisibility(View.VISIBLE);
            error.printStackTrace();
            Toast.makeText(getContext(), "Periksa koneksi anda!", Toast.LENGTH_SHORT).show();
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nama", nama);
                map.put("email", email);
                map.put("username", username);
                map.put("password", password);
                map.put("token_id", token);
                return map;
            }
        };
        RequestQueue koneksi = Volley.newRequestQueue(requireContext());
        koneksi.add(kirimDataUser);
    }

    private void cekvalidasi() {
        setGetText();
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
        setGetText();
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
}
