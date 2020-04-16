package id.co.myproject.gozakat_masjid.view.penyaluran;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PeringatanFragment extends DialogFragment {

    TextView tv_peringatan, tv_ya, tv_belum, tv_nama_muzakki, tv_nama_mustahiq, tv_nominal, tv_jenis_zakat;
    String idZakat, idMustahiq;
    ApiRequest apiRequest;

    public PeringatanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_peringatan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        tv_peringatan = view.findViewById(R.id.tv_peringatan);
        tv_ya = view.findViewById(R.id.tv_ya);
        tv_belum = view.findViewById(R.id.tv_belum);
        tv_nama_muzakki = view.findViewById(R.id.tv_nama_muzakki);
        tv_nama_mustahiq = view.findViewById(R.id.tv_nama_muzakki);
        tv_nominal = view.findViewById(R.id.tv_nominal);
        tv_jenis_zakat = view.findViewById(R.id.tv_jenis_zakat);

        idZakat = getArguments().getString("id_zakat");
        idMustahiq = getArguments().getString("id_mustahiq");
        String namaMuzakki = getArguments().getString("nama_muzakki");
        String namaMustahiq = getArguments().getString("nama_mustahiq");
        String nominal = getArguments().getString("nominal");
        String jenisZakat = getArguments().getString("type_zakat");

        tv_nama_muzakki.setText("Muzakki : "+namaMuzakki);
        tv_nama_mustahiq.setText("Mustahiq : "+namaMustahiq);
        tv_nominal.setText("Nominal : "+nominal);
        tv_jenis_zakat.setText(jenisZakat);
        tv_peringatan.setText("Apakah anda yakin ingin menyalurkan ke "+namaMustahiq+" ?");

        tv_ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Value> inputPenyalur = apiRequest.inputPenyalurRequest(idZakat, idMustahiq);
                inputPenyalur.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            if (response.body().getValue() == 1){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Pengiriman data berhasil, pembayaran akan dilakukan di masjid yang anda telah pilih");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                                builder.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tv_belum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
