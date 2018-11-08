package com.GTI.Grupo1.IoT;

public interface UsuarioInterface {
    Usuario elemento(int id); //Devuelve el elemento dado su id
    void anyade(Usuario usuario); //Añade el elemento indicado
    int nuevo(); //Añade un elemento en blanco y devuelve su id
    void borrar(int id); //Elimina el elemento con el id indicado
    int tamanyo(); //Devuelve el número de elementos
    void actualiza(int id, Usuario usuario); //Reemplaza un elemento
}