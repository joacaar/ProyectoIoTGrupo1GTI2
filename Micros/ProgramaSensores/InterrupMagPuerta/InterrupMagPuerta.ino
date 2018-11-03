


#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>

const int Pin = 5;

const char * ssid = "Grupo1";
const char * password = "123456789";

AsyncUDP udp;
StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
JsonObject& envio = jsonBuffer.createObject();    //creación del objeto "envio"

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);

  pinMode(Pin, INPUT_PULLUP);
  
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

bool puertaAbierta = false;

void loop() {
  // put your main code here, to run repeatedly:

  char texto[200];

  if (puertaAbierta) {
    if (digitalRead(Pin) == LOW) {
      delay(1000);
      if (digitalRead(Pin) == LOW) {
        envio["Puerta"] = "Principal";
        envio["Estado"] = "Cerrada";
        puertaAbierta = false;
        envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        Serial.print("Enviarndo: ");
        Serial.println(texto);
        delay(200);
      }
    }

  } else {
    if (digitalRead(Pin) == HIGH) {
      delay(1000);
      if (digitalRead(Pin) == HIGH) {
        envio["Puerta"] = "Principal";
        envio["Estado"] = "Abierta";
        puertaAbierta = true;
        envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        Serial.print("Enviarndo: ");
        Serial.println(texto);
        delay(200);
      }
    }
  }

}
