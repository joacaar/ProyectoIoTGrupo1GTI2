package com.GTI.Grupo1.IoT;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.protobuf.StringValue;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class PesoNow extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=1;
    private static final String SERVICE_ID = "com.GTI.Grupo1.IoT";
    private static final String TAG = "Mobile:";
//    private String nameNearby = "DomoHouse.zx45b";

    TextView info;
    ProgressBar buscando;

    private ArrayList<String> dispositivos;
    private ArrayList<Dispositivo> deviceInfo;
    private ArrayAdapter<String> adaptador;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peso_now);

        Log.i("Nearby"," Despues mostrar el layout");

        // Comprobación de permisos peligrosos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }

        Log.i("Nearby"," Despues comprobar permiso");


        dispositivos =new ArrayList<String>();
        deviceInfo = new ArrayList<Dispositivo>();
        adaptador=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dispositivos);
        lista = findViewById(R.id.listaDispo);
        lista.setAdapter(adaptador);

        Log.i("Nearby"," Despues inicializar los componentes de la lista");

        info = findViewById(R.id.infoView);

//        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
//            {
//
//                Nearby.getConnectionsClient(getApplicationContext())
//                        .requestConnection(deviceInfo.get(position).getName(),deviceInfo.get(position).getEndPointId(),
//                                mConnectionLifecycleCallback)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override public void onSuccess(Void unusedResult) {
//                                Log.i(TAG, "Solicitud lanzada, falta que ambos " +"lados acepten");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override public void onFailure(@NonNull Exception e) {
//                                Log.e(TAG, "Error en solicitud de conexión", e);
//                                info.setText("Desconectado");
//                            }
//                        });
//            }
//        });

        Log.i("Nearby"," Despues esperar a que la lista escuche un click");

        startDiscovery();
        Log.i("Nearby"," Despues comenzar ");
    }

    // Gestión de permisos
    @Override public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permisos concedidos");
                } else {
                    Log.i(TAG, "Permisos denegados");
                    info.setText("Debe aceptar los permisos para comenzar");
                }
                return;
            }
        }
    }

    private void startDiscovery() {
        Nearby.getConnectionsClient(this).startDiscovery(SERVICE_ID,
                mEndpointDiscoveryCallback, new DiscoveryOptions(Strategy.P2P_STAR))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override public void onSuccess(Void unusedResult) {
                        Log.i(TAG, "Estamos en modo descubrimiento!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Modo descubrimiento no iniciado.", e);
                    }
                });
    }
    private void stopDiscovery() {
        Nearby.getConnectionsClient(this).stopDiscovery();
        Log.i(TAG, "Se ha detenido el modo descubrimiento.");
    }
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback =
            new EndpointDiscoveryCallback() {
                @Override public void onEndpointFound(String endpointId,
                                                      DiscoveredEndpointInfo discoveredEndpointInfo) {

                    Log.i(TAG, "Descubierto dispositivo con Id: " + endpointId);

                    dispositivos.add(discoveredEndpointInfo.getEndpointName());

                    Log.i(TAG, "Despues de añadir el nombre del dispositivo a la lista");
                    Log.i(TAG, "Endpoint: " + endpointId + " getId: " + discoveredEndpointInfo.getServiceId());
                    Dispositivo device = new Dispositivo(endpointId, discoveredEndpointInfo);
                    Log.i(TAG, "Despues de crear el objeto device");
                    deviceInfo.add(device);

                    Log.i(TAG, "Despues de añadir el dispositivo a la lista");


                    adaptador.notifyDataSetChanged();

                    Log.i(TAG, "Despues de notificar que los datos a mostrar han cambiado");

                }
                @Override public void onEndpointLost(String endpointId) {}
            };
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override public void onConnectionInitiated(
                        String endpointId, ConnectionInfo connectionInfo) {
                    // Aceptamos la conexión automáticamente en ambos lados.
                    Log.i(TAG, "Aceptando conexión entrante sin autenticación");
                    Nearby.getConnectionsClient(getApplicationContext())
                            .acceptConnection(endpointId, mPayloadCallback);
                }
                @Override public void onConnectionResult(String endpointId,
                                                         ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.i(TAG, "Estamos conectados!");
                            info.setText("Conectado");
                            //sendData(endpointId, texto.getText().toString());
                            sendData(endpointId, "SWITCH");
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            Log.i(TAG, "Conexión rechazada por uno o ambos lados");
                            info.setText("Desconectado");
                            break;
                        case ConnectionsStatusCodes.STATUS_ERROR:
                            Log.i(TAG, "Conexión perdida antes de poder ser " +
                                    "aceptada");
                            info.setText("Desconectado");
                            break;
                    }
                }
                @Override public void onDisconnected(String endpointId) {
                    Log.i(TAG, "Desconexión del endpoint, no se pueden " +
                            "intercambiar más datos.");
                    info.setText("Desconectado");
                }
            };
    private final PayloadCallback mPayloadCallback = new PayloadCallback() {// En este ejemplo, el móvil no recibirá transmisiones de la RP3
        @Override public void onPayloadReceived(String endpointId,
                                                Payload payload) {
            // Payload recibido
        }
        @Override public void onPayloadTransferUpdate(String endpointId,
                                                      PayloadTransferUpdate update) {
            // Actualizaciones sobre el proceso de transferencia
        }
    };
    private void sendData(String endpointId, String mensaje) {
        info.setText("Transfiriendo...");
        Payload data = null;
        try {
            data = Payload.fromBytes(mensaje.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error en la codificación del mensaje.", e);
        }
        Nearby.getConnectionsClient(this).sendPayload(endpointId, data);
        Log.i(TAG, "Mensaje enviado.");
    }


}

class Dispositivo {

    private String endPointId;
    private String name;
    private String serviceId;
    private DiscoveredEndpointInfo device;

    public Dispositivo (String EndpointId, DiscoveredEndpointInfo dispositivo){
        this.endPointId = EndpointId;
        this.name = dispositivo.getEndpointName();
        this.serviceId = dispositivo.getServiceId();
        this.device = dispositivo;
    }

    public String getEndPointId() {
        return endPointId;
    }

    public String getName() {
        return name;
    }

    public String getServiceId() {
        return serviceId;
    }

    public DiscoveredEndpointInfo getDevice() {
        return device;
    }

}