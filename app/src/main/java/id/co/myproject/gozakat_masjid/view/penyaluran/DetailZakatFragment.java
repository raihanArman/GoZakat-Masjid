package id.co.myproject.gozakat_masjid.view.penyaluran;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Muzakki;
import id.co.myproject.gozakat_masjid.model.Value;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.rupiahFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailZakatFragment extends Fragment {

    ImageView iv_muzakki;
    TextView tv_nama_muzakki, tv_alamat, tv_jenis_zakat, tv_nominal, tv_jatuh_tempo, tv_pilih_mustahiq;
    ApiRequest apiRequest;

    public DetailZakatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_zakat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        ((PenyaluranZakatActivity)getActivity()).getSupportActionBar().setTitle("Detail Zakat");
        PenyaluranZakatActivity.prosesZakat = false;

        iv_muzakki = view.findViewById(R.id.iv_muzakki);
        tv_nama_muzakki = view.findViewById(R.id.tv_muzakki);
        tv_alamat = view.findViewById(R.id.tv_alamat_muzakki);
        tv_jenis_zakat = view.findViewById(R.id.tv_jenis_zakat);
        tv_nominal = view.findViewById(R.id.tv_nominal);
        tv_pilih_mustahiq = view.findViewById(R.id.tv_pilih_mustahiq);
        tv_jatuh_tempo = view.findViewById(R.id.tv_jatuh_tempo);

        String idZakat = getArguments().getString("id_zakat");

        loadDataZakat(idZakat);

        tv_pilih_mustahiq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PilihMustahiqFragment pilihMustahiqFragment = new PilihMustahiqFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_zakat", idZakat);
                bundle.putString("nama_muzakki", tv_nama_muzakki.getText().toString());
                bundle.putString("type_zakat", tv_jenis_zakat.getText().toString());
                bundle.putString("nominal", tv_nominal.getText().toString());
                bundle.putString("jatuh_tempo", tv_jatuh_tempo.getText().toString());
                pilihMustahiqFragment.setArguments(bundle);
                ((PenyaluranZakatActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_penyaluran, pilihMustahiqFragment)
                        .addToBackStack("")
                        .commit();
            }
        });

    }

    private void loadDataZakat(String idZakat) {
        Call<Zakat> zakatCall =  apiRequest.getZakatItemRequest(idZakat);
        zakatCall.enqueue(new Callback<Zakat>() {
            @Override
            public void onResponse(Call<Zakat> call, Response<Zakat> response) {
                Zakat zakat = response.body();
                if (zakat.getJenisZakat().equals("Profesi")) {
                    tv_jenis_zakat.setText("Jenis : Zakat " + zakat.getJenisZakat());
                    tv_nominal.setText(rupiahFormat(Integer.parseInt(zakat.getNominal())));
                }else if(zakat.getJenisZakat().equals("Mal")){
                    tv_jenis_zakat.setText("Jenis : Zakat " + zakat.getJenisZakat());
                    tv_nominal.setText(zakat.getNominal()+" gram");
                }else if (zakat.getJenisZakat().equals("Infaq")){
                    tv_jenis_zakat.setText("Jenis : " + zakat.getJenisZakat());
                    tv_nominal.setText(rupiahFormat(Integer.parseInt(zakat.getNominal())));
                }
                String date = DateFormat.format("dd MMM yyyy", zakat.getJatuhTempo()).toString();
                tv_jatuh_tempo.setText("Tanggal masuk : "+date);
                Call<Muzakki> muzakkiCall = apiRequest.getMuzakkiItemRequest(zakat.getIdUser());
                muzakkiCall.enqueue(new Callback<Muzakki>() {
                    @Override
                    public void onResponse(Call<Muzakki> call, Response<Muzakki> response) {
                        if (response.isSuccessful()){
                            Muzakki muzakki = response.body();
                            tv_nama_muzakki.setText(muzakki.getNama());
                            Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"muzakki/"+muzakki.getAvatar()).into(iv_muzakki);
                            tv_alamat.setText(muzakki.getAlamat());
                        }
                    }

                    @Override
                    public void onFailure(Call<Muzakki> call, Throwable t) {
                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Zakat> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
