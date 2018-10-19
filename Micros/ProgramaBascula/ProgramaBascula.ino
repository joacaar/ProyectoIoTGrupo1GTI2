/*
   Programa que se conecta a la wifi especificada para transmitir datos a M5
   Obtiene los datos de altura y peso (simulado)
   Los almacena en un json que convierte posteriormente a texto
   Envia los datos en formato texto por UDP al M5

   Implementado: Wifi, UDP, formato JSON, recopila datos del sensor.

   TO DO: Configurar el envio de datos al pulsar el pulsador, 
          faltara configurar la entrada como pulldown o en su defecto como pullup e invertir el funcionamiento
*/

#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>

const int EchoPin = 19;
const int TriggerPin = 18;
const int botonAuxiliar = 4;

const char * ssid = "Grupo1";
const char * password = "123456789";

AsyncUDP udp;
StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
JsonObject& envio = jsonBuffer.createObject();    //creación del objeto "envio"


void setup()
{
  Serial.begin(115200);

  pinMode(TriggerPin, OUTPUT);
  pinMode(EchoPin, INPUT);
  pinMode(botonAuxiliar);
  /*
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
    }
    Serial.println(" CONNECTED");

    if (udp.listen(1234)) {
      Serial.print("UDP Listening on IP: ");
      Serial.println(WiFi.localIP());

      udp.onPacket([](AsyncUDPPacket packet) {
        Serial.write(packet.data(), packet.length());
        Serial.println();
      });
    }
  */
}

void loop()
{
  if (digitalRead(4) != HIGH) {
    while (digitalRead(4) != HIGH) {
    }
    
    delay(200);
    char texto[200];

    envio["Altura"] = distancia(TriggerPin, EchoPin);
    envio["Peso"] = 65;

    envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo

    //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto

    Serial.print("Enviarndo: ");
    Serial.println(texto);
  }
}


//------------------------------------------------------------------------------------------------
//  Funciones
//------------------------------------------------------------------------------------------------

int distancia (int TriggerPin, int EchoPin) {
  long duracion, distanciaCM;
  digitalWrite(TriggerPin, LOW);
  delayMicroseconds(4);
  digitalWrite(TriggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(TriggerPin, LOW);
  duracion = pulseIn(EchoPin, HIGH);
  distanciaCM = (((duracion * 10) / 292) / 2); //medicion en cm //mínimo de 4 cm a máximo de 3 m
  return distanciaCM;
}
