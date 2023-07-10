package com.azhar.reportapps.ui.main;

public class DataClass {
    private String imageURL,caption,deskrip,tanggal,lokasi;
    public DataClass(){
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDeskrip() {
        return deskrip;
    }
    public void setDeskrip(String deskrip) {
        this.deskrip = deskrip;
    }

    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    public DataClass(String imageURL, String caption,String lokasi,String tanggal,String deskrip) {
        this.imageURL = imageURL;
        this.caption = caption;
        this.lokasi = lokasi;
        this.deskrip = deskrip;
        this.tanggal=tanggal;
    }
}