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
