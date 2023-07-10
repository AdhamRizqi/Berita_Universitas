package com.azhar.reportapps.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azhar.reportapps.R;
import com.azhar.reportapps.model.ModelDatabase;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    List<ModelDatabase> modelDatabase;
    Context mContext;
    HistoryAdapterCallback mAdapterCallback;

    public MainAdapter(Context context, List<ModelDatabase> modelDatabaseList,
                       HistoryAdapterCallback adapterCallback) {
        this.mContext = context;
        this.modelDatabase = modelDatabaseList;
        this.mAdapterCallback = adapterCallback;
    }

    public void setDataAdapter(List<ModelDatabase> items) {
        modelDatabase.clear();
        modelDatabase.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history, parent, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ModelDatabase data = modelDatabase.get(position);

        holder.tvKategori.setText(data.getKategori());
        holder.tvNama.setText(data.getNama());
        holder.tvDate.setText(data.getTanggal());
        holder.tvLokasi.setText(data.getLokasi());




        switch (data.getKategori()) {
            case "Laporan":
                holder.layoutHeader.setBackgroundResource(R.color.red);
                break;
            case "Laporan Medis":
                holder.layoutHeader.setBackgroundResource(R.color.blue);
                break;
            case "Acara Perkuliahan":
                holder.layoutHeader.setBackgroundResource(R.color.green);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return modelDatabase.size();
    }

    public interface HistoryAdapterCallback {
        void onDelete(ModelDatabase modelDatabase);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvKategori, tvNama, tvDate, tvLokasi;
        public CardView cvHistory;
        public LinearLayout layoutHeader;
        public ImageView tvimage;
        public ImageView imageProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            tvimage = itemView.findViewById(R.id.tvimage);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            cvHistory = itemView.findViewById(R.id.cvHistory);
            layoutHeader = itemView.findViewById(R.id.layoutHeader);

            cvHistory.setOnClickListener(view -> {
                ModelDatabase modelLaundry = modelDatabase.get(getAdapterPosition());
                mAdapterCallback.onDelete(modelLaundry);
            });
        }
    }

}
