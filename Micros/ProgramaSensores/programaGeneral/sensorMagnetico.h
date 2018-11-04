#include <M5Stack.h>

AsyncUDP udp;
StaticJsonBuffer<200> jsonBuffer;                 //tamaño maximo de los datos
JsonObject& envio = jsonBuffer.createObject(); //creación del objeto "envio"


//Constantes y variables globales
const int Pin = 5;
bool puertaAbierta = false;
char texto2[200];
void configuracionPuerta() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  pinMode(Pin, INPUT_PULLUP);
  if (digitalRead(Pin) == LOW) {

	  envio["Puerta"] = "Principal";
	  envio["Estado"] = "Cerrada";
	  puertaAbierta = false;
	  envio.printTo(texto2);         //paso del objeto "envio" a texto para transmitirlo
	  //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
	  Serial.print("Enviando: ");
	  Serial.println(texto2);
	 

  }
  else if(digitalRead(Pin)== HIGH){
	  envio["Puerta"] = "Principal";
	  envio["Estado"] = "Abierta";
	  puertaAbierta = true;
	  envio.printTo(texto2);         //paso del objeto "envio" a texto para transmitirlo
	  //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
	  Serial.print("Enviando: ");
	  Serial.println(texto2);
  }
}

//PUERTA
void lecturaPuerta(int Pin){
  
  char texto[200];

  if (puertaAbierta) {
    if (digitalRead(Pin) == LOW) {
      delay(1000);
     
        envio["Puerta"] = "Principal";
        envio["Estado"] = "Cerrada";
        puertaAbierta = false;
        envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        Serial.print("Enviando: ");
        Serial.println(texto);
        delay(200);
      
    }

  } else {
    if (digitalRead(Pin) == HIGH) {
      delay(1000);
    
        envio["Puerta"] = "Principal";
        envio["Estado"] = "Abierta";
        puertaAbierta = true;
        envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        Serial.print("Enviando: ");
        Serial.println(texto);
        delay(200);
      }
    }
  }
}//lecturaPuerta()

