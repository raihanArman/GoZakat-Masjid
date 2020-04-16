package id.co.myproject.gozakat_masjid.view.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.MainActivity;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.RequestAdapter;
import id.co.myproject.gozakat_masjid.adapter.ZakatTerbaruAdapter;
import id.co.myproject.gozakat_masjid.model.Masjid;
import id.co.myproject.gozakat_masjid.model.Value;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import id.co.myproject.gozakat_masjid.view.datazakat.DataKelolaFragment;
import id.co.myproject.gozakat_masjid.view.mustahiq.DataMustahiqFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    RecyclerView rv_request, rv_aktivitas;
    LinearLayout lv_mustahiq, lv_no_activity;
    FloatingActionButton fb_add_mustahiq;
    ConstraintLayout view_notif;
    SharedPreferences sharedPreferences;
    TextView tvNotif, tvTitleZakat, tvTitleRequest,tvMasjid, tvLihatZakat, tvZakatProfesi, tvZakatMal, tvInfaq, tvKelola, tv_jumah_mustahiq, tv_update_mustahiq;
    ImageView ivMasjid;
    ApiRequest apiRequest;
    RelativeLayout rl_notif;
    RequestAdapter requestAdapter;
    ZakatTerbaruAdapter zakatTerbaruAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idMasjid = sharedPreferences.getInt("id_masjid", 0);
        tvZakatProfesi = view.findViewById(R.id.tv_zakat_profesi);
        tvZakatMal = view.findViewById(R.id.tv_zakat_mal);
        tvInfaq = view.findViewById(R.id.tv_infaq);
        tv_jumah_mustahiq = view.findViewById(R.id.tv_jumlah_mustahiq);
        tv_update_mustahiq = view.findViewById(R.id.tv_update_mustahiq);
        tvTitleZakat = view.findViewById(R.id.tv_title_zakat);
        tvNotif = view.findViewById(R.id.tv_notif);
        tvTitleRequest = view.findViewById(R.id.tv_title_request);
        lv_no_activity = view.findViewById(R.id.lv_no_activity);
        tvKelola = view.findViewById(R.id.tv_kelola);
        rv_request = view.findViewById(R.id.rv_request);
        rv_aktivitas = view.findViewById(R.id.rv_aktivitas);
        tvMasjid = view.findViewById(R.id.tv_masjid);
        view_notif = view.findViewById(R.id.view_notif);
        tvLihatZakat = view.findViewById(R.id.tv_lihat_zakat);
        ivMasjid = view.findViewById(R.id.iv_masjid);
        lv_mustahiq = view.findViewById(R.id.lv_mustahiq);
        fb_add_mustahiq = view.findViewById(R.id.fb_add_mustahiq);
        rl_notif = view.findViewById(R.id.rl_notif);

        rv_request.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_aktivitas.setLayoutManager(new LinearLayoutManager(getActivity()));
        requestAdapter = new RequestAdapter(getActivity(), apiRequest);
        zakatTerbaruAdapter = new ZakatTerbaruAdapter(getActivity(), apiRequest);
        rv_request.setAdapter(requestAdapter);
        rv_aktivitas.setAdapter(zakatTerbaruAdapter);

        loadJumlahKas(idMasjid);

        tvKelola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, new DataKelolaFragment())
                        .addToBackStack("")
                        .commit();
            }
        });

        lv_mustahiq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_home, new DataMustahiqFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        loadDataMasjid(idMasjid);
        loadDataRequest(idMasjid);
        loadDataZakatRecent(idMasjid);

        tvLihatZakat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, new ZakatLainnyaFragment())
                        .addToBackStack("")
                        .commit();
            }
        });

        view_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, new RequestLainnyaFragment())
                        .addToBackStack("")
                        .commit();
            }
        });

    }

    private void loadJumlahKas(int idMasjid) {
        Call<Value> getHomeMasjid = apiRequest.homeMasjidRequest(idMasjid);
        getHomeMasjid.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    tvZakatProfesi.setText("Zakat profesi : "+rupiahFormat(Integer.parseInt(response.body().getJumlahProfesi())));
                    tvZakatMal.setText("Zakat mal : "+response.body().getJumlahMal()+" gram");
                    tvInfaq.setText("Infaq : "+rupiahFormat(Integer.parseInt(response.body().getJumlahInfaq())));
                    tv_jumah_mustahiq.setText(response.body().getJumlahMustahiq()+" Mustahiq");
                    tv_update_mustahiq.setText("terakhir ditambahkan "+ DateFormat.format("dd MMM yyyy", response.body().getTimestamp()).toString());
                    if (response.body().getJumlahNotif().equals("0")){
                        rl_notif.setVisibility(View.GONE);
                    }else {
                        tvNotif.setText(response.body().getJumlahNotif());
                    }

                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataZakatRecent(int idMasjid) {
        Call<List<ZakatHistory>> getZakatRecent = apiRequest.getHistoryRequest(idMasjid);
        getZakatRecent.enqueue(new Callback<List<ZakatHistory>>() {
            @Override
            public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                if (response.isSuccessful()){
                    List<ZakatHistory> zakatHistoryList = response.body();
                    if (zakatHistoryList.size() <= 0){
                        tvLihatZakat.setVisibility(View.GONE);
                        rv_aktivitas.setVisibility(View.GONE);
                        tvTitleZakat.setVisibility(View.GONE);
//                        lv_no_activity.setVisibility(View.VISIBLE);
                    }else {
                        tvLihatZakat.setVisibility(View.VISIBLE);
                        rv_aktivitas.setVisibility(View.VISIBLE);
                        tvTitleZakat.setVisibility(View.VISIBLE);
                        lv_no_activity.setVisibility(View.GONE);
                        zakatTerbaruAdapter.setZakatHistoryList(zakatHistoryList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataRequest(int idMasjid) {
        Call<List<Zakat>> getRequest = apiRequest.getZakatRequest(idMasjid);
        getRequest.enqueue(new Callback<List<Zakat>>() {
            @Override
            public void onResponse(Call<List<Zakat>> call, Response<List<Zakat>> response) {
                if (response.isSuccessful()){
                    List<Zakat> zakatList = response.body();
                    if (zakatList.size() <= 0){
                        tvTitleRequest.setVisibility(View.GONE);
                        rv_request.setVisibility(View.GONE);
                    }else {
                        tvTitleRequest.setVisibility(View.VISIBLE);
                        rv_request.setVisibility(View.VISIBLE);
                        requestAdapter.setZakatList(zakatList);
                        lv_no_activity.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Zakat>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataMasjid(int idMasjid) {
        Call<Masjid> masjidCall = apiRequest.getMasjidItemRequest(idMasjid);
        masjidCall.enqueue(new Callback<Masjid>() {
            @Override
            public void onResponse(Call<Masjid> call, Response<Masjid> response) {
                if (response.isSuccessful()){
                    Masjid masjid = response.body();
                    tvMasjid.setText(masjid.getNamaMasjid());
                    String masjidGambar = masjid.getGambar();
                    if (masjid.getGambar().equals("")){
                        masjidGambar = BuildConfig.BASE_URL_GAMBAR+"masjid/masjid.png";
                        Glide.with(getActivity()).load(masjidGambar).into(ivMasjid);
                    }else {
                        Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"masjid/"+masjidGambar).into(ivMasjid);
                    }
                }
            }

            @Override
            public void onFailure(Call<Masjid> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
