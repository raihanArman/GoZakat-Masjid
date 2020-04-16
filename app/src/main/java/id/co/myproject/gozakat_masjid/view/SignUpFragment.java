package id.co.myproject.gozakat_masjid.view;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
public class SignUpFragment extends Fragment {

    EditText etNama, etUsername, etEmail, etPassword, etKonfirm, etAlamat, et_noTelp;
    Button btnSignUp;
    TextView tv_login;
    private FrameLayout parentFrameLayout;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private ProgressDialog progressDialog;
    public static final String TAG = SignUpFragment.class.getSimpleName();
    //    ApiRequest apiRequest;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNama = view.findViewById(R.id.et_nama);
        etAlamat = view.findViewById(R.id.et_alamat);
        et_noTelp = view.findViewById(R.id.et_noTelp);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        etKonfirm = view.findViewById(R.id.et_confirm_password);
        etUsername = view.findViewById(R.id.et_username);
        btnSignUp = view.findViewById(R.id.btn_sign_up);
        tv_login = view.findViewById(R.id.tv_login);

        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);

        parentFrameLayout = getActivity().findViewById(R.id.frame_login);

        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Proses ...");


        btnSignUp.setEnabled(false);
        btnSignUp.setTextColor(Color.argb(50,255,255, 255));

        etUsername.addTextChangedListener(new TextWatcher() {
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

        etNama.addTextChangedListener(new TextWatcher() {
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

        etAlamat.addTextChangedListener(new TextWatcher() {
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

        et_noTelp.addTextChangedListener(new TextWatcher() {
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

        etKonfirm.addTextChangedListener(new TextWatcher() {
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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
    }

    private void checkInput() {
        if (!TextUtils.isEmpty(etUsername.getText())) {
            if (!TextUtils.isEmpty(etNama.getText())) {
                if (!TextUtils.isEmpty(etEmail.getText())) {
                    if (!TextUtils.isEmpty(etAlamat.getText())) {
                        if (!TextUtils.isEmpty(et_noTelp.getText())) {
                            if (!TextUtils.isEmpty(etPassword.getText()) && etPassword.length() >= 8) {
                                if (!TextUtils.isEmpty(etKonfirm.getText())) {
                                    btnSignUp.setEnabled(true);
                                    btnSignUp.setTextColor(Color.rgb(255, 255, 255));
                                } else {
                                    btnSignUp.setEnabled(false);
                                    btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                                }
                            } else {
                                btnSignUp.setEnabled(false);
                                btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                            }
                        }else {
                            btnSignUp.setEnabled(false);
                            btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                        }
                    }else {
                        btnSignUp.setEnabled(false);
                        btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                    }
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
                }
            } else {
                btnSignUp.setEnabled(false);
                btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
            }
        }else {
            btnSignUp.setEnabled(false);
            btnSignUp.setTextColor(Color.argb(50, 255, 255, 255));
        }
    }

    private void daftar(){
        final String username = etUsername.getText().toString();
        final String alamat = etAlamat.getText().toString();
        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String noTelp= et_noTelp.getText().toString();
        if (etEmail.getText().toString().matches(emailPattern)){
            if (etPassword.getText().toString().equals(etKonfirm.getText().toString())){
                progressDialog.show();
                Call<Value> cekEmail = apiRequest.cekMasjidRequest(email);
                cekEmail.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        if (response.isSuccessful()){
                            if (response.body().getValue() == 1){
                                progressDialog.dismiss();
                                btnSignUp.setEnabled(true);
                                Toast.makeText(getActivity(), "Email sudah ada, gunakan email yang lain", Toast.LENGTH_SHORT).show();
                            }else if (response.body().getValue() == 0){
                                btnSignUp.setEnabled(false);
                                progressDialog.dismiss();
                                inputUser(username, nama, email, alamat, password, noTelp);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Tunggu konfirmasi dari admin maksimal 24 jam");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setFragment(new SignInFragment());
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                etPassword.setError("Password tidak cocok");
            }
        }else {
            etEmail.setError("Email tidak cocok");
        }
    }

    private void inputUser(String username, String nama, String email, String alamat, String password, String noTelp){
        Call<Value> inputUser = apiRequest.inputMasjidRequest(username, nama, email, alamat, password, "", noTelp);
        inputUser.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.body().getValue() == 1){

                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

}