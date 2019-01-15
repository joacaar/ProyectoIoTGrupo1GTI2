package com.GTI.Grupo1.IoT;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;


public class TemperaturaActivity extends Activity {

    private int numero = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperatura);

        TextView t = findViewById(R.id.textotemp);
        t.setText(String.valueOf(numero));

    }

    public void Sumar (View view){
        TextView t = findViewById(R.id.textotemp);
        numero++;
        t.setText(String.valueOf(numero));
    }
    public void Restar (View view){
        TextView t = findViewById(R.id.textotemp);
        numero--;
        t.setText(String.valueOf(numero));
    }

}
