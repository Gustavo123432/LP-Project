package com.example.myapplication.models;

public class FavModel {
    private String imagem;
    private String local;
    private String tmax;
    private String tmin;

    public FavModel() {
    }

    public FavModel(String imagem, String local, String tmax, String tmin) {
        this.imagem = imagem;
        this.local = local;
        this.tmax = tmax;
        this.tmin = tmin;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getTmax() {
        return tmax;
    }

    public void setTmax(String tmax) {
        this.tmax = tmax;
    }

    public String getTmin() {
        return tmin;
    }

    public void setTmin(String tmin) {
        this.tmin = tmin;
    }
}

