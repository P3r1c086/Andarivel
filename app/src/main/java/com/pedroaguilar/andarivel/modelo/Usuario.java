package com.pedroaguilar.andarivel.modelo;

public class Usuario {
    private Double horasDescanso;
    private String ID;
    private String apellidos;
    private String departamento;
    private Double diasVacaciones;
    private String direccion;
    private String email;
    private String foto;
    private Double horasTrabajadas;
    private String motivoAusencia;
    private String nombre;
    private String nombreUsuario;
    private String password;
    private String puesto;
    private String rol;
    private String telefono;

    public Usuario() {
    }

    public Double getHorasDescanso() {
        return horasDescanso;
    }

    public void setHorasDescanso(Double horasDescanso) {
        this.horasDescanso = horasDescanso;
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Double getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(Double diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
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

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "horasDescanso=" + horasDescanso +
                ", ID=" + ID +
                ", apellidos='" + apellidos + '\'' +
                ", departamento='" + departamento + '\'' +
                ", diasVacaciones=" + diasVacaciones +
                ", direccion='" + direccion + '\'' +
                ", email='" + email + '\'' +
                ", foto='" + foto + '\'' +
                ", horasTrabajadas=" + horasTrabajadas +
                ", motivoAusencia='" + motivoAusencia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", password='" + password + '\'' +
                ", puesto='" + puesto + '\'' +
                ", rol='" + rol + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    public void editarDatosPersonales(){

    }
    public void pedirPermiso(){

    }
    public Boolean concederPermiso(){
    return false;
    }
    public Double consultarVacaciones(){
        return 0.0;
    }
    public Double consultarHorasExtras(){
        return 0.0;
    }
    public Double consultarHorasTrabajadas(){
        return 0.0;
    }

}
