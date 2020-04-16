package id.co.myproject.gozakat_masjid.view.penyaluran;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.PilihMustahiqAdapter;
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PilihMustahiqFragment extends Fragment {

    String nominal, jatuhTempo, namaMuzakki, typeZakat, idZakat;
    EditText et_cari;
    TextView tv_nominal, tv_lanjut;
    RecyclerView rv_mustahiq;
    PilihMustahiqAdapter pilihMustahiqAdapter;
    ApiRequest apiRequest;
    public static TextView tv_mustahiq_pilihan;
    public static boolean selectMustahiq = false;
    public static String idMustahiq, namaMustahiq;

    public PilihMustahiqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilih_mustahiq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        PenyaluranZakatActivity.prosesZakat = true;
        ((PenyaluranZakatActivity)getActivity()).getSupportActionBar().setTitle("Pilih Mustahiq");
        idZakat = getArguments().getString("id_zakat");
        nominal = getArguments().getString("nominal");
        jatuhTempo = getArguments().getString("jatuh_tempo");
        typeZakat = getArguments().getString("type_zakat");

        et_cari = view.findViewById(R.id.et_cari);
        tv_nominal = view.findViewById(R.id.tv_nominal);
        tv_lanjut = view.findViewById(R.id.tv_lanjut);
        rv_mustahiq = view.findViewById(R.id.rv_mustahiq);
        tv_mustahiq_pilihan = view.findViewById(R.id.tv_mustahiq_pilihan);
        tv_nominal.setText(nominal);

        namaMuzakki = getArguments().getString("nama_muzakki");
        typeZakat = getArguments().getString("type_zakat");

        pilihMustahiqAdapter = new PilihMustahiqAdapter(getActivity());
        rv_mustahiq.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mustahiq.setAdapter(pilihMustahiqAdapter);

        loadDataMustahiq();

        tv_lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectMustahiq) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putString("id_zakat", idZakat);
                    bundle.putString("id_mustahiq", idMustahiq);
                    bundle.putString("nama_mustahiq", namaMustahiq);
                    bundle.putString("nama_muzakki", namaMuzakki);
                    bundle.putString("type_zakat", typeZakat);
                    bundle.putString("nominal", tv_nominal.getText().toString());
                    PeringatanFragment peringatanFragment = new PeringatanFragment();
                    peringatanFragment.setArguments(bundle);
                    peringatanFragment.show(fm, "");
                }else {
                    Toast.makeText(getActivity(), "Pilih mustahiq terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loadCariMustahiq(editable.toString());
            }
        });

    }

    private void loadCariMustahiq(String toString) {
        Call<List<Mustahiq>> getAllMustahiq = apiRequest.getMustahiqCariRequest(toString);
        getAllMustahiq.enqueue(new Callback<List<Mustahiq>>() {
            @Override
            public void onResponse(Call<List<Mustahiq>> call, Response<List<Mustahiq>> response) {
                if (response.isSuccessful()){
                    List<Mustahiq> mustahiqList = response.body();
                    pilihMustahiqAdapter.setMustahiqList(mustahiqList);
                }
            }

            @Override
            public void onFailure(Call<List<Mustahiq>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataMustahiq() {
        Call<List<Mustahiq>> getAllMustahiq = apiRequest.getMustahiqAllRequest();
        getAllMustahiq.enqueue(new Callback<List<Mustahiq>>() {
            @Override
            public void onResponse(Call<List<Mustahiq>> call, Response<List<Mustahiq>> response) {
                if (response.isSuccessful()){
                    List<Mustahiq> mustahiqList = response.body();
                    pilihMustahiqAdapter.setMustahiqList(mustahiqList);
                }
            }

            @Override
            public void onFailure(Call<List<Mustahiq>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
