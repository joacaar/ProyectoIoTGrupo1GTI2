/*
   Programa que se conecta a la wifi especificada para transmitir datos a M5
   Obtiene los datos de altura y peso (simulado)
   Los almacena en un json que convierte posteriormente a texto
   Envia los datos en formato texto por UDP al M5

   Implementado: Wifi, UDP, formato JSON, recopila datos del sensor.

   TO DO:
*/
#include "SSD1306.h" // alias for `#include "SSD1306Wire.h"'
#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include "altura.h"
#include "peso.h"
#include <ArduinoJson.h>


// Initialize the OLED display using Wire library
SSD1306  display(0x3c, 5, 4);


const char * ssid = "Grupo1";
const char * password = "123456789";

AsyncUDP udp;
StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
JsonObject& envio = jsonBuffer.createObject();    //creación del objeto "envio"


void setup(){
  Serial.begin(115200);

  display.init();
  display.flipScreenVertically();
  //display.setColor(WHITE);
  display.setTextAlignment(TEXT_ALIGN_CENTER);
  //display.setFont(ArialMT_Plain_24);
  
  configuracionAltura();
  configuracionPeso();
  
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
  }
}


bool aux = false;
double listaDePesos[30];
int cuantos = 0;
void loop()
{
  /*if (digitalRead(4) == LOW) {
    while (digitalRead(4) == LOW) {
    }*/

  // delay(500);
  int dis = distancia();
  double pes = peso();
  //if (pes >= 1 && aux == false) {
    delay(2000);
    char texto[200];

    envio["altura"] = dis;
    envio["peso"] = peso();

    envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo

    udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto

    Serial.println(texto);
    display.clear();
    display.drawString(30, 20, String(50));
    display.drawString(60, 20, String(20));
    //listaDePesos[cuantos]=pes;//rellenamos el array con medidas de peso significativas mientras este vaya variando con cada llamada a la funcion peso();
    //cuantos++;
    aux = true;
  //}
  //if (pes < 1) {
    aux = false;
  //}
}
