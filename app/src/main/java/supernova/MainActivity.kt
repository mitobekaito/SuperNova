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

        // ON/OFFボタン
        val btnLEDOn: Button = findViewById(R.id.btnLEDOn)
        val btnLEDOff: Button = findViewById(R.id.btnLEDOff)

        // ON/OFFボタン
        val btnSoundOn: Button = findViewById(R.id.btnSoundOn)
        val btnSoundOff: Button = findViewById(R.id.btnSoundOff)

        // アラームON/OFFボタン
        val btnAlarmOn: Button = findViewById(R.id.btnAlarmOn)
        val btnAlarmOff: Button = findViewById(R.id.btnAlarmOff)

        // **初期状態を設定**
        updateToggleButtons(isOn = true, btnLEDOn, btnLEDOff) // LED: ON
        updateToggleButtons(isOn = true, btnSoundOn, btnSoundOff) // Motion Detected: ON
        updateToggleButtons(isOn = true, btnAlarmOn, btnAlarmOff) // Alarm: ON

        // 温度の初期表示を "--°C" のみに設定
        tvTemperature.text = "--°C"

        // 湿度
        tvHumidity.text = "Humidity: --%"


        // Mongo DB接続処理
        btnWifiConnect.setOnClickListener {
            tvMoving.text = "Mongo DB: Connecting..."
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


        // LED ONボタンの処理
        btnLEDOn.setOnClickListener {
            updateToggleButtons(isOn = true, btnLEDOn, btnLEDOff)
        }

        // LED OFFボタンの処理
        btnLEDOff.setOnClickListener {
            updateToggleButtons(isOn = false, btnLEDOn, btnLEDOff)
        }

        // Motion Detected ONボタンの処理
        btnSoundOn.setOnClickListener {
            updateToggleButtons(isOn = true, btnSoundOn, btnSoundOff)
        }

        // Motion Detected OFFボタンの処理
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
            // 背景の黄色を少し暗くする
            btnOn.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_yellow))
            btnOn.setTextColor(ContextCompat.getColor(this, R.color.black)) // ON時の文字色を黒に

            btnOff.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            btnOff.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            btnOn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            btnOn.setTextColor(ContextCompat.getColor(this, R.color.white))

            // 背景の黄色を少し暗くする
            btnOff.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_yellow))
            btnOff.setTextColor(ContextCompat.getColor(this, R.color.black)) // OFF時の文字色を黒に
        }
    }


}
