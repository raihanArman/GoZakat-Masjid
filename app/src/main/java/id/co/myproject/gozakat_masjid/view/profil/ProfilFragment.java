package id.co.myproject.gozakat_masjid.view.profil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.MainActivity;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Masjid;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import id.co.myproject.gozakat_masjid.view.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {
    ImageView ivUser, ivBackground, ivSetting, ivBack;
    TextView tvUser, tvEmail;
    Button btnLogOut;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;
    int idMasjid;
    public static boolean statusUpdate = false;

    public ProfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        idMasjid = sharedPreferences.getInt("id_masjid", 0);

        ivUser = view.findViewById(R.id.iv_user);
        ivSetting = view.findViewById(R.id.iv_setting);
        ivBackground = view.findViewById(R.id.iv_background);
        tvUser = view.findViewById(R.id.tv_user);
        tvEmail = view.findViewById(R.id.tv_email);
        btnLogOut = view.findViewById(R.id.btn_log_out);
        ivSetting.setVisibility(View.VISIBLE);
        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfilFragment editProfilFragment = new EditProfilFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_masjid", idMasjid);
                editProfilFragment.setArguments(bundle);
                ((MainActivity)view.getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_home, editProfilFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        loadDataMasjid();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void loadDataMasjid() {
        Call<Masjid> masjidCall = apiRequest.getMasjidItemRequest(idMasjid);
        masjidCall.enqueue(new Callback<Masjid>() {
            @Override
            public void onResponse(Call<Masjid> call, Response<Masjid> response) {
                if (response.isSuccessful()){
                    Masjid masjid = response.body();
                    tvUser.setText(masjid.getNamaMasjid());
                    tvEmail.setText(masjid.getEmailMasjid());
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"masjid/"+masjid.getGambar()).into(ivUser);
                    Glide.with(getActivity()).load(BuildConfig.BASE_URL_GAMBAR+"masjid/"+masjid.getGambar()).into(ivBackground);
                }
            }

            @Override
            public void onFailure(Call<Masjid> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        editor.putInt("id_masjid", 0);
        editor.commit();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusUpdate) {
            loadDataMasjid();
        }
    }
}
