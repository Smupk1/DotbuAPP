package com.smu.dotbuv10.Cita.Model;

public class EntidadCita {

    private int id;
    private String imgFoto;
    private String titulo;
    private String contenido;
    private String Medico;

    public EntidadCita(int id, String imgFoto, String titulo, String contenido, String medico) {
        this.id = id;
        this.imgFoto = imgFoto;
        this.titulo = titulo;
        this.contenido = contenido;
        Medico = medico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgFoto() {
        return imgFoto;
    }

    public void setImgFoto(String imgFoto) {
        this.imgFoto = imgFoto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getMedico() {
        return Medico;
    }

    public void setMedico(String medico) {
        Medico = medico;
    }
}
