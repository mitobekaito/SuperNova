package supernova.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import supernova.R
import supernova.ui.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSensorData: TextView = findViewById(R.id.tvSensorData)
        val btnWifiConnect: Button = findViewById(R.id.btnWifiConnect)
        val btnRed: Button = findViewById(R.id.btnRed)
        val btnBlue: Button = findViewById(R.id.btnBlue)
        val btnGreen: Button = findViewById(R.id.btnGreen)
        val btnLedOn: Button = findViewById(R.id.btnLedOn)
        val btnLedOff: Button = findViewById(R.id.btnLedOff)
        val btnFetchData: Button = findViewById(R.id.btnFetchData) // XML の ID に合わせて変更
        val btnSendData: Button = findViewById(R.id.btnSendData)   // XML の ID に合わせて変更

        // Wi-Fi接続処理（仮実装）
        btnWifiConnect.setOnClickListener {
            tvSensorData.text = "Wi-Fi: Connecting..."
            Toast.makeText(this, "Wi-Fi に接続します...", Toast.LENGTH_SHORT).show()
            // TODO: 実際の Wi-Fi 接続処理を追加
        }

        // LED制御ボタン
        btnRed.setOnClickListener {
            tvSensorData.text = "LED Color: Red"
            // TODO: LEDの色変更処理を実装
        }
        btnBlue.setOnClickListener {
            tvSensorData.text = "LED Color: Blue"
            // TODO: LEDの色変更処理を実装
        }
        btnGreen.setOnClickListener {
            tvSensorData.text = "LED Color: Green"
            // TODO: LEDの色変更処理を実装
        }

        // LEDのON/OFF
        btnLedOn.setOnClickListener {
            tvSensorData.text = "LED: ON"
            // TODO: LED ON処理
        }
        btnLedOff.setOnClickListener {
            tvSensorData.text = "LED: OFF"
            // TODO: LED OFF処理
        }

        // センサーデータの取得
        btnFetchData.setOnClickListener {
            viewModel.fetchSensorData { data ->
                tvSensorData.text = "Temperature: ${data.temperature}°C\nMotion: ${if (data.motion_detected) "Detected" else "None"}"
            }
            Toast.makeText(this, "データ取得中...", Toast.LENGTH_SHORT).show()
        }

        // センサーデータの送信
        btnSendData.setOnClickListener {
            val randomTemp = (20..35).random().toDouble() // 20〜35℃のランダム値
            viewModel.sendSensorData(temperature = randomTemp, motionDetected = true)
            Toast.makeText(this, "データ送信中...\nTemp: $randomTemp", Toast.LENGTH_SHORT).show()
        }
    }
}
