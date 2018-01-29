package com.example.bycod.gunlukparahesapla;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class OzelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Bilgi> mBilgiListesi;

    public OzelAdapter(Activity activity, List<Bilgi> bilgiler){

        //Layoutu ve listeyi oluşturuyoruz.

        mInflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBilgiListesi = bilgiler;
    }

    @Override
    public int getCount() {
        return mBilgiListesi.size();
    }

    @Override
    public Object getItem(int pos) {
        return mBilgiListesi.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        //Layotu belirliyoruz.(Listview de listelenecek olan layout)
        View satirView=mInflater.inflate(R.layout.satir_layout,null);


        TextView textView = (TextView) satirView.findViewById(R.id.Para);
        TextView textView2 = (TextView) satirView.findViewById(R.id.Tarih);
        TextView textView3 = (TextView) satirView.findViewById(R.id.Saat);

        //Bilgileri Bilgi.java clasından alıyoruz ve textviewlerde gösteriyoruz.
        Bilgi bilgi = mBilgiListesi.get(pos);
        textView.setText(bilgi.getPara());
        textView2.setText(bilgi.getTarih());
        textView3.setText(bilgi.getSaat());

        return satirView;
    }
}
