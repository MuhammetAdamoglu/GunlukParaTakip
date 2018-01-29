package com.example.bycod.gunlukparahesapla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {

    private static final String VERITABANI = "gecici";
    private static final String TABLE = "gecicipara";
    private static String PARA = "para";
    private static String TARİH = "tarih";
    private static String SAAT = "saat";
    private static String IDORTAK = "IDortak";
    private static String MESAJ = "mesaj";


    int sayac1=0;
    int sayac2=0;


    public database(Context context) {
        super(context, VERITABANI, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Günlük veri için
        db.execSQL(
                "CREATE TABLE " + TABLE + " ( "

                        + PARA + " TEXT, "
                        + TARİH + " TEXT, "
                        + SAAT + " TEXT, "
                        + IDORTAK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MESAJ + " TEXT "
                        + " )"
        );

        //Kullanıcının belirttiği süredeki veriler için(aylık,yıllık,haftalık)
        db.execSQL(
                "CREATE TABLE KISITLI_KALICI ( "

                        + PARA + " TEXT, "
                        + TARİH + " TEXT, "
                        + SAAT + " TEXT, "
                        + IDORTAK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MESAJ + " TEXT "
                        + " )"
        );
        //Kullanıcının tüm kayıtları için
        db.execSQL(
                "CREATE TABLE KALICI ( "

                        + PARA + " TEXT, "
                        + TARİH + " TEXT, "
                        + SAAT + " TEXT, "
                        + IDORTAK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MESAJ + " TEXT "
                        + " )"
        );

        //Harcanılan para verileri için(harcanılan para grafiği için)
        db.execSQL(
                "CREATE TABLE HARCANILAN_PARA ( "

                        + PARA + " TEXT "
                        + " )"
        );
        //Kalan paralar için(kalan para grafiği için)
        db.execSQL(
                "CREATE TABLE KALAN_PARA ( "

                        + "kalan_para" + " TEXT "
                        + " )"
        );
        //Kullanıcnın ayırdığı paralar için
        db.execSQL(
                "CREATE TABLE AYRILAN_PARA ( "

                        + "ayrilan_para" + " TEXT, "
                        + "ayrilan_para_mesaj" + " TEXT, "
                        + "id_ayrilan" + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "kontrol" + " TEXT "
                        + " )"
        );


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXITS" + TABLE);
        db.execSQL("DROP TABLE IF EXITS" + "KISITLI_KALICI");
        db.execSQL("DROP TABLE IF EXITS" + "HARCANILAN_PARA");
        db.execSQL("DROP TABLE IF EXITS" + "KALAN_PARA");
        db.execSQL("DROP TABLE IF EXITS" + "AYRILAN_PARA");
        db.execSQL("DROP TABLE IF EXITS" + "KALICI");
        onCreate(db);

    }


    public boolean ekle(String para, String tarih,String saat, String mesaj) {

        //Günlük veri eklemek için

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues veriler = new ContentValues();

        veriler.put(PARA, para);
        veriler.put(TARİH, tarih);
        veriler.put(SAAT, saat);
        veriler.put(MESAJ, mesaj );



        long result = db.insert(TABLE, null, veriler);
        if (result == -1)
            return false;
        else
            return true;

    }
    public void ekle_kisitli_kalici(String para, String tarih, String saat, String mesaj){

        //Kullanıcını belirttiği süre içerisindeki verileri için

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues veriler = new ContentValues();


        veriler.put(PARA, para);
        veriler.put(TARİH, tarih);
        veriler.put(SAAT, saat);
        veriler.put(MESAJ, mesaj);


        db.insert("KISITLI_KALICI", null, veriler);

    }
    public void ekle_kalici(String para, String tarih, String saat, String mesaj){

        //Kullanıcını tüm verileri için

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues veriler = new ContentValues();


        veriler.put(PARA, para);
        veriler.put(TARİH, tarih);
        veriler.put(SAAT, saat);
        veriler.put(MESAJ, mesaj);


        db.insert("KALICI", null, veriler);

    }
    public void ekle_harcanilanpara_veriler(String para, String kalan_para){

        //Eklenen harcanılan para verileri için

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues veriler = new ContentValues();
        ContentValues veriler2 = new ContentValues();


        veriler.put(PARA, para);
        veriler2.put("kalan_para", kalan_para);


        db.insert("KALAN_PARA", null, veriler2);
        db.insert("HARCANILAN_PARA", null, veriler);


    }

    public boolean ekle_ayrilan_para(String para, String mesaj, String kontrol){

        //Kullanıcnın ayırdığı paralar için

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues veriler = new ContentValues();

        veriler.put("ayrilan_para", para);
        veriler.put("ayrilan_para_mesaj", mesaj);
        veriler.put("kontrol", kontrol);



        long result = db.insert("AYRILAN_PARA", null, veriler);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {

        //günlük verileri çekmek için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from " + TABLE, null);

        return result;
    }

    public Cursor getAllData_kisitli_kalici(){

        //Kullanıcını belirttiği süre içerisindeki verileri almak için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result= db.rawQuery("select * from " + "KISITLI_KALICI", null);
        return result;
    }
    public Cursor getAllData_kalici(){

        //Kullanıcını tüm verileri almak için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result= db.rawQuery("select * from " + "KALICI", null);
        return result;
    }
    public Cursor getAllData_harcanilanpara_veriler(){

        //Eklenen tüm harcanan para verilerini almak için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result= db.rawQuery("select * from " + "HARCANILAN_PARA", null);
        return result;
    }
    public Cursor getAllData_kalan_para(){

        //Kalan para verilerini almak için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result= db.rawQuery("select * from " + "KALAN_PARA", null);
        return result;
    }
    public Cursor getAllData_ayrilan_para() {

        //Kullanıcnın ayırdığı paraları almak için

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from " + "AYRILAN_PARA", null);

        return result;
    }



    public Integer deleteData( String idortak) {

        //Günlük ve kalıcı verileri silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("KISITLI_KALICI", "IDortak = ?", new String[]{idortak});

        db.delete(TABLE, "IDortak = ?", new String[]{idortak});

        return  db.delete("KALICI", "IDortak = ?", new String[]{idortak});
    }
    public Integer deleteData_AyrilanPara(String id) {

        //Ayrılan parayı silmek için
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete("AYRILAN_PARA", "id_ayrilan = ?", new String[]{id});
    }



    public void updateMessage(String id, String mesaj) {

        //Değişen açıklamayı güncellemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(MESAJ, mesaj);


        db.update(TABLE, contentValues, "IDortak = ?", new String[]{id});
        db.update("KISITLI_KALICI", contentValues, "IDortak = ?", new String[]{id});
        db.update("KALICI", contentValues, "IDortak = ?", new String[]{id});
    }
    public void updatePara(String id, String para) {
        //değiştirilen parayı güncellemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(PARA, para);


        db.update(TABLE, contentValues, "IDortak = ?", new String[]{id});
        db.update("KISITLI_KALICI", contentValues, "IDortak = ?", new String[]{id});
        db.update("KALICI", contentValues, "IDortak = ?", new String[]{id});
    }

    public void updateKontrol(String id, String kontrol) {
        //Ayrılan paradaki ödenen paranın kontrol değişkenini güncelemek için(0 ödenmemiş, 1 ödenmiş)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("kontrol", kontrol);


        db.update("AYRILAN_PARA", contentValues, "id_ayrilan = ?", new String[]{id});
    }
    public void updateParaAyrilan(String id, String para) {
        //Ayrılan parayı güncellemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put("ayrilan_para", para);


        db.update("AYRILAN_PARA", contentValues, "id_ayrilan = ?", new String[]{id});
    }






    public void DeleteAllData_onlytable1(){
        //Sadece günlük verilerin tümünü silmek için
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,null,null);
        db.close();

    }
    public void DeleteAllData(){
        //
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,null,null);
        db.delete("KISITLI_KALICI",null,null);
        db.delete("KALAN_PARA",null,null);
        db.delete("AYRILAN_PARA",null,null);
        db.close();

    }

    public void DeleteGraph(){
        //HArcanılan tüm parayı silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("HARCANILAN_PARA",null,null);

    }

    public void DeleteAllAyrilanPara(){
        //Ayrılan tüm parayı silmek için
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("AYRILAN_PARA",null,null);

    }
    public void DeleteAllKalici(){
        //Tüm kayıtlı verileri silmek için
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("KALICI",null,null);

    }

}