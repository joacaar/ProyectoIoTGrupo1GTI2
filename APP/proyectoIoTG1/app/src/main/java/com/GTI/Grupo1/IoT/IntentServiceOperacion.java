package com.GTI.Grupo1.IoT;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.List;

import static santi.example.rpi_uart.comun.Mqtt.TAG;
import static santi.example.rpi_uart.comun.Mqtt.broker;
import static santi.example.rpi_uart.comun.Mqtt.clientId;
import static santi.example.rpi_uart.comun.Mqtt.qos;
import static santi.example.rpi_uart.comun.Mqtt.topicRoot;

public class IntentServiceOperacion extends Service implements MqttCallback, SensorEventListener {
    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int[] NOTIFICACION_ID = {1,2,3,4,5,6};

    View inicio;
    Context that=this;
    private List<Sensor> listaSensores;
    MqttClient client;

    @Override public void onCreate() {
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        listaSensores = sm.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);

        int n = 0;
        for (Sensor sensor : listaSensores) {

            sm.registerListener((SensorEventListener) that, sensor, SensorManager.SENSOR_DELAY_UI);
            n++;
        }
        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot, "App desconectada".getBytes(),
                    qos, false);

            client.connect(connOpts);
        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar.", e);
        }
        try {
            Log.i(TAG, "Suscrito a " + topicRoot+"alertas");
            client.subscribe(topicRoot+"alertas/puerta", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        try {
            Log.i(TAG, "Suscrito a " + topicRoot+"alertas");
            client.subscribe(topicRoot+"alertas/incendio", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        try {
            Log.i(TAG, "Suscrito a " + topicRoot+"alertas");
            client.subscribe(topicRoot+"alertas/intruso", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        } try {
            Log.i(TAG, "Suscrito a " + topicRoot+"alertas");
            client.subscribe(topicRoot+"alertas/apagon", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        try {
            Log.i(TAG, "Suscrito a " + topicRoot+"alertas");
            client.subscribe(topicRoot+"alertas/letargo", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }


        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "POWER");
            client.subscribe(topicRoot + "POWER", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }

        //----
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "medicamentos");
            client.subscribe(topicRoot + "medicamentos", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        //----
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "personas");
            client.subscribe(topicRoot + "personas", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        //----
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "puerta");
            client.subscribe(topicRoot + "puerta", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        //----
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "temperatura");
            client.subscribe(topicRoot + "temperatura", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
        //----
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + "humedad");
            client.subscribe(topicRoot + "humedad", qos);
            client.setCallback(this);

        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
    }
    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {


        inicio = InicioFragment.view;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 100, 300, 100});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        return START_STICKY;
    }

    @Override public IBinder onBind(Intent intencion) {
        return null;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override public void messageArrived( String topic, MqttMessage message)
            throws Exception {
        final String payload = new String(message.getPayload());
        final String Topic=new String(topic);
        Handler tHandler = new Handler(Looper.getMainLooper());
        tHandler.post(new Runnable() {

            @Override
            public void run() {

            }
        });
        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable(){

            @Override public void run() {
                if (Topic.equals(topicRoot + "alertas/puerta")) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(
                                CANAL_ID, "Mis Notificaciones",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                            R.drawable.alerta))
                                    .setContentTitle("¡Atención!")
                                    .setContentText(payload)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setAutoCancel(true)
                                    //.setVibrate(new long[]{0, 100, 200, 300})
                                    .setWhen(System.currentTimeMillis());
                    PendingIntent intencionPendiente = PendingIntent.getActivity(
                            that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                    notificacion.setContentIntent(intencionPendiente);
                    notificationManager.notify(NOTIFICACION_ID[0], notificacion.build());
                }
                if (Topic.equals(topicRoot + "alertas/intruso")) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(
                                CANAL_ID, "Mis Notificaciones",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                            R.drawable.alerta))
                                    .setContentTitle("¡Intruso!")
                                    .setContentText(payload)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    //.setVibrate(new long[]{0, 100, 200, 300})
                                    .setWhen(System.currentTimeMillis())
                                    .setAutoCancel(true);
                    PendingIntent intencionPendiente = PendingIntent.getActivity(
                            that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                    notificacion.setContentIntent(intencionPendiente);
                    notificationManager.notify(NOTIFICACION_ID[1], notificacion.build());
                }
                if (Topic.equals(topicRoot + "alertas/incendio")) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(
                                CANAL_ID, "Mis Notificaciones",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                            R.drawable.alerta))
                                    .setContentTitle("¡Fuego!")
                                    .setContentText(payload)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setAutoCancel(true)
                                    //.setVibrate(new long[]{0, 100, 200, 300})
                                    .setWhen(System.currentTimeMillis());
                    PendingIntent intencionPendiente = PendingIntent.getActivity(
                            that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                    notificacion.setContentIntent(intencionPendiente);

                    notificationManager.notify(NOTIFICACION_ID[2], notificacion.build());

                }
                if (Topic.equals(topicRoot + "alertas/apagon")) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(
                                CANAL_ID, "Mis Notificaciones",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                            R.drawable.alerta))
                                    .setContentTitle("¡Apagón!")
                                    .setContentText(payload)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setAutoCancel(true)
                                    //.setVibrate(new long[]{0, 100, 200, 300})
                                    .setWhen(System.currentTimeMillis());
                    PendingIntent intencionPendiente = PendingIntent.getActivity(
                            that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                    notificacion.setContentIntent(intencionPendiente);
                    notificationManager.notify(NOTIFICACION_ID[3], notificacion.build());
                }
                if (Topic.equals(topicRoot + "alertas/letargo")) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(
                                CANAL_ID, "Mis Notificaciones",
                                NotificationManager.IMPORTANCE_HIGH);
                        notificationChannel.setDescription("Descripcion del canal");
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    NotificationCompat.Builder notificacion =
                            new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                            R.drawable.alerta))
                                    .setContentTitle("¿Está todo bien?")
                                    .setContentText(payload)
                                    .setDefaults(Notification.DEFAULT_SOUND)
                                    .setDefaults(Notification.DEFAULT_VIBRATE)
                                    .setAutoCancel(true)
                                    //.setVibrate(new long[]{0, 100, 200, 300})
                                    .setWhen(System.currentTimeMillis());
                    PendingIntent intencionPendiente = PendingIntent.getActivity(
                            that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                    notificacion.setContentIntent(intencionPendiente);
                    notificationManager.notify(NOTIFICACION_ID[4], notificacion.build());
                }
//--------------------------------------------------
                if (!payload.isEmpty()) {
                    if (payload.equals("ON") || payload.equals("OFF")) {
                        TextView a = inicio.findViewById(R.id.luces);
                        a.setText(payload);
                    }
                    if (Topic.equals(topicRoot+"medicamentos")) {
                        TextView b = inicio.findViewById(R.id.medicamentos);
                        b.setText(payload);
                    }
                    if (Topic.equals(topicRoot+"personas")) {
                        TextView c = inicio.findViewById(R.id.personas);
                        c.setText(payload);
                    }
                    if (Topic.equals(topicRoot+"puerta")) {
                        TextView d = inicio.findViewById(R.id.puerta);
                        d.setText(payload);
                    }
                    if (Topic.equals(topicRoot+"humedad")) {
                        TextView i = inicio.findViewById(R.id.hum);
                        i.setText(payload);
                    }
                    if (Topic.equals(topicRoot+"temperatura")){
                        TextView e = inicio.findViewById(R.id.temp);
                        e.setText(payload);
                    }


                }
            }
        });
//sonoff.setText(payload);
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
    @Override public void onSensorChanged(SensorEvent event) {
        synchronized (this) {

            double modulo=Math.sqrt(event.values[0]*event.values[0]+event.values[1]*event.values[1]+event.values[2]*event.values[2]);
            if(modulo > 40){
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(
                            CANAL_ID, "Mis Notificaciones",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationChannel.setDescription("Descripcion del canal");
                    notificationManager.createNotificationChannel(notificationChannel);
                }
                NotificationCompat.Builder notificacion =
                        new NotificationCompat.Builder(IntentServiceOperacion.this, CANAL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.drawable.alerta))
                                .setContentTitle("¿Alguien se ha caído?")
                                .setContentText("Es posible que el móvil se haya caído")
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setDefaults(Notification.DEFAULT_VIBRATE)
                                .setAutoCancel(true)
                                //.setVibrate(new long[]{0, 100, 200, 300})
                                .setWhen(System.currentTimeMillis());
                PendingIntent intencionPendiente = PendingIntent.getActivity(
                        that, 0, new Intent(IntentServiceOperacion.this, MainActivity.class), 0);
                notificacion.setContentIntent(intencionPendiente);
                notificationManager.notify(NOTIFICACION_ID[5], notificacion.build());
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}