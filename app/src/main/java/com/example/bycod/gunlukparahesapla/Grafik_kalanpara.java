package com.example.bycod.gunlukparahesapla;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;


public class Grafik_kalanpara extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;

    final database myDb = new database(this);

    ArrayList<Double> dizi_para_double = new ArrayList();

    double x=-1;
    double y=0;

    int sayac=1;

    double sepet=0;

    double veritabani;

    double enbuyuk=0;
    double enkucuk=0;

    TextView tv;

    final NumberFormat numberformat = NumberFormat.getInstance();


    public void kayitlar(){



        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        //Grafiği kullanıcının parasından başlatıyoruz.
        dizi_para_double.add(Double.parseDouble(prefSettings.getString("para_grafik","0")));
        sepet=dizi_para_double.get(0);

        final Cursor res = myDb.getAllData_kalan_para();

        if(res.getCount()==0){



        }
        else
        {


            //veriler bitene kadar çekiyoruz
            while(res.moveToNext())
            {

                Random r = new Random();

                veritabani = Double.parseDouble(res.getString(0));

                sepet =Double.parseDouble(res.getString(0));


                dizi_para_double.add(veritabani);
                sayac=sayac+1;

                if(enbuyuk<veritabani){

                    enbuyuk=veritabani;//Grafiğin en büyük veriye göre şekillenmesi için en büyük veriyi alıyoruz.

                }
                if(veritabani<0 && enkucuk>veritabani)
                {
                    enkucuk=veritabani;//Grafiğin en küçük veriye göre şekillenmesi için en küçük veriyi alıyoruz.
                }
            }



        }


        if(Double.parseDouble(prefSettings.getString("para_grafik","0"))>enbuyuk){

            enbuyuk= Double.parseDouble(prefSettings.getString("para_grafik","0"));

        }



    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_kalanpara);

        Toast.makeText(this, R.string.kalanparagrafigi, Toast.LENGTH_SHORT).show();


        kayitlar();


        GraphView graph = (GraphView) findViewById(R.id.graph_kalan);
        TextView tv= (TextView) findViewById(R.id.kalanpara);


        //Grafik için verileri veriyoruz.

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        for(int i=0; i<sayac; i++){

            y=dizi_para_double.get(i);
            x=x+1;
            series.appendData(new DataPoint(x, y),true,sayac);
        }


        //Grafik özelliklerinin giriyoruz.

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueY) {
                if (isValueY) {
                    // show normal x values
                    return super.formatLabel(value, isValueY);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueY) + getString(R.string.Para_Birimi);
                }
            }
        });


        tv.setText(getString(R.string.kalanpara)+" "+numberformat.format(sepet)+" "+getString(R.string.Para_Birimi));


        graph.addSeries(series);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(enkucuk);
        graph.getViewport().setMaxY(enbuyuk+5);

        graph.getViewport().setXAxisBoundsManual(true);



        if(sayac>30){

            graph.getViewport().setMinX(sayac-30);
            graph.getViewport().setMaxX(sayac);
        }else
        {

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(sayac);
        }



        series.setColor(Color.rgb(37,138,162));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(9);
        series.setThickness(8);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);


        final SharedPreferences prefSettings =  getSharedPreferences("", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefSettings.edit();

        if(prefSettings.getString("veri_sifirlama_kontrol","0").toString().trim().equals("1"))
        {
            boolean kontrol = editor.clear().commit();
            myDb.DeleteAllData();
            if(kontrol==true){
                Toast.makeText(getApplicationContext(),R.string.eskiverilersilindi,Toast.LENGTH_SHORT).show();
            }

        }


    }
}
