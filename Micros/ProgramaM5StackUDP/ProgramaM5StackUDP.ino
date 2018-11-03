/*
 * Programa que recibe los datos, en formato texto, por UDP, via wifi, del ESP32
 * Los convierte los datos a json
 * Guarda la hora del momento en que recibe cada dato y lo almacena junto a el
 * Transmite los datos por la UART a la RPI
 * 
 * Implementado: Wifi, UDP, Tiempo autonomo, conexion UART, Formato JSON.
 */
#include <M5Stack.h>
#include "WiFi.h"
#include "AsyncUDP.h"
#include "time.h"
#include <ArduinoJson.h>

#define BLANCO 0XFFFF
#define NEGRO 0
#define ROJO 0xF800
#define VERDE 0x07E0
#define AZUL 0x001F



const char * ssid = "Grupo1";
const char * password = "123456789";

const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = 3600;
const int   daylightOffset_sec = 3600;

char texto[200];
int hora;
boolean rec = 0;

AsyncUDP udp;

struct tm timeinfo;

void setup()
{
  M5.begin();
  M5.Lcd.setTextSize(2); //Tamaño del texto

  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password); //Inicializamos la conexión
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    //Serial.print(".");
  }
  //Serial.println(" CONNECTED");

  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  getLocalTime(&timeinfo);
  
  if (udp.listen(1234)) {
    //Serial.print("UDP Listening on IP: ");
    //Serial.println(WiFi.localIP());
    udp.onPacket([](AsyncUDPPacket packet) {

      int i = 200;
      while (i--) {
        *(texto + i) = *(packet.data() + i);
      }
      rec = 1;      //indica mensaje recibido

    });
  }
}

int altura;
int peso;

void loop()
{
  if (rec) {
    //Send broadcast
    rec = 0;            //mensaje procesado

    //udp.broadcastTo("Recibido", 1234);  //envia confirmacion
    //udp.broadcastTo(texto, 1234);       //y dato recibido
    //hora = atol(texto);                 //paso de texto a entero
    //Serial.print("Antes de texto");
    //Serial.println (texto);
    //Serial.println (hora);

    StaticJsonBuffer<200> jsonBufferRecv; //definición del buffer para almacenar el objero JSON, 200 máximo
    JsonObject& recibo = jsonBufferRecv.parseObject(texto); //paso de texto a formato JSON
    //recibo.printTo(Serial);       //envio por el puerto serie el objeto "recibido"

    altura = recibo["Altura"];
    peso = recibo["Peso"];

    //Serial.println();             //nueva línea
    //int segundo = recibo["Segundo"]; //extraigo el dato "Segundo" del objeto "recibido" y lo almaceno en la variable "segundo"
    //Serial.println(segundo);      //envio por el puerto serie la variable segundo

    //mandar a M%Stack
    M5.Lcd.fillScreen(NEGRO);   //borrar pantalla
    M5.Lcd.setCursor(0, 10);    //posicion inicial del cursor
    M5.Lcd.setTextColor(BLANCO);  //color del texto
    M5.Lcd.print("Altura: ");
    M5.Lcd.println(altura);
    M5.Lcd.println();
    M5.Lcd.print("Peso :");
    M5.Lcd.println(peso);
    getLocalTime(&timeinfo);
    M5.Lcd.print(&timeinfo, "%A, %B %d %Y %H:%M:%S");
    

  }

  if (Serial.available() > 0) {
    char command = (char) Serial.read();
    switch (command) {
      case 'A':
        Serial.print("Altura: ");
        Serial.print(altura);
        Serial.print(" a :");
        Serial.print(&timeinfo, "%H:%M:%S %A, %d %B %Y");
        break;
      case 'P':
        Serial.print("Peso: ");
        Serial.print(peso);
        Serial.print(" a :");
        Serial.print(&timeinfo, "%A, %B %d %Y %H:%M:%S");
        break;
    }
  }
}
