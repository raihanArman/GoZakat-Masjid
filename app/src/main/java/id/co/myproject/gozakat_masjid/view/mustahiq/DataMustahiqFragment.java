package id.co.myproject.gozakat_masjid.view.mustahiq;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.adapter.MustahiqAdapter;
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.co.myproject.gozakat_masjid.util.Helper.TYPE_ADD;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataMustahiqFragment extends Fragment {

    ApiRequest apiRequest;
    RecyclerView rv_mustahiq;
    SharedPreferences sharedPreferences;
    MustahiqAdapter mustahiqAdapter;
    FloatingActionButton fb_tambah;
    ImageView iv_back;
    EditText et_cari;

    public DataMustahiqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_mustahiq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        rv_mustahiq = view.findViewById(R.id.rv_mustahiq);
        fb_tambah = view.findViewById(R.id.fb_tambah);
        iv_back = view.findViewById(R.id.iv_back);
        et_cari = view.findViewById(R.id.et_cari);

        mustahiqAdapter = new MustahiqAdapter(getActivity());
        rv_mustahiq.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_mustahiq.setAdapter(mustahiqAdapter);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        fb_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TambahMustahiqActivity.class);
                intent.putExtra("type", TYPE_ADD);
                startActivity(intent);
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

        loadDataMustahiq();

    }
    private void loadCariMustahiq(String toString) {
        Call<List<Mustahiq>> getAllMustahiq = apiRequest.getMustahiqCariRequest(toString);
        getAllMustahiq.enqueue(new Callback<List<Mustahiq>>() {
            @Override
            public void onResponse(Call<List<Mustahiq>> call, Response<List<Mustahiq>> response) {
                if (response.isSuccessful()){
                    List<Mustahiq> mustahiqList = response.body();
                    mustahiqAdapter.setMustahiqList(mustahiqList);
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
                    mustahiqAdapter.setMustahiqList(mustahiqList);
                }
            }

            @Override
            public void onFailure(Call<List<Mustahiq>> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataMustahiq();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }



}
