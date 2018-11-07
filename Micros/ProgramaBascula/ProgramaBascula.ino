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
#include "altura.h"
#include "peso.h"
#include <ArduinoJson.h>


const char * ssid = "Grupo1";
const char * password = "123456789";

AsyncUDP udp;
StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
JsonObject& envio = jsonBuffer.createObject();    //creación del objeto "envio"


void setup()
{
  Serial.begin(115200);
  configuracionAltura();
  configuracionPeso();
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

void loop()
{
  /*if (digitalRead(4) == LOW) {
    while (digitalRead(4) == LOW) {
    }*/
    
    delay(200);

    int dis = distancia();  
    double pes = peso();
    
    char texto[200];

    envio["Altura"] = dis;
    envio["Peso"] = pes;

    envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo

    udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto

    Serial.print("Enviando: ");
    Serial.println(texto);
  }

//}
