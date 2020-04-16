package id.co.myproject.gozakat_masjid.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_ZAKAT = "tb_zakat";
    static final class PesananColumns implements BaseColumns {
        static String ID_MASJID = "id_masjid";
        static String ID_ZAKAT = "id_zakat";
        static String NAMA_MUZAKKI = "nama_muzakki";
        static String NAMA_MUSTAHIQ = "nama_mustahiq";
        static String JENIS_ZAKAT = "jenis_zakat";
        static String NOMINAL = "nominal";
        static String TANGGAL = "tanggal";
    }
}
