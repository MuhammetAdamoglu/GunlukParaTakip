package com.example.bycod.gunlukparahesapla;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;

import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;




public class AnaActivity extends AppCompatActivity {

    AlertDialog.Builder builder;

    EditText parola_olustur,parola_kaldir,parola_tekrar,parola_eski, parola_yeni, parola_yeni_tekrar;

    TextView Textview_ParaGoster, TextView_Yok, Para_Birimi, Textview_gunluk;
    EditText Edittext_ParaGir;
    ListView ListView;
    Button Button_Harca, Button_Menu,Button_Sifre, Button_Sifirla, Button_ParaEkle, Button_Kayitlar, Button_Borclar;
    ConstraintLayout ConstraintLayout;
    TableLayout Tablelayout;

    String goster_EklenenPara;
    String goster_HarcananPara;
    String Goster_Param;
    String Goster_KalanGun;

    Timer timer;


    double para;
    double gunsayisi;
    double gunsay;
    double gunluk_harcanacak_para;
    double girilen_para;
    double sepet_para=0;
    double sepet_harcanan_para=0;

    int kontrol_düzenle_btn=0;

    int sayac_menü=0;


    final NumberFormat numberformat = NumberFormat.getInstance();

    //Data baseyi bağliyoruz.
    final database myDb = new database(this);

    //Gerekli diziler oluşturuluyor.
    final ArrayList<String> dizi_para = new ArrayList();
    final ArrayList<String> dizi_tarih = new ArrayList();
    final ArrayList<String> dizi_saat = new ArrayList();
    final ArrayList<String> dizi_id = new ArrayList();
    final ArrayList<String> dizi_yorum = new ArrayList();


    final static Calendar now = Calendar.getInstance();


    int ay;
    int yil;
    int gun;



    //Zamanı dizilere ekliyoruz.
    //Bunu yapmamın sebebi bir düzen olması, örnegin saat 12:01 olsaydı kullanıcıya 12:1 yazacakdı ve buda güzel gözükmezdi. Diziye 1 i 01 olarak tanımladım ve diziden çektim.
    static final String hour[] = {"12", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};

    static final String minute[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};

    static final String second[] = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};

    static final String day[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33"};

    static final String month[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};


    //Gecemi gündüzmü hesabı için String belirtiyoruz
    final String[] gündüz_gece = new String[1];







    public void idler(){

        Textview_ParaGoster = (TextView) findViewById(R.id.Textview_ParaGoster);
        Textview_gunluk = (TextView) findViewById(R.id.Textview_gunluk);
        Edittext_ParaGir = (EditText) findViewById(R.id.Edittext_ParaGir);
        ListView = (ListView) findViewById(R.id.Listview);
        Button_Harca = (Button) findViewById(R.id.Button_Harca);
        Button_ParaEkle = (Button) findViewById(R.id.Button_ParaEkle);
        Button_Menu = (Button) findViewById(R.id.Button_Menu);
        Button_Borclar = (Button) findViewById(R.id.Button_Borclar);
        Button_Sifirla = (Button) findViewById(R.id.Button_Sifirla);
        Button_Sifre = (Button) findViewById(R.id.Button_Sifre);
        Button_Kayitlar = (Button) findViewById(R.id.Button_Kayitlar);
        TextView_Yok = (TextView) findViewById(R.id.Textview);
        Para_Birimi = (TextView) findViewById(R.id.Para_Birimi);
        Tablelayout = (TableLayout) findViewById(R.id.tableLayout);
        ConstraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);



    }



    public  void yorum_düzenle_ekle(final int position, final SharedPreferences.Editor editor, final SharedPreferences prefSettings){
        //Harcamaya girilen yorumu düzenler veya ekler.

        AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.edittext, null);

        final EditText et= (EditText) dialoglayout.findViewById(R.id.hatırlama);

        //Eğer düzenle butonuna basılmış ise eski mesajı hint olarak koyar.
        if(kontrol_düzenle_btn==1){
            et.setHint(dizi_yorum.get(position));
            kontrol_düzenle_btn=0;
        }

        builder.setView(dialoglayout);

        builder.setMessage(R.string.nealdiniz);
        builder.setPositiveButton(R.string.ekle,new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id){


                if(et.getText().toString().trim().equals("")){

                    //Eger kullanıcı yeni  bir yorum girmemiş ise alerdialogdan çıkıyor.
                }
                else
                {
                    myDb.updateMessage( dizi_id.get(position)  , et.getText().toString());

                    database_veri_cek(editor,prefSettings);
                    ekle_listview();
                }



            }

        });


        builder.show();

    }




    public void harcama_yorum(final int position, final SharedPreferences.Editor editor, final SharedPreferences prefSettings){
        //Harcanılan parada eğer herhangi bir yorum varsa düzenleme ve kaldır seçeneği sunulur, yoksa yorum ekleme imkanı sunulur.


        if(!dizi_para.get(0).trim().equals("")) {



            if (dizi_yorum.get(position).toString().trim().equals("")) {
                //Yorum yoksa burası çalışır.
                yorum_düzenle_ekle(position, editor, prefSettings);

            } else {
                //Yorum varsa burası çalışır.
                AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);


                builder.setMessage(dizi_yorum.get(position));


                builder.setPositiveButton(R.string.duzenle, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {


                        kontrol_düzenle_btn = 1;//Eski Yorumu hint şeklinde yollamak için kullanılır.Eğer bu değişken 1 ise edittext e hint eklenir.Sonra bu değişken sıfırlanır.
                        yorum_düzenle_ekle(position, editor, prefSettings);


                    }

                });


                builder.setNegativeButton(R.string.kaldir, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {

                        //Yorum kaldırılmak istenirse veri tabanından silme işlemi yapılır
                        myDb.updateMessage(dizi_id.get(position), "");

                        database_veri_cek(editor, prefSettings);
                        ekle_listview();


                    }

                });

                builder.show();

            }

        }
    }


    public void sil_düzenle(final int position, final SharedPreferences.Editor editor, final SharedPreferences prefSettings)
    {
        //Kullanıcı harcanılan parayı silmek isterse bu metot çalışır.

        AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);
            builder.setCancelable(false);

            builder.setTitle(R.string.silmekisdedigineeminmisiniz);
            builder.setMessage(dizi_para.get(position)+getString(R.string.Para_Birimi)+"\n"+dizi_tarih.get(position)+"\n"+" "+ dizi_yorum.get(position));

        //İptale dokunulduğunda yapılacaklar
        builder.setNeutralButton(R.string.iptal,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){


            }
        });
        //Sil e dokunulduğunda yapılacaklar
        builder.setNegativeButton(R.string.sil,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){


                int count;
                int Eklenmis_para_kontrol=0;


                //Silinen veri harcamamı eklememi kontrolü yapiyoruz.
                if(Double.parseDouble(dizi_para.get(position))>0){

                    //Eğer eklenmiş veri ise Eklenmis_para_kontrol değişkenini 1 yapiyoruz.
                    Eklenmis_para_kontrol=1;
                }


                    Integer deletedRows = myDb.deleteData(dizi_id.get(position));


                    //Silinme işleminin gerçekleşip gerçekleşmediğini kullanıcıya bildiriyoruz.
                    if(deletedRows>0){
                        Toast.makeText(getApplicationContext(), R.string.silinde,
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), R.string.silmebasarisiz,
                                Toast.LENGTH_LONG).show();
                    }


                database_veri_cek(editor,prefSettings);


                //Eger Eklenmis_para_kontrol değişkeni 1 ise kalan parayı tekrar aliyoruz.
                if(Eklenmis_para_kontrol==1){

                    para = Double.parseDouble(prefSettings.getString("para","0"));
                    Eklenmis_para_kontrol=0;
                }

                //Kullanıcıya günlük kalan parasını gösteriyoruz.
               gunluk_kalan_para();


            }
        });


            builder.setPositiveButton(R.string.duzenle,new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){

                    //Eger kullanıcı harcanılan parayı düzenlemek isterse...


                    AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);

                    LayoutInflater inflater = getLayoutInflater();
                    View dialoglayout = inflater.inflate(R.layout.para_duzenle_edittext, null);

                    final EditText et= (EditText) dialoglayout.findViewById(R.id.para_duzenle);

                        et.setHint(dizi_para.get(position));//Kullanıcının eski harcamasını hint şeklinde edit text de gösderiyoruz



                    builder.setView(dialoglayout);

                    builder.setMessage(R.string.duzenle);
                    builder.setPositiveButton(R.string.duzenle,new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){


                            boolean eklenmis_para_kontrol=false;

                            if(et.getText().toString().trim().equals(""))
                            {
                              //Eger kullanıcı yeni harcama girmemiş ise hiç birşey yapmadan alerdialogdan çıkıyor.
                            }
                            else if(dizi_para.get(position).substring(0,1).toString().trim().equals("+")){
                                //Eger para harcanılmamış, eklenmiş ise girilen yeni değeri pozitif olarak ekliyoruz.

                                myDb.updatePara( dizi_id.get(position)  , et.getText().toString());
                                eklenmis_para_kontrol=true;

                            }
                            else
                            {
                                //Eger para eklenmemiş, harcanılmış ise girilen yeni değeri negative olarak ekliyoruz.
                                myDb.updatePara( dizi_id.get(position)  , String.valueOf(-1*Double.parseDouble(et.getText().toString())));

                            }




                            database_veri_cek(editor,prefSettings);

                            if(eklenmis_para_kontrol==true)
                            {
                                para = Double.parseDouble(prefSettings.getString("para","0"));
                                gunluk_kalan_para();
                                eklenmis_para_kontrol=false;
                            }

                            ekle_listview();



                        }

                    });


                    builder.show();

                }
            });

        builder.show();


    }

    public void ekle_listview(){
        //Veritabanındaki verileri listview e koyuyoruz.

        final List<Bilgi> dizi= new ArrayList<Bilgi>();
        final OzelAdapter adaptorumuz = new OzelAdapter(this,dizi);


            for(int say=0; say<dizi_para.size(); say++){


                dizi.add(new Bilgi(numberformat.format(Double.parseDouble(dizi_para.get(say)))+getString(R.string.Para_Birimi),dizi_tarih.get(say),dizi_saat.get(say)));

            }

            ListView.setAdapter(adaptorumuz);




    }

    public void tum_para(){
        //Bu metot,kullanıcının harcama verilerinin toplamını bulur ve değiişkene kaydeder;

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        final Cursor res = myDb.getAllData_kisitli_kalici();

        double sepet_tum_para=0;

        if(res.getCount()==0){

        }
        else
        {

            while(res.moveToNext()){

                sepet_tum_para=sepet_tum_para +  Double.parseDouble(res.getString(0));

            }

        }

        //Kullanıcnın harcadığı parayı, belirlediği bütceden çıkartarak kalan parasını buluyoruz.Ve bu kalan parayı para isminde kaydediyoruz.
        double para= Double.parseDouble(prefSettings.getString("kayitpara","0"))+sepet_tum_para;


        editor.putString("para",String.valueOf(para)).commit();


    }

    public void goster_tum_kayitlar(SharedPreferences.Editor editor, SharedPreferences prefSettings, String buffer, Double sepet_tum_para, Double sepet_harcanilan_para, Double sepet_eklenen_para){
        //Kullanıcı bütün harcamalarını görmek isderse bu metot çalışır.

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AnaActivity.this);

        int gunsayisi = Integer.parseInt(prefSettings.getString("gunsayisi","0"));
        int gunsay = Integer.parseInt(prefSettings.getString("gunsay","0"));


        String para;
        para= String.valueOf(numberformat.format(Double.parseDouble(prefSettings.getString("para","0"))));


        builder.setCancelable(false);
        builder.setTitle(R.string.tumkayitlar);

        String sepet_strng;
        String kalangun;

        if(sepet_harcanilan_para>=0)
        {
            sepet_strng=getString(R.string.yok);
        }
        else
        {
            sepet_strng=String.valueOf(numberformat.format(-1*sepet_harcanilan_para))+getString(R.string.Para_Birimi);
        }

        if((gunsayisi-gunsay)==1)
        {
            kalangun="";
        }
        else
        {
            kalangun=String.valueOf(gunsayisi-gunsay);
        }


        if(sepet_eklenen_para==0)
        {
            builder.setMessage(
                            getString(R.string.hacanilanpara)+" "+sepet_strng+"\n"
                            +getString(R.string.paraniz)+" "+para+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.son)+" "+kalangun+" "+getString(R.string.gun)+"\n\n"
                            +buffer.toString()
            );
        }
        else
        {
            builder.setMessage(
                    getString(R.string.eklenenpara)+" "+ numberformat.format(sepet_eklenen_para)+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.hacanilanpara)+" "+sepet_strng+"\n"
                            +getString(R.string.paraniz)+" "+para+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.son)+" "+kalangun+" "+getString(R.string.gun)+"\n\n"
                            +buffer.toString()
            );

        }


        builder.setPositiveButton(R.string.tamam,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

            }
        });

        builder.setNegativeButton(R.string.kayitlar,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

                Intent i = new Intent(getApplicationContext(),Kayitlar.class);
                startActivity(i);

            }
        });


        builder.setNeutralButton(getString(R.string.grafik),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent i = new Intent(getApplicationContext(),Grafik_kalanpara.class);
                startActivity(i);

            }
        });

        builder.show();
    }

    public long gun_farki(SharedPreferences.Editor editor, SharedPreferences prefSettings){



        Calendar now = Calendar.getInstance();

        long Fark = 0;

        ay  = Integer.valueOf(prefSettings.getString("zaman_ay","0"));
        yil = Integer.valueOf(prefSettings.getString("zaman_yil","0"));
        gun = Integer.valueOf(prefSettings.getString("zaman_gun","0"));

        if(yil!=0)
        {
            Date IlkGun = new GregorianCalendar(yil, ay, gun, 00, 00).getTime();
            Date Suan = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 00, 00).getTime();


            Fark= Suan.getTime() - IlkGun.getTime();

            Fark = Fark / (1000 * 60 * 60 * 24);
        }


        return Fark;
    }

    public void zaman_kontrol(SharedPreferences.Editor editor, SharedPreferences prefSettings){

        //Bu metot çok önemli, çünkü bu metot 1 günün dolup dolmadığını kontrol ediyor.
        Calendar now = Calendar.getInstance();

        long Fark=gun_farki(editor,prefSettings);

        if((Fark > 0) ) {


            //Eger uygulamadaki son kaydedilen zaman ile şimdiki zamandan küçük ise gün değişmiş demek oluor ve burası çalışıyor.

            //Son kaydedilen zamandan şimdiki zamana de kadar gün geçdiğini bulmak için bu metot çağrılıyor.
            Gun_say(editor,prefSettings);


            //Kalan parayı grafikde kullanmak için SQLite kaydediyoruz.
            String kalan_para= String.valueOf(Double.parseDouble(prefSettings.getString("para","0")));

            //Geçen gün içinde harcanılan parayı grafikde kullanmak için SQLite kaydeiyoruz.
            myDb.ekle_harcanilanpara_veriler(prefSettings.getString("sepet_harcanan","0"),kalan_para);

            editor.putString("sepet_harcanan","0").commit();



            //Eger kullanıcı belii bir süre uygulamaya girmemiş ise o günlerin harcamasını 0, kalan para ise sabit bir şekilde grafiğe ekliyoruz.
            if(Fark>1){


                for(int say=0; say<(Fark)-1; say++){

                    myDb.ekle_harcanilanpara_veriler(prefSettings.getString("sepet_harcanan","0"),kalan_para);

                }

            }


            //Günlük verileri silip kullanıcıya yeni bir sayfa sunuyoruz.
            myDb.DeleteAllData_onlytable1();


        }
        else if(Fark < 0)
        {
            Gun_say(editor,prefSettings);
        }


        //Şimdiki zamanı kaydediyoruz.
        editor.putString("zaman_gun",String.valueOf(now.get(Calendar.DATE)));
        editor.putString("zaman_ay",String.valueOf(now.get(Calendar.MONTH)));
        editor.putString("zaman_yil",String.valueOf(now.get(Calendar.YEAR)));

        editor.putString("zaman",String.valueOf(now.get(Calendar.DATE)));
        editor.commit();

    }

    public void zaman_kontrol_2(SharedPreferences.Editor editor, SharedPreferences prefSettings){
        //Eğer gün değişdiyse ve uygulama çalışır vaziyetde ise, zaman_kontrol(üstteki metot) metodu çalışmaz ve bu yüzden uygulama sıfırlanmaz
        //Bu metot süreki çalışır ve eğer uygulama açıkken gün geçse bile bu metot çalışdığından uygulamaya ufak bir restart atılacak ve zaan_kontrol metodu çalışmış olacak.

        Calendar now = Calendar.getInstance();
        int Bugun = now.get(Calendar.DATE);

        int zaman = Integer.valueOf(prefSettings.getString("zaman",String.valueOf(Bugun)));


        if((zaman!=Bugun) ) {

            Toast.makeText(getApplicationContext(), R.string.yenigun,Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(),Tab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);


        }
        editor.putString("zaman",String.valueOf(now.get(Calendar.DATE)));
        editor.commit();




    }


    public void Gun_say(SharedPreferences.Editor editor, SharedPreferences prefSettings){

        //Bu metotda geçen gün sayiliyor.
        //Telefondaki en son kayıtlı olan zaman il şimdiki zaman farkı alınıyr.
        Calendar now = Calendar.getInstance();

        ay  = Integer.valueOf(prefSettings.getString("ay","0"));
        yil = Integer.valueOf(prefSettings.getString("yil","0"));
        gun = Integer.valueOf(prefSettings.getString("gun","0"));


        Date IlkGun = new GregorianCalendar(yil, ay, gun, 00, 00).getTime();
        Date Suan = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 00, 00).getTime();


        long Fark = Suan.getTime() - IlkGun.getTime();

        Fark = Fark / (1000 * 60 * 60 * 24);



        editor.putString("gunsay",String.valueOf(Fark));
    }

    public void database_veri_cek(SharedPreferences.Editor editor, SharedPreferences prefSettings){

        //Verileri SQLite den çekip diziye koyuyoruz ve diziyi adapter yardımı ile list viewde gösteriyoruz.

        final Cursor res = myDb.getAllData();



        sepet_para=0;
        sepet_harcanan_para=0;

        //veri çekmeden önce diziyi sıfırlıyoruz.
        dizi_para.clear();
        dizi_tarih.clear();
        dizi_saat.clear();
        dizi_yorum.clear();
        dizi_id.clear();

        if(res.getCount()!=0){

            //veriler bitene kadar çekiyoruz
            while(res.moveToNext()){

                //Veri tabanındaki bütün harcamaları topluyoruz(Günlük harcamalar)

                sepet_para=sepet_para + -1*Double.parseDouble(res.getString(0));

                //Eklenen paraları almayıp sadece harcanılan paraları topluyoruz
                if(Double.parseDouble(res.getString(0))<0){

                    sepet_harcanan_para=sepet_harcanan_para+  -1*Double.parseDouble(res.getString(0));

                }



                //Eger dizinin sıfırıncı indeksi boş ise verimizi sıfırıncı indekse koyuyoruz.
                if(res.getString(0).trim().equals("")) {

                    String para=res.getString(0);

                    //Burda, eger para eklenmiş ise başına + işareti ekliyoruz
                    if(Double.parseDouble(para)>0)
                    {
                        para="+"+res.getString(0);
                    }

                    dizi_para.add( para );
                    dizi_tarih.add( res.getString(1) );
                    dizi_saat.add( res.getString(2) );
                    dizi_id.add( res.getString(3) );
                    dizi_yorum.add(res.getString(4));
                }
                //dizinin ilk indiksi dolu ise verimizi sıfırıncı indiksden önce koyuyoruz.Bu şekilde yeni veriler en üstde olur
                else
                {
                    String para=res.getString(0);

                    //Burda, eger para eklenmiş ise başına + işareti ekliyoruz
                    if(Double.parseDouble(para)>0)
                    {
                        para="+"+res.getString(0);
                    }

                    dizi_para.add(0,para );
                    dizi_tarih.add(0,res.getString(1) );
                    dizi_saat.add(0,res.getString(2) );
                    dizi_id.add(0,res.getString(3) );
                    dizi_yorum.add(0,res.getString(4));

                }


            }

        }

        //Harcanılan para ve genel para(eklenen paralarda dahil) toplamını kaydediyoruz
        editor.putString("sepet",String.valueOf(sepet_para)).commit();
        editor.putString("sepet_harcanan",String.valueOf(sepet_harcanan_para)).commit();

        //Kalan parayı bulmak için bu metodu çağırıyoruz ve listview verileri ekliyoruz.
        tum_para();


        ekle_listview();

        //Günlük kalan parayı kullanıcıya gösteriyoruz
        gunluk_kalan_para();


    }

    public void gunluk_kalan_para(){

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        //Kalan gün sayisini kalan zamana bölerek günlük harcanılacak parayı kullanıcıya gösteriyoruz


        double para;
        para= Double.parseDouble(prefSettings.getString("para","0"));
        gunluk_harcanacak_para =para/(gunsayisi-gunsay);

        if(para<=0){

            TextView_Yok.setVisibility(TextView_Yok.GONE);
            Textview_gunluk.setVisibility(Textview_gunluk.GONE);
            Textview_ParaGoster.setText("\n"+getString(R.string.paranizyok)+numberformat.format(para)+getString(R.string.Para_Birimi)+")");
        }
        else {
            TextView_Yok.setVisibility(TextView_Yok.VISIBLE);
            Textview_gunluk.setVisibility(Textview_gunluk.VISIBLE);
            TextView_Yok.setText(numberformat.format(gunluk_harcanacak_para)+getString(R.string.Para_Birimi));
            Textview_ParaGoster.setText(getString(R.string.paraniz)+" "+numberformat.format(para)+getString(R.string.Para_Birimi));
        }


    }


    public void para_ekle(SharedPreferences.Editor editor, SharedPreferences prefSettings, EditText eklenen_para){
        //Kullanıcı para eklediğinde bu metot çalışır.

        //Kullanıcı bir şekilde karakter girdiğinde uygulama çökmesin diye bir kontrol oluşturuyoruz.
        boolean kontrol_hata = false;


        try{
            //Girilen veri eger Integere başarılı bir şekilde dönüştürebiliyorsa sorun yok demektir.
            Double.parseDouble(eklenen_para.getText().toString());
        }
        catch (Exception ex){

            //Eger uygulamayı çöktürtecek bir veri girişi olursa(karakter) Toast mesajı veriyoruz.
            Toast.makeText(AnaActivity.this, getString(R.string.gecerlibirharcamagiriniz), Toast.LENGTH_SHORT).show();
            //Kontrol_hata değişkenini true yapıyoruz.
            kontrol_hata=true;

            Aletdialog_Para_Ekle();
        }

        if(!kontrol_hata){

            //Eger kullanıcı 0 veya bir şekilde negatif veri girerse buna engel oluyoruz ve edittextin içini boşaltıyoruz.
            if(!(Double.parseDouble(eklenen_para.getText().toString())<=0)){

                girilen_para= Double.parseDouble(eklenen_para.getText().toString());



                //gündüz akşam hesabı yapıyoruz.
                if (now.get(Calendar.AM_PM) == 0)
                    gündüz_gece[0] = getString(R.string.oglendenonce);
                else
                    gündüz_gece[0] = getString(R.string.oglendensonra);



                boolean kontrol;


                Calendar now = Calendar.getInstance();

                //SQLite ekliyoruz(Pozitif olarak)
                try {
                    kontrol = myDb.ekle(String.valueOf(girilen_para),String.valueOf(
                            day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                    ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));

                    myDb.ekle_kisitli_kalici(String.valueOf(girilen_para),String.valueOf(
                            day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                    ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));

                    myDb.ekle_kalici(String.valueOf(girilen_para),String.valueOf(
                            day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                    ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));


                }
                catch (Exception ex){
                    kontrol=false;
                }
                finally {
                    myDb.close();
                }

                //Kullanıcıya sonucu bildiriyoruz.
                if(kontrol == true) {
                    Toast.makeText(getApplicationContext(), R.string.kaydedildi,
                            Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(), R.string.kayitbasarisiz,
                            Toast.LENGTH_LONG).show();
                }

                Edittext_ParaGir.setText("");



                database_veri_cek(editor,prefSettings);

                para = Double.parseDouble(prefSettings.getString("para","0"));
                //Kalan günlük parayı kullanıcıya gösteriyoruz.
                gunluk_kalan_para();
            }
            else {

                Aletdialog_Para_Ekle();
            }

        }

    }

    public void Aletdialog_Para_Ekle(){

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        final String kalan_para = prefSettings.getString("para","0");
        final int gunsayisi = Integer.parseInt(prefSettings.getString("gunsayisi","0"));
        final int gunsay = Integer.parseInt(prefSettings.getString("gunsay","0"));
        String kalangun=String.valueOf(gunsayisi-gunsay);


        AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.para_duzenle_edittext, null);

        final EditText et= (EditText) dialoglayout.findViewById(R.id.para_duzenle);


        builder.setView(dialoglayout);

        String para;
        para= String.valueOf(numberformat.format(Double.parseDouble(prefSettings.getString("para","0"))));


        builder.setTitle(R.string.nekadareklemeyapacaksiniz);
        builder.setMessage(kalangun+" "+getString(R.string.günicindekalanparaniz)+" "+para+getString(R.string.Para_Birimi));


        builder.setPositiveButton(R.string.ekle,new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id){

                para_ekle(editor,prefSettings,et);

            }

        });


        builder.show();

    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana);



        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        idler();


        Edittext_ParaGir.addTextChangedListener(watcher);


        //Burda, zaman_kontrol_2 metodunu her 3 saniyede bir çağırıp gün geçmişmi diye kontrol ediyoruz.
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    AnaActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            zaman_kontrol_2(editor,prefSettings);

                        }
                    });

                }


            }, 0, 3000);

        //Kullanıcının belirttiği günsayisini aliyoruz.
            gunsayisi = Integer.parseInt(prefSettings.getString("gunsayisi","0"));
        //Gün geçip geçmediğini birkez daha kontrol ediyoruz.
            zaman_kontrol(editor,prefSettings);
        //Geçen gün sayisini alıyoruz.
            gunsay = Integer.parseInt(prefSettings.getString("gunsay","0"));

        //Eğer geçen gün sayisi kullanıcını belirttiği günü geçerse...
            if(gunsayisi<=gunsay){
                //telefona gundoldu_kontro isminde 2 verisini kaydediyoruz.Bunu kullanarak buraya girildiğii başka activitelerde öğrenebiliriz.
                editor.putString("gundoldu_kontrol","1").commit();
                Toast.makeText(getApplicationContext(),gunsayisi+getString(R.string.gündoldo),Toast.LENGTH_LONG).show();

                //Para activitesine yönlendiriyoruz.
                Intent intent = new Intent(AnaActivity.this,Para.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }


        //SQlitedewn verileri çekiyoruz.
            database_veri_cek(editor,prefSettings);
        //Kaydedilen parayı alıyoruz.

        para = Double.parseDouble(prefSettings.getString("para","0"));
        gunluk_kalan_para();



            Button_Sifirla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Sifirla butonuna basılırsa bütün verileri siliyoruz.


                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AnaActivity.this);

                    builder.setCancelable(false);
                    builder.setTitle(R.string.bütünkayıtlarınızsilinmekuzere);
                    builder.setMessage(R.string.silmeuyarisi);

                    builder.setNegativeButton(R.string.iptal,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){



                        }
                    });



                    builder.setPositiveButton(R.string.sil,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){


                            boolean kontrol = editor.clear().commit();
                            myDb.DeleteAllData();
                            myDb.DeleteGraph();
                            myDb.DeleteAllKalici();
                            myDb.DeleteAllAyrilanPara();

                            if(kontrol==true){
                                Toast.makeText(getApplicationContext(), R.string.eskiverilersilindi,Toast.LENGTH_SHORT).show();
                            }

                            Intent intent = new Intent(getApplicationContext(),Para.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });

                    builder.show();



                }
            });


            Button_Borclar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Borçlar butonuna tıklanınca Para_ayir activitesi çağırılıyor.
                    Intent i = new Intent(getApplicationContext(),Para_ayir.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
            });


            Button_Kayitlar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Kayıtlar butonuna tıklanınca Bütün kayıtlar kullanıcıya gösteriliyor

                    final ArrayList<String> dizi_para = new ArrayList();
                    final ArrayList<String> dizi_tarih = new ArrayList();
                    final ArrayList<String> dizi_saat = new ArrayList();
                    final ArrayList<String> dizi_mesaj = new ArrayList();


                    StringBuffer buffer = new StringBuffer();

                    final Cursor res = myDb.getAllData_kisitli_kalici();

                    double sepet_harcanilan_para=0;
                    double sepet_eklenen_para=0;
                    double sepet_tum_para=0;



                    if(res.getCount()==0){

                        buffer.append(getString(R.string.harcamayok));
                        goster_tum_kayitlar(editor,prefSettings,buffer.toString(),sepet_tum_para,sepet_harcanilan_para,sepet_eklenen_para);
                    }
                    else
                    {

                        //veriler bitene kadar çekiyoruz
                        while(res.moveToNext()){

                            if(Double.parseDouble(res.getString(0))<0)
                            {
                                sepet_harcanilan_para=sepet_harcanilan_para+Double.parseDouble(res.getString(0));
                            }
                            else
                            {
                                sepet_eklenen_para=sepet_eklenen_para+Double.parseDouble(res.getString(0));
                            }

                            sepet_tum_para=sepet_eklenen_para+Double.parseDouble(res.getString(0));


                                buffer.append(

                                        res.getString(0)+getString(R.string.Para_Birimi)+"\n"+
                                                res.getString(1)+"  "+
                                                res.getString(2)+"\n"+
                                                res.getString(4)+"\n\n"

                                );
                            }



                        goster_tum_kayitlar(editor,prefSettings,buffer.toString(),sepet_tum_para,sepet_harcanilan_para,sepet_eklenen_para);

                    }


                }
            });

            Button_ParaEkle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Aletdialog_Para_Ekle();

                }
            });



            Button_Sifre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Kullanıcı şifre oluşturmak,değiştirme veya kaldırmak isterse gerekli metotlar çalışır.


                    //Veritabanındaki şifreyi alıyoruz.Eğer yoksa 0 koyuyoruz.
                    String kontrol = prefSettings.getString("sifre", "0");


                    final AlertDialog.Builder builder = new AlertDialog.Builder(AnaActivity.this);

                    //Eğer şifre yoksa oluştur seçeneği çıkar.
                    if(kontrol.toString().trim().equals("0")){

                        builder.setTitle(R.string.parolaolustur);

                        builder.setPositiveButton(R.string.olustur,new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int id){


                                AnaActivity.this.builder = new AlertDialog.Builder(AnaActivity.this);
                                AnaActivity.this.builder.setTitle(R.string.parolaolustur);

                                LayoutInflater inflater = getLayoutInflater();
                                View dialoglayout = inflater.inflate(R.layout.parola_olustur, null);



                                parola_olustur = (EditText) dialoglayout.findViewById(R.id.parola_oluıstur);
                                parola_tekrar = (EditText) dialoglayout.findViewById(R.id.parola_tekrar);

                                parola_olustur.addTextChangedListener(parola_olustur_watcher);
                                parola_tekrar.addTextChangedListener(parola_tekrar_watcher);




                                AnaActivity.this.builder.setView(dialoglayout);
                                AnaActivity.this.builder.show();


                            }

                        });



                    }
                    //Eğer şifre varsa kaldır ve değiştir seçeneği çıkar.
                    else
                    {
                        builder.setTitle(R.string.paroladegis);

                        builder.setPositiveButton(R.string.degis,new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int id){

                                AlertDialog.Builder builder_değiş = new AlertDialog.Builder(AnaActivity.this);
                                builder_değiş.setTitle(R.string.paroladegis);

                                LayoutInflater inflater = getLayoutInflater();
                                View dialoglayout = inflater.inflate(R.layout.parola_degis, null);

                                parola_eski= (EditText) dialoglayout.findViewById(R.id.para_duzenle);
                                parola_yeni= (EditText) dialoglayout.findViewById(R.id.parola_yeni);
                                parola_yeni_tekrar= (EditText) dialoglayout.findViewById(R.id.parola_yeni_tekrar);


                                parola_eski.addTextChangedListener(parola_eski_watcher);

                                parola_yeni.addTextChangedListener(parola_yeni_watcher);

                                parola_yeni_tekrar.addTextChangedListener(parola_yeni_tekrar_watcher);


                                builder_değiş.setView(dialoglayout);
                                builder_değiş.show();

                            }

                        });
                        builder.setNegativeButton(R.string.kaldir,new DialogInterface.OnClickListener(){

                            public void onClick(DialogInterface dialog, int id){

                                AlertDialog.Builder builder_kaldır = new AlertDialog.Builder(AnaActivity.this);
                                builder_kaldır.setTitle(R.string.parolakaldir);


                                LayoutInflater inflater = getLayoutInflater();
                                View dialoglayout = inflater.inflate(R.layout.parola_kaldir, null);

                               parola_kaldir= (EditText) dialoglayout.findViewById(R.id.parola_kaldir);

                                parola_kaldir.addTextChangedListener(parola_kaldir_watcher);





                                builder_kaldır.setView(dialoglayout);
                                builder_kaldır.show();


                            }

                        });


                    }


                    builder.setMessage(" ");


                    builder.show();


                }
            });



            Button_Menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Menu butonuna tıklanınca iconu değişir ve animasyonlar ile menü görünür olur.

                    boolean i;



                    sayac_menü=sayac_menü+1;

                    if(sayac_menü%2==1){

                        Button_Menu.setBackgroundResource(R.drawable.menu_close);
                        TranslateAnimation animate = new TranslateAnimation(1000,0,0,0);
                        animate.setDuration(300);
                        animate.setFillAfter(false);
                        ConstraintLayout.startAnimation(animate);
                        ConstraintLayout.setVisibility(view.VISIBLE);




                        TranslateAnimation animate2 = new TranslateAnimation(0,0,0,10);
                        animate2.setDuration(80);
                        animate2.setFillAfter(false);
                        ListView.startAnimation(animate2);
                        Tablelayout.startAnimation(animate2);



                    }
                    else
                    {
                        Button_Menu.setBackgroundResource(R.drawable.menu);

                        TranslateAnimation animate = new TranslateAnimation(0,1000,0,0);
                        animate.setDuration(280);
                        animate.setFillAfter(false);
                        ConstraintLayout.startAnimation(animate);
                        ConstraintLayout.setVisibility(view.GONE);



                        TranslateAnimation animate2 = new TranslateAnimation(0,0, Tablelayout.getHeight()/2,0);
                        animate2.setDuration(400);
                        animate2.setFillAfter(true);
                        ListView.startAnimation(animate2);
                        Tablelayout.startAnimation(animate2);


                    }


                }
            });



            Button_Harca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //Kullanıcı bir şekilde karakter girdiğinde uygulama çökmesin diye bir kontrol oluşturuyoruz.
                    boolean kontrol_hata = false;


                    try{
                        //Girilen veri eger Integere başarılı bir şekilde dönüştürebiliyorsa sorun yok demektir.
                        Double.parseDouble(Edittext_ParaGir.getText().toString());
                    }
                    catch (Exception ex){

                        //Eger uygulamayı çöktürtecek bir veri girişi olursa(karakter) Toast mesajı veriyoruz.
                            Toast.makeText(AnaActivity.this, getString(R.string.gecerlibirharcamagiriniz), Toast.LENGTH_SHORT).show();
                        //Kontrol_hata değişkenini true yapıyoruz.
                        kontrol_hata=true;

                        Edittext_ParaGir.setText("");
                    }

                    //Eger kontrol_true değişkeni true değilse girilen veride sorun yok demektir ve koşul çalışır.
                    if(!kontrol_hata){

                        if(Double.parseDouble(Edittext_ParaGir.getText().toString())>0){

                            //Eğer harca butonuna tıklırsa girilen değer negative olarak SQlite a eklenir.
                            girilen_para= -1*Double.parseDouble(Edittext_ParaGir.getText().toString());

                            //gündüz akşam hesabı yapıyoruz.
                            if (now.get(Calendar.AM_PM) == 0)
                                gündüz_gece[0] = getString(R.string.oglendenonce);
                            else
                                gündüz_gece[0] =  getString(R.string.oglendensonra);



                            boolean kontrol;


                            Calendar now = Calendar.getInstance();


                            try {
                                kontrol = myDb.ekle(String.valueOf(girilen_para),String.valueOf(
                                        day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                                ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));

                                myDb.ekle_kisitli_kalici(String.valueOf(girilen_para),String.valueOf(
                                        day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                                ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));

                                myDb.ekle_kalici(String.valueOf(girilen_para),String.valueOf(
                                        day[now.get(Calendar.DATE) - 1] + "/" + month[now.get(Calendar.MONTH)] + "/" + String.valueOf(now.get(Calendar.YEAR))
                                ),String.valueOf(hour[now.get(Calendar.HOUR)] + ":" + minute[now.get(Calendar.MINUTE)] + ":" + second[now.get(Calendar.SECOND)] + " " + String.valueOf(gündüz_gece[0])),String.valueOf(""));



                            }
                            catch (Exception ex){
                                //Eger Sqlite ye veri yüklerken durdurucu bir hata olursa kontrol değiikenini false yaparak kullanıcıya Toast mesajı veriyoruz.
                                kontrol=false;

                            }
                            finally {
                                //Her ne olursa olsun veritabanını kapatıyoruz.
                                myDb.close();
                            }



                            //Databaseden gelen bilgiye göre kullanıcıya toast mesajı veriyoruz.
                            if(kontrol == true) {

                                Toast.makeText(getApplicationContext(), getString(R.string.kaydedildi),
                                        Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),getString(R.string.kayitbasarisiz),
                                        Toast.LENGTH_LONG).show();
                            }


                            database_veri_cek(editor,prefSettings);
                            Edittext_ParaGir.setText("");
                            gunluk_kalan_para();

                        }
                        else {
                            Edittext_ParaGir.setText("");
                        }

                    }


                }
            });


            ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {


                        harcama_yorum(position,editor,prefSettings);



                }
            });

            ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {


                    //Gerekli metodu çağırıyoruz.
                    sil_düzenle(position,editor,prefSettings);

                    return true;
                }
            });




    }



    TextWatcher watcher = new TextWatcher() {



        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();



            if(Edittext_ParaGir.getText().toString().trim().equals(".")){

                Edittext_ParaGir.setText("");
                Edittext_ParaGir.setBackgroundResource(R.drawable.button);
            }
            else if(!Edittext_ParaGir.getText().toString().trim().equals(""))
            {

                Para_Birimi.setVisibility(View.VISIBLE);

                Edittext_ParaGir.setBackgroundResource(R.drawable.pressbutton);

                String para;
                para= String.valueOf(Double.parseDouble(prefSettings.getString("para","0")));

                double gunluk_harcanacak_para =(Double.parseDouble(para)-Double.parseDouble(Edittext_ParaGir.getText().toString()))/(gunsayisi-gunsay);


                if(Double.parseDouble(para)-Double.parseDouble(Edittext_ParaGir.getText().toString())<=0){

                    TextView_Yok.setVisibility(TextView_Yok.GONE);
                    Textview_gunluk.setVisibility(Textview_gunluk.GONE);
                    Textview_ParaGoster.setText("\n"+getString(R.string.paranizyok)+numberformat.format(Double.parseDouble(para) -Double.parseDouble(Edittext_ParaGir.getText().toString()))+getString(R.string.Para_Birimi)+")");

                }
                else {
                    TextView_Yok.setVisibility(TextView_Yok.VISIBLE);
                    Textview_gunluk.setVisibility(Textview_gunluk.VISIBLE);
                    TextView_Yok.setText(numberformat.format(gunluk_harcanacak_para)+getString(R.string.Para_Birimi));
                    Textview_ParaGoster.setText(getString(R.string.paraniz)+" "+numberformat.format(Double.parseDouble(para) -Double.parseDouble(Edittext_ParaGir.getText().toString()))+getString(R.string.Para_Birimi));
                }



            }
            else if(Edittext_ParaGir.getText().toString().trim().equals(""))
            {
                Para_Birimi.setVisibility(View.GONE);


                String para;
                para= String.valueOf(Double.parseDouble(prefSettings.getString("para","0")));

                if(Double.parseDouble(para)<=0){

                    TextView_Yok.setVisibility(TextView_Yok.GONE);
                    Textview_gunluk.setVisibility(Textview_gunluk.GONE);
                    Textview_ParaGoster.setText("\n"+getString(R.string.paranizyok)+numberformat.format(Double.parseDouble(para))+getString(R.string.Para_Birimi)+")");

                }
                else {
                    TextView_Yok.setVisibility(TextView_Yok.VISIBLE);
                    Textview_gunluk.setVisibility(Textview_gunluk.VISIBLE);
                    TextView_Yok.setText(numberformat.format(gunluk_harcanacak_para)+getString(R.string.Para_Birimi));
                    Textview_ParaGoster.setText(getString(R.string.paraniz)+" "+numberformat.format(Double.parseDouble(para))+getString(R.string.Para_Birimi));
                }
                Edittext_ParaGir.setBackgroundResource(R.drawable.button);
            }


        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    TextWatcher parola_olustur_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(parola_olustur.getText().toString().length()==4){

                parola_tekrar.setVisibility(parola_tekrar.VISIBLE);
                parola_olustur.setFocusable(false);
                parola_tekrar.setFocusableInTouchMode(true);
                parola_tekrar.setFocusable(true);

            }
            else
            {
                parola_tekrar.setVisibility(parola_tekrar.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };



    TextWatcher parola_tekrar_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_olustur.getText().toString().equals(parola_tekrar.getText().toString())){

                Toast.makeText(AnaActivity.this, R.string.parolaolusturuldu, Toast.LENGTH_SHORT).show();

                editor.putString("sifre",parola_tekrar.getText().toString()).commit();

                Intent intent = new Intent(getApplicationContext(),Sifre.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_kaldir_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();


            if(parola_kaldir.getText().toString().trim().equals(prefSettings.getString("sifre","null"))){

                Toast.makeText(AnaActivity.this, R.string.parolakaldirildi, Toast.LENGTH_SHORT).show();
                editor.putString("sifre","0").commit();

                Intent intent = new Intent(getApplicationContext(),Tab.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
            else if(parola_kaldir.getText().toString().length()==4){

                parola_kaldir.setText("");

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    TextWatcher parola_eski_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_eski.getText().toString().trim().equals(prefSettings.getString("sifre","0"))){

                parola_yeni.setVisibility(parola_yeni.VISIBLE);
                parola_eski.setFocusable(false);
                parola_yeni.setFocusable(true);
                parola_yeni.setFocusableInTouchMode(true);

            }
            else
            {
                parola_yeni.setVisibility(parola_yeni.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_yeni_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(parola_yeni.getText().toString().length()==4){

                parola_yeni_tekrar.setVisibility(parola_yeni.VISIBLE);
                parola_yeni.setFocusable(false);
                parola_yeni_tekrar.setFocusable(true);
                parola_yeni_tekrar.setFocusableInTouchMode(true);
            }
            else
            {
                parola_yeni_tekrar.setVisibility(parola_yeni.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher parola_yeni_tekrar_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = prefSettings.edit();

            if(parola_yeni_tekrar.getText().toString().trim().equals(parola_yeni.getText().toString().trim())){

                Toast.makeText(AnaActivity.this, getString(R.string.paroladegistirildi)+parola_yeni_tekrar.getText().toString()+")", Toast.LENGTH_SHORT).show();
                editor.putString("sifre",parola_yeni_tekrar.getText().toString()).commit();

                Intent intent = new Intent(getApplicationContext(),Sifre.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


}
