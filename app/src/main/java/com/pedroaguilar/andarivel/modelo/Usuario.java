package com.pedroaguilar.andarivel.modelo;

import java.util.ArrayList;

public class Usuario {

    private String ID;
    private String apellidos;
    private String direccion;
    private String email;
    private String motivoAusencia;
    private String descripcionAusencia;
    private String fechaInicioAusencia;
    private String fechaFinAusencia;
    private String nombre;
    private String nombreUsuario;
    private String password;
    private Boolean esAdiminstrador;
    private String telefono;
    private String horaEntrada;
    private String horaSalida;
    private ArrayList<Boolean> fichaje;
    private String fecha;

    public Usuario() {
    }

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public ArrayList<Boolean> getFichaje() {
        return fichaje;
    }

    public void setFichaje(ArrayList<Boolean> fichaje) {
        this.fichaje = fichaje;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotivoAusencia() {
        return motivoAusencia;
    }

    public void setMotivoAusencia(String motivoAusencia) {
        this.motivoAusencia = motivoAusencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEsAdiminstrador() {
        return esAdiminstrador;
    }

    public void setEsAdiminstrador(Boolean esAdiminstrador) {
        this.esAdiminstrador = esAdiminstrador;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getDescripcionAusencia() {
        return descripcionAusencia;
    }

    public void setDescripcionAusencia(String descripcionAusencia) {
        this.descripcionAusencia = descripcionAusencia;
    }
}
