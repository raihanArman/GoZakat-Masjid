package id.co.myproject.gozakat_masjid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Value {
    @SerializedName("value")
    @Expose
    private int value;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id_masjid")
    @Expose
    private int idMasjid;

    @SerializedName("jumlah_profesi")
    @Expose
    private String JumlahProfesi;

    @SerializedName("jumlah_mal")
    @Expose
    private String jumlahMal;

    @SerializedName("jumlah_infaq")
    @Expose
    private String jumlahInfaq;

    @SerializedName("jumlah_mustahiq")
    @Expose
    private String jumlahMustahiq;

    @SerializedName("jumlah_muzakki_profesi")
    @Expose
    private String jumlahMuzakkiProfesi;

    @SerializedName("jumlah_muzakki_mal")
    @Expose
    private String jumlahMuzakkiMal;

    @SerializedName("jumlah_muzakki_infaq")
    @Expose
    private String jumlahMuzakkiInfaq;

    @SerializedName("timestamp")
    @Expose
    private Date timestamp;

    @SerializedName("jumlah_notif")
    @Expose
    private String jumlahNotif;

    public Value(int value, String message, int idMasjid, String jumlahProfesi, String jumlahMal, String jumlahInfaq, String jumlahMustahiq, String jumlahMuzakkiProfesi, String jumlahMuzakkiMal, String jumlahMuzakkiInfaq, Date timestamp, String jumlahNotif) {
        this.value = value;
        this.message = message;
        this.idMasjid = idMasjid;
        JumlahProfesi = jumlahProfesi;
        this.jumlahMal = jumlahMal;
        this.jumlahInfaq = jumlahInfaq;
        this.jumlahMustahiq = jumlahMustahiq;
        this.jumlahMuzakkiProfesi = jumlahMuzakkiProfesi;
        this.jumlahMuzakkiMal = jumlahMuzakkiMal;
        this.jumlahMuzakkiInfaq = jumlahMuzakkiInfaq;
        this.timestamp = timestamp;
        this.jumlahNotif = jumlahNotif;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public int getIdMasjid() {
        return idMasjid;
    }

    public String getJumlahProfesi() {
        return JumlahProfesi;
    }

    public String getJumlahMal() {
        return jumlahMal;
    }

    public String getJumlahInfaq() {
        return jumlahInfaq;
    }

    public String getJumlahMustahiq() {
        return jumlahMustahiq;
    }

    public String getJumlahMuzakkiProfesi() {
        return jumlahMuzakkiProfesi;
    }

    public String getJumlahMuzakkiMal() {
        return jumlahMuzakkiMal;
    }

    public String getJumlahMuzakkiInfaq() {
        return jumlahMuzakkiInfaq;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getJumlahNotif() {
        return jumlahNotif;
    }
}
