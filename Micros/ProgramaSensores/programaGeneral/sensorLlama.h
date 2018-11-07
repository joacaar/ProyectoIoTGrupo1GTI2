const int sensorPin = 26;
 bool fuego = false;

void LlamaSetup()
{
 
   pinMode(sensorPin, INPUT);
}
 
void medirFuego(JsonObject& envio, char ( &texto )[1000])
{
   if (fuego) {
    if (digitalRead(sensorPin) == HIGH) {
      delay(1000);
 
    envio["Incendio"] = "No hay fuego";
    envio.printTo(texto);         //paso del objeto "envio" a texto para transmitirlo
    Serial.print("Enviando: ");
    Serial.println(texto);
        
        fuego = false;
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        delay(200);
       
      
    }

  } else {
    if (digitalRead(sensorPin) == LOW) {
      delay(1000);
     
    envio["Incendio"] = "¡¡HAY FUEGO!!";
    envio.printTo(texto); 
    Serial.print("Enviando: ");
    Serial.println(texto);
       fuego = true;
        //udp.broadcastTo(texto, 1234); //se envía por el puerto 1234 el JSON como texto
        delay(200);

        
      

    }
  }
}
