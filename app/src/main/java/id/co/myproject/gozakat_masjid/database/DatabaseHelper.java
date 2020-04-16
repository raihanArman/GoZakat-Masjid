package id.co.myproject.gozakat_masjid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "db_zakat";
    public static final int DATABASE_VERSION = 1;
    public static final String SQL_CREATE_TABLE_ZAKAT = String.format("CREATE TABLE %s"+
            "(%s INTEGER, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL, "+
            "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_ZAKAT,
            DatabaseContract.PesananColumns.ID_MASJID,
            DatabaseContract.PesananColumns.ID_ZAKAT,
            DatabaseContract.PesananColumns.NAMA_MUZAKKI,
            DatabaseContract.PesananColumns.NAMA_MUSTAHIQ,
            DatabaseContract.PesananColumns.NOMINAL,
            DatabaseContract.PesananColumns.JENIS_ZAKAT,
            DatabaseContract.PesananColumns.TANGGAL
    );
    public DatabaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ZAKAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DatabaseContract.TABLE_ZAKAT);
        onCreate(sqLiteDatabase);
    }
}
