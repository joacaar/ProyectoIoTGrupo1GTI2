package com.proyectoiot.grupo1gti.rpi_uart;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
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

        //A
        Log.d(TAG, "Mandado a Arduino: A");
        uart.escribir("A");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String altura = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+altura);


        //P
        Log.d(TAG, "Mandado a Arduino: P");
        uart.escribir("P");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String peso = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+ peso);

        //E
        Log.d(TAG, "Mandado a Arduino: E");
        uart.escribir("E");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String puerta = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+ puerta);

        //F
        Log.d(TAG, "Mandado a Arduino: F");
        uart.escribir("F");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.w(TAG, "Error en sleep()", e);
        }
        String movimiento = uart.leer();
        Log.d(TAG, "Recibido de Arduino: "+ movimiento);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
       // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
