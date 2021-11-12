package com.adi.e_posyandu.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.adapter.AdapterJadwalAll;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.model.Jadwal;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JadwalActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private SearchView search_data;
    private SwipeRefreshLayout sw_data;
    private RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getjadwal;
    private ArrayList<Jadwal> jadwals;
    private AdapterJadwalAll adapter;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal);
        init();
        setButton();
    }

    private void setButton() {
        sw_data.setOnRefreshListener(this::setJadwal);
        btn_kembali.setOnClickListener(v -> {
            onBackPressed();
        });
        search_data.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setJadwal();
                return false;
            }
        });
    }

    private void init() {
        btn_kembali = findViewById(R.id.btn_kembali);
        search_data = findViewById(R.id.search_data);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        total = findViewById(R.id.total);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setHasFixedSize(true);
        rc_data.setLayoutManager(layoutManager);
    }

    @SuppressLint( "SetTextI18n" )
    private void setJadwal() {
        jadwals = new ArrayList<>();
        sw_data.setRefreshing(true);
        getjadwal = new StringRequest(Request.Method.GET, URLServer.GETJADWAL, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        Jadwal jadwal = new Jadwal();
                        jadwal.setKelurahan(getData.getString("kelurahan"));
                        jadwal.setTgl_kegiatan(getData.getString("tgl_kegiatan"));
                        jadwal.setKeterangan(getData.getString("keterangan"));
                        jadwal.setWaktu_kegiatan(getData.getString("waktu_kegiatan"));
                        jadwals.add(jadwal);
                    }
                    adapter = new AdapterJadwalAll(this, jadwals);
                    total.setText("" + jadwals.size());
                    rc_data.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
                    rc_data.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Message: " + object.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            sw_data.setRefreshing(false);
        }, error -> {
            sw_data.setRefreshing(false);
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