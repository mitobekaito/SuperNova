#include <DHT.h>

// Pin của các cảm biến và LED
#define PIN_PIR 7
#define PIN_LED_R 9
#define PIN_LED_G 10
#define PIN_LED_B 11
#define DHT_PIN 2
#define BUZZER_SIG 6
#define FLAME_SENSOR_DO 5

// Định nghĩa loại cảm biến DHT
#define DHT_TYPE DHT11

// Khởi tạo đối tượng DHT
DHT dht(DHT_PIN, DHT_TYPE);

// Biến lưu trạng thái của cảm biến PIR và cảm biến lửa
int motionDetected = 0;
int flameDetected = 0;
bool alarmTriggered = false;

// Biến đếm số lần gửi thông tin
int messageCount = 0;

void setup() {
  Serial.begin(9600);
  dht.begin();
  
  pinMode(PIN_LED_R, OUTPUT);
  pinMode(PIN_LED_G, OUTPUT);
  pinMode(PIN_LED_B, OUTPUT);
  pinMode(BUZZER_SIG, OUTPUT);
  pinMode(PIN_PIR, INPUT);
  pinMode(FLAME_SENSOR_DO, INPUT);
}

void loop() {
  motionDetected = digitalRead(PIN_PIR);
  flameDetected = digitalRead(FLAME_SENSOR_DO); // LOW khi có lửa

  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  
  if (isnan(temperature) || isnan(humidity)) {
    Serial.println("Lỗi đọc dữ liệu từ cảm biến DHT!");
    return;
  }

  Serial.print("Nhiệt độ: "); Serial.print(temperature); Serial.print(" C | ");
  Serial.print("Độ ẩm: "); Serial.print(humidity); Serial.print(" % | ");
  Serial.print("Chuyển động: "); Serial.print(motionDetected == HIGH ? "Có" : "Không"); Serial.print(" | ");
  Serial.print("Lửa: "); Serial.println(flameDetected == LOW ? "Có" : "Không");

  if (motionDetected == HIGH || flameDetected == LOW) {
    alarmTriggered = true;
  }

  if (alarmTriggered) {
    setColor(255, 0, 0);
    digitalWrite(BUZZER_SIG, HIGH);
    if (motionDetected == HIGH) {
      Serial.println("Cảnh báo: Phát hiện chuyển động!");
    }
    if (flameDetected == LOW) {
      Serial.println("Cảnh báo: Phát hiện lửa!");
    }
    delay(500);
    return; // Dừng các hoạt động khác, báo động chỉ dừng khi reset mạch
  }

  if (messageCount >= 20) {
    Serial.print("\033[2J\033[H"); // Xóa màn hình Serial Monitor
    messageCount = 0;
  }

  messageCount++;
  
  if (temperature >= 27) {
    setColor(0, 0, 255);
  } else {
    setColor(255, 255, 0);
  }

  delay(1000);
}

void setColor(int red, int green, int blue) {
  analogWrite(PIN_LED_R, 255 - red);
  analogWrite(PIN_LED_G, 255 - green);
  analogWrite(PIN_LED_B, 255 - blue);
}