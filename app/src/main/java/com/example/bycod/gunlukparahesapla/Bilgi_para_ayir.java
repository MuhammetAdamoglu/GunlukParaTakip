package com.example.bycod.gunlukparahesapla;


public class Bilgi_para_ayir {
    private String para;
    private String mesaj;
    private String kontrol;

    public Bilgi_para_ayir(String gelenpara, String gelen_mesaj, String kontrol){
        //Gelen bilgileri set yapiyoruz
        this.setPara(gelenpara);
        this.setMesaj(gelen_mesaj);
        this.setKontrol(kontrol);


    }



    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getKontrol() {
        return kontrol;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public void setKontrol(String kontrol) {
        this.kontrol = kontrol;
    }







}
