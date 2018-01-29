package com.example.bycod.gunlukparahesapla;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class Para_ayir extends AppCompatActivity {

    int kontrol=0;

    ListView Listview;
    Button Button_ekle, Button_geri;
    TextView tv, Textview_Para;

    final database myDb = new database(this);

    ArrayList<String> dizi_para = new ArrayList();
    ArrayList<String> dizi_mesaj = new ArrayList();
    ArrayList<String> dizi_kontrol = new ArrayList();
    ArrayList<String> dizi_id = new ArrayList();



    String para;
    String mesaj;
    int sayac=0;

    double sepet_odenmeyen=0;
    double sepet_odenen=0;
    double kalan_para_kontrol=0;

    final NumberFormat nf = NumberFormat.getInstance();

    public void para_ayir(){


        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Para_ayir.this);

        builder.setTitle(R.string.paraayir);

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.borc_ekle_edittext, null);

        final EditText et_mesaj= (EditText) dialoglayout.findViewById(R.id.borc_mesaji);
        final EditText et_para= (EditText) dialoglayout.findViewById(R.id.borc_para);

        builder.setView(dialoglayout);


        builder.setPositiveButton(getString(R.string.ekle),new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int id) {

                if(!et_para.getText().toString().trim().equals(""))
                {

                    kalan_para_kontrol= Double.parseDouble(prefSettings.getString("para", "0"))-Double.valueOf(et_para.getText().toString());

                }


                if(et_para.getText().toString().trim().equals("")){

                    Toast.makeText(Para_ayir.this, R.string.boskalamaz, Toast.LENGTH_SHORT).show();
                    para_ayir();

                }
                else if (kalan_para_kontrol<=0) {


                    Toast.makeText(Para_ayir.this, R.string.yeterliparanizyok, Toast.LENGTH_SHORT).show();
                    para_ayir();

                }
                else {



                    boolean r;

                    String mesaj=et_mesaj.getText().toString();

                    if(et_mesaj.getText().toString().trim().equals(""))
                    {
                        mesaj="----------";
                    }


                    try {
                        r = myDb.ekle_ayrilan_para(et_para.getText().toString(),mesaj, "0");
                    } finally {
                        myDb.close();
                    }

                    if (r == false) {

                        Toast.makeText(Para_ayir.this, R.string.hata, Toast.LENGTH_SHORT).show();

                    } else if (r == true) {

                        Toast.makeText(Para_ayir.this, R.string.eklendi, Toast.LENGTH_SHORT).show();

                    }



                    Double para = Double.parseDouble(prefSettings.getString("kayitpara", "0"));

                    para = para - Double.parseDouble(et_para.getText().toString());

                    editor.putString("kayitpara", String.valueOf(para)).commit();

                    tum_para();
                    veri_al();
                    ekle();

                }


            }



        });





        builder.show();

    }


    public void tum_para(){

        //butun parayı çekip sepete atıyoruz.


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

        double para= Double.parseDouble(prefSettings.getString("kayitpara","0"))+sepet_tum_para;

        editor.putString("para",String.valueOf(para)).commit();



    }



    public void ekle(){




        final List<Bilgi_para_ayir> dizi= new ArrayList<Bilgi_para_ayir>();
        final OzelAdapter_para_ayir adaptorumuz = new OzelAdapter_para_ayir(this,dizi);



            for(int say=0; say<dizi_para.size(); say++){


                if(Integer.parseInt(dizi_kontrol.get(say)) ==1){

                    dizi.add(new Bilgi_para_ayir(nf.format(Double.parseDouble(dizi_para.get(say)))+getString(R.string.Para_Birimi),dizi_mesaj.get(say),"1"));

                }
                else
                {

                    dizi.add(new Bilgi_para_ayir(nf.format(Double.parseDouble(dizi_para.get(say)))+getString(R.string.Para_Birimi),dizi_mesaj.get(say),"0"));

                }

            }



        Listview.setAdapter(adaptorumuz);

    }

    public void veri_al(){


        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        final Cursor res = myDb.getAllData_ayrilan_para();

        if(res.getCount()==0){

            dizi_para.clear();
            dizi_id.clear();
            dizi_kontrol.clear();
            dizi_mesaj.clear();


            Textview_Para.setText(getString(R.string.sizekalanpara)+" "+nf.format(Double.parseDouble(prefSettings.getString("para","0")))+getString(R.string.Para_Birimi)+"\n"
                    +getString(R.string.ayrilanpara)+" "+nf.format(Double.parseDouble(String.valueOf(sepet_odenmeyen)))+getString(R.string.Para_Birimi)+"\n"
                    +getString(R.string.odenenpara)+" "+nf.format(Double.parseDouble(String.valueOf(sepet_odenen)))+getString(R.string.Para_Birimi)
            );

            editor.putString("sayac_paraayir","0");
            editor.commit();


        }
        else
        {

            dizi_para.clear();
            dizi_id.clear();
            dizi_kontrol.clear();
            dizi_mesaj.clear();

            sepet_odenmeyen=0;
            sepet_odenen=0;



            while(res.moveToNext()){




                if(res.getString(0).trim().equals(""))
                {
                    dizi_para.add(res.getString(0));
                    dizi_mesaj.add(res.getString(1));
                    dizi_id.add(res.getString(2));
                    dizi_kontrol.add(res.getString(3));
                }
                //dizinin ilk indiksi dolu ise verimizi sıfırıncı indiksden sonra koyuyoruz.Bu şekilde yeni veriler en üstde olur
                else
                {
                    dizi_para.add(0,res.getString(0));
                    dizi_mesaj.add(0,res.getString(1));
                    dizi_id.add(0,res.getString(2));
                    dizi_kontrol.add(0,res.getString(3));

                }


                if(res.getString(3).toString().trim().equals("1")){

                    sepet_odenen=sepet_odenen+Double.valueOf(res.getString(0));

                }
                else {
                    sepet_odenmeyen=sepet_odenmeyen+Double.valueOf(res.getString(0));
                }



            }




            Textview_Para.setText(getString(R.string.sizekalanpara)+" "+nf.format(Double.parseDouble(prefSettings.getString("para","0")))+getString(R.string.Para_Birimi)+"\n"
                    +getString(R.string.ayrilanpara)+" "+nf.format(Double.parseDouble(String.valueOf(sepet_odenmeyen)))+getString(R.string.Para_Birimi)+"\n"
                    +getString(R.string.odenenpara)+" "+nf.format(Double.parseDouble(String.valueOf(sepet_odenen)))+getString(R.string.Para_Birimi)
            );


            sepet_odenen=0;
            sepet_odenmeyen=0;

        }




    }

    public  void ode(final int i){

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Para_ayir.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.para_duzenle_edittext, null);

        final EditText et = (EditText) dialoglayout.findViewById(R.id.para_duzenle);


        builder.setView(dialoglayout);


        builder.setTitle(R.string.odemenizitekrargiriniz);
        builder.setMessage("");


        builder.setPositiveButton(R.string.ode, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                boolean kontrol=false;

                try {

                    Double.parseDouble(et.getText().toString());

                }catch (Exception ex){
                    kontrol=true;
                    ode(i);
                }

                if(!kontrol)
                {

                    if(!(Double.parseDouble(et.getText().toString())<=0))
                    {
                        if (Integer.valueOf(dizi_kontrol.get(i)) == 1) {
                            myDb.updateKontrol(dizi_id.get(i).toString(), "0");
                        } else {
                            myDb.updateKontrol(dizi_id.get(i).toString(), "1");
                        }

                        myDb.updateParaAyrilan(dizi_id.get(i), et.getText().toString());

                        Double para = Double.parseDouble(prefSettings.getString("kayitpara", "0"));

                        para = para + Double.parseDouble(dizi_para.get(i)) - Double.parseDouble(et.getText().toString());

                        editor.putString("kayitpara", String.valueOf(para)).commit();


                        tum_para();

                        veri_al();

                        ekle();
                    }


                }

            }

        });


        builder.show();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_ayir);





        Listview = (ListView) findViewById(R.id.Listview);
        Button_ekle = (Button) findViewById(R.id.Button_Ekle);
        Button_geri = (Button) findViewById(R.id.Button_Geri);
        Textview_Para = (TextView) findViewById(R.id.Textview_Para);


        tum_para();

        veri_al();

        ekle();





        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        Button_geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ana activitiye dönmek için

                Intent i = new Intent(getApplicationContext(),Tab.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });


        Button_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Kullanıcı para ayırmak isterse..
              para_ayir();

            }
        });



        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {





                    if (dizi_kontrol.get(i).toString().trim().equals("0")) {

                        ode(i);


                    } else {

                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Para_ayir.this);
                        builder.setTitle(R.string.gerial);


                        builder.setPositiveButton(R.string.evet, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {


                                if (Integer.valueOf(dizi_kontrol.get(i)) == 1) {
                                    myDb.updateKontrol(dizi_id.get(i).toString(), "0");
                                } else {
                                    myDb.updateKontrol(dizi_id.get(i).toString(), "1");
                                }


                                veri_al();

                                ekle();


                            }

                        });


                        builder.setNegativeButton(R.string.hayir, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {


                            }

                        });


                        builder.show();


                    }




            }

        });


        Listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {






                if (Integer.parseInt(dizi_kontrol.get(i)) == 0) {




                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Para_ayir.this);
                    builder.setTitle(R.string.kaldirma);
                    builder.setCancelable(false);


                    builder.setPositiveButton(getString(R.string.sil), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {



                            Integer deletedRows = myDb.deleteData_AyrilanPara(dizi_id.get(i));

                            if (deletedRows > 0) {
                                Toast.makeText(getApplicationContext(), R.string.silinde,
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.silmebasarisiz,
                                        Toast.LENGTH_LONG).show();
                            }

                            Double para = Double.parseDouble(prefSettings.getString("kayitpara", "0"));

                            para = para + Double.parseDouble(dizi_para.get(i));

                            editor.putString("kayitpara", String.valueOf(para)).commit();


                            tum_para();

                            veri_al();

                            ekle();

                        }


                    });

                    builder.setNegativeButton(getString(R.string.hayir), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {


                        }


                    });


                    builder.show();

                }

                return true;
            }
        });



    }

}
