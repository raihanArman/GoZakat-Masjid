package id.co.myproject.gozakat_masjid.view.mustahiq;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import id.co.myproject.gozakat_masjid.BuildConfig;
import id.co.myproject.gozakat_masjid.R;
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.model.Value;
import id.co.myproject.gozakat_masjid.request.ApiRequest;
import id.co.myproject.gozakat_masjid.request.RetrofitRequest;
import id.co.myproject.gozakat_masjid.util.ConvertBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static id.co.myproject.gozakat_masjid.util.Helper.GALLERY_REQUEST;
import static id.co.myproject.gozakat_masjid.util.Helper.TYPE_ADD;
import static id.co.myproject.gozakat_masjid.util.Helper.TYPE_EDIT;

public class TambahMustahiqActivity extends AppCompatActivity implements ConvertBitmap {

    Toolbar toolbar;
    Spinner spJenis;
    ApiRequest apiRequest;
    EditText et_nama, et_alamat, et_no_telp;
    TextView tv_ganti_gambar, tv_simpan, tv_update, tv_hapus,tv_bahan_baku, tv_bahan;
    ImageView iv_menu;
    int type_intent;
    int spinnerPosition;
    ProgressDialog progressDialog;
    LinearLayout btnLayoutAddImage;
    Bitmap bitmap;
    String image;
    String id_menu;
    int idCafe;
    SharedPreferences sharedPreferences;

    List<String> listKategori = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_mustahiq);
        toolbar = findViewById(R.id.toolbar_menu_add);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        et_nama = findViewById(R.id.et_nama);
        et_alamat = findViewById(R.id.et_alamat);
        et_no_telp = findViewById(R.id.et_no_telp);
        spJenis = findViewById(R.id.sp_jenis);
        btnLayoutAddImage = findViewById(R.id.layout_btn_add_image);
        iv_menu = findViewById(R.id.iv_menu);
        tv_ganti_gambar = findViewById(R.id.tv_ganti_gambar);
        tv_simpan = findViewById(R.id.tv_simpan);
        tv_update = findViewById(R.id.tv_update);
        tv_hapus = findViewById(R.id.tv_hapus);
        tv_bahan = findViewById(R.id.tv_bahan);

        type_intent = getIntent().getIntExtra("type", 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");

        listKategori.add("Fakir Miskin");
        listKategori.add("Amil");
        listKategori.add("Muallaf");
        listKategori.add("Memerdekan Budak");
        listKategori.add("Gharim");
        listKategori.add("Sabilillah");
        listKategori.add("Ibnu Sabil");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.item_spinner, R.id.weekofday, listKategori);
        spJenis.setAdapter(adapter);
        apiRequest = RetrofitRequest.getRetrofitInstance().create(ApiRequest.class);


        btnLayoutAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });


        tv_ganti_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        et_nama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cekInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_alamat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cekInput();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (type_intent == TYPE_ADD) {
            tv_simpan.setVisibility(View.VISIBLE);
            tv_hapus.setVisibility(View.GONE);
            tv_update.setVisibility(View.GONE);
            tv_simpan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    inputMenu();
                }
            });

        }else if (type_intent == TYPE_EDIT){
            tv_simpan.setVisibility(View.GONE);
            tv_hapus.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.VISIBLE);
            String id_mustahiq = getIntent().getStringExtra("id_mustahiq");
            loadMenuItem(id_mustahiq);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    updateMustahiq(id_mustahiq);
                }
            });


            tv_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    deleteMustahiq(id_mustahiq);
                }
            });
        }

    }


    private void deleteMustahiq(String id_mustahiq) {
        Call<Value> deleteMenu = apiRequest.deleteMustahiqRequest(id_mustahiq);
        deleteMenu.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahMustahiqActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahMustahiqActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahMustahiqActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMustahiq(String id_mustahiq) {
        Call<Value> editMenu = apiRequest.editMustahiqRequest(
                id_mustahiq,
                et_nama.getText().toString(),
                et_alamat.getText().toString(),
                spJenis.getSelectedItem().toString(),
                image,
                et_no_telp.getText().toString());
        editMenu.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().getValue() == 1) {
                        Toast.makeText(TambahMustahiqActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TambahMustahiqActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Toast.makeText(TambahMustahiqActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMenuItem(String id_mustahiq) {
        Call<Mustahiq> mustahiqCall = apiRequest.getMustahiqItemRequest(id_mustahiq);
        mustahiqCall.enqueue(new Callback<Mustahiq>() {
            @Override
            public void onResponse(Call<Mustahiq> call, Response<Mustahiq> response) {
                if (response.isSuccessful()){
                    Mustahiq menu = response.body();
                    setDataMustahiq(menu);
                }
            }

            @Override
            public void onFailure(Call<Mustahiq> call, Throwable t) {
                Toast.makeText(TambahMustahiqActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDataMustahiq(Mustahiq menu) {
        iv_menu.setVisibility(View.VISIBLE);
        btnLayoutAddImage.setVisibility(View.INVISIBLE);
        tv_ganti_gambar.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.bg_image);
        Glide.with(this).applyDefaultRequestOptions(options).load(BuildConfig.BASE_URL_GAMBAR + "mustahiq/" + menu.getFoto()).into(iv_menu);
        et_nama.setText(menu.getNamaMustahiq());
        et_alamat.setText(menu.getAlamat());
        et_no_telp.setText(menu.getNoTelp());

        int index = listKategori.indexOf(menu.getJenis_mustahiq());
        spJenis.setSelection(index);

    }

    private void inputMenu() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Proses ...");
        progressDialog.show();
        Call<Value> inputMustahiq = apiRequest.inputMustahiqRequest(
                et_nama.getText().toString(),
                et_alamat.getText().toString(),
                spJenis.getSelectedItem().toString(),
                image,
                et_no_telp.getText().toString()
        );
        inputMustahiq.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(TambahMustahiqActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getValue() == 1){
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TambahMustahiqActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cekInput(){
        if (bitmap != null){
            if (!TextUtils.isEmpty(et_nama.getText().toString())){
                if (!TextUtils.isEmpty(et_alamat.getText().toString())){
                    tv_simpan.setEnabled(true);
                }else {
                    tv_simpan.setEnabled(false);
                }
            }else {
                tv_simpan.setEnabled(false);
            }
        }else {
            tv_simpan.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST){
            if (resultCode == RESULT_OK && data != null){
                Uri imageResepUri = data.getData();
                iv_menu.setVisibility(View.VISIBLE);
                btnLayoutAddImage.setVisibility(View.INVISIBLE);
                tv_ganti_gambar.setVisibility(View.VISIBLE);
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                iv_menu.setImageBitmap(bitmap);
                cekInput();
                new LoadBitmapConvertCallback(TambahMustahiqActivity.this, this).execute();
            }
        }
    }

    @Override
    public void bitmapToString(String imgConvert) {
        image = imgConvert;
    }

    private class LoadBitmapConvertCallback extends AsyncTask<Void, Void, String> {
        private WeakReference<Context> weakContext;
        private WeakReference<ConvertBitmap> weakInsert;

        public LoadBitmapConvertCallback(Context context, ConvertBitmap insertKategori) {
            this.weakContext = new WeakReference<>(context);
            this.weakInsert = new WeakReference<>(insertKategori);
        }

        @Override
        protected String doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();
            String imageBitmap = Base64.encodeToString(imgByte,Base64.DEFAULT);
            return imageBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakContext.get();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            weakInsert.get().bitmapToString(aVoid);
        }
    }

}
