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
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.RequestLainnyaAdapter;
import id.co.myproject.gozakat_masjid.adapter.ZakatLainnyaAdapter;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestLainnyaFragment extends Fragment {

    RecyclerView rv_request_lainnya;
    ImageView iv_back;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;
    LinearLayout lv_no_notif;

    public RequestLainnyaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_lainnya, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        int idMasjid = sharedPreferences.getInt("id_masjid", 0);

        rv_request_lainnya = view.findViewById(R.id.rv_request_lainnya);
        iv_back = view.findViewById(R.id.iv_back);
        lv_no_notif = view.findViewById(R.id.lv_no_notif);

        RequestLainnyaAdapter zakatLainnyaAdapter = new RequestLainnyaAdapter(getActivity(), apiRequest);
        rv_request_lainnya.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_request_lainnya.setAdapter(zakatLainnyaAdapter);

        Call<List<Zakat>> getRequest = apiRequest.getZakatRequest(idMasjid);
        getRequest.enqueue(new Callback<List<Zakat>>() {
            @Override
            public void onResponse(Call<List<Zakat>> call, Response<List<Zakat>> response) {
                if (response.isSuccessful()){
                    List<Zakat> zakatList = response.body();
                    if (zakatList.size() <= 0){
                        lv_no_notif.setVisibility(View.VISIBLE);
                        rv_request_lainnya.setVisibility(View.INVISIBLE);
                    }else {
                        lv_no_notif.setVisibility(View.GONE);
                        rv_request_lainnya.setVisibility(View.VISIBLE);
                        zakatLainnyaAdapter.setZakatList(zakatList);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Zakat>> call, Throwable t) {
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
