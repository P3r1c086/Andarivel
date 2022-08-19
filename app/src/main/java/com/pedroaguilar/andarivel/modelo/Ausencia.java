package com.pedroaguilar.andarivel.modelo;

public class Ausencia {

    private String idAusencia;
    private String nombreUsuario;
    private String motivoAusencia;
    private String descripcionAusencia;
    private String fechaInicioAusencia;
    private String fechaFinAusencia;
    private String estado;

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    private String adjunto;

    public Ausencia() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdAusencia() {
        return idAusencia;
    }

    public void setIdAusencia(String idAusencia) {
        this.idAusencia = idAusencia;
    }

    public String getMotivoAusencia() {
        return motivoAusencia;
    }

    public void setMotivoAusencia(String motivoAusencia) {
        this.motivoAusencia = motivoAusencia;
    }

    public String getDescripcionAusencia() {
        return descripcionAusencia;
    }

    public void setDescripcionAusencia(String descripcionAusencia) {
        this.descripcionAusencia = descripcionAusencia;
    }

    public String getFechaInicioAusencia() {
        return fechaInicioAusencia;
    }

    public void setFechaInicioAusencia(String fechaInicioAusencia) {
        this.fechaInicioAusencia = fechaInicioAusencia;
    }

    public String getFechaFinAusencia() {
        return fechaFinAusencia;
    }

    public void setFechaFinAusencia(String fechaFinAusencia) {
        this.fechaFinAusencia = fechaFinAusencia;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
