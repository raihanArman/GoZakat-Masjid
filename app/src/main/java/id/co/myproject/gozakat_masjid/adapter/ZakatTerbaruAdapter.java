package id.co.myproject.gozakat_masjid.adapter;

import android.content.Context;
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
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.model.Muzakki;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

public class ZakatTerbaruAdapter extends RecyclerView.Adapter<ZakatTerbaruAdapter.ViewHolder> {
    List<ZakatHistory> zakatHistoryList = new ArrayList<>();
    Context context;
    ApiRequest apiRequest;

    public ZakatTerbaruAdapter(Context context, ApiRequest apiRequest) {
        this.context = context;
        this.apiRequest = apiRequest;
    }

    public void setZakatHistoryList(List<ZakatHistory> zakatHistoryList){
        this.zakatHistoryList.clear();
        this.zakatHistoryList.addAll(zakatHistoryList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ZakatTerbaruAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktivitas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZakatTerbaruAdapter.ViewHolder holder, int position) {
        String jatuhTempo = DateFormat.format("dd MMM yyyy", zakatHistoryList.get(position).getJatuhTempo()).toString();
        String tglDistribusi = DateFormat.format("dd MMM yyyy", zakatHistoryList.get(position).getTanggalDistribusi()).toString();
        holder.tv_jatuh_tempo.setText("Jatuh tempo : "+jatuhTempo);
        String nominal = zakatHistoryList.get(position).getNominal();
        if (zakatHistoryList.get(position).getJenisZakat().equals("Infaq")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }else if (zakatHistoryList.get(position).getJenisZakat().equals("Mal")){
            nominal = nominal+" gram";
        }else if (zakatHistoryList.get(position).getJenisZakat().equals("Profesi")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }
        holder.tv_nominal.setText("Nominal : "+nominal);
        holder.tv_tanggal_distribusi.setText("Tanggal penyaluran : "+tglDistribusi);
        Call<Mustahiq> getMustahiq = apiRequest.getMustahiqItemRequest(zakatHistoryList.get(position).getIdMustahiq());
        getMustahiq.enqueue(new Callback<Mustahiq>() {
            @Override
            public void onResponse(Call<Mustahiq> call, Response<Mustahiq> response) {
                if (response.isSuccessful()){
                    Mustahiq mustahiq = response.body();
                    String foto = mustahiq.getFoto();
                    if (mustahiq.getFoto().equals("")){
                        foto = BuildConfig.BASE_URL_GAMBAR+"mustahiq/mustahiq.png";
                    }else {
                        foto = BuildConfig.BASE_URL_GAMBAR+"mustahiq/"+foto;
                    }
                    holder.tv_nama_mustahiq.setText(mustahiq.getNamaMustahiq());
                    Glide.with(context).load(foto).into(holder.iv_mustahiq);
                }
            }

            @Override
            public void onFailure(Call<Mustahiq> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Call<Muzakki> muzakkiCall = apiRequest.getMuzakkiItemRequest(zakatHistoryList.get(position).getIdUser());
        muzakkiCall.enqueue(new Callback<Muzakki>() {
            @Override
            public void onResponse(Call<Muzakki> call, Response<Muzakki> response) {
                if (response.isSuccessful()){
                    Muzakki muzakki = response.body();
                    Glide.with(context).load(BuildConfig.BASE_URL_GAMBAR+"muzakki/"+muzakki.getAvatar()).into(holder.iv_muzakki);
                    holder.tv_nama_muzakki.setText(muzakki.getNama());
                }
            }

            @Override
            public void onFailure(Call<Muzakki> call, Throwable t) {
                Toast.makeText(context, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (zakatHistoryList.size() > 0){
            return 1;
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_jatuh_tempo, tv_nama_mustahiq, tv_jenis_zakat, tv_nama_muzakki, tv_nominal, tv_nama_masjid, tv_tanggal_distribusi;
        ImageView iv_mustahiq, iv_muzakki;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_mustahiq = itemView.findViewById(R.id.iv_mustahiq);
            iv_muzakki = itemView.findViewById(R.id.iv_muzakki);
            tv_jatuh_tempo = itemView.findViewById(R.id.tv_jatuh_tempo);
            tv_nama_mustahiq = itemView.findViewById(R.id.tv_nama_mustahiq);
            tv_jenis_zakat = itemView.findViewById(R.id.tv_jenis_zakat);
            tv_nama_muzakki = itemView.findViewById(R.id.tv_nama_muzakki);
            tv_nominal = itemView.findViewById(R.id.tv_jenis);
            tv_tanggal_distribusi = itemView.findViewById(R.id.tv_distribusi);
        }
    }
}
