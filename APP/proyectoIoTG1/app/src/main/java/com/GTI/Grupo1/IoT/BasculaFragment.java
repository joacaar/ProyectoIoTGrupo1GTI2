package com.GTI.Grupo1.IoT;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.lang.Double.*;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class BasculaFragment extends Fragment {

    View vistaInicio;

    float ultimoPeso;
    float[] valoresPeso = new float[5];

    List<java.util.Date> fechas;


    public BasculaFragment() {
        // Required empty public constructor

    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            //Inflate the layout for this fragment

            vistaInicio = inflater.inflate(R.layout.bascula, container, false);

            consultaDatos();

            return vistaInicio;
        }

        private void graficaCircular (){
            PieChartView pieChartView = (PieChartView) vistaInicio.findViewById(R.id.graficaInicio);
            List<SliceValue> pieData = new ArrayList<>();
            pieData.add(new SliceValue(10, Color.parseColor("#2b778c")).setLabel("Grasa corporal"));
            pieData.add(new SliceValue(23, Color.parseColor("#56b0ca")).setLabel("Masa corporal"));
            pieData.add(new SliceValue(17, Color.parseColor("#f1a378")).setLabel("Agua"));
            //pieData.add(new SliceValue(50, Color.WHITE).setLabel(""));
            PieChartData pieChartData = new PieChartData(pieData);
            pieChartData.setHasLabels(true);
            pieChartData.setHasCenterCircle(true);
            pieChartData.setCenterText1("20/11/2018").setCenterText1FontSize(15);
            pieChartView.setPieChartData(pieChartData);
            pieChartView.setChartRotation(180, true);
            pieChartView.setChartRotationEnabled(false);
        }


        private void generateData() {
            int numSubcolumns = 1;
            int numColumns = 5;

            ColumnChartView chart = (ColumnChartView) vistaInicio.findViewById(R.id.chart);

            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;

            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {

                    //float pesoValor = (float) Math.random() * 50f + 5;

                    if(i == numColumns-1){
                        values.add(new SubcolumnValue(valoresPeso[i], Color.parseColor("#56b0ca")));
                        ultimoPeso = valoresPeso[i];
                    }else {
                        values.add(new SubcolumnValue(valoresPeso[i], Color.parseColor("#2b778c")));
                    }
                }

                Column column = new Column(values);
                column.setHasLabels(true);// muestra el valor de la columna
                column.setHasLabelsOnlyForSelected(false);//muestra el valor de la columna al pulsar en ella
                columns.add(column);
            }

            ColumnChartData data = new ColumnChartData(columns);

            AxisValue axisValueX;
            List<AxisValue> valores = new ArrayList<AxisValue>();

            String[] dias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingas"};

            for (int i = 0; i < numColumns; i++){
                axisValueX = new AxisValue(i).setLabel(dias[i]);// se le asigna a cada posicion el label que se desea
                // "i" es el valor del indice y dias es el string que mostrara el label
                valores.add(axisValueX);//añadimos cada valor del eje x a una lista
            }

            Axis axisX = new Axis().setValues(valores);//cuando creamos el eje le pasamos la lista de los valores que tendra el eje
            Axis axisY = Axis.generateAxisFromRange(0, 100, 5);// para añadir un rango al eje Y
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
            TextView vistaPeso = vistaInicio.findViewById(R.id.peso);
            vistaPeso.setText(Float.toString(ultimoPeso));
        }

        public void consultaDatos (){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
//                    .build();
//            db.setFirestoreSettings(settings);

            db.collection("pruebaDatosBascula")
                    .orderBy("fecha", Query.Direction.ASCENDING)
                    .limit(5)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int i = 0;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println(document.getId() + " => " + document.getData());
                                    System.out.println(document.getData().get("peso").getClass());

                                    String numero = document.getData().get("peso").toString();
                                    valoresPeso[i] = Float.parseFloat(numero);

                                    //Double numero2 = document.getData().get("peso");

                                    //valoresPeso[i] = document.getData().get("peso");

                                    Timestamp timestamp = document.getTimestamp("fecha");
                                    fechas.add(timestamp.toDate());
                                    i++;
                                }

                                generateData();
                                datoUltimoPeso();
                                graficaCircular();

                            } else {
                                System.out.println("Error getting documents." + task.getException());
                            }
                        }
                    });
        }
    }

