package com.GTI.Grupo1.IoT;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class BasculaFragment extends Fragment  {
    View vistaGraficas;
    View vistaBascula;
    private RecyclerView Historial;
    public AdaptadorHistorial adaptador;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUser user = MainActivity.user;
    float ultimoPeso;
    float[] valoresPeso = new float[5];
    float[] valoresPeso1 = new float[10];
    String altura;

    List<Date> fechas = new ArrayList<Date>();
    List<Date> fechas1 = new ArrayList<Date>();
    Date ultimaFecha = new Date();
    boolean fabPulsado=false;

    public BasculaFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment

        vistaBascula = inflater.inflate(R.layout.bascula, container, false);

        consultaDatos();
        vistaGraficas=vistaBascula.findViewById(R.id.esteConstraint);

        Historial = vistaBascula.findViewById(R.id.historial);
        adaptador = new AdaptadorHistorial(getActivity(), valoresPeso1, fechas1, altura);
        Historial.setAdapter(adaptador);

        layoutManager = new LinearLayoutManager(getActivity());
        Historial.setLayoutManager(layoutManager);
        FloatingActionButton fab = vistaBascula.findViewById(R.id.fab);
        ObjectAnimator animation = ObjectAnimator.ofFloat(Historial, "translationY", -2000f);
        animation.setDuration(500);
        animation.start();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabPulsado){

                    //vistaGraficas.setAlpha(0);

                    Historial.setVisibility(View.VISIBLE);
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(Historial, "translationY", 0f);
                    animation2.setDuration(700);
                    animation2.start();
                    fabPulsado=true;
                }else{
                    ObjectAnimator animation = ObjectAnimator.ofFloat(Historial, "translationY",  -2000f);
                    animation.setDuration(700);
                    animation.start();
                    if(!animation.isRunning()) {

                        Historial.setVisibility(View.INVISIBLE);
                        // vistaGraficas.setAlpha(1);
                    }

                    fabPulsado=false;
                }
            }
        });

        return vistaBascula;
    }

//    private void consultaDatosRT(){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("coleccion").addSnapshotListener(
//                new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
//                                        @Nullable FirebaseFirestoreException e) {
//
//                    }
//                }
//        );

//        db.collection("coleccion").document("documento").addSnapshotListener(
//                new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot snapshot,
//                                        @Nullable FirebaseFirestoreException e){
//                        if (e != null) {
//                            Log.e("Firebase", "Error al leer", e);
//                        } else if (snapshot == null || !snapshot.exists()) {
//                            Log.e("Firebase", "Error: documento no encontrado ");
//                        } else {
//                            Log.e("Firestore", "datos:" + snapshot.getData());
//                        }
//                    }
//                });
    //}

    private void consultaDatos (){
        altura="187";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
//                    .build();
//            db.setFirestoreSettings(settings);

        db.collection("USUARIOS")
                .document(user.getUid())
                .collection("Bascula")
                .orderBy("fecha", Query.Direction.ASCENDING)
                .limit(5)
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

                                altura = document.getData().get("altura").toString();
                                //altura = Float.parseFloat(alturaS);

                                Timestamp timestamp = document.getTimestamp("fecha");
                                fechas.add(timestamp.toDate());
                                ultimaFecha = timestamp.toDate();
                                i++;
                            }

                            generateData();
                            datoUltimoPeso();
                            graficaCircular();
                            mostrarAltura();

                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });
/*
        //System.out.println(db.collection("pruebaDatosBascula").getId());
        db.collection("pruebaDatosBascula")
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
                                valoresPeso1[i] = Float.parseFloat(numero);

                                altura = document.getData().get("altura").toString();
                                //altura = Float.parseFloat(alturaS);

                                Timestamp timestamp = document.getTimestamp("fecha");
                                fechas1.add(timestamp.toDate());
                                ultimaFecha = timestamp.toDate();
                                i++;
                            }

                            generateData();
                            datoUltimoPeso();
                            graficaCircular();
                            mostrarAltura();

                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });*/
        for(int i=0; i<5; i++) {
            String numero = "67";
            valoresPeso[i] = Float.parseFloat(numero)+i;
            Timestamp timestamp = Timestamp.now();
            fechas.add(timestamp.toDate());
        }

        for(int i=0; i<10; i++) {
            String numero2 = "50";
            valoresPeso1[i] = Float.parseFloat(numero2)+i*i;
            SimpleDateFormat formateador = tipoFecha();
            Timestamp timestamp1 = Timestamp.now();
            fechas1.add(timestamp1.toDate());
        }
        generateData();
        datoUltimoPeso();
        graficaCircular();
        mostrarAltura();

    }

    private void graficaCircular (){
        PieChartView pieChartView = (PieChartView) vistaBascula.findViewById(R.id.graficaInicio);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(10, Color.parseColor("#2b778c")).setLabel("Grasa corporal"));
        pieData.add(new SliceValue(23, Color.parseColor("#56b0ca")).setLabel("Masa corporal"));
        pieData.add(new SliceValue(17, Color.parseColor("#f1a378")).setLabel("Agua"));
        //pieData.add(new SliceValue(50, Color.WHITE).setLabel(""));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasCenterCircle(true);
        SimpleDateFormat formateador = tipoFecha();
        pieChartData.setCenterText1(formateador.format(ultimaFecha)).setCenterText1FontSize(15);
        pieChartView.setPieChartData(pieChartData);
        pieChartView.setChartRotation(180, true);
        pieChartView.setChartRotationEnabled(false);
    }

    private void mostrarAltura (){
        TextView vistaAltura = vistaBascula.findViewById(R.id.altura);
        float altura = cambioMedidaAltura(Float.parseFloat(this.altura));
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (pref.getString("altura", "0").equals("1")) {
            vistaAltura.setText(altura + " ft");
        } else if (pref.getString("altura", "0").equals("2")) {
            vistaAltura.setText(altura + " in");
        } else {
            vistaAltura.setText(altura + " cm");
        }

    }


    private void generateData() {
        int numSubcolumns = 1;
        int numColumns = 5;

        ColumnChartView chart = (ColumnChartView) vistaBascula.findViewById(R.id.chart);

        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {

                //float pesoValor = (float) Math.random() * 50f + 5;

                if(i == numColumns-1){
                    values.add(new SubcolumnValue(cambioMedidaPeso(valoresPeso[i]), Color.parseColor("#56b0ca")));
                    ultimoPeso = cambioMedidaPeso(valoresPeso[i]);
                }else {
                    values.add(new SubcolumnValue(cambioMedidaPeso(valoresPeso[i]), Color.parseColor("#2b778c")));
                }
            }

            Column column = new Column(values);
//                ColumnChartValueFormatter formatter = new ColumnChartValueFormatter() {
//                    @Override
//                    public int formatChartValue(char[] formattedValue, SubcolumnValue value) {
//                        return 0;
//                    }
//                }
            column.setFormatter(new SimpleColumnChartValueFormatter(2));
            column.setHasLabels(true);// muestra el valor de la columna
            column.setHasLabelsOnlyForSelected(false);//muestra el valor de la columna al pulsar en ella
            columns.add(column);
        }

        ColumnChartData data = new ColumnChartData(columns);

        AxisValue axisValueX;
        List<AxisValue> valores = new ArrayList<AxisValue>();

        String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingas"};
        String[] diasPesado = new String[numColumns];
        SimpleDateFormat formateador = tipoFecha();

//            for(int i = 0; i<numColumns; i++){
//                diasPesado[i] = formateador.format(fechas.get(i));
//            }

        for (int i = 0; i < numColumns; i++){
            diasPesado[i] = formateador.format(fechas.get(i));
            axisValueX = new AxisValue(i).setLabel(diasPesado[i]);// se le asigna a cada posicion el label que se desea
            // "i" es el valor del indice y dias es el string que mostrara el label
            valores.add(axisValueX);//añadimos cada valor del eje x a una lista
        }

        Axis axisX = new Axis().setValues(valores);//cuando creamos el eje le pasamos la lista de los valores que tendra el eje
        Axis axisY = Axis.generateAxisFromRange(0, 150, 10);// para añadir un rango al eje Y
        axisY.setHasLines(true);

        // Añadimos titulo a los indices
        axisX.setName("Dias del mes");
        axisY.setName("Peso");

        // asignamos cada eje a su posicion en la grafica
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        //Le pasamos toda la informacion a la vista de la grafica
        chart.setColumnChartData(data);

    }

    private void datoUltimoPeso (){
        TextView vistaPeso = vistaBascula.findViewById(R.id.peso);
        vistaPeso.setText(Float.toString(ultimoPeso));
    }

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

