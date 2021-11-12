package com.adi.e_posyandu.activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.adapter.AdapterJadwalAll;
import com.adi.e_posyandu.activity.adapter.CatatanAdapter;
import com.adi.e_posyandu.activity.api.URLServer;
import com.adi.e_posyandu.activity.model.Catatan;
import com.adi.e_posyandu.activity.model.Jadwal;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CatatanActivity extends AppCompatActivity {
    private ImageView btn_kembali;
    private SearchView search_data;
    private SwipeRefreshLayout sw_data;
    private RecyclerView rc_data;
    private RecyclerView.LayoutManager layoutManager;
    private StringRequest getCatatan;
    private ArrayList<Catatan> catatans;
    private CatatanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan);
        init();
        setButton();
    }

    private void setButton() {
        btn_kembali.setOnClickListener(v -> {
            super.onBackPressed();
        });
        sw_data.setOnRefreshListener(this::setGetCatatan);

        search_data.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getSearchData().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setGetCatatan();
                return false;
            }
        });
    }

    private void init() {
        btn_kembali = findViewById(R.id.btn_kembali);
        search_data = findViewById(R.id.search_data);
        sw_data = findViewById(R.id.sw_data);
        rc_data = findViewById(R.id.rc_data);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rc_data.setLayoutManager(layoutManager);
        rc_data.setHasFixedSize(true);
    }

    private void setGetCatatan() {
        catatans = new ArrayList<>();
        sw_data.setRefreshing(true);
        getCatatan = new StringRequest(Request.Method.GET, URLServer.GETCATATAN, response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("status")) {
                    JSONArray data = new JSONArray(object.getString("data"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject getData = data.getJSONObject(i);
                        Catatan catatanget = new Catatan();
                        catatanget.setBerat_badan(getData.getString("berat_badan"));
                        catatanget.setKeluhan(getData.getString("keluhan"));
                        catatanget.setTgl(getData.getString("tgl"));
                        catatanget.setTekanan_darah(getData.getString("tekanan_darah"));
                        catatans.add(catatanget);
                    }
                    adapter = new CatatanAdapter(this, catatans);
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
        koneksi.add(getCatatan);
    }

    @Override
    protected void onResume() {
        setGetCatatan();
        super.onResume();
    }
}