package com.GTI.Grupo1.IoT;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClasePrueba extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=1;
    private static final String SERVICE_ID = "com.appsmoviles.joanca.nearbyconnections";


    List<String> list = new ArrayList<>();
    Map<String, DiscoveredEndpointInfo> dispositivos = new HashMap<>();

    String nombreCasa;

    //Elementos para el recyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorPesoNow adaptador;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peso_now);
    }

}
