package supernova

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvTemperature: TextView = findViewById(R.id.tvTemperature)
        val tvHumidity: TextView = findViewById(R.id.tvHumidity)
        val tvMoving: TextView = findViewById(R.id.tvMoving)
        val btnWifiConnect: Button = findViewById(R.id.btnWifiConnect)
        val btnRed: Button = findViewById(R.id.btnRed)
        val btnBlue: Button = findViewById(R.id.btnBlue)
        val btnGreen: Button = findViewById(R.id.btnGreen)
        val btnLedOn: Button = findViewById(R.id.btnLedOn)
        val btnLedOff: Button = findViewById(R.id.btnLedOff)

        // ON/OFFボタン
        val btnSoundOn: Button = findViewById(R.id.btnSoundOn)
        val btnSoundOff: Button = findViewById(R.id.btnSoundOff)

        // アラームON/OFFボタン
        val btnAlarmOn: Button = findViewById(R.id.btnAlarmOn)
        val btnAlarmOff: Button = findViewById(R.id.btnAlarmOff)

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


        // サウンドONボタンの処理
        btnSoundOn.setOnClickListener {
            updateToggleButtons(isOn = true, btnSoundOn, btnSoundOff)
        }

        // サウンドOFFボタンの処理
        btnSoundOff.setOnClickListener {
            updateToggleButtons(isOn = false, btnSoundOn, btnSoundOff)
        }


        // アラームONボタンの処理
        btnAlarmOn.setOnClickListener {
            updateToggleButtons(isOn = true, btnAlarmOn, btnAlarmOff)
        }

        // アラームOFFボタンの処理
        btnAlarmOff.setOnClickListener {
            updateToggleButtons(isOn = false, btnAlarmOn, btnAlarmOff)
        }
    }

    // ボタンの状態を更新する共通関数
    private fun updateToggleButtons(isOn: Boolean, btnOn: Button, btnOff: Button) {
        if (isOn) {
            btnOn.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            btnOn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnOff.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            btnOff.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            btnOn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            btnOn.setTextColor(ContextCompat.getColor(this, R.color.white))
            btnOff.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            btnOff.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }

}
