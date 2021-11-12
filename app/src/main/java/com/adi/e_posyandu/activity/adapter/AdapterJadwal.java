package com.adi.e_posyandu.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.model.Jadwal;

import java.util.ArrayList;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.HolderData> {
    private Context context;
    private ArrayList<Jadwal> datajadwal;

    public AdapterJadwal(Context context, ArrayList<Jadwal> datajadwal) {
        this.context = context;
        this.datajadwal = datajadwal;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_jadwal, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Jadwal listjadwal = datajadwal.get(position);

        holder.kelurahan.setText(listjadwal.getKelurahan());
        holder.tanggal.setText(listjadwal.getTgl_kegiatan());
        holder.keterangan.setText(listjadwal.getKeterangan());
        holder.jam.setText(listjadwal.getWaktu_kegiatan());
    }

    @Override
    public int getItemCount() {
        return datajadwal.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView kelurahan, tanggal, keterangan, jam;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            kelurahan = itemView.findViewById(R.id.kelurahan);
            tanggal = itemView.findViewById(R.id.tanggal);
            keterangan = itemView.findViewById(R.id.keterangan);
            jam = itemView.findViewById(R.id.jam);
        }
    }
}
