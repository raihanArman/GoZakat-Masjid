package id.co.myproject.gozakat_masjid.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
public class SignInFragment extends Fragment {
    ImageView signInButton;
    EditText etEmail, etPassword;
    Button btnSignIn;
    TextView tvRegistrasi, tvLupaPassword, tv_email;
    FrameLayout parentFrameLayout;
    int id_masjid, login_level;
    String nama,email,url, avatar, namaUser, emailUser, avatarUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    GoogleApiClient googleApiClient;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    ApiRequest apiRequest;
    private boolean userExists = false;
    ProgressDialog progressDialog;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnSignIn = view.findViewById(R.id.btn_sign_in);
        tvRegistrasi = view.findViewById(R.id.tv_registrasi);
        tvLupaPassword = view.findViewById(R.id.tv_lupa_password);
        tv_email = view.findViewById(R.id.tv_email);
        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Memproses ...");
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        btnSignIn.setEnabled(false);
        btnSignIn.setTextColor(Color.argb(50,255,255, 255));

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProcess();
            }
        });

        tvRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });

        tvLupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ForgotPasswordFragment());
            }
        });

    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void loginProcess(){
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (etEmail.getText().toString().matches(emailPattern)) {
            if (etPassword.length() >= 8) {
                progressDialog.show();
                btnSignIn.setEnabled(true);
                prosesLogin(email, password);
            } else {
                Toast.makeText(getActivity(), "Password kurang boss", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Username atau Password salah boss", Toast.LENGTH_SHORT).show();
        }

    }

    private void prosesLogin(final String email, final String password) {
        Call<Value> cekUser = apiRequest.cekMasjidRequest(email);
        cekUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    if (response.body().getValue() == 1) {
                        id_masjid = response.body().getIdMasjid();
                        Call<Value> loginMasjid = apiRequest.loginMasjidRequest(email, password);
                        loginMasjid.enqueue(new Callback<Value>() {
                            @Override
                            public void onResponse(Call<Value> call, Response<Value> response) {
                                if (response.isSuccessful()){
                                    Toast.makeText(getActivity(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    if(response.body().getValue() == 1){
                                        progressDialog.dismiss();
                                        editor.putInt("id_masjid", id_masjid);
                                        editor.commit();
                                        updateUI(true);
                                    }else {
                                        progressDialog.dismiss();
                                        btnSignIn.setEnabled(true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Value> call, Throwable t) {
                                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Email tidak terdaftar", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(final boolean isSignedIn) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        final int idMasjid = sharedPreferences.getInt("id_masjid", 0);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSignedIn) {
                    if (idMasjid != 0) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                } else {
                    progressDialog.dismiss();
                    signInButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(etEmail.getText())){
            if (!TextUtils.isEmpty(etPassword.getText())){
                btnSignIn.setEnabled(true);
                btnSignIn.setTextColor(Color.rgb(255,255, 255));
            }else {
                btnSignIn.setEnabled(false);
                btnSignIn.setTextColor(Color.argb(50,255,255, 255));
            }
        }else {
            btnSignIn.setEnabled(false);
            btnSignIn.setTextColor(Color.argb(50,255,255, 255));
        }
    }

}