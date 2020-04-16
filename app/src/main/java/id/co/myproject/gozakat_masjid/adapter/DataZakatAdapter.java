package id.co.myproject.gozakat_masjid.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

public class DataZakatAdapter extends RecyclerView.Adapter<DataZakatAdapter.ViewHolder> {

    List<ZakatHistory> zakatHistoryList = new ArrayList<>();
    Context context;

    public DataZakatAdapter(Context context) {
        this.context = context;
    }

    public void setZakatHistoryList(List<ZakatHistory> zakatHistoryList){
        this.zakatHistoryList.clear();
        this.zakatHistoryList.addAll(zakatHistoryList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataZakatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_zakat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataZakatAdapter.ViewHolder holder, int position) {
        String date = DateFormat.format("dd MMM yyyy", zakatHistoryList.get(position).getTanggalDistribusi()).toString();
        holder.tv_tanggal.setText(date);
        holder.tv_muzakki.setText("Muzakki : "+zakatHistoryList.get(position).getNamaMuzakki());
        holder.tv_mustahiq.setText("Mustahiq : "+zakatHistoryList.get(position).getNamaMustahiq());
        holder.tv_jenis_zakat.setText("Zakat "+zakatHistoryList.get(position).getJenisZakat());
        String nominal = zakatHistoryList.get(position).getNominal();
        if (zakatHistoryList.get(position).getJenisZakat().equals("Infaq")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }else if (zakatHistoryList.get(position).getJenisZakat().equals("Mal")){
            nominal = nominal+" gram";
        }else if (zakatHistoryList.get(position).getJenisZakat().equals("Profesi")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }
        holder.tv_nominal.setText("Nominal : "+nominal);
    }

    @Override
    public int getItemCount() {
        return zakatHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tanggal, tv_muzakki, tv_mustahiq, tv_nominal, tv_jenis_zakat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tanggal = itemView.findViewById(R.id.tv_tanggal);
            tv_muzakki = itemView.findViewById(R.id.tv_nama_muzakki);
            tv_mustahiq = itemView.findViewById(R.id.tv_nama_mustahiq);
            tv_nominal = itemView.findViewById(R.id.tv_nominal);
            tv_jenis_zakat = itemView.findViewById(R.id.tv_jenis_zakat);
        }
    }
}
