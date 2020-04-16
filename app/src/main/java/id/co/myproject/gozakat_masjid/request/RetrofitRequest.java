package id.co.myproject.gozakat_masjid.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.myproject.gozakat_masjid.BuildConfig;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {
    public static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm")
                .create();
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
