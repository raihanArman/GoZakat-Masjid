package id.co.myproject.gozakat_masjid.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Muzakki;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.view.penyaluran.PenyaluranZakatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

public class RequestLainnyaAdapter extends RecyclerView.Adapter<RequestLainnyaAdapter.ViewHolder> {

    List<Zakat> zakatList = new ArrayList<>();
    Context context;
    ApiRequest apiRequest;

    public RequestLainnyaAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;
    }

    public void setZakatList(List<Zakat> zakatList){
        this.zakatList.clear();
        this.zakatList.addAll(zakatList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestLainnyaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestLainnyaAdapter.ViewHolder holder, int position) {
        Call<Muzakki> muzakkiCall = apiRequest.getMuzakkiItemRequest(zakatList.get(position).getIdUser());
        muzakkiCall.enqueue(new Callback<Muzakki>() {
            @Override
            public void onResponse(Call<Muzakki> call, Response<Muzakki> response) {
                if (response.isSuccessful()){
                    Muzakki muzakki = response.body();
                    Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"muzakki/"+muzakki.getAvatar()).into(holder.ivMuzakki);
                    holder.tvNamaMuzakki.setText(muzakki.getNama());
                    holder.tvAlamatMuzakki.setText(muzakki.getAlamat());
                }
            }

            @Override
            public void onFailure(Call<Muzakki> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        String date = DateFormat.format("dd MMM yyyy", zakatList.get(position).getJatuhTempo()).toString();
        holder.tvJatuhTempo.setText("Jatuh tempo : "+date);
        holder.tvJenisZakat.setText(zakatList.get(position).getJenisZakat());
        String nominal = zakatList.get(position).getNominal();
        if (zakatList.get(position).getJenisZakat().equals("Infaq")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }else if (zakatList.get(position).getJenisZakat().equals("Mal")){
            nominal = nominal+" gram";
        }else if (zakatList.get(position).getJenisZakat().equals("Profesi")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }
        holder.tvNominal.setText(nominal);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PenyaluranZakatActivity.class);
                intent.putExtra("id_zakat", zakatList.get(position).getIdZakat());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return zakatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMuzakki;
        TextView tvNamaMuzakki, tvAlamatMuzakki, tvNominal, tvJenisZakat, tvJatuhTempo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMuzakki = itemView.findViewById(R.id.iv_mustahiq);
            tvNamaMuzakki = itemView.findViewById(R.id.tv_nama_muzakki);
            tvAlamatMuzakki = itemView.findViewById(R.id.tv_alamat_mustahiq);
            tvNominal = itemView.findViewById(R.id.tv_jenis);
            tvJenisZakat = itemView.findViewById(R.id.tv_jenis_zakat);
            tvJatuhTempo = itemView.findViewById(R.id.tv_jatuh_tempo);
        }
    }
}
