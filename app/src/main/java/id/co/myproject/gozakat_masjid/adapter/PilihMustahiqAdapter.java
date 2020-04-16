package id.co.myproject.gozakat_masjid.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.view.mustahiq.TambahMustahiqActivity;
import id.co.myproject.gozakat_masjid.view.penyaluran.PilihMustahiqFragment;

import static id.co.myproject.gozakat_masjid.util.Helper.TYPE_EDIT;

public class PilihMustahiqAdapter extends RecyclerView.Adapter<PilihMustahiqAdapter.ViewHolder> {

    List<Mustahiq> mustahiqList = new ArrayList<>();
    Context context;
    int row_index = -1;

    public PilihMustahiqAdapter(Context context) {
        this.context = context;
    }

    public void setMustahiqList(List<Mustahiq> mustahiqList){
        this.mustahiqList.clear();
        this.mustahiqList.addAll(mustahiqList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PilihMustahiqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mustahiq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PilihMustahiqAdapter.ViewHolder holder, int position) {
        String fotoMustahiq = mustahiqList.get(position).getFoto();
        if(fotoMustahiq.equals("")){
            Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"mustahiq/mustahiq.png").into(holder.ivMustahiq);
        }else {
            Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR + "mustahiq/" + fotoMustahiq).into(holder.ivMustahiq);
        }
        holder.tvNamaMustahiq.setText(mustahiqList.get(position).getNamaMustahiq());
        holder.tvAlamat.setText(mustahiqList.get(position).getAlamat());
        holder.tvJenis.setText(mustahiqList.get(position).getJenis_mustahiq());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TambahMustahiqActivity.class);
                intent.putExtra("id_mustahiq", mustahiqList.get(position).getIdMustahiq());
                intent.putExtra("type", TYPE_EDIT);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                PilihMustahiqFragment.tv_mustahiq_pilihan.setText("Pilihan : "+mustahiqList.get(position).getNamaMustahiq());
                PilihMustahiqFragment.tv_mustahiq_pilihan.setVisibility(View.VISIBLE);
                PilihMustahiqFragment.idMustahiq = mustahiqList.get(position).getIdMustahiq();
                PilihMustahiqFragment.namaMustahiq = mustahiqList.get(position).getNamaMustahiq();
                PilihMustahiqFragment.selectMustahiq = true;
            }
        });

        if (row_index == position){
            holder.lvBackgroundMustahiq.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.tvNamaMustahiq.setTextColor(Color.parseColor("#ffffff"));
            holder.tvAlamat.setTextColor(Color.parseColor("#ffffff"));
            holder.tvNamaMustahiq.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.tvJenis.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.lvBackgroundMustahiq.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tvNamaMustahiq.setTextColor(Color.parseColor("#000000"));
            holder.tvAlamat.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.tvNamaMustahiq.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.tvJenis.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

    }

    @Override
    public int getItemCount() {
        return mustahiqList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMustahiq;
        TextView tvNamaMustahiq, tvAlamat, tvJenis;
        LinearLayout lvBackgroundMustahiq;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMustahiq = itemView.findViewById(R.id.iv_mustahiq);
            tvNamaMustahiq = itemView.findViewById(R.id.tv_nama_muzakki);
            tvAlamat = itemView.findViewById(R.id.tv_alamat_mustahiq);
            tvJenis = itemView.findViewById(R.id.tv_jenis);
            lvBackgroundMustahiq = itemView.findViewById(R.id.lv_background_mustahiq);
        }
    }
}
