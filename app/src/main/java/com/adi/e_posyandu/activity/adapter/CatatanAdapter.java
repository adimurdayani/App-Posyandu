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
import com.adi.e_posyandu.activity.model.Catatan;

import java.util.ArrayList;
import java.util.Collection;

public class CatatanAdapter extends RecyclerView.Adapter<CatatanAdapter.HolderData> {
    private Context context;
    private ArrayList<Catatan> data;

    public CatatanAdapter(Context context, ArrayList<Catatan> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_catatan, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Catatan catatan = data.get(position);

        holder.keluhan.setText(catatan.getKeluhan());
        holder.tekanan.setText(catatan.getTekanan_darah());
        holder.berat.setText(catatan.getBerat_badan());
        holder.tanggal.setText(catatan.getTgl());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    Filter searchData = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Catatan> searchList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                searchList.addAll(data);
            } else {
                for (Catatan catatanlist : data) {
                    if (catatanlist.getBerat_badan().toLowerCase().contains(constraint.toString().toLowerCase())
                            || catatanlist.getKeluhan().toLowerCase().contains(constraint.toString().toLowerCase())
                            || catatanlist.getTgl().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        searchList.add(catatanlist);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = searchList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((Collection<? extends Catatan>) results.values);
            notifyDataSetChanged();
        }
    };
    public Filter getSearchData() {
        return searchData;
    }

    public class HolderData extends RecyclerView.ViewHolder {
        private TextView keluhan, tekanan, berat, tanggal;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            keluhan = itemView.findViewById(R.id.keluhan);
            tekanan = itemView.findViewById(R.id.tekanan);
            berat = itemView.findViewById(R.id.berat);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
