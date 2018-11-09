package com.GTI.Grupo1.IoT;

import java.util.ArrayList;
import java.util.List;

public class UsuariosVector implements UsuarioInterface {

    protected List<Usuario> vectorUsuarios = ejemploUsuarios();

    public UsuariosVector() {
        vectorUsuarios = ejemploUsuarios();
    }

    public Usuario elemento(int id) {
        return vectorUsuarios.get(id);
    }

    public void anyade(Usuario usuario) {
        vectorUsuarios.add(usuario);
    }

    public int nuevo() {
        Usuario usuario = new Usuario();
        vectorUsuarios.add(usuario);
        return vectorUsuarios.size()-1;
    }

    public void borrar(int id) {
        vectorUsuarios.remove(id);
    }

    public int tamanyo() {
        return vectorUsuarios.size();
    }

    public void actualiza(int id, Usuario lugar) {
        vectorUsuarios.set(id, lugar);
    }
    public static ArrayList<Usuario> ejemploUsuarios() {
        ArrayList<Usuario> lugares = new ArrayList<Usuario>();
        lugares.add(new Usuario("Abuelo Tiranias", " Abuelo de marta"));
        lugares.add(new Usuario("Harold Holdepein", " Amigo de mi mujer"));
        lugares.add(new Usuario("Manolo", "  "));
        lugares.add(new Usuario("La chica cuyo nombre no recuerdo", "No recuerdo su nombre"));
        lugares.add(new Usuario("El tio Gepeto", "No es tan rico como parece"));
        lugares.add(new Usuario("Agustin", "En realidad está incomodin"));
        lugares.add(new Usuario("Mi tío abuelo Stu", "'Stu'pefacto se quedará cuando vea la app"));
        lugares.add(new Usuario("Jorge", "Esta vez no se apellida Nitales"));
        return lugares;
    }
}
