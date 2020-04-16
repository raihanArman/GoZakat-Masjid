package id.co.myproject.gozakat_masjid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.myproject.gozakat_masjid.model.ZakatHistory;

import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.ID_MASJID;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.ID_ZAKAT;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.JENIS_ZAKAT;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.NAMA_MUSTAHIQ;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.NAMA_MUZAKKI;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.NOMINAL;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.PesananColumns.TANGGAL;
import static id.co.myproject.gozakat_masjid.database.DatabaseContract.TABLE_ZAKAT;

public class ZakatHelper {
    public static DatabaseHelper databaseHelper;
    private static ZakatHelper INSTANCE;

    private static SQLiteDatabase database;
    Context context;

    public ZakatHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public static ZakatHelper getINSTANCE(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new ZakatHelper(context);
                }
            }
        }

        return INSTANCE;
    }

    public void open(){
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public long addToZakat(int idMasjid, ZakatHistory zakatHistory){
        ContentValues args = new ContentValues();
        args.put(ID_MASJID, idMasjid);
        args.put(ID_ZAKAT, zakatHistory.getIdDistribusi());
        args.put(NAMA_MUZAKKI, zakatHistory.getNamaMuzakki());
        args.put(NAMA_MUSTAHIQ, zakatHistory.getNamaMustahiq());
        args.put(NOMINAL, zakatHistory.getNominal());
        args.put(JENIS_ZAKAT, "Zakat "+zakatHistory.getJenisZakat());
        args.put(TANGGAL, DateFormat.format("dd MMM yyyy", zakatHistory.getTanggalDistribusi()).toString());
        return database.insert(TABLE_ZAKAT, null, args);
    }

    public List<ZakatHistory> getAllZakat(){
        List<ZakatHistory> zakatHistoryList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_ZAKAT, null,
                null,
                null,
                null,
                null,
                null,
                null);
        cursor.moveToFirst();
        ZakatHistory zakatHistory;
        if (cursor.getCount() > 0){
            do{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy");
                Date date = new Date();
                try {
                    date = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(TANGGAL)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                zakatHistory = new ZakatHistory();
                zakatHistory.setIdDistribusi(cursor.getString(cursor.getColumnIndex(ID_ZAKAT)));
                zakatHistory.setNamaMuzakki(cursor.getString(cursor.getColumnIndex(NAMA_MUZAKKI)));
                zakatHistory.setNamaMustahiq(cursor.getString(cursor.getColumnIndex(NAMA_MUSTAHIQ)));
                zakatHistory.setNominal(cursor.getString(cursor.getColumnIndex(NOMINAL)));
                zakatHistory.setJenisZakat(cursor.getString(cursor.getColumnIndex(JENIS_ZAKAT)));
                zakatHistory.setTanggalDistribusi(date);
                zakatHistoryList.add(zakatHistory);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return zakatHistoryList;
    }

    public long cleanZakat(int id_masjid){
        return database.delete(TABLE_ZAKAT, ID_MASJID+" = "+id_masjid, null);
    }

}
