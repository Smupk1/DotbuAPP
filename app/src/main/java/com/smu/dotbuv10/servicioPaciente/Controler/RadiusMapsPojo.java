package com.smu.dotbuv10.servicioPaciente.Controler;

public class RadiusMapsPojo {
    private double latitud;
    private double longitud;
    private float radio;
    public RadiusMapsPojo() {


    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public float getRadio() {
        return radio;
    }

    public void setRadio(float radio) {
        this.radio = radio;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
