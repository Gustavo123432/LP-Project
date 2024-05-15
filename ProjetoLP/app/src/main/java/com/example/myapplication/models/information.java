package com.example.myapplication.models;

public class information {
    private String dia;
    private String tempoImage;
    private String minTemperatura;
    private String maxTemperatura;
    private String ventoDirection;
    private String precipition;

    public information() {
    }

    public information(String dia, String tempoImage, String minTemperatura, String maxTemperatura, String ventoDirection, String precipition) {
        this.dia = dia;
        this.tempoImage = tempoImage;
        this.minTemperatura = minTemperatura;
        this.maxTemperatura = maxTemperatura;
        this.ventoDirection = ventoDirection;
        this.precipition = precipition;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getTempoImage() {
        return tempoImage;
    }

    public void setTempoImage(String tempoImage) {
        this.tempoImage = tempoImage;
    }

    public String getMinTemperatura() {
        return minTemperatura;
    }

    public void setMinTemperatura(String minTemperatura) {
        this.minTemperatura = minTemperatura;
    }

    public String getMaxTemperatura() {
        return maxTemperatura;
    }

    public void setMaxTemperatura(String maxTemperatura) {
        this.maxTemperatura = maxTemperatura;
    }

    public String getVentoDirection() {
        return ventoDirection;
    }

    public void setVentoDirection(String ventoDirection) {
        this.ventoDirection = ventoDirection;
    }

    public String getPrecipition() {
        return precipition;
    }

    public void setPrecipition(String precipition) {
        this.precipition = precipition;
    }
}
