package supernova

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTemperature: TextView = findViewById(R.id.tvTemperature)
        val tvHumidity: TextView = findViewById(R.id.tvHumidity)
        val tvMoving: TextView = findViewById(R.id.tvMoving)
        val tvMotionStatus: TextView = findViewById(R.id.tvMotionStatus)
        val tvAlarmStatus: TextView = findViewById(R.id.tvAlarmStatus)
        val btnWifiConnect: Button = findViewById(R.id.btnWifiConnect)
        val btnRed: Button = findViewById(R.id.btnRed)
        val btnBlue: Button = findViewById(R.id.btnBlue)
        val btnGreen: Button = findViewById(R.id.btnGreen)
        val btnLedOn: Button = findViewById(R.id.btnLedOn)
        val btnLedOff: Button = findViewById(R.id.btnLedOff)

        // 温度の初期表示を "--°C" のみに設定
        tvTemperature.text = "--°C"

        // 湿度
        tvHumidity.text = "Humidity: --%"


        // Wi-Fi接続処理
        btnWifiConnect.setOnClickListener {
            tvMoving.text = "Wi-Fi: Connecting..."
        }


        // LED制御ボタン
        btnRed.setOnClickListener {
            tvMoving.text = "LED Color: Red"
        }
        btnBlue.setOnClickListener {
            tvMoving.text = "LED Color: Blue"
        }
        btnGreen.setOnClickListener {
            tvMoving.text = "LED Color: Green"
        }

        // LEDのON/OFF
        btnLedOn.setOnClickListener {
            tvMoving.text = "LED: ON"
        }
        btnLedOff.setOnClickListener {
            tvMoving.text = "LED: OFF"
        }


        // 動体検知の状態を切り替える例（テスト用）
        tvMotionStatus.setOnClickListener {
            if (tvMotionStatus.text == "Motion Detected: No") {
                tvMotionStatus.text = "Motion Detected: Yes"
            } else {
                tvMotionStatus.text = "Motion Detected: No"
            }
        }

        // アラームのON/OFFを切り替える例（テスト用）
        tvAlarmStatus.setOnClickListener {
            if (tvAlarmStatus.text == "Alarm: OFF") {
                tvAlarmStatus.text = "Alarm: ON"
            } else {
                tvAlarmStatus.text = "Alarm: OFF"
            }
        }
    }
}
