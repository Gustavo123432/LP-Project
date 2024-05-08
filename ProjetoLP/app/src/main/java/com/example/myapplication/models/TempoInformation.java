package com.example.myapplication.models;

public class TempoInformation {
    public String precipitaProb;
    public String minTemp;
    public String maxTemp;
    public String predWindDir;
    public String forecastDate;
    public String idWeatherType;

    public TempoInformation() {
    }

    public TempoInformation(String precipitaProb, String minTemp, String maxTemp, String predWindDir, String forecastDate, String idWeatherType) {
        this.precipitaProb = precipitaProb;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.predWindDir = predWindDir;
        this.forecastDate = forecastDate;
        this.idWeatherType = idWeatherType;
    }

    public String getPrecipitaProb() {
        return precipitaProb;
    }

    public void setPrecipitaProb(String precipitaProb) {
        this.precipitaProb = precipitaProb;
    }

    public String getminTemp() {
        return minTemp;
    }

    public void setminTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getmaxTemp() {
        return maxTemp;
    }

    public void setmaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getPredWindDir() {
        return predWindDir;
    }

    public void setPredWindDir(String predWindDir) {
        this.predWindDir = predWindDir;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getIdWeatherType() {
        return idWeatherType;
    }

    public void setIdWeatherType(String idWeatherType) {
        this.idWeatherType = idWeatherType;
    }
}

