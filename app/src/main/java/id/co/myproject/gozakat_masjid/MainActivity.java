package id.co.myproject.gozakat_masjid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import id.co.myproject.gozakat_masjid.adapter.RequestAdapter;
import id.co.myproject.gozakat_masjid.model.Masjid;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import id.co.myproject.gozakat_masjid.view.datazakat.DataZakatFragment;
import id.co.myproject.gozakat_masjid.view.home.HomeFragment;
import id.co.myproject.gozakat_masjid.view.profil.ProfilFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.frame_home);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home_nav){
                    setFragment(new HomeFragment());
                }else if (item.getItemId() == R.id.data_zakat_nav){
                    setFragment(new DataZakatFragment());
                }else if (item.getItemId() == R.id.profil_nav){
                    setFragment(new ProfilFragment());
                }

                return true;
            }
        });

        setFragment(new HomeFragment());

    }

    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(frameLayout.getId(), fragment);
        transaction.commit();
    }


}
