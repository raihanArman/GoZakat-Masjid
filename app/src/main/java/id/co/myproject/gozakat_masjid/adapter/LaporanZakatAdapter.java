package id.co.myproject.gozakat_masjid.adapter;

import android.text.format.DateFormat;
import android.widget.TextView;

import com.app.feng.fixtablelayout.inter.IDataAdapter;

import java.util.List;

import id.co.myproject.gozakat_masjid.model.ZakatHistory;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

public class LaporanZakatAdapter implements IDataAdapter {
    public String[] title = {"Tanggal","Muzakki", "Mustahiq", "Nominal", "Jenis Zakat"};
    public List<ZakatHistory> zakatHistoryList;

    public LaporanZakatAdapter(List<ZakatHistory> zakatHistoryList) {
        this.zakatHistoryList = zakatHistoryList;
    }

    @Override
    public String getTitleAt(int i) {
        return title[i];
    }

    @Override
    public int getTitleCount() {
        return title.length;
    }

    @Override
    public int getItemCount() {
        return zakatHistoryList.size();
    }

    @Override
    public void convertData(int i, List<TextView> list) {
        ZakatHistory zakatHistory = zakatHistoryList.get(i);
        list.get(1).setText(zakatHistory.getNamaMuzakki());
        list.get(2).setText(zakatHistory.getNamaMustahiq());
        String nominal = zakatHistoryList.get(i).getNominal();
        if (zakatHistoryList.get(i).getJenisZakat().equals("Infaq")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }else if (zakatHistoryList.get(i).getJenisZakat().equals("Mal")){
            nominal = nominal+" gram";
        }else if (zakatHistoryList.get(i).getJenisZakat().equals("Profesi")){
            nominal = rupiahFormat(Integer.parseInt(nominal));
        }
        list.get(3).setText(nominal);
        list.get(4).setText(zakatHistory.getJenisZakat());
    }

    @Override
    public void convertLeftData(int i, TextView textView) {

        String date = DateFormat.format("dd MMM yyyy", zakatHistoryList.get(i).getTanggalDistribusi()).toString();
        textView.setText(date);

    }
}
