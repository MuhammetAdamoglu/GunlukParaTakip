package com.example.bycod.gunlukparahesapla;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;


public class Grafik extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;

    final database myDb = new database(this);

    ArrayList<Double> dizi_para_double = new ArrayList();

    double x=-1;
    double y=0;

    int sayac=1;

    double sepet=0;

    double veritabani;

    double enbuyuk=0;

    TextView tv;

    final NumberFormat numberformat = NumberFormat.getInstance();

    public void kayitlar(){

        final Cursor res = myDb.getAllData_harcanilanpara_veriler();

        //İlk başa 0 ekliyoruz.
        dizi_para_double.add((double) 0);

        //SQLite den harcanılan para verilerini çekiyoruz

        if(res.getCount()!=0){

            //veriler bitene kadar çekiyoruz
            while(res.moveToNext())
            {

                Random r = new Random();

                veritabani = Double.parseDouble(res.getString(0));

                sepet =sepet + Double.parseDouble(res.getString(0));


                    dizi_para_double.add(veritabani);
                    sayac=sayac+1;

                if(enbuyuk<veritabani){

                    enbuyuk=veritabani;//Grafiğin en büyük veriye göre şekillenmesi için en büyük veriyi alıyoruz.

                }


            }

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik);

        Toast.makeText(this, R.string.gunlukharcamagrafigi, Toast.LENGTH_SHORT).show();




        kayitlar();



        GraphView graph = (GraphView) findViewById(R.id.graph);
        tv= (TextView) findViewById(R.id.harcanilanpara);


        //grafik için verileri veriyruz.

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        for(int i=0; i<sayac; i++){

            y=dizi_para_double.get(i);
            x=x+1;
            series.appendData(new DataPoint(x, y),true,sayac);
        }


        //Grafik ayarını yapıyoruz

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueY) {
                if (isValueY) {
                    // show normal x values
                    return super.formatLabel(value, isValueY);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueY)+getString(R.string.Para_Birimi);
                }
            }
        });


      tv.setText(getString(R.string.hacanilanpara)+" "+numberformat.format(sepet)+getString(R.string.Para_Birimi));



        graph.addSeries(series);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
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





    }
}
