package id.co.myproject.gozakat_masjid.view.datazakat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.feng.fixtablelayout.FixTableLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.LaporanZakatAdapter;
import id.co.myproject.gozakat_masjid.database.ZakatHelper;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_HARI_INI;
import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_JENIS_ZAKAT_INFAQ;
import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_JENIS_ZAKAT_MAL;
import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_JENIS_ZAKAT_PROFESI;
import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_JENIS_ZAKAT_SEMUA;
import static id.co.myproject.gozakat_masjid.util.Helper.FILTER_SEMUA;
import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewFragment extends Fragment {

    FixTableLayout tb_laporan;
    LinearLayout lv_laporan;
    FloatingActionButton fb_cetak;
    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    ImageView iv_back;
    ProgressDialog progressDialog;
    LinearLayout lv_empty;
    ZakatHelper zakatHelper;
    int type_filter, type_jenis_zakat;
    private Bitmap bitmap;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public PreviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idMasjid = sharedPreferences.getInt("id_masjid", 0);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        zakatHelper = ZakatHelper.getINSTANCE(getActivity());
        zakatHelper.open();
        zakatHelper.cleanZakat(idMasjid);

        tb_laporan = view.findViewById(R.id.tb_laporan);
        lv_laporan = view.findViewById(R.id.lv_laporan);
        fb_cetak = view.findViewById(R.id.fb_cetak);
        lv_empty = view.findViewById(R.id.lv_empty);
        iv_back = view.findViewById(R.id.iv_back);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        type_filter = getArguments().getInt("type_filter");
        type_jenis_zakat = getArguments().getInt("type_jenis_zakat");

        loadTableZakat(idMasjid);

        fb_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                bitmap = loadBitmapFromView(tb_laporan, tb_laporan.getWidth(), tb_laporan.getHeight());
                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });


    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void loadTableZakat(int idMasjid) {
        if (type_filter == FILTER_HARI_INI) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time1 = " 00:00:00";
            String time2 = " 23:59:00";

            String tanggal1 = date + time1;
            String tanggal2 = date + time2;
            loadTodayZakat(idMasjid, tanggal1, tanggal2);
        }else if(type_filter == FILTER_SEMUA){
            loadAllZakat(idMasjid);
        }
    }

    private void loadAllZakat(int idMasjid) {
        String jenisZakat = "";
        if (type_jenis_zakat == FILTER_JENIS_ZAKAT_SEMUA){
            jenisZakat = "";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_PROFESI){
            jenisZakat = "Profesi";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_MAL){
            jenisZakat = "Mal";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_INFAQ){
            jenisZakat = "Infaq";
        }
        Call<List<ZakatHistory>> getAllZakat = apiRequest.getHistoryRequest(idMasjid, jenisZakat);
        getAllZakat.enqueue(new Callback<List<ZakatHistory>>() {
            @Override
            public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                if (response.isSuccessful()){
                    List<ZakatHistory> zakatHistoryList = response.body();
                    setDataZakatAll(idMasjid, zakatHistoryList);
                }
            }

            @Override
            public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataZakatAll(int idMasjid, List<ZakatHistory> zakatHistoryList) {
        LaporanZakatAdapter laporanZakatAdapter = new LaporanZakatAdapter(zakatHistoryList);
        tb_laporan.setAdapter(laporanZakatAdapter);
        if (zakatHistoryList.size() <= 0) {
            lv_empty.setVisibility(View.VISIBLE);
            tb_laporan.setVisibility(View.INVISIBLE);
        } else {
            lv_empty.setVisibility(View.GONE);
            tb_laporan.setVisibility(View.VISIBLE);
        }
        for (ZakatHistory zakatHistory : zakatHistoryList) {
            zakatHelper.addToZakat(idMasjid, zakatHistory);
        }
    }

    private void loadTodayZakat(int idMasjid, String tanggal1, String tanggal2) {
        String jenisZakat = "";
        if (type_jenis_zakat == FILTER_JENIS_ZAKAT_SEMUA){
            jenisZakat = "";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_PROFESI){
            jenisZakat = "Profesi";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_MAL){
            jenisZakat = "Mal";
        }else if(type_jenis_zakat == FILTER_JENIS_ZAKAT_INFAQ){
            jenisZakat = "Infaq";
        }
        Call<List<ZakatHistory>> getTodayZakat = apiRequest.getHistoryTodayRequest(idMasjid, tanggal1, tanggal2, jenisZakat);
        getTodayZakat.enqueue(new Callback<List<ZakatHistory>>() {
            @Override
            public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                if (response.isSuccessful()){
                    List<ZakatHistory> zakatHistoryList = response.body();
                    setDataZakatToday(idMasjid, zakatHistoryList);
                }
            }

            @Override
            public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataZakatToday(int idMasjid, List<ZakatHistory> zakatHistoryList) {
        LaporanZakatAdapter laporanZakatAdapter = new LaporanZakatAdapter(zakatHistoryList);
        tb_laporan.setAdapter(laporanZakatAdapter);
        if (zakatHistoryList.size() <= 0) {
            lv_empty.setVisibility(View.VISIBLE);
            tb_laporan.setVisibility(View.INVISIBLE);
        } else {
            lv_empty.setVisibility(View.GONE);
            tb_laporan.setVisibility(View.VISIBLE);
        }
        for (ZakatHistory zakatHistory : zakatHistoryList) {
            zakatHelper.addToZakat(idMasjid, zakatHistory);
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        if (checkPermissions()){
            String path = "";
            String filename = "";
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GoZakat";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();

            String tanggal_harini = getArguments().getString("tanggal");

            String random = UUID.randomUUID().toString();
            String code = random.substring(0,3);
            filename = "Laporan data zakat " + tanggal_harini + "_" + code + ".pdf";
            File file = new File(dir, filename);
            // write the document content

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            Paragraph p3 = new Paragraph();
            p3.add("Tanggal "+tanggal_harini+"\n\n");
            document.add(p3);

            PdfPTable table;

            table = new PdfPTable(5);
            table.addCell("Tanggal");
            table.addCell("Muzakki");
            table.addCell("Mustahiq");
            table.addCell("Nominal");
            table.addCell("Jenis Zakat");
            List<ZakatHistory> zakatHistoryList = zakatHelper.getAllZakat();
            for (ZakatHistory zakatHistory : zakatHistoryList) {
                String tanggal = DateFormat.format("dd MMM yyyy", zakatHistory.getTanggalDistribusi()).toString();
                String muzakki = zakatHistory.getNamaMuzakki();
                String mustahiq = zakatHistory.getNamaMustahiq();
                String nominal = zakatHistory.getNominal();
                String jenis_zakat = zakatHistory.getJenisZakat();
                table.addCell(tanggal);
                table.addCell(muzakki);
                table.addCell(mustahiq);
                table.addCell(nominal);
                table.addCell(jenis_zakat);
            }

            document.add(table);
            document.addCreationDate();
            document.close();


            openGeneratedPDF(filename);

        }

    }

    private void openGeneratedPDF(String filename){
        progressDialog.dismiss();
        String path = "";
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/GoZakat";
        File dir = new File(path);
        File file = new File(dir, filename);
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getActivity(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

}
