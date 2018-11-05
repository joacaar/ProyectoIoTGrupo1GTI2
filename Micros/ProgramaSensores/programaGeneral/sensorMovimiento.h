const int LEDPin = 5;        // pin para el LED
const int PIRPin = 16;         // pin de entrada (para PIR sensor)
 
int pirState = LOW;           // de inicio no hay movimiento
int val = 0;                  // estado del pin

 //creación del objeto "envio"
 
void configuracionSensorMovimiento() 
{
   pinMode(LEDPin, OUTPUT); 
   pinMode(PIRPin, INPUT);
}
 
bool lecturaMovimiento()
{
   val = digitalRead(PIRPin);
   if (val == HIGH)   //si está activado
   { 
      digitalWrite(LEDPin, HIGH);  //LED ON
      if (pirState == LOW)  //si previamente estaba apagado
      {
        pirState = HIGH; //Hay alguien
        return true;
      }
   } 
   else   //si esta desactivado
   {
      digitalWrite(LEDPin, LOW); // LED OFF
      if (pirState == HIGH)  //si previamente estaba encendido
      {
        pirState = LOW; //No hay nadie
        return false;
      }
   }
}
