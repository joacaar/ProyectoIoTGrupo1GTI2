package com.GTI.Grupo1.IoT;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdaptadorHistorial extends RecyclerView.Adapter<AdaptadorHistorial.ViewHolder> {

    protected float[] datos; // Lista de datos a mostrar
    protected List<Date> fechas;
    protected LayoutInflater inflador; // Crea Layouts a partir del XML
    protected Context contexto; // Lo necesitamos para el inflador
    protected String altura;

    public AdaptadorHistorial(Context contexto, float[] datos, List<Date> fechas, String altura) {
        this.contexto = contexto;
        this.datos = datos;
        this.fechas=fechas;
        this.altura=altura;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, fecha, altura;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            altura = itemView.findViewById(R.id.altura);
            fecha=itemView.findViewById(R.id.fecha);
        }
    }

    // Creamos el ViewHolder con la vista de un elemento sin personalizar
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflador.inflate(R.layout.elemento_historial, parent, false);
        return new ViewHolder(v);
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        float dato = this.datos[posicion];
        Date fecha=fechas.get(posicion);
        String altura1=this.altura;
        personalizaVista(holder, dato, fecha, altura);
    }

    public void personalizaVista(ViewHolder holder, float dato, Date fecha, String altura1) {
        String string1= String.valueOf(dato)+"kg";
        holder.nombre.setText(string1);
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String string2= "Fecha: "+String.valueOf(formateador.format(fecha));
        holder.fecha.setText(string2);
        float z= (float) 0.99;
        holder.itemView.setAlpha(z);
        holder.altura.setText("Altura: "+altura1+"m");
        holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //holder.itemView.setBackground(Drawable.createFromPath("@contenedor_rnd"));
        double imc=dato/Math.pow(Float.parseFloat(altura1)/100, 2);
        if(imc<25 && imc>18.5) {
            holder.nombre.setTextColor(Color.parseColor("#FF99D21F"));
        }else if(imc <=18.5 && imc>14){
            holder.nombre.setTextColor(Color.parseColor("#FFF1A378"));
        }else if(imc<=14){
            holder.nombre.setTextColor(Color.parseColor("#FFF45500"));
        }
        else if(imc <30 && imc>=25){
            holder.nombre.setTextColor(Color.parseColor("#FFF1A378"));
        }else if(imc>=30){
            holder.nombre.setTextColor(Color.parseColor("#FFF45500"));
        }
    }

    // Indicamos el número de elementos de la lista
    @Override public int getItemCount() {
        return datos.length;
    }
}

