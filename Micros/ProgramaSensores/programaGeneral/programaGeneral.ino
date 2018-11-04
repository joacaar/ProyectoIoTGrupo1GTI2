/*
   Programa que se conecta a la wifi especificada para transmitir datos a M5
   Obtiene los datos de altura y peso (simulado)
   Los almacena en un json que convierte posteriormente a texto
   Envia los datos en formato texto por UDP al M5

   Implementado: Wifi, UDP, formato JSON, recopila datos del sensor.

   TO DO: 
*/

#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>
#include "sensorMagnetico.h"
#include "sensorMovimiento.h"


const char * ssid = "Grupo1";
const char * password = "123456789";

void setup() {
  
  Serial.begin(115200);
  Serial.println("Inicializando...");
  configuracionPuerta();
  configuracionSensorMovimiento();

//--------------CONEXION WIFI----------------
  WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
      delay(500);
      Serial.print(".");
    }
    Serial.println(" CONNECTED");
/*
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

void loop() {
  // put your main code here, to run repeatedly:

  // Si no se usasen variables globales
  /*int PinMag = 5;  //Pin del sensor magnético
  int PinMov = 16; //Pin delsensor de movimiento
  int PinLedMov = 5; //Pin del led del circuito del sensor de movimiento*/
  
  lecturaPuerta();
  lecturaMovimiento();


  
}
