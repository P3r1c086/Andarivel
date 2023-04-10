package com.pedroaguilar.andarivel.modelo;

public class Fichaje {
    private String idFichaje;
    private String IDUsuario;
    private String fecha;
    private String nombreUsuario;
    private String horaEntrada;
    private String horaSalida;
    private String tiempoTrabajadoDia;

    public String getFecha() {
        return fecha;
    }

    public String getIdFichaje() {
        return idFichaje;
    }

    public void setIdFichaje(String idFichaje) {
        this.idFichaje = idFichaje;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTiempoTrabajadoDia() {
        return tiempoTrabajadoDia;
    }

    public void setTiempoTrabajadoDia(String tiempoTrabajadoDia) {
        this.tiempoTrabajadoDia = tiempoTrabajadoDia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(String IDUsuario) {
        this.IDUsuario = IDUsuario;
    }
}
