#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include <ArduinoJson.h>
#include "sensorMagnetico.h"

const char * ssid = "Grupo1";
const char * password = "123456789";

void setup() {
  
  Serial.begin(115200);
  Serial.println("Inicializando...");
  configuracionPuerta();
}

void loop() {
  // put your main code here, to run repeatedly:
  int Pin = 5;

  lecturaPuerta(Pin);
  

  
}
