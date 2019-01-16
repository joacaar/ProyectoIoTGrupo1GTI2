package com.GTI.Grupo1.IoT;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class Bascula extends Fragment {
    View vistaGraficas;
    View vistaBascula;

    private FirebaseUser user = MainActivity.user;
    float ultimoPeso;
    float[] valoresPeso = new float[10];
    // float[] valoresPeso1 = new float[10];
    String altura;
    String altura1;

    List<Date> fechas = new ArrayList<Date>();
    // List<Date> fechas1 = new ArrayList<Date>();
    Date ultimaFecha = new Date();
    //boolean fabPulsado=false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vistaBascula= inflater.inflate(R.layout.tab1, container, false);
        consultaDatos();
        vistaGraficas=vistaBascula.findViewById(R.id.esteConstraint);




        return vistaBascula;
    }

    private void consultaDatos (){
        altura="Tu altura";
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
                                if (Float.parseFloat(altura1)>0.0){
                                    altura=altura1;
                                }
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

                            // adaptador = new AdaptadorHistorial(getActivity(), valoresPeso, fechas, altura);
                            // Historial.setAdapter(adaptador);


                        } else {
                            System.out.println("Error getting documents." + task.getException());
                        }
                    }
                });



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
        int numColumns;
        if(fechas.size()<5) {
            numColumns = fechas.size();
        }else{
            numColumns=5;
        }
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
