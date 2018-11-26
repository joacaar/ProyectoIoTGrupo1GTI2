/*package com.GTI.Grupo1.IoT;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder> {

    protected UsuarioInterface usuarios; // Lista de lugares a mostrar
    protected LayoutInflater inflador; // Crea Layouts a partir del XML
    protected Context contexto; // Lo necesitamos para el inflador

    public AdaptadorUsuarios(Context contexto, UsuarioInterface usuarios) {
        this.contexto = contexto;
        this.usuarios = usuarios;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, comentario;
        public ImageView foto;
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            comentario = itemView.findViewById(R.id.comentario);
            foto = itemView.findViewById(R.id.foto);
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_usuario, parent, false);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Usuario usuario = usuarios.elemento(posicion);
        personalizaVista(holder, usuario);
    }

    public void personalizaVista(ViewHolder holder, Usuario usuario) {
        holder.nombre.setText(usuario.getNombre());
        holder.comentario.setText(usuario.getComentario());
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return usuarios.tamanyo();
    }
}
*/

