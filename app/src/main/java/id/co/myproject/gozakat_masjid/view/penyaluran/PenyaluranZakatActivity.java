package id.co.myproject.gozakat_masjid.view.penyaluran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import id.co.myproject.gozakat_masjid.R;

import android.os.Bundle;
import android.view.MenuItem;

public class PenyaluranZakatActivity extends AppCompatActivity {

    public static boolean prosesZakat = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penyaluran_zakat);

        getSupportActionBar().setTitle("Detail Zakat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailZakatFragment detailZakatFragment = new DetailZakatFragment();
        String idZakat = getIntent().getStringExtra("id_zakat");
        Bundle bundle = new Bundle();
        bundle.putString("id_zakat", idZakat);
        detailZakatFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_penyaluran,detailZakatFragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            if (!prosesZakat){
                finish();
            }else {
                PilihMustahiqFragment.selectMustahiq = false;
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
