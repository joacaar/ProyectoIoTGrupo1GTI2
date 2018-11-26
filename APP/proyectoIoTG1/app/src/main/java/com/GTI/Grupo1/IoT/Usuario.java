package com.GTI.Grupo1.IoT;

public class Usuario
{
    private String correoElectronico;
    private String fechaNacimiento;
    private long nivelEjercicio;
    private String nombre;
    private String sexo;

    public Usuario(String correoElectronico, String fechaNacimiento, long nivelEjercicio, String nombre, String sexo)
    {
        this.correoElectronico = correoElectronico;
        this.fechaNacimiento = fechaNacimiento;
        this.nivelEjercicio = nivelEjercicio;
        this.nombre = nombre;
        this.sexo = sexo;
    }

    public Usuario ()
    {
        this.correoElectronico = "No tienes Email";
        this.fechaNacimiento = "No tienes fecha de nacimiento";
        this.nivelEjercicio = 0;
        this.nombre = "No tienes nombre, señor";
        this.sexo = "asexual";
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public long getNivelEjercicio() {
        return nivelEjercicio;
    }

    public void setNivelEjercicio(long nivelEjercicio) {
        this.nivelEjercicio = nivelEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "correoElectronico='" + correoElectronico + '\'' +
                "fechaNacimiento='" + fechaNacimiento + '\'' +
                "nivelEjercicio='" + nivelEjercicio + '\'' +
                "nombre='" + nombre + '\'' +
                "sexo='" + sexo + '\'' +
                '}';
    }
}