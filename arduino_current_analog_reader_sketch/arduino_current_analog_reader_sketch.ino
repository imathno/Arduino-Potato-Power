int potPen = A0;
int sensorValue;

void setup() {
  pinMode(potPen, INPUT);
  Serial.begin(9600);
}

void loop() {
  sensorValue = analogRead(potPen);
  Serial.println(toVoltage(sensorValue));
  delay(250);
}

double toVoltage(int value) {
  return value * (5.0 / 1023.0);
}
