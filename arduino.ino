#include <DHT.h>

// Pin của các cảm biến và LED
#define PIN_PIR 7
#define PIN_LED_R 9
#define PIN_LED_G 10
#define PIN_LED_B 11
#define DHT_PIN 2

// Định nghĩa loại cảm biến DHT
#define DHT_TYPE DHT11

// Khởi tạo đối tượng DHT
DHT dht(DHT_PIN, DHT_TYPE);

// Biến lưu trạng thái của cảm biến PIR
int motionDetected = 0;

void setup() {
  // Bắt đầu giao tiếp Serial
  Serial.begin(9600);
  
  // Khởi tạo cảm biến DHT
  dht.begin();
  
  // Cài đặt các chân LED RGB làm đầu ra
  pinMode(PIN_LED_R, OUTPUT);
  pinMode(PIN_LED_G, OUTPUT);
  pinMode(PIN_LED_B, OUTPUT);
  
  // Khởi tạo cảm biến PIR
  pinMode(PIN_PIR, INPUT);
}

void loop() {
  // Đọc giá trị cảm biến PIR
  motionDetected = digitalRead(PIN_PIR);
  
  // Đọc nhiệt độ và độ ẩm từ cảm biến DHT
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();
  
  // Kiểm tra xem cảm biến có lỗi không
  if (isnan(temperature) || isnan(humidity)) {
    Serial.println("Lỗi đọc dữ liệu từ cảm biến DHT!");
    return;
  }
  
  // In ra thông tin nhiệt độ và độ ẩm
  Serial.print("Nhiệt độ: ");
  Serial.print(temperature);
  Serial.print(" C, Độ ẩm: ");
  Serial.print(humidity);
  Serial.println(" %");
  
  // Chuyển màu LED dựa trên nhiệt độ
  if (temperature > 27) {
    // Nếu nhiệt độ trên 27°C, LED sẽ màu xanh dương
    setColor(0, 0, 255);  // Màu xanh dương
  } else {
    // Nếu nhiệt độ dưới 27°C, LED sẽ màu vàng
    setColor(255, 255, 0); // Màu vàng
  }
  
  // Nếu có chuyển động, đổi LED sang màu đỏ
  if (motionDetected == HIGH) {
    setColor(255, 0, 0);  // Màu đỏ
    Serial.println("Phát hiện chuyển động!");
  }
  
  // Đợi một chút trước khi đọc lại cảm biến
  delay(1000);
}

void setColor(int red, int green, int blue) {
  // Điều khiển các chân LED RGB (vì LED Cathode chung, giá trị lớn là tắt, giá trị nhỏ là sáng)
  analogWrite(PIN_LED_R, 255 - red);   // Chân đỏ
  analogWrite(PIN_LED_G, 255 - green); // Chân xanh lá
  analogWrite(PIN_LED_B, 255 - blue);  // Chân xanh dương
}
