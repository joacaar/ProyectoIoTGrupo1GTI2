package com.GTI.Grupo1.IoT;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class InicioFragment extends Fragment {

    private String peso;
    private String altura;
    private boolean puerta;
    private String estadoPuerta;
    private String temperatura;
    private String humedad;
    private boolean luces;
    private String estadoLuces;
    private String personas;


    private FirebaseUser user = MainActivity.user;

    public static  View view;

    public InicioFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view = inflater.inflate(R.layout.inicio, container, false);


        getActivity().startService(new Intent(getActivity(),
                IntentServiceOperacion.class));
//----------------------------- DATOS DE BASCULA Y ALTURA ---------------------------------------------------------
        final TextView textoPeso = view.findViewById(R.id.peso);
        final TextView textoAltura = view.findViewById(R.id.altura);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("USUARIOS")
                .document(user.getUid())
                .collection("Bascula").orderBy("fecha",Query.Direction.DESCENDING).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int i = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
//
                            //Recoger los valores de la bd
                            peso = document.getData().get("peso").toString();
                            altura = document.getData().get("altura").toString();
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            //Añadir el texto de la bd en el layout
                            if (pref.getString("masa", "0").equals("1")) {
                                textoPeso.setText(cambioMedidaPeso(Float.parseFloat(peso)) + " st");
                            } else if (pref.getString("masa", "0").equals("2")) {
                                textoPeso.setText(cambioMedidaPeso(Float.parseFloat(peso)) + " lb");
                            } else {
                                textoPeso.setText(cambioMedidaPeso(Float.parseFloat(peso)) + " kg");
                            }
                            if (pref.getString("altura", "0").equals("1")) {
                                textoAltura.setText(cambioMedidaAltura(Float.parseFloat(altura)) + " ft");
                            } else if (pref.getString("altura", "0").equals("2")) {
                                textoAltura.setText(cambioMedidaAltura(Float.parseFloat(altura)) + " in");
                            } else {
                                textoAltura.setText(cambioMedidaAltura(Float.parseFloat(altura)) + " cm");
                            }


                            i++;
                        }
                    }
                });
//----------------------------- DATOS DE SENSORES ---------------------------------------------------------
        final TextView textoPuerta = view.findViewById(R.id.puerta);
        final TextView textoTemp = view.findViewById(R.id.temp);
        final TextView textoHum = view.findViewById(R.id.hum);
        //final TextView textoLuces = view.findViewById(R.id.luces);
        final TextView textoPersonas = view.findViewById(R.id.personas);


        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("CASAS").document("Casa1.123456789").get()
                .addOnCompleteListener(
                        new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                                if (task.isSuccessful()) {

                                    //Recoger los valores de la bd
                                    puerta = task.getResult().getBoolean("PuertaAbierta");
                                    temperatura = task.getResult().getDouble("Temperatura").toString();
                                    humedad = task.getResult().getDouble("Humedad").toString();
                                    //luces = task.getResult().getBoolean("Luces");
                                    personas = task.getResult().getDouble("Personas").toString();

                                    if(puerta==false || luces==false) {
                                        estadoPuerta = "Cerrada";
                                        //estadoLuces = "Apagada";

                                    }

                                    //Añadir el texto de la bd en el layout
                                    textoPuerta.setText(estadoPuerta);
                                    textoTemp.setText(temperatura + " ºC");
                                    textoHum.setText(humedad + " % ");
                                    textoPersonas.setText("Hay " + personas);

                                }
                            }
                        });

        return view;

    }//onCreate()

    //funcion de cambio de peso
    public float cambioMedidaPeso (float pesoACambiar) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DecimalFormat formato = new DecimalFormat("#.#");

        if (pref.getString("masa", "0").equals("1")) {
            float res;
            res = pesoACambiar * 0.157473f; //stones
            return Float.parseFloat(formato.format(res));
        } else if (pref.getString("masa", "0").equals("2")) {
            float res;
            res = pesoACambiar * 2.20462f; //libras
            return Float.parseFloat(formato.format(res));
        } else {
            return Float.parseFloat(formato.format(pesoACambiar)); //kg
        }
    }

    // funcion cambio de altura
    public float cambioMedidaAltura (float alturaACambiar) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        DecimalFormat formato = new DecimalFormat("#.#");

        if (pref.getString("altura", "0").equals("1")) {
            float res;
            res = alturaACambiar * 0.0328084f; //stones
            return Float.parseFloat(formato.format(res));
        } else if (pref.getString("altura", "0").equals("2")) {
            float res;
            res = alturaACambiar * 0.393701f;
            return Float.parseFloat(formato.format(res));
        } else {
            return Float.parseFloat(formato.format(alturaACambiar));
        }
    }


}//()
