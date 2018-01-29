package com.example.bycod.gunlukparahesapla;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.angle;


public class Sifre extends AppCompatActivity {

    TextView b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,b_sil,b_iptal;
    EditText et1,et2,et3,et4;



    int  sayac=0;
    int kontrol=0;

    int dizi[] = new int[4];

    int sifre[] =new int[4];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final SharedPreferences prefSettings = getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        int verikontrol= Integer.parseInt(prefSettings.getString("gunsayisi","0"));


        if(verikontrol==0){


            setContentView(R.layout.acilis_ekran);




            Thread thread=  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            wait(1000);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    Intent i = new Intent(getApplicationContext(),Para.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            };

            thread.start();

        }
        else if (prefSettings.getString("sifre", "0").toString().trim().equals("0")) {

            setContentView(R.layout.acilis_ekran);


                 Thread thread=  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            wait(1000);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    Intent i = new Intent(getApplicationContext(),Tab.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            };

            thread.start();


    }
        else {




            setContentView(R.layout.activity_sifre);

        b1 = (TextView) findViewById(R.id.button1);
        b2 = (TextView) findViewById(R.id.button2);
        b3 = (TextView) findViewById(R.id.button3);
        b4 = (TextView) findViewById(R.id.button4);
        b5 = (TextView) findViewById(R.id.button5);
        b6 = (TextView) findViewById(R.id.button6);
        b7 = (TextView) findViewById(R.id.button7);
        b8 = (TextView) findViewById(R.id.button8);
        b9 = (TextView) findViewById(R.id.button9);
        b_sil = (TextView) findViewById(R.id.button_sil);
        b_iptal = (TextView) findViewById(R.id.button_iptal);
        b0 = (TextView) findViewById(R.id.button0);

        et1 = (EditText) findViewById(R.id.sifre_editText1);

        et2 = (EditText) findViewById(R.id.sifre_editText2);

        et3 = (EditText) findViewById(R.id.sifre_editText3);

        et4 = (EditText) findViewById(R.id.sifre_editText4);


        int sifre_al = Integer.parseInt(prefSettings.getString("sifre", "0"));

        int i = 1; //Yazdırılacak olan değişken.
        int a = 1; //Basamak değeri.
        int say = 3;

        while (sifre_al != 0) {
            i = sifre_al % 10;
            sifre_al = sifre_al / 10;
            System.out.println(i * a);
            a = a * 10;

            sifre[say] = i;

            say = say - 1;
        }


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 1;

                sayac += 1;
                sonraki("1");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);


            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 2;
                sayac += 1;
                sonraki("2");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 3;
                sayac += 1;
                sonraki("3");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 4;
                sayac += 1;
                sonraki("4");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 5;
                sayac += 1;
                sonraki("5");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);


            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 6;
                sayac += 1;
                sonraki("6");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 7;
                sayac += 1;
                sonraki("7");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 8;
                sayac += 1;
                sonraki("8");


                Vibrator v
                        = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 9;
                sayac += 1;
                sonraki("9");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });

        b_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sil();

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(50);
            }
        });

        b_iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                System.exit(-1);


            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dizi[sayac] = 0;
                sayac += 1;
                sonraki("0");

                Vibrator v = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                v.vibrate(60);

            }
        });


    }

    }



    public void sonraki(String sayi){

        if(sayac==1){

            et1.setFocusable(false);et1.setText(sayi);
            et2.setFocusableInTouchMode(true);
            et2.setFocusable(true);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }
        else if(sayac==2)
        {
            et1.setFocusable(false);et1.setText("*");
            et2.setFocusable(false);et2.setText(sayi);
            et3.setFocusableInTouchMode(true);
            et3.setFocusable(true);
            et4.setFocusable(false);
        }
        else if(sayac==3){

            et1.setFocusable(false);
            et2.setFocusable(false);et2.setText("*");
            et3.setFocusable(false);et3.setText(sayi);
            et4.setFocusableInTouchMode(true);
            et4.setFocusable(true);

        }
        else if(sayac==4){

            et3.setText("*");
            et4.setText(String.valueOf(sayi));

            for(int say=0; say<4; say++)
            {

                if(dizi[say]!=sifre[say]){

                    sayac=0;

                    et1.setFocusable(true);
                    et1.setFocusableInTouchMode(true);
                    et2.setFocusable(false);
                    et3.setFocusable(false);
                    et4.setFocusable(false);

                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    et4.setText("");

                   kontrol=1;break;

                }

            }

            if(kontrol==1){

                kontrol=0;

                Toast.makeText(this, R.string.parolahatali, Toast.LENGTH_SHORT).show();

            }
            else
            {
                Thread thread=  new Thread(){
                    @Override
                    public void run(){
                        try {
                            synchronized(this){
                                wait(100);
                            }
                        }
                        catch(InterruptedException ex){
                        }

                        Intent i = new Intent(getApplicationContext(),Tab.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                };

                thread.start();
            }



        }




    }

    public void onceki(String sayi){


        if(sayac==0){

            et1.setFocusable(true);et1.setText(sayi);
            et1.setFocusableInTouchMode(true);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }
        else if(sayac==1){

            et1.setFocusable(false);
            et2.setFocusable(true);et2.setText(sayi);
            et2.setFocusableInTouchMode(true);
            et3.setFocusable(false);
            et4.setFocusable(false);

        }
        else if(sayac==2)
        {
            et1.setFocusable(false);
            et2.setFocusable(false);et3.setText(sayi);
            et3.setFocusable(true);
            et3.setFocusableInTouchMode(true);
            et4.setFocusable(false);

        }
        else if(sayac==3){

            et1.setFocusable(false);
            et2.setFocusable(false);
            et3.setFocusable(false);
            et3.setFocusableInTouchMode(true);
            et4.setFocusable(true);

        }





    }

    public void sil(){

        if(sayac!=0)
        {
            sayac-=1;
            onceki("");
        }





    }


}
