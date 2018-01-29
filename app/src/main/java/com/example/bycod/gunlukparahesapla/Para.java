package com.example.bycod.gunlukparahesapla;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Para extends AppCompatActivity {

    EditText Edittext_ParaGir;
    RadioButton rb_aylik,rb_yillik,rb_haftalik,rb_ozel;
    RadioGroup Radiogroup;
    Button Button_Ileri;
    TextView Textview_Tarih_Mesaj, Textview_MesajHarcama, Para_Birimi;
    DatePicker dp;

    double sepet=0;
    double sepet_harcanan=0;
    double sepet_eklenen=0;
    double sepet_odenmeyen=0;



    final Calendar now = Calendar.getInstance();



    int kontrol_radiobutton=0;
    int yıl;
    int ay;
    int gun;

    int sayac_animation=0;

    int kayitay;
    int kayityıl;
    int kayitgun;

    long Fark_Tarih;



    final database myDb = new database(this);
    StringBuffer buffer = new StringBuffer();
    StringBuffer buffer_ayrilan_para = new StringBuffer();

    public void idler(){

        Edittext_ParaGir = (EditText) findViewById(R.id.Edittext_ParaGir);
        rb_aylik= (RadioButton) findViewById(R.id.rb_aylik);
        rb_haftalik= (RadioButton) findViewById(R.id.rb_haftalik);
        rb_yillik= (RadioButton) findViewById(R.id.rb_yillik);
        rb_ozel= (RadioButton) findViewById(R.id.ozel);
        Para_Birimi = (TextView) findViewById(R.id.Para_Birimi);
        Radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        Button_Ileri = (Button) findViewById(R.id.button_ileri);
        Textview_Tarih_Mesaj = (TextView) findViewById(R.id.Textview_tarih_mesaj);
        Textview_MesajHarcama = (TextView) findViewById(R.id.mesajharcama);

    }

    public void goster_ayrilan_para(){

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(Para.this);
        builder.setCancelable(false);

        builder.setTitle(
                R.string.buborclarodenmedi
        );

        builder.setMessage(
                buffer_ayrilan_para.toString()
        );

        builder.setPositiveButton(getString(R.string.tamam),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

                kayitlar(editor,prefSettings);

            }
        });

        builder.show();

    }

    public void goster(final SharedPreferences.Editor editor, SharedPreferences prefSettings){

        //Kullanıcıya alert dialogda harcama hareketlerni açıklamam ve tarihiyle beraber gösteiyoruz.

        AlertDialog.Builder builder = new AlertDialog.Builder(Para.this);

        int gunsayisi = Integer.parseInt(prefSettings.getString("gunsayisi","0"));
        String para = String.valueOf(Double.parseDouble(prefSettings.getString("para","0"))+sepet_odenmeyen);

        builder.setCancelable(false);
        builder.setTitle(
                String.valueOf(gunsayisi)+" "+getString(R.string.gundoldubusureicerisinde)+"\n"
        );

        if(sepet_harcanan==0 && sepet_eklenen==0){

            builder.setMessage(
                    getString(R.string.kalanpara)+" "+para+getString(R.string.Para_Birimi)+"\n\n"
                            +buffer.toString()
            );

        }
        else if(sepet_eklenen==0){

            builder.setMessage(
                            getString(R.string.hacanilanpara)+" "+-1*sepet_harcanan+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.kalanpara)+" "+para+getString(R.string.Para_Birimi)+"\n\n"
                            +buffer.toString()
            );

        }
        else if(sepet_harcanan==0){

            builder.setMessage(
                            getString(R.string.eklenenpara)+" "+sepet_eklenen+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.kalanpara)+" "+para+getString(R.string.Para_Birimi)+"\n\n"
                            +buffer.toString()
            );

        }
        else
        {

            builder.setMessage(
                    getString(R.string.hacanilanpara)+" "+-1*sepet_harcanan+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.eklenenpara)+" "+sepet_eklenen+getString(R.string.Para_Birimi)+"\n"
                            +getString(R.string.kalanpara)+" "+para+getString(R.string.Para_Birimi)+"\n\n"
                            +buffer.toString()
            );

        }






        builder.setPositiveButton(getString(R.string.tamam),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

                //Kullanıcı tamam butonuna tıklayınca verileri siliyoruz ve yeni bir başlangıç sunuyoruz.

                boolean kontrol = editor.clear().commit();
                myDb.DeleteAllData();
                if(kontrol==true){
                    Toast.makeText(getApplicationContext(),getString(R.string.eskiverilersilindi),Toast.LENGTH_SHORT).show();
                }

            }
        });


        builder.setNeutralButton(getString(R.string.grafik),new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

                Intent i = new Intent(getApplicationContext(),Grafik_kalanpara.class);
                startActivity(i);

                //eger grafik butonuna basılırsa veri_sifirlama_kontrol isminde 1 verisini kaydediyoruz.
                editor.putString("veri_sifirlama_kontrol","1").commit();


            }
        });

        builder.show();
    }

    public void kayitlar_ayrilan_para(){

        //SQLiteden ayrılan paraları çekiyoruz.

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        final Cursor res = myDb.getAllData_ayrilan_para();
        boolean kontrol=false;

        if(res.getCount()==0)
        {
            kayitlar(editor,prefSettings);
        }
        else
        {
            while (res.moveToNext()){

                if(Integer.parseInt(res.getString(3))==0){
                    //Eğer borç ödenmiş ise buffere kaydediyoruz ve sepete ekliyoruz.

                    buffer_ayrilan_para.append(

                            res.getString(0)+"₺\n"+
                                    res.getString(1)+"\n\n"

                    );

                    sepet_odenmeyen=sepet_odenmeyen+Double.parseDouble(res.getString(0));

                    kontrol=true;
                }



            }

            if(kontrol==true)
            {
                //Eger borç ödenmemiş ise gösteriyoruz.
                goster_ayrilan_para();
                kontrol=false;
            }
            else
            {
                kayitlar(editor,prefSettings);
            }


        }




    }


    public void kayitlar(SharedPreferences.Editor editor, SharedPreferences prefSettings){

        final Cursor res = myDb.getAllData_kisitli_kalici();


        if(res.getCount()==0){
            //Harcama yoksa harcama yok mesajı veriyouz
            buffer.append(getString(R.string.harcamayok));
            goster(editor,prefSettings);
        }
        else
        {

            //SQLite kalıcı table'sindeki verileri çekiyoruz ve buffer e kaydediyoruz.
            while(res.moveToNext()){

                sepet=sepet+Double.parseDouble(res.getString(0));

                if(Double.parseDouble(res.getString(0))<0){

                    sepet_harcanan=sepet_harcanan+Double.parseDouble(res.getString(0));

                }
                else
                {
                    sepet_eklenen=sepet_eklenen+Double.parseDouble(res.getString(0));
                }


                    buffer.append(
                                    res.getString(0)+getString(R.string.Para_Birimi)+"\n"+
                                    res.getString(1)+" "+
                                    res.getString(2)+"\n"+
                                    res.getString(4)+"\n\n"

                    );
            }

            goster(editor,prefSettings);

        }



    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para);

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();



        idler();

        Edittext_ParaGir.addTextChangedListener(watcher);

        String menu_paragir = prefSettings.getString("gundoldu_kontrol","0");

        //Eger kullanıcının belirttiği süre dolmuş ise..
        if(menu_paragir.trim().equals("1")){

            kayitlar_ayrilan_para();




        }



            ay = now.get(Calendar.MONTH);
            yıl = now.get(Calendar.YEAR);
            gun = now.get(Calendar.DATE);


            kayitay = now.get(Calendar.MONTH);
            kayityıl = now.get(Calendar.YEAR);
            kayitgun = now.get(Calendar.DATE);

        //radio buton özelliklerini veriyoruz.

            rb_ozel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final DatePickerDialog datePicker = new DatePickerDialog(Para.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int yıl1, int ay1, int gun1) {


                            kayitay = ay1;
                            kayityıl = yıl1;
                            kayitgun = gun1;

                            Date IlkGun = new GregorianCalendar(yıl, ay, gun, 00, 00).getTime();
                            Date SonGun = new GregorianCalendar(kayityıl, kayitay, kayitgun, 00, 00).getTime();

                            Fark_Tarih = SonGun.getTime() - IlkGun.getTime();
                            Fark_Tarih = Fark_Tarih / (1000 * 60 * 60 * 24);

                            if (Fark_Tarih < 0) {
                                Textview_Tarih_Mesaj.setText(R.string.ileritarihsec);
                            } else if (Fark_Tarih == 0) {
                                Textview_Tarih_Mesaj.setText("");
                            } else {
                                Textview_Tarih_Mesaj.setText(String.valueOf(Fark_Tarih) + " "+getString(R.string.gun));
                            }


                        }
                    }, kayityıl, kayitay, kayitgun);//başlarken set edilcek değerlerimizi atıyoruz
                    datePicker.setTitle(getString(R.string.tarihsec));
                    datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.tarihayarla), datePicker);

                    datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.iptal), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            rb_haftalik.setChecked(false);


                        }
                    });
                    datePicker.setCancelable(false);
                    datePicker.show();

                    kontrol_radiobutton = 1;


                }
            });




            Radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                    int rgid = Radiogroup.getCheckedRadioButtonId();

                    Fark_Tarih = 0;


                    if (rgid == R.id.rb_yillik) {

                        Date IlkGun = new GregorianCalendar(yıl, ay, gun, 00, 00).getTime();
                        Date SonGun = new GregorianCalendar(yıl + 1, ay, gun, 00, 00).getTime();

                        Fark_Tarih = SonGun.getTime() - IlkGun.getTime();
                        Fark_Tarih = Fark_Tarih / (1000 * 60 * 60 * 24);

                        kontrol_radiobutton = 1;


                    } else if (rgid == R.id.rb_aylik) {

                        Date IlkGun = new GregorianCalendar(yıl, ay, gun, 00, 00).getTime();
                        Date SonGun = new GregorianCalendar(yıl, ay + 1, gun, 00, 00).getTime();

                        Fark_Tarih = SonGun.getTime() - IlkGun.getTime();
                        Fark_Tarih = Fark_Tarih / (1000 * 60 * 60 * 24);
                        kontrol_radiobutton= 1;


                    } else if (rgid == R.id.rb_haftalik) {

                        Date IlkGun = new GregorianCalendar(yıl, ay, gun, 00, 00).getTime();
                        Date SonGun = new GregorianCalendar(yıl, ay, gun + 7, 00, 00).getTime();


                        Fark_Tarih = SonGun.getTime() - IlkGun.getTime();
                        Fark_Tarih = Fark_Tarih / (1000 * 60 * 60 * 24);
                        kontrol_radiobutton = 1;


                    }


                    if (Fark_Tarih < 0) {
                        Textview_Tarih_Mesaj.setText(getString(R.string.ileritarihsec));
                    } else if (Fark_Tarih == 0) {
                        Textview_Tarih_Mesaj.setText("");
                    } else {
                        Textview_Tarih_Mesaj.setText(String.valueOf(Fark_Tarih) + " "+getString(R.string.gun));
                    }


                }
            });


            Button_Ileri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (Edittext_ParaGir.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), R.string.lutfenparagir, Toast.LENGTH_SHORT).show();
                    } else {
                        if (kontrol_radiobutton == 0) {
                            Toast.makeText(getApplicationContext(), R.string.lutfensecimyapiniz, Toast.LENGTH_SHORT).show();
                        } else {
                            if (Fark_Tarih <= 0) {
                                Toast.makeText(getApplicationContext(), getString(R.string.ileritarihsec), Toast.LENGTH_SHORT).show();
                            } else {


                                editor.putString("gunsayisi", String.valueOf(Fark_Tarih));
                                editor.putString("para", Edittext_ParaGir.getText().toString());
                                editor.putString("kayitpara", Edittext_ParaGir.getText().toString());
                                editor.putString("para_borc", Edittext_ParaGir.getText().toString());
                                editor.putString("para_grafik", Edittext_ParaGir.getText().toString());


                                int ay = now.get(Calendar.MONTH);
                                int yıl = now.get(Calendar.YEAR);
                                int gun = now.get(Calendar.DATE);


                                editor.putString("ay", String.valueOf(ay));
                                editor.putString("gun", String.valueOf(gun));
                                editor.putString("yil", String.valueOf(yıl));


                                editor.commit();

                                Intent intent = new Intent(getApplicationContext(), Para_ayir_sorusu.class);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }


                        }
                    }
                }
            });

    }


    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            final NumberFormat nf = NumberFormat.getInstance();


            if(Edittext_ParaGir.getText().toString().trim().equals("."))
            {
                Edittext_ParaGir.setText("");
            }
            else if(!Edittext_ParaGir.getText().toString().trim().equals("")){

                Para_Birimi.setVisibility(View.VISIBLE);

                Edittext_ParaGir.setBackgroundResource(R.drawable.pressbutton);

                if(sayac_animation==0){



                    Radiogroup.setVisibility(Radiogroup.VISIBLE);

                    //Animasyon ekliyoruz

                    TranslateAnimation animate1 = new TranslateAnimation(1000,0,0,0);
                    animate1.setDuration(800);


                    TranslateAnimation animate2 = new TranslateAnimation(1000,0,0,0);
                    animate2.setDuration(700);


                    TranslateAnimation animate3 = new TranslateAnimation(1000,0,0,0);
                    animate3.setDuration(600);


                    TranslateAnimation animate4 = new TranslateAnimation(1000,0,0,0);
                    animate4.setDuration(500);


                    rb_yillik.startAnimation(animate4);
                    rb_aylik.startAnimation(animate3);
                    rb_haftalik.startAnimation(animate2);
                    rb_ozel.startAnimation(animate1);

                    sayac_animation=sayac_animation+1;




                }

            }
            else
            {
                Para_Birimi.setVisibility(View.GONE);
                Edittext_ParaGir.setBackgroundResource(R.drawable.buttonstyle);
            }



        }

        @Override
        public void afterTextChanged(Editable editable) {


        }
    };

}

