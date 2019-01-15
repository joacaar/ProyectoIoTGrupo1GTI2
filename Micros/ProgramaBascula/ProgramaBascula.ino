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
  if (pes >= 1 && aux == false) {
    delay(2000);
    char texto[200];

    envio["Altura"] = dis;
    envio["Peso"] = peso();

    Serial.println(pes);

    envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo

    udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto

    Serial.print("Enviando: ");
    Serial.println(texto);
    //listaDePesos[cuantos]=pes;//rellenamos el array con medidas de peso significativas mientras este vaya variando con cada llamada a la funcion peso();
    //cuantos++;
    aux = true;
  }
  if (pes < 1) {
    aux = false;
  }
  /*if(cuantos !=0 && pes<1){//Si hay valores en el array de pesos y la balanza ya no tiene ningún peso significativo encima
    double peso=0;//Variable a la que asignaremos el peso bueno despues de realizar un bucle para calcular la moda
    int cont=0;
    for(int i=0; i<=cuantos; i++){ //bucle que determina la media de los valores más abundantes que estén cercanos (con precisión de menos de 0.5kg de momento) entre sí
      int cont2=0;
      double listaParaHacerMedia[10];
      for(int j=0; j<=cuantos; j++){
        if (listaDePesos[i]>listaDePesos[j]-0.5 && listaDePesos[i]<listaDePesos[j]+0.5){
          listaParaHacerMedia[cont2]=listaDePesos[j];
          cont2++;
          }

        }
         if(cont2 >= cont){

          cont=cont2;
          int cont3=0;
          for(int k=0; k<=cont2; k++){
            cont3+=listaParaHacerMedia[k];
            }
        peso = (cont3/(cont2)); //la media de los pesos cercanos
          }
      }*/




}
