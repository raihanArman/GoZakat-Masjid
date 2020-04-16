package id.co.myproject.gozakat_masjid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import static id.co.myproject.gozakat_masjid.util.Helper.TYPE_EDIT;

public class MustahiqAdapter extends RecyclerView.Adapter<MustahiqAdapter.ViewHolder> {

    List<Mustahiq> mustahiqList = new ArrayList<>();
    Context context;

    public MustahiqAdapter(Context context) {
        this.context = context;
    }

    public void setMustahiqList(List<Mustahiq> mustahiqList){
        this.mustahiqList.clear();
        this.mustahiqList.addAll(mustahiqList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MustahiqAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mustahiq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MustahiqAdapter.ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return mustahiqList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMustahiq;
        TextView tvNamaMustahiq, tvAlamat, tvJenis;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMustahiq = itemView.findViewById(R.id.iv_mustahiq);
            tvNamaMustahiq = itemView.findViewById(R.id.tv_nama_muzakki);
            tvAlamat = itemView.findViewById(R.id.tv_alamat_mustahiq);
            tvJenis = itemView.findViewById(R.id.tv_jenis);
        }
    }
}
