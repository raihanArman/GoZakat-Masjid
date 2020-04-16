package id.co.myproject.gozakat_masjid.view.datazakat;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.co.myproject.gozakat_masjid.MainActivity;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Value;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataKelolaFragment extends Fragment {

    ImageView iv_back;
    TextView tv_nominal_profesi, tv_muzakki_profesi, tv_nominal_mal, tv_muzakki_mal, tv_nominal_infaq, tv_muzakki_infaq;
    LinearLayout lv_profesi, lv_mal, lv_infaq;
    SharedPreferences sharedPreferences;
    ApiRequest apiRequest;

    public DataKelolaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_kelola, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        int idMasjid = sharedPreferences.getInt("id_masjid", 0);

        iv_back = view.findViewById(R.id.iv_back);
        tv_nominal_infaq = view.findViewById(R.id.tv_nominal_infaq);
        tv_nominal_mal = view.findViewById(R.id.tv_nominal_mal);
        tv_nominal_profesi = view.findViewById(R.id.tv_nominal_profesi);
        tv_muzakki_infaq = view.findViewById(R.id.tv_muzakki_infaq);
        tv_muzakki_mal = view.findViewById(R.id.tv_muzakki_mal);
        tv_muzakki_profesi = view.findViewById(R.id.tv_muzakki_profesi);
        lv_infaq = view.findViewById(R.id.lv_infaq);
        lv_mal = view.findViewById(R.id.lv_mal);
        lv_profesi = view.findViewById(R.id.lv_profesi);
        loadJumlahKas(idMasjid);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        DataListZakatFragment dataListZakatFragment = new DataListZakatFragment();
        Bundle bundle = new Bundle();
        lv_profesi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("jenis_zakat", "Profesi");
                dataListZakatFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, dataListZakatFragment)
                        .addToBackStack("")
                        .commit();
            }
        });
        lv_mal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("jenis_zakat", "Mal");
                dataListZakatFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, dataListZakatFragment)
                        .addToBackStack("")
                        .commit();
            }
        });
        lv_infaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("jenis_zakat", "Infaq");
                dataListZakatFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_home, dataListZakatFragment)
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
                    tv_nominal_infaq.setText(rupiahFormat(Integer.parseInt(response.body().getJumlahInfaq())));
                    tv_nominal_profesi.setText(rupiahFormat(Integer.parseInt(response.body().getJumlahProfesi())));
                    tv_nominal_mal.setText(response.body().getJumlahMal()+" gram");
                    tv_muzakki_infaq.setText(response.body().getJumlahMuzakkiInfaq()+" Muzakki");
                    tv_muzakki_mal.setText(response.body().getJumlahMuzakkiMal()+" Muzakki");
                    tv_muzakki_profesi.setText(response.body().getJumlahMuzakkiProfesi()+" Muzakki");
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
