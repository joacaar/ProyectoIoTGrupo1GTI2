package com.GTI.Grupo1.IoT;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Historial extends Fragment {
    private RecyclerView Historial;
    public AdaptadorHistorial adaptador;
    private RecyclerView.LayoutManager layoutManager;
    View vistaGraficas;
    View vistaBascula;

    private FirebaseUser user = MainActivity.user;
    float ultimoPeso;
    float[] valoresPeso = new float[10];
    // float[] valoresPeso1 = new float[10];
    String altura;
    String altura1;
    int cont=0;
    List<Date> fechas = new ArrayList<Date>();
    // List<Date> fechas1 = new ArrayList<Date>();
    Date ultimaFecha = new Date();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vistaBascula= inflater.inflate(R.layout.tab2, container, false);

        Historial = vistaBascula.findViewById(R.id.historial);
        // adaptador = new AdaptadorHistorial(getActivity(), valoresPeso, fechas, altura);
        // Historial.setAdapter(adaptador);

        layoutManager = new LinearLayoutManager(getActivity());

        Historial.setLayoutManager(layoutManager);

        consultaDatos();

        return vistaBascula;
    }
    private void consultaDatos () {
        altura = "0";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
//                    .build();
//            db.setFirestoreSettings(settings);

        db.collection("USUARIOS")
                .document(user.getUid())
                .collection("Bascula")
                .orderBy("fecha", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                System.out.println(document.getId() + " => " + document.getData());
//                                System.out.println(document.getData().get("peso").getClass());

                                String numero = document.getData().get("peso").toString();
                                valoresPeso[i] = Float.parseFloat(numero);

                                altura1 = document.getData().get("altura").toString();
                                if (Float.parseFloat(altura1) > 0.0) {
                                    altura = altura1;
                                }
                                //altura = Float.parseFloat(alturaS);

                                Timestamp timestamp = document.getTimestamp("fecha");
                                fechas.add(i,timestamp.toDate());
                                ultimaFecha = timestamp.toDate();
                                i++;
                            }


                            adaptador = new AdaptadorHistorial(getActivity(), valoresPeso, fechas, altura);

                            Historial.setAdapter(adaptador);


                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
    }
    //funcion de cambio de peso
    public float cambioMedidaPeso(float pesoACambiar) {
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

    // funcion para cambiar el formato de fecha
    public SimpleDateFormat tipoFecha () {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SimpleDateFormat formato;

        if (pref.getString("fecha", "0").equals("1")) {
            formato = new SimpleDateFormat("MM/dd/yyyy");
            return formato;
        } else if (pref.getString("fecha", "0").equals("2")) {
            formato = new SimpleDateFormat("yyyy/MM/dd");
            return formato;
        } else {
            formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato;
        }
    }



}


