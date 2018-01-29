package com.example.bycod.gunlukparahesapla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Kayitlar extends AppCompatActivity {


    ListView lv;
    TextView sonpara;

    final NumberFormat nf = NumberFormat.getInstance();


    double sepet_harcanilan =0;
    double sepet_eklenen=0;

    final database myDb = new database(this);

    final ArrayList<String> dizi_para = new ArrayList();
    final ArrayList<String> dizi_tarih = new ArrayList();
    final ArrayList<String> dizi_saat = new ArrayList();
    final ArrayList<String> dizi_acıklama = new ArrayList();




    public void goster(){

        final List<Bilgi> dizi= new ArrayList();
        final OzelAdapter adaptorumuz = new OzelAdapter(this,dizi);


        if(dizi_para.size()!=0){

            for(int say=0; say<dizi_para.size(); say++){

                dizi.add(new Bilgi(dizi_para.get(say),dizi_tarih.get(say),dizi_saat.get(say)));


            }

            lv.setAdapter(adaptorumuz);


        }
    }


    public void harcama_acıklama(final int position, final SharedPreferences.Editor editor, final SharedPreferences prefSettings){
        //Harcanılan parada eğer herhangi bir açıklama varsa düzenleme ve kaldır seçeneği sunulur, yoksa açıklama ekleme imkanı sunulur.


        if(!dizi_para.get(0).trim().equals("")) {



            if (dizi_acıklama.get(position).toString().trim().equals("")) {
                //Açıklamam yoksa burası çalışır.
                Toast.makeText(this, R.string.aciklamayok, Toast.LENGTH_SHORT).show();

            }
            else {
                //Açıklama varsa burası çalışır.
                AlertDialog.Builder builder = new AlertDialog.Builder(Kayitlar.this);


                builder.setMessage(dizi_acıklama.get(position));


                builder.setPositiveButton(R.string.tamam, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {



                    }

                });



                builder.show();

            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitlar);

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

       int gunsayisi = Integer.parseInt(prefSettings.getString("gunsayisi","0"));
       int gunsay = Integer.parseInt(prefSettings.getString("gunsay","0"));
        String kalangun;

        sonpara= (TextView) findViewById(R.id.Textview_Para);
        lv= (ListView) findViewById(R.id.Listview);



        //Verileri çekip diziye koyuyoruz ve diziyi adapter yardımı ile list viewde gösteriyoruz.

        //Verileri çekip diziye koyuyoruz ve diziyi adapter yardımı ile list viewde gösteriyoruz.

        final Cursor res = myDb.getAllData_kalici();

        sepet_eklenen=0;
        sepet_harcanilan=0;

        if(res.getCount()==0){

            dizi_para.clear();
            dizi_para.add(0,"");

            dizi_tarih.clear();
            dizi_tarih.add(0,"");

            dizi_saat.clear();
            dizi_saat.add(0,"");

        }
        else
        {
            //veri çekmeden önce diziyi sıfırlıyoruz.
            dizi_para.clear();
            dizi_tarih.clear();
            dizi_saat.clear();


            //veriler bitene kadar çekiyoruz
            while(res.moveToNext()){



                if( Double.parseDouble(res.getString(0))<0)
                {
                    sepet_harcanilan=sepet_harcanilan +  Double.parseDouble(res.getString(0));
                }
                else
                {
                    sepet_eklenen =sepet_eklenen +  Double.parseDouble(res.getString(0));
                }


                //Eger dizinin sıfırıncı indeksi boş ise verimizi sıfırıncı indekse koyuyoruz.
                if(res.getString(0).trim().equals(""))
                {

                    dizi_para.add( res.getString(0)+getString(R.string.Para_Birimi) );
                    dizi_tarih.add( res.getString(1) );
                    dizi_saat.add( res.getString(2) );
                    dizi_acıklama.add( res.getString(4) );


                }
                //dizinin ilk indiksi dolu ise verimizi sıfırıncı indiksden sonra koyuyoruz.Bu şekilde yeni veriler en üstde olur
                else
                {
                    dizi_para.add(0,res.getString(0)+getString(R.string.Para_Birimi) );
                    dizi_tarih.add(0,res.getString(1) );
                    dizi_saat.add(0,res.getString(2) );
                    dizi_acıklama.add(0,res.getString(4) );


                }


            }


        }

        goster();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!dizi_para.get(position).toString().trim().equals(""))
                {
                    harcama_acıklama(position,editor,prefSettings);
                }


            }
        });


            if((gunsayisi-gunsay)==1)
            {
                kalangun="";
            }
            else
            {
                kalangun=String.valueOf(gunsayisi-gunsay);
            }



        if(sepet_harcanilan==0 && sepet_eklenen!=0){

            sonpara.setText(getString(R.string.eklenenpara)+" "+sepet_eklenen+"\n"
            );

        }
        else if(sepet_eklenen==0 && sepet_harcanilan!=0){

            sonpara.setText(getString(R.string.hacanilanpara)+" "+-1*sepet_harcanilan+"\n"
            );

        }
        else
        {
            sonpara.setText(getString(R.string.hacanilanpara)+" "+-1*sepet_harcanilan+"\n"+getString(R.string.eklenenpara)+" "+sepet_eklenen+"\n"
            );

        }





    }
}
