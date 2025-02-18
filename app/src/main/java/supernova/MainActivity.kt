package supernova

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import supernova.R

class MainActivity : AppCompatActivity() {

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

        // Wi-Fi接続処理
        btnWifiConnect.setOnClickListener {
            tvSensorData.text = "Wi-Fi: Connecting..."
        }

        // LED制御ボタン
        btnRed.setOnClickListener {
            tvSensorData.text = "LED Color: Red"
        }
        btnBlue.setOnClickListener {
            tvSensorData.text = "LED Color: Blue"
        }
        btnGreen.setOnClickListener {
            tvSensorData.text = "LED Color: Green"
        }

        // LEDのON/OFF
        btnLedOn.setOnClickListener {
            tvSensorData.text = "LED: ON"
        }
        btnLedOff.setOnClickListener {
            tvSensorData.text = "LED: OFF"
        }
    }
}

