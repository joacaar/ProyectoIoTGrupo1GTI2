package com.GTI.Grupo1.IoT;

public class Usuario
{
    private String nombre;
    private String foto;
    private String comentario;

    public Usuario(String nombre, String comentario)
    {
        this.nombre = nombre;
        this.comentario = comentario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Lugar{" +
                "nombre='" + nombre + '\'' +
                ", foto='" + foto + '\'' +
                ", comentario='" + comentario + '\'' +
                '}';
    }

    public Usuario () {
        this.nombre = "Nombre Común";
        this.comentario = "Comentario común";
    }
}