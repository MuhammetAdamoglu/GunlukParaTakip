package com.example.bycod.gunlukparahesapla;




public class Bilgi {
    private String para;
    private String tarih;
    private String saat;

    public Bilgi(String gelenpara, String gelentarih, String gelensaat){

        //gelen bilgileri set yapiyoruz.
        this.setPara(gelenpara);
        this.setTarih(gelentarih);
        this.setSaat(gelensaat);


    }


    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }

    public String getSaat() {
        return saat;
    }

    public void setSaat(String saat) {
        this.saat = saat;
    }

}
