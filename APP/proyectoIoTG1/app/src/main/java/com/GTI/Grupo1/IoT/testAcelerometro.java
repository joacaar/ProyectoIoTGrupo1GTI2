package com.GTI.Grupo1.IoT;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class testAcelerometro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();*/
        setContentView(R.layout.temperatura);
    }
}
