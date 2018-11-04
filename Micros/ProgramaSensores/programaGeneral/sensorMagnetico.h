
 //creación del objeto "envio"


//Constantes y variables globales
const int Pin = 25;
bool puertaAbierta = false;

void configuracionPuerta() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  pinMode(Pin, INPUT_PULLUP);
}

//PUERTA
bool lecturaPuerta(){
  

  if (puertaAbierta) {
    if (digitalRead(Pin) == LOW) {
      delay(1000);
      if (digitalRead(Pin) == LOW) {
        
        puertaAbierta = false;
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        delay(200);
        return true;
      }
    }

  } else {
    if (digitalRead(Pin) == HIGH) {
      delay(1000);
      if (digitalRead(Pin) == HIGH) {
        puertaAbierta = true;
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        delay(200);
        return false;
      }
    }
  }
}//lecturaPuerta()

