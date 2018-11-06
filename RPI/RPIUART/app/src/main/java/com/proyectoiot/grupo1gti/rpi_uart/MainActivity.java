package com.proyectoiot.grupo1gti.rpi_uart;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Lista de UART disponibles: " + ArduinoUart.disponibles());
        ArduinoUart uart = new ArduinoUart("UART0", 115200);
        Log.d(TAG, "Mandado a Arduino: A");
        uart.escribir("A");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String altura = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+altura);

        Log.d(TAG, "Mandado a Arduino: P");
        uart.escribir("P");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String peso = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+peso);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> datos = new HashMap<>();
        datos.put("Altura", altura);
        datos.put("Peso", peso);
        db.collection("coleccion").document("documento").update(datos);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
