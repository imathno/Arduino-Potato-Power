int potPen = A0;
int readValue;

void setup() {
  pinMode(potPen, INPUT);
  Serial.begin(9600);
}

void loop() {
  readValue = analogRead(potPen);
  Serial.println(readValue);
  delay(250);
}
