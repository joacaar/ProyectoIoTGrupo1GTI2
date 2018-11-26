/*package com.GTI.Grupo1.IoT;

import java.util.ArrayList;
import java.util.List;

public class UsuariosVector implements UsuarioInterface {

    protected List<Usuario> vectorUsuarios;

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
        lugares.add(new Usuario("Manolo", " Padre de miguel "));
        lugares.add(new Usuario("Elisa", " Amiga de miguel"));
        lugares.add(new Usuario("Pepe ", " Amigo mio"));
        lugares.add(new Usuario("Agustin", " Mi hijo"));
        lugares.add(new Usuario("Mariola", " Amiga de mi madre"));
        lugares.add(new Usuario("Jorge", " Mi sobrino"));
        return lugares;
    }
}
*/