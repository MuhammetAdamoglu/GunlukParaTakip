package com.example.bycod.gunlukparahesapla;



import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;
import android.content.Intent;


public class TabGrafik extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_grafik);
        TabHost tabh = (TabHost)findViewById(android.R.id.tabhost);
        TabSpec tab1 = tabh.newTabSpec("tab menü 1. seçenek");
        TabSpec tab2 = tabh.newTabSpec("tab menü 2. seçenek");

        tab1.setIndicator(getString(R.string.harcama));
        tab1.setContent(new Intent(this,Grafik.class));
        tab2.setIndicator(getString(R.string.kalanpara));
        tab2.setContent(new Intent(this,Grafik_kalanpara.class));

        tabh.addTab(tab1); tabh.addTab(tab2);
    }
}