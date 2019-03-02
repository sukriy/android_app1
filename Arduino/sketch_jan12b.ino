#include "HX711.h"  //You must have this library in your arduino library folder
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
String  httpurl;
HTTPClient http;
 
HX711 scale1(D3, D4); // DT,SCK
HX711 scale2(D7, D8);
 
float calibration_factor = -96650; //-106600 worked for my 40Kg max scale setup 
  
void setup() {
  Serial.begin(115200);  
  
  WiFi.disconnect();
   WiFi.begin("Note9","1sampai8");
  while ((!(WiFi.status() == WL_CONNECTED))){
    delay(500);

  }
  Serial.println("Connected");
  
//  Serial.println("Press T to tare");
  scale1.set_scale(calibration_factor);  //Calibration Factor obtained from first sketch
  scale1.tare();             //Reset the scale to 0  
  scale2.set_scale(calibration_factor);  //Calibration Factor obtained from first sketch
  scale2.tare();             //Reset the scale to 0  
}
 
void loop() {
  String meja1 = String(scale1.get_units(), 3);
  String meja2 = String(scale2.get_units(), 3);
  Serial.println("Meja_1 = "+meja1);
  Serial.println("Meja_2 = "+meja2);

  httpurl = "http://prores.000webhostapp.com/upload_data.php?1="+meja1+"&2="+meja2;
  http.begin(httpurl);
  http.GET();
  http.end();
  Serial.println(httpurl);
  delay(2000);
}
