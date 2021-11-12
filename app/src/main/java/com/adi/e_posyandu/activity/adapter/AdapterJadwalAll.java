package com.adi.e_posyandu.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.e_posyandu.R;
import com.adi.e_posyandu.activity.model.Jadwal;

import java.util.ArrayList;
import java.util.Collection;

public class AdapterJadwalAll extends RecyclerView.Adapter<AdapterJadwalAll.HolderData> {
    private Context context;
    private ArrayList<Jadwal> datajadwal;

    public AdapterJadwalAll(Context context, ArrayList<Jadwal> datajadwal) {
        this.context = context;
        this.datajadwal = datajadwal;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_all_jadwal, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Jadwal listjadwal = datajadwal.get(position);

        holder.kelurahan.setText(listjadwal.getKelurahan());
        holder.tanggal.setText(listjadwal.getTgl_kegiatan());
        holder.waktu.setText(listjadwal.getWaktu_kegiatan());
        holder.keterangan.setText(listjadwal.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return datajadwal.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Jadwal> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(datajadwal);
            } else {
                for (Jadwal jadwallit : datajadwal) {
                    if (jadwallit.getKelurahan().toLowerCase().contains(constraint.toString().toLowerCase())
                            || jadwallit.getKeterangan().toLowerCase().contains(constraint.toString().toLowerCase())
                            || jadwallit.getTgl_kegiatan().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(jadwallit);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            datajadwal.clear();
            datajadwal.addAll((Collection<? extends Jadwal>) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView kelurahan, tanggal, waktu, keterangan;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            kelurahan = itemView.findViewById(R.id.kelurahan);
            tanggal = itemView.findViewById(R.id.tanggal);
            waktu = itemView.findViewById(R.id.waktu);
            keterangan = itemView.findViewById(R.id.keterangan);
        }
    }
}
