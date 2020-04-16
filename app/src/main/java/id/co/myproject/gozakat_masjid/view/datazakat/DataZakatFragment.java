package id.co.myproject.gozakat_masjid.view.datazakat;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.MainActivity;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.DataZakatAdapter;
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

public class DataZakatFragment extends Fragment {

    RecyclerView rv_data_zakat;
    LinearLayout lv_preview;
    TextView tv_hari_ini;
    LinearLayout lv_cetak, lv_empty, lv_filter;
    ApiRequest apiRequest;
    DataZakatAdapter dataZakatAdapter;
    SharedPreferences sharedPreferences;
    Spinner sp_filter, sp_jenis_zakat;
    int type_filter, typ_jenis_zakat;
    ProgressDialog progressDialog;

    public DataZakatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_zakat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idMasjid = sharedPreferences.getInt("id_masjid", 0);
        rv_data_zakat = view.findViewById(R.id.rv_data_zakat);
        lv_preview = view.findViewById(R.id.lv_preview);
        lv_empty = view.findViewById(R.id.lv_empty);
        lv_filter = view.findViewById(R.id.lv_filter);
        tv_hari_ini = view.findViewById(R.id.tv_tanggal_hari_ini);
        rv_data_zakat.setLayoutManager(new LinearLayoutManager(getActivity()));
        dataZakatAdapter = new DataZakatAdapter(getActivity());
        rv_data_zakat.setAdapter(dataZakatAdapter);
        sp_filter = view.findViewById(R.id.sp_filter);
        sp_jenis_zakat = view.findViewById(R.id.sp_jenis_zakat);

        List<String> filterList = new ArrayList<>();
        filterList.add("Hari ini");
        filterList.add("Semua");

        List<String> jenisZakatList = new ArrayList<>();
        jenisZakatList.add("Semua");
        jenisZakatList.add("Profesi");
        jenisZakatList.add("Mal");
        jenisZakatList.add("Infaq");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner,R.id.weekofday, filterList);
        sp_filter.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapterJenisZakat = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner,R.id.weekofday, jenisZakatList);
        sp_jenis_zakat.setAdapter(arrayAdapterJenisZakat);

        sp_jenis_zakat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typ_jenis_zakat = i;
                loadDataPemasukan(idMasjid, type_filter, typ_jenis_zakat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_filter = i;
                loadDataPemasukan(idMasjid, type_filter, typ_jenis_zakat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        String hari_ini = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        tv_hari_ini.setText("Hari ini "+hari_ini);

        lv_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hari_ini = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
                PreviewFragment cobahFragment = new PreviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString("tanggal", hari_ini);
                bundle.putInt("type_filter", type_filter);
                bundle.putInt("type_jenis_zakat", typ_jenis_zakat);
                cobahFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, cobahFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void loadDataPemasukan(int idMasjid, int filter_position, int jenis_zakat_position) {
        progressDialog.show();
        String jenisZakat = "";
        if (jenis_zakat_position == FILTER_JENIS_ZAKAT_SEMUA){
            jenisZakat = "";
        }else if(jenis_zakat_position == FILTER_JENIS_ZAKAT_PROFESI){
            jenisZakat = "Profesi";
        }else if(jenis_zakat_position == FILTER_JENIS_ZAKAT_MAL){
            jenisZakat = "Mal";
        }else if(jenis_zakat_position == FILTER_JENIS_ZAKAT_INFAQ){
            jenisZakat = "Infaq";
        }
        if (filter_position == FILTER_HARI_INI) {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String time1 = " 00:00:00";
            String time2 = " 23:59:00";
            String tanggal1 = date + time1;
            String tanggal2 = date + time2;

            Call<List<ZakatHistory>> getZakatToday = apiRequest.getHistoryTodayRequest(idMasjid, tanggal1, tanggal2, jenisZakat);
            getZakatToday.enqueue(new Callback<List<ZakatHistory>>() {
                @Override
                public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                    if (response.isSuccessful()){
                        List<ZakatHistory> zakatHistoryList = response.body();
                        dataZakatAdapter.setZakatHistoryList(zakatHistoryList);
                        if (zakatHistoryList.size() <= 0){
                            lv_empty.setVisibility(View.VISIBLE);
                            rv_data_zakat.setVisibility(View.INVISIBLE);
                        }else{
                            lv_empty.setVisibility(View.GONE);
                            rv_data_zakat.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else if (type_filter == FILTER_SEMUA){
            Call<List<ZakatHistory>> getZakatAll = apiRequest.getHistoryRequest(idMasjid, jenisZakat);
            getZakatAll.enqueue(new Callback<List<ZakatHistory>>() {
                @Override
                public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                    if (response.isSuccessful()){
                        List<ZakatHistory> zakatHistoryList = response.body();
                        dataZakatAdapter.setZakatHistoryList(zakatHistoryList);
                        if (zakatHistoryList.size() <= 0){
                            lv_empty.setVisibility(View.VISIBLE);
                            rv_data_zakat.setVisibility(View.INVISIBLE);
                        }else{
                            lv_empty.setVisibility(View.GONE);
                            rv_data_zakat.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                    Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        progressDialog.dismiss();
    }
}
