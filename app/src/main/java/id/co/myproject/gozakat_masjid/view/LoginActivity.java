package id.co.myproject.gozakat_masjid.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.gozakat_masjid.MainActivity;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;


public class LoginActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    //    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApiRequest apiRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        frameLayout = findViewById(R.id.frame_login);
    }

    private void updateUI(final boolean isSignedIn) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        final int idMasjid = sharedPreferences.getInt("id_masjid", 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSignedIn) {
                    if (idMasjid != 0) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        int idMasjid = sharedPreferences.getInt("id_masjid", 0);
        if (idMasjid != 0 ){
            updateUI(true);
        }else {
            setFragment(new SignInFragment());
//            if (setSignUpFragment) {
//                setSignUpFragment = false;
//                setFragment(new SignUpFragment());
//            } else {
//                setFragment(new SignInFragment());
//            }
        }
    }
}
