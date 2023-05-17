package com.smu.dotbuv10.Cita.Controller;

import java.util.Date;

public class CitaAlarm {

    String lugar;
    Date  fecha;
    String hora;
    String Doctor;

    public CitaAlarm(String lugar, Date  fecha, String hora, String Doctor){
        this.lugar=lugar;
        this.fecha=fecha;
        this.hora=hora;
        this.Doctor=Doctor;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }
}
