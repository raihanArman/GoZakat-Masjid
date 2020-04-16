package id.co.myproject.gozakat_masjid.request;

import java.util.List;

import id.co.myproject.gozakat_masjid.model.Masjid;
import id.co.myproject.gozakat_masjid.model.Mustahiq;
import id.co.myproject.gozakat_masjid.model.Muzakki;
import id.co.myproject.gozakat_masjid.model.Value;
import id.co.myproject.gozakat_masjid.model.Zakat;
import id.co.myproject.gozakat_masjid.model.ZakatHistory;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRequest {

    @GET("home_masjid.php")
    Call<Value> homeMasjidRequest(
            @Query("id_masjid") int idMasjid
    );

    @FormUrlEncoded
    @POST("input_masjid.php")
    Call<Value> inputMasjidRequest(
            @Field("username") String username,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("alamat") String alamat,
            @Field("password") String password,
            @Field("gambar") String gambar,
            @Field("no_telp") String noTelp
    );


    @FormUrlEncoded
    @POST("cek_masjid.php")
    Call<Value> cekMasjidRequest(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("login_masjid.php")
    Call<Value> loginMasjidRequest(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("tampil_user.php")
    Call<Muzakki> getMuzakkiItemRequest(
            @Query("id_user") String idUser
    );


    @GET("tampil_zakat.php")
    Call<List<Zakat>> getZakatRequest(
            @Query("id_masjid") int idMasjid
    );

    @GET("tampil_zakat.php")
    Call<List<Zakat>> getZakatByJenisRequest(
            @Query("id_masjid") int idMasjid,
            @Query("jenis_zakat") String jenisZakat
    );

    @GET("tampil_zakat.php")
    Call<Zakat> getZakatItemRequest(
            @Query("id_zakat") String idZakat
    );


    @GET("tampil_masjid.php")
    Call<Masjid> getMasjidItemRequest(
            @Query("id_masjid") int idMasjid
    );

    @GET("tampil_mustahiq.php")
    Call<Mustahiq> getMustahiqItemRequest(
            @Query("id_mustahiq") String idMustahiq
    );

    @GET("tampil_mustahiq.php")
    Call<List<Mustahiq>> getMustahiqAllRequest();

    @GET("tampil_mustahiq.php")
    Call<List<Mustahiq>> getMustahiqCariRequest(
            @Query("cari") String cari
    );

    @GET("tampil_histori.php")
    Call<List<ZakatHistory>> getHistoryRequest(
            @Query("id_masjid") int idMasjid
    );

    @GET("tampil_histori.php")
    Call<List<ZakatHistory>> getHistoryRequest(
            @Query("id_masjid") int idMasjid,
            @Query("jenis_zakat") String jenisZakat
    );

    @GET("tampil_histori.php")
    Call<List<ZakatHistory>> getHistoryTodayRequest(
            @Query("id_masjid") int idMasjid,
            @Query("tanggal_1") String tanggal1,
            @Query("tanggal_2") String tanggal2,
            @Query("jenis_zakat") String jenisZakat
    );

    @FormUrlEncoded
    @POST("input_mustahiq.php")
    Call<Value> inputMustahiqRequest(
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("jenis") String jenis,
            @Field("foto") String foto,
            @Field("no_telp") String noTelp
    );

    @FormUrlEncoded
    @POST("hapus_mustahiq.php")
    Call<Value> deleteMustahiqRequest(
            @Field("id_mustahiq") String idMustahiq
    );

    @FormUrlEncoded
    @POST("edit_mustahiq.php")
    Call<Value> editMustahiqRequest(
            @Field("id_mustahiq") String idMustahiq,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("jenis") String jenis,
            @Field("foto") String foto,
            @Field("no_telp") String noTelp
    );

    @FormUrlEncoded
    @POST("edit_masjid.php")
    Call<Value> editMasjidRequest(
            @Field("id_masjid") int idMasjid,
            @Field("nama") String nama,
            @Field("username") String username,
            @Field("alamat") String alamat,
            @Field("gambar") String gambar,
            @Field("no_telp") String noTelp
    );

    @FormUrlEncoded
    @POST("input_penyalur.php")
    Call<Value> inputPenyalurRequest(
            @Field("id_zakat") String idZakat,
            @Field("id_mustahiq") String idMustahiq
    );

    @FormUrlEncoded
    @POST("lupa_password_masjid.php")
    Call<Value> lupaPasswordMasjidRequest(
            @Field("id_masjid") int idMasjid,
            @Field("password") String password
    );
}
