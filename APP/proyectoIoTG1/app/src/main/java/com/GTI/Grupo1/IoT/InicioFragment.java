package com.GTI.Grupo1.IoT;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    private String tiposMedidasPeso = " Kg ";
    private String tiposMedidasAltura = " cm ";

    private FirebaseUser user = MainActivity.user;


    public InicioFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.inicio, container, false);



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
                            //Añadir el texto de la bd en el layout
                            textoPeso.setText(peso + tiposMedidasPeso);
                            textoAltura.setText(altura + tiposMedidasAltura);

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

}//()
