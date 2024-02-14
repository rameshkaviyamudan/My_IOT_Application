#include <SoftwareSerial.h>
#include "Wire.h"
#include "RichShieldLightSensor.h"
#include "RichShieldTM1637.h"
#include "DHT.h"
#include <ArduinoJson.h> // Library for handling JSON data

#define DEBUG true
// Pins for lamp and fan control
const int lampButtonPin = 5; // Lamp button connected to digital pin 5
const int fanButtonPin = 8;  // Fan button connected to digital pin 8
// Define initial states of the lamp and fan
int lampState;
int fanState;
bool lampButtonChanged = false;
bool fanButtonChanged = false;
// Pins for lamp and fan control
int lampPin = 6;
int fanPin = 9;
SoftwareSerial ESP01(2, 3); // RX, TX

// Pins for sensors and display
#define CLK 10 // CLK of the TM1637 IC connect to D10 of OPEN-SMART UNO R3
#define DIO 11 // DIO of the TM1637 IC connect to D11 of OPEN-SMART UNO R3
#define LIGHTSENSOR_PIN A0 // SIG pin of Rocker Switch module connect to A0 of IO Shield, that is pin A2 of OPEN-SMART UNO R3
#define IRSENSOR_PIN 4 // IR sensor pin
#define DHTPIN 7    // Digital pin connected to the DHT sensor
#define DHTTYPE DHT11 // Change this to DHT22 or DHT21 if you're using a different sensor

// WiFi credentials
const char* ssid = "eee-iot";
const char* password = "NTl3tCYDwOTI";

// Initialize DHT sensor
DHT dht(DHTPIN, DHTTYPE);

// Initialize TM1637 display and light sensor
TM1637 disp(CLK, DIO);
LightSensor lightsensor(LIGHTSENSOR_PIN);

// ThingSpeak API key
String apiKey = "C7K4LI6SFXXPFSPJ"; // API key for the single channel
String channelID = "2348974"; // Channel ID for ThingSpeak
int lampButtonLastState = HIGH; // Previous state of the lamp button
int fanButtonLastState = HIGH;  // Previous state of the fan button
int lampButtonState;
int fanButtonState;
void setup() {
  pinMode(lampButtonPin, INPUT_PULLUP); // Set lamp button pin as input with internal pull-up resistor
  pinMode(fanButtonPin, INPUT_PULLUP);  // Set fan button pin as input with internal pull-up resistor
  pinMode(lampPin, OUTPUT);
  pinMode(fanPin, OUTPUT);
  Serial.begin(9600);
  while (!Serial) {} // Wait for Serial Monitor to open
  Serial.println("Starting...");
  ESP01.begin(9600);
  // Connect to WiFi
  ESP01.println("AT+CWJAP=\"" + String(ssid) + "\",\"" + String(password) + "\"");
  dht.begin();
  disp.init();
  
  // Attach interrupts for lamp and fan buttons
  //attachInterrupt(digitalPinToInterrupt(lampButtonPin), lampState = !lampState, RISING);
  //attachInterrupt(digitalPinToInterrupt(fanButtonPin), fanButtonPressed, RISING);
}

void loop() {
  
  // Read temperature, humidity, light intensity, and IR sensor
  float temp = dht.readTemperature();
  float humi = dht.readHumidity();
  int irValue = digitalRead(IRSENSOR_PIN);
  int brightness = map(lightsensor.getRes(), 10000, 300, 1, 100); // Map light sensor reading to brightness

  // Send data to ThingSpeak
 sendDataToThingspeak(temp, humi, irValue, brightness);
// Check button states and update variables accordingly
  checkButtonState();

  // If the fan button state changed, send fan state to ThingSpeak
  if (fanButtonChanged) {
    sendFanStateToThingspeak();
    fanButtonChanged = false; // Reset the flag
  }
    // If the lamp button state changed, send lamp state to ThingSpeak
  if (lampButtonChanged) {
    sendLampStateToThingspeak();
    lampButtonChanged = false; // Reset the flag
  }
  // Fetch lamp and fan states from ThingSpeak
  fetchFanState();
  fetchLampState();

  // 15 seconds delay between updates
  //delay(15000);
}
void sendLampStateToThingspeak() {
  // Make TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += "184.106.153.149"; // ThingSpeak IP address
  cmd += "\",80\r\n";
  sendData(cmd, 2000, DEBUG);

  // Prepare GET string for lamp control
  String lampGetStr = "GET /update?api_key=" + apiKey + "&field5=" + String(lampState) + "\r\n";
  // Send data length & GET string for lamp control
  ESP01.print("AT+CIPSEND=");
  ESP01.println(lampGetStr.length());
  delay(500);
  if (ESP01.find(">")) {
    Serial.print(">");
    sendData(lampGetStr, 2000, DEBUG);
  }
}
void lampButtonPressed() {
  // Toggle the lamp state
  lampState = !lampState;
  // Update the lamp pin
  digitalWrite(lampPin, lampState);
  // Print the updated state to the serial monitor
  Serial.print("Lamp state: ");
  Serial.println(lampState ? "ON" : "OFF");
}

void fanButtonPressed() {
  // Toggle the fan state
  fanState = !fanState;
  // Update the fan pin
  digitalWrite(fanPin, fanState);
  // Print the updated state to the serial monitor
  Serial.print("Fan state: ");
  Serial.println(fanState ? "ON" : "OFF");
}

String sendData(String command, const int timeout, boolean debug) {
  String response = "";
  ESP01.print(command);
  long int time = millis();
  while ((time + timeout) > millis()) {
    while (ESP01.available()) {
      char c = ESP01.read();
      response += c;
    }
  }
  
  if (debug) {
    Serial.print(response);
  }
  return response;
}

// Function to send data to ThingSpeak
// Function to send data to ThingSpeak
void sendDataToThingspeak(float temp, float humi, int irValue, int brightness) {
  // Make TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += "184.106.153.149"; // ThingSpeak IP address
  cmd += "\",80\r\n";
  sendData(cmd, 2000, DEBUG);

  // Prepare GET string
  String getStr = "GET /update?api_key=";
  getStr += apiKey;
  getStr += "&field1=";
  getStr += String(temp);
  getStr += "&field2=";
  getStr += String(humi);
  getStr += "&field3=";
  getStr += String(irValue);
  getStr += "&field6=";
  getStr += String(brightness);
  getStr += "\r\n";

  // Send data length & GET string
  ESP01.print("AT+CIPSEND=");
  ESP01.println(getStr.length());
  Serial.print("AT+CIPSEND=");
  Serial.println(getStr.length());
  delay(500);
  if (ESP01.find(">")) {
    Serial.print(">");
    sendData(getStr, 2000, DEBUG);
  }
}

void fetchFanState() {
  // Make TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += "184.106.153.149"; // ThingSpeak IP address
  cmd += "\",80\r\n";
  sendData(cmd, 2000, DEBUG);

  // Prepare GET string for fan control
  String fanGetStr = "GET /channels/" + channelID + "/fields/4/last.csv\r\n";
  // Send data length & GET string for fan control
  ESP01.print("AT+CIPSEND=");
  ESP01.println(fanGetStr.length());
  delay(500);
  if (ESP01.find(">")) {
    Serial.print(">");
    String fanReply = sendData(fanGetStr, 2000, DEBUG);
    char num = fanReply.length();
    Serial.println("--- Actuator value from Thingspeak: ---");
    Serial.println(fanReply[num - 10]);
    if (fanReply[num - 10] == '0') {
      Serial.println("--- 0 => Turning off ---");
      fanState = LOW;
      digitalWrite(fanPin, fanState);
    } else {
      Serial.println("--- 1 => Turning on ---");
      fanState = HIGH;
      digitalWrite(fanPin, fanState);
    }
  }
}

void fetchLampState() {
  // Make TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += "184.106.153.149"; // ThingSpeak IP address
  cmd += "\",80\r\n";
  sendData(cmd, 2000, DEBUG);

  // Prepare GET string for lamp control
  String lampGetStr = "GET /channels/" + channelID + "/fields/5/last.csv\r\n";
  // Send data length & GET string for lamp control
  ESP01.print("AT+CIPSEND=");
  ESP01.println(lampGetStr.length());
  delay(500);
  if (ESP01.find(">")) {
    Serial.print(">");
    String lampReply = sendData(lampGetStr, 2000, DEBUG);
    char num = lampReply.length();
    Serial.println("--- Actuator value from Thingspeak: ---");
    Serial.println(lampReply[num - 10]);
    if (lampReply[num - 10] == '0') {
      Serial.println("--- 0 => Turning off ---");
      lampState = LOW;
      digitalWrite(lampPin, lampState);
    } else {
      Serial.println("--- 1 => Turning on ---");
      lampState = HIGH;
      digitalWrite(lampPin, lampState);
    }
  }
}
void checkButtonState() {
  // Check lamp button state
  // Check for a button press (transition from HIGH to LOW)
  if (digitalRead(lampButtonPin) == HIGH) {
    // Toggle the lamp state
    lampState = !lampState;
    // Update the lamp pin
    digitalWrite(lampPin, lampState);
    // Print the updated state to the serial monitor
    Serial.print("Lamp state: ");
    Serial.println(lampState ? "ON" : "OFF");
    delay(500); // Debounce delay
     // Set the flag to indicate that the lamp button state changed
    lampButtonChanged = true;
  }

  // Check fan button state
  if (digitalRead(fanButtonPin) == HIGH) {
    // Toggle the fan state
    fanState = !fanState;
    // Update the fan pin
    digitalWrite(fanPin, fanState);
    // Print the updated state to the serial monitor
    Serial.print("Fan state: ");
    Serial.println(fanState ? "ON" : "OFF");
    delay(500); // Debounce delay
        // Set the flag to indicate that the fan button state changed
    fanButtonChanged = true;
  }
}
void sendFanStateToThingspeak() {
  // Make TCP connection
  String cmd = "AT+CIPSTART=\"TCP\",\"";
  cmd += "184.106.153.149"; // ThingSpeak IP address
  cmd += "\",80\r\n";
  sendData(cmd, 2000, DEBUG);

  // Prepare GET string for fan control
  String fanGetStr = "GET /update?api_key=" + apiKey + "&field4=" + String(fanState) + "\r\n";
  // Send data length & GET string for fan control
  ESP01.print("AT+CIPSEND=");
  ESP01.println(fanGetStr.length());
  delay(500);
  if (ESP01.find(">")) {
    Serial.print(">");
    sendData(fanGetStr, 2000, DEBUG);
  }
}

