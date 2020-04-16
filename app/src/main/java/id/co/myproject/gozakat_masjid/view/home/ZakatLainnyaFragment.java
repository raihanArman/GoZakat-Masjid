package id.co.myproject.gozakat_masjid.view.home;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.ZakatLainnyaAdapter;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZakatLainnyaFragment extends Fragment {

    RecyclerView rv_zakat_lainnya;
    ImageView iv_back;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;

    public ZakatLainnyaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zakat_lainnya, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        int idMasjid = sharedPreferences.getInt("id_masjid", 0);

        rv_zakat_lainnya = view.findViewById(R.id.rv_zakat_lainnya);
        iv_back = view.findViewById(R.id.iv_back);

        ZakatLainnyaAdapter zakatLainnyaAdapter = new ZakatLainnyaAdapter(getActivity(), apiRequest);
        rv_zakat_lainnya.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_zakat_lainnya.setAdapter(zakatLainnyaAdapter);

        Call<List<ZakatHistory>> getZakatRecent = apiRequest.getHistoryRequest(idMasjid);
        getZakatRecent.enqueue(new Callback<List<ZakatHistory>>() {
            @Override
            public void onResponse(Call<List<ZakatHistory>> call, Response<List<ZakatHistory>> response) {
                if (response.isSuccessful()){
                    List<ZakatHistory> zakatHistoryList = response.body();
                    zakatLainnyaAdapter.setZakatHistoryList(zakatHistoryList);
                }
            }

            @Override
            public void onFailure(Call<List<ZakatHistory>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }
}
