package com.example.bycod.gunlukparahesapla;


import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.widget.TabWidget;

public class Tab extends TabActivity {

    TabWidget tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        TabHost tabh = (TabHost)findViewById(android.R.id.tabhost);
        TabSpec tab1 = tabh.newTabSpec("tab menü 1. seçenek");
        TabSpec tab2 = tabh.newTabSpec("tab menü 2. seçenek");
        tab1.setIndicator(getString(R.string.harca));
        tab1.setContent(new Intent(this,AnaActivity.class));
        tab2.setIndicator(getString(R.string.grafik));
        tab2.setContent(new Intent(this,TabGrafik.class));
        tabh.addTab(tab1);tabh.addTab(tab2);
    }
}