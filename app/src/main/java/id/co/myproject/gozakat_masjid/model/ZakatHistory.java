package id.co.myproject.gozakat_masjid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ZakatHistory {
    @SerializedName("id_distribusi")
    @Expose
    private String idDistribusi;

    @SerializedName("id_zakat")
    @Expose
    private String idZakat;

    @SerializedName("id_user")
    @Expose
    private String idUser;

    @SerializedName("id_masjid")
    @Expose
    private String idMasjid;

    @SerializedName("id_mustahiq")
    @Expose
    private String idMustahiq;

    @SerializedName("nama_muzakki")
    @Expose
    private String namaMuzakki;

    @SerializedName("nama_mustahiq")
    @Expose
    private String namaMustahiq;

    @SerializedName("nama_masjid")
    @Expose
    private String namaMasjid;

    @SerializedName("jenis_zakat")
    @Expose
    private String jenisZakat;

    @SerializedName("nominal")
    @Expose
    private String nominal;

    @SerializedName("jatuh_tempo")
    @Expose
    private Date jatuhTempo;

    @SerializedName("tanggal_distribusi")
    @Expose
    private Date tanggalDistribusi;

    public ZakatHistory() {
    }

    public ZakatHistory(String idDistribusi, String idZakat, String idUser, String idMasjid, String idMustahiq, String namaMuzakki, String namaMustahiq, String namaMasjid, String jenisZakat, String nominal, Date jatuhTempo, Date tanggalDistribusi) {
        this.idDistribusi = idDistribusi;
        this.idZakat = idZakat;
        this.idUser = idUser;
        this.idMasjid = idMasjid;
        this.idMustahiq = idMustahiq;
        this.namaMuzakki = namaMuzakki;
        this.namaMustahiq = namaMustahiq;
        this.namaMasjid = namaMasjid;
        this.jenisZakat = jenisZakat;
        this.nominal = nominal;
        this.jatuhTempo = jatuhTempo;
        this.tanggalDistribusi = tanggalDistribusi;
    }

    public String getIdDistribusi() {
        return idDistribusi;
    }

    public String getIdZakat() {
        return idZakat;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdMasjid() {
        return idMasjid;
    }

    public String getIdMustahiq() {
        return idMustahiq;
    }

    public String getNamaMuzakki() {
        return namaMuzakki;
    }

    public String getNamaMustahiq() {
        return namaMustahiq;
    }

    public String getNamaMasjid() {
        return namaMasjid;
    }

    public String getJenisZakat() {
        return jenisZakat;
    }

    public String getNominal() {
        return nominal;
    }

    public Date getJatuhTempo() {
        return jatuhTempo;
    }

    public Date getTanggalDistribusi() {
        return tanggalDistribusi;
    }

    public void setIdDistribusi(String idDistribusi) {
        this.idDistribusi = idDistribusi;
    }

    public void setIdZakat(String idZakat) {
        this.idZakat = idZakat;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setIdMasjid(String idMasjid) {
        this.idMasjid = idMasjid;
    }

    public void setIdMustahiq(String idMustahiq) {
        this.idMustahiq = idMustahiq;
    }

    public void setNamaMuzakki(String namaMuzakki) {
        this.namaMuzakki = namaMuzakki;
    }

    public void setNamaMustahiq(String namaMustahiq) {
        this.namaMustahiq = namaMustahiq;
    }

    public void setNamaMasjid(String namaMasjid) {
        this.namaMasjid = namaMasjid;
    }

    public void setJenisZakat(String jenisZakat) {
        this.jenisZakat = jenisZakat;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public void setJatuhTempo(Date jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public void setTanggalDistribusi(Date tanggalDistribusi) {
        this.tanggalDistribusi = tanggalDistribusi;
    }
}
