package com.adi.e_posyandu.activity.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.adapter.AdapterJadwal;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.model.Jadwal;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private TextView txt_nama,btn_all;
    private SharedPreferences preferences;
    private CardView btn_user, btn_catatan, btn_jadwal, btn_kontak;
    private RecyclerView rc_data;
    private StringRequest getjadwal;
    private ArrayList<Jadwal> jadwals;
    private AdapterJadwal adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        init();
        setButton();
    }

    private void setButton() {
        btn_user.setOnClickListener(v -> {
            startActivity(new Intent(this, UserActivity.class));
        });
        btn_catatan.setOnClickListener(v -> {
            startActivity(new Intent(this, CatatanActivity.class));

        });
        btn_jadwal.setOnClickListener(v -> {
            startActivity(new Intent(this, JadwalActivity.class));

        });
        btn_all.setOnClickListener(v -> {
            startActivity(new Intent(this, JadwalActivity.class));

        });
        btn_kontak.setOnClickListener(v -> {
            startActivity(new Intent(this, KontakActivity.class));

        });
    }

    private void init() {
        txt_nama = findViewById(R.id.txt_nama);
        btn_user = findViewById(R.id.btn_user);
        btn_catatan = findViewById(R.id.btn_catatan);
        btn_jadwal = findViewById(R.id.btn_jadwal);
        btn_kontak = findViewById(R.id.btn_kontak);
        rc_data = findViewById(R.id.rc_data);
        btn_all = findViewById(R.id.btn_all);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);
        txt_nama.setText(preferences.getString("nama", ""));
    }

    private void setJadwal(){
        jadwals = new ArrayList<>();
        getjadwal = new StringRequest(Request.Method.GET, URLServer.GETJADWAL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0;  i<data.length();i++){
                        JSONObject getData = data.getJSONObject(i);
                        Jadwal jadwal  = new Jadwal();
                        jadwal.setKelurahan(getData.getString("kelurahan"));
                        jadwal.setTgl_kegiatan(getData.getString("tgl_kegiatan"));
                        jadwal.setKeterangan(getData.getString("keterangan"));
                        jadwal.setWaktu_kegiatan(getData.getString("waktu_kegiatan"));
                        jadwals.add(jadwal);
                    }
                    adapter = new AdapterJadwal(this, jadwals);
                    rc_data.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
        });
        RequestQueue koneksi = Volley.newRequestQueue(this);
        koneksi.add(getjadwal);
    }

    @Override
    protected void onResume() {
        setJadwal();
        super.onResume();
    }
}