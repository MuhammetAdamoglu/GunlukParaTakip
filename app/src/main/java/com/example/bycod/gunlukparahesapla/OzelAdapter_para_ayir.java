package com.example.bycod.gunlukparahesapla;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;



public class OzelAdapter_para_ayir extends BaseAdapter {


    TextView tv;
    TextView para;


    private LayoutInflater mInflater;
    private List<Bilgi_para_ayir> mBilgiListesi2;


    public OzelAdapter_para_ayir(Activity activity, List<Bilgi_para_ayir> bilgiler){

        //Layoutu ve listeyi oluşturuyoruz.
        mInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBilgiListesi2 = bilgiler;
    }


    @Override
    public int getCount() {
        return mBilgiListesi2.size();
    }

    @Override
    public Object getItem(int pos) {
        return mBilgiListesi2.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        View satirView = null;



        Bilgi_para_ayir bilgi = mBilgiListesi2.get(pos);



        if(bilgi.getKontrol().trim().equals("1")){
            //Eğer kontrol değişkeni 1 ise(Borç ödenmiş ise) farklı bir layout çagırıyoruz ve Bilgi_para_ayir.java clasından gelen bilgileri textviewlerde gösteriyoruz.
            satirView=mInflater.inflate(R.layout.cizgi_para_ayir_satir_layout,null);

            para = (TextView) satirView.findViewById(R.id.Textview_Para);
            tv  = (TextView) satirView.findViewById(R.id.mesaj);

            para.setText(bilgi.getPara());
            tv.setText(bilgi.getMesaj());

        }
        else
        {

            //Eğer kontrol değişkeni 0 ise(Borç ödenmemiş ise) farklı bir layout çagırıyoruz ve Bilgi_para_ayir.java clasından gelen bilgileri textviewlerde gösteriyoruz.
            satirView=mInflater.inflate(R.layout.para_ayir_satir_layout,null);

            para = (TextView) satirView.findViewById(R.id.Textview_Para);
            tv  = (TextView) satirView.findViewById(R.id.mesaj);

            para.setText(bilgi.getPara());
            tv.setText(bilgi.getMesaj());

        }

        return satirView;
    }


}
