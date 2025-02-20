#include <DHT.h>

// Pin của các cảm biến và LED
#define PIN_PIR 7
#define PIN_LED_R 9
#define PIN_LED_G 10
#define PIN_LED_B 11
#define PIN_LED_WHITE 8  // Chuyển sang chân 8 để đảm bảo hoạt động ổn định
#define DHT_PIN 2
#define BUZZER_SIG 6
#define FLAME_SENSOR_DO 5
#define LM35_PIN A1  // Chuyển LM35 sang A1 để tránh xung đột

// Định nghĩa loại cảm biến DHT
#define DHT_TYPE DHT11

// Khởi tạo đối tượng DHT
DHT dht(DHT_PIN, DHT_TYPE);

// Biến lưu trạng thái của cảm biến PIR, cảm biến lửa và nhiệt độ
int motionDetected = 0;
int flameDetected = 1; // Mặc định là không có lửa
float temperature = 0;
bool alarmActive = false; // Trạng thái báo động
bool whiteLedOn = false; // Trạng thái của đèn trắng

// Biến đếm số lần gửi thông tin
int messageCount = 0;

void setup() {
  Serial.begin(9600);
  dht.begin();
  
  pinMode(PIN_LED_R, OUTPUT);
  pinMode(PIN_LED_G, OUTPUT);
  pinMode(PIN_LED_B, OUTPUT);
  pinMode(PIN_LED_WHITE, OUTPUT);
  pinMode(BUZZER_SIG, OUTPUT);
  pinMode(PIN_PIR, INPUT);
  pinMode(FLAME_SENSOR_DO, INPUT);
  pinMode(LM35_PIN, INPUT);
}

void loop() {
  motionDetected = digitalRead(PIN_PIR);
  flameDetected = digitalRead(FLAME_SENSOR_DO); // LOW khi có lửa
  temperature = readTemperatureLM35();

  if (motionDetected == HIGH || flameDetected == LOW) {
    alarmActive = true;
  }

  if (alarmActive) {
    setColor(255, 0, 0);
    digitalWrite(BUZZER_SIG, HIGH);
    Serial.println("Cảnh báo: Phát hiện nguy hiểm!");
  }

  if (flameDetected == LOW) { // Nếu phát hiện có lửa
    digitalWrite(PIN_LED_WHITE, HIGH); // Giữ LED trắng luôn sáng
    whiteLedOn = true;
    Serial.println("Phát hiện cháy, bật đèn trắng!");
  }

  if (whiteLedOn) {
    float tempAtExit = readTemperatureLM35();
    if (tempAtExit >= 50) {
      digitalWrite(PIN_LED_WHITE, LOW); // Nếu nhiệt độ lối thoát tăng lên, tắt đèn trắng
      whiteLedOn = false;
      Serial.println("Nhiệt độ tại lối thoát tăng lên, tắt đèn trắng!");
    } else {
      digitalWrite(PIN_LED_WHITE, HIGH); // Giữ LED trắng sáng liên tục nếu chưa đạt ngưỡng nhiệt độ
    }
  }

  float dhtTemperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  
  if (isnan(dhtTemperature) || isnan(humidity)) {
    Serial.println("Lỗi đọc dữ liệu từ cảm biến DHT!");
    return;
  }

  if (messageCount >= 20) {
    Serial.print("\033[2J\033[H"); // Xóa màn hình Serial Monitor
    messageCount = 0;
  }

  Serial.print("Nhiệt độ: ");
  Serial.print(dhtTemperature);
  Serial.print(" C, Độ ẩm: ");
  Serial.print(humidity);
  Serial.print(" %, Trạng thái: ");
  Serial.print(motionDetected == HIGH ? "Có chuyển động | " : "Không có chuyển động | ");
  Serial.println(flameDetected == LOW ? "Có lửa" : "Không có lửa");
  
  messageCount++;
  
  if (!alarmActive) {
    if (dhtTemperature >= 27) {
      setColor(0, 0, 255);
    } else {
      setColor(255, 255, 0);
    }
  }

  delay(1000);
}

void setColor(int red, int green, int blue) {
  analogWrite(PIN_LED_R, 255 - red);
  analogWrite(PIN_LED_G, 255 - green);
  analogWrite(PIN_LED_B, 255 - blue);
}

float readTemperatureLM35() {
  float sum = 0;
  for (int i = 0; i < 5; i++) {
    sum += analogRead(LM35_PIN) * (5.0 / 1023.0) * 100.0;
    delay(10);
  }
  return sum / 5.0; // Trả về giá trị trung bình để giảm nhiễu
}
