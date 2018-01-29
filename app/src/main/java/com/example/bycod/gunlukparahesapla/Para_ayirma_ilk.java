package com.example.bycod.gunlukparahesapla;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
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


public class Para_ayirma_ilk extends AppCompatActivity {

    int kontrol=0;

    ListView Listview;
    Button Button_harca, Button_Ileri;
    EditText Edittext_Ayir;
    TextView Textview_Para;

    final database myDb = new database(this);

    ArrayList<String> dizi_para = new ArrayList();
    ArrayList<String> dizi_mesaj = new ArrayList();
    ArrayList<String> dizi_kontrol = new ArrayList();
    ArrayList<String> dizi_id = new ArrayList();


    String para;
    String mesaj;
    int sayac_button =0;


    double sepet_ayrilan_para=0.0;


    final NumberFormat nf = NumberFormat.getInstance();


    public void ekle_listview(){
        //Verileri listviewe ekliyoruz ve gösteriyoruz.

        final List<Bilgi_para_ayir> dizi= new ArrayList<Bilgi_para_ayir>();
        final OzelAdapter_para_ayir adaptorumuz = new OzelAdapter_para_ayir(this,dizi);


            for(int say=0; say<dizi_para.size(); say++){

                    dizi.add(new Bilgi_para_ayir(nf.format(Double.parseDouble(dizi_para.get(say)))+getString(R.string.Para_Birimi),dizi_mesaj.get(say),"0"));

            }


        Listview.setAdapter(adaptorumuz);

    }

    public void veri_al(){

        //Verileri SQLiteden çekiyoruz.

        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        final Cursor res = myDb.getAllData_ayrilan_para();

        //Veri çekmeden önce dizileri ve ayrılan paranın sepetini sıfırlıyoruz.
        sepet_ayrilan_para=0;

        dizi_para.clear();
        dizi_id.clear();
        dizi_kontrol.clear();
        dizi_mesaj.clear();

        if(res.getCount()==0){


        }
        else
        {

            while(res.moveToNext()){

                sepet_ayrilan_para=sepet_ayrilan_para +  Double.parseDouble(res.getString(0));


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




            }

            //Ayrılan Para sepetini kaydediyoruz.
            editor.putString("sepet_ayrilan_para",String.valueOf(sepet_ayrilan_para)).commit();


        }

        //Kullanıcıya gerekli bilgileri gösteriyoruz.
        Textview_Para.setText(getString(R.string.paraniz)+" "+nf.format(Double.parseDouble(prefSettings.getString("para_borc","0")))+getString(R.string.Para_Birimi)+"\n"
                +getString(R.string.ayrilanpara)+" "+nf.format(sepet_ayrilan_para)+getString(R.string.Para_Birimi)+"\n"
                +getString(R.string.sizekalanpara)+" "+nf.format(Double.parseDouble(prefSettings.getString("para_borc","0"))-sepet_ayrilan_para)+getString(R.string.Para_Birimi)
        );

        //Gerekli kayıtları yapıyoruz.

        editor.putString("para",String.valueOf(Double.parseDouble(prefSettings.getString("para_borc","0"))-sepet_ayrilan_para)).commit();
        editor.putString("kayitpara", String.valueOf(Double.parseDouble(prefSettings.getString("para_borc","0"))-sepet_ayrilan_para)).commit();
        editor.putString("para_grafik", String.valueOf(Double.parseDouble(prefSettings.getString("para_borc","0"))-sepet_ayrilan_para)).commit();




    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_para_ayirma_ilk);





        Listview = (ListView) findViewById(R.id.Listview);
        Button_harca = (Button) findViewById(R.id.Button_Harca);
        Button_Ileri = (Button) findViewById(R.id.Button_Ileri);
        Edittext_Ayir = (EditText) findViewById(R.id.Edittext_Ayir);
        Textview_Para = (TextView) findViewById(R.id.Textview_Para);


        veri_al();

        ekle_listview();





        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();


        //Listview tıklanınca yapılacaklar..

        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                    //Veri silme işlemlerini gerçekleştiriyoruz.
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Para_ayirma_ilk.this);

                    builder.setTitle(getString(R.string.kaldir));
                    builder.setMessage(dizi_mesaj.get(i)+"\n"+dizi_para.get(i)+getString(R.string.Para_Birimi));


                    builder.setPositiveButton(getString(R.string.kaldir),new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){


                            Integer deletedRows = myDb.deleteData_AyrilanPara(dizi_id.get(i));

                            if(deletedRows>0){
                                Toast.makeText(getApplicationContext(), getString(R.string.silinde),
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), getString(R.string.silmebasarisiz),
                                        Toast.LENGTH_LONG).show();
                            }

                            veri_al();

                            ekle_listview();


                        }

                    });



                    builder.setNegativeButton(getString(R.string.iptal),new DialogInterface.OnClickListener(){

                        public void onClick(DialogInterface dialog, int id){


                        }

                    });


                    builder.show();


            }

        });



        Button_Ileri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Devam etme butonu
                Intent intent = new Intent(getApplicationContext(), Tab.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        Button_harca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Açıklamam ve Para girme butonu.

                if(sayac_button ==0){

                    //Eğer buton ilk kez tıklanırsa..

                    mesaj= Edittext_Ayir.getText().toString();
                    Edittext_Ayir.setText("");

                    Edittext_Ayir.setInputType(InputType.TYPE_CLASS_NUMBER);
                    Edittext_Ayir.setHint(R.string.nekadar);
                    Edittext_Ayir.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
                    Edittext_Ayir.setTextSize(20);
                    Button_harca.setText(R.string.paragir);

                    if(mesaj.toString().trim().equals(""))
                    {
                        //Açıklama girilmemiş ise
                        mesaj="----------";
                    }
                    sayac_button = sayac_button +1;

                }
                else if(sayac_button ==1){

                    //Butona ikinci kez tıklanınca yapılacaklar..

                    if(Edittext_Ayir.getText().toString().trim().equals("")){
                        //Eğer edittext boş ise..
                        Toast.makeText(Para_ayirma_ilk.this, getString(R.string.boskalamaz), Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        para= Edittext_Ayir.getText().toString();

                        //ayrılan para toplamı ile girilen paranın toplamını alıp, kullanıcını girdiği bütceyi aşıp aşmadığını kontrol ediyoruz.
                        sepet_ayrilan_para=sepet_ayrilan_para+Double.parseDouble(para);

                        if(sepet_ayrilan_para>=Double.parseDouble(prefSettings.getString("para_borc","0"))){

                            //Eğer kullanıcının bütcesi aşılırsa kullanıcıyı bilgilendiriyoruz ve giridiği parayı sepetten çıkartıp metotdan çıkıyoruz.

                            sepet_ayrilan_para=sepet_ayrilan_para-Double.parseDouble(para);
                            Toast.makeText(Para_ayirma_ilk.this, getString(R.string.yeterliparanizyok), Toast.LENGTH_SHORT).show();
                            return;


                        }
                        else
                        {
                            //Eğer kullanıcı bütcesini aşmamış ise sepetten girdiği parayı çıkartıp işlemlerimizi yapıyoruz.
                            //Burdaki mantık, kullanıcının ayırdığı parayla ayırmak isdediği parayı toplayıp bütcesini geçip geçmediğini kontrol ediyoruz
                            // ve sonra sepetten kullanıcını ayırmak isdediği parayı çıkartıyoruz.Yani ekleyip kontrol edip tekrar çıkartıyoruz.
                            sepet_ayrilan_para=sepet_ayrilan_para-Double.parseDouble(para);

                            Edittext_Ayir.setText("");

                            Edittext_Ayir.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            Edittext_Ayir.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
                            Edittext_Ayir.setHint(R.string.neicinparaayiracaksiniz);
                            Edittext_Ayir.setTextSize(16);
                            Button_harca.setText(R.string.aciklamagir);


                            //sayacı sıfırlamamızın sebebi, kullanıcını tekrar başa dönmesi(Açıklama gir->Para Gir-> Açıklama Gir-> Para Gir-> ....)
                            sayac_button =0;


                            boolean r;



                            if(!para.toString().trim().equals(""))
                            {
                                //Eğer para miktarı girilmiş ise veri tabanına kaydediyoruz.
                                try{r =myDb.ekle_ayrilan_para(para,mesaj,"0");
                                }
                                finally {
                                    myDb.close();
                                }

                                //Kullanıcıyı bilgilendiriyoruz.
                                if(r==false){

                                    Toast.makeText(Para_ayirma_ilk.this,getString(R.string.hata), Toast.LENGTH_SHORT).show();

                                }
                                else if ( r==true){

                                    Toast.makeText(Para_ayirma_ilk.this,getString(R.string.eklendi) , Toast.LENGTH_SHORT).show();

                                }
                            }

                            veri_al();
                            ekle_listview();
                        }

                    }





                }



            }
        });

    }



}
