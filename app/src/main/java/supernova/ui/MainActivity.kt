package supernova.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMoving: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 5000L // 5秒ごとに更新

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UIコンポーネントの取得
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvMoving)

        val btnWifiConnect: Button = findViewById(R.id.btnWifiConnect)
        val btnRed: Button = findViewById(R.id.btnRed)
        val btnBlue: Button = findViewById(R.id.btnBlue)
        val btnGreen: Button = findViewById(R.id.btnGreen)
        val btnLEDOn: Button = findViewById(R.id.btnLedOn)
        val btnLEDOff: Button = findViewById(R.id.btnLedOff)
        val btnSoundOn: Button = findViewById(R.id.btnSoundOn)
        val btnSoundOff: Button = findViewById(R.id.btnSoundOff)
        val btnAlarmOn: Button = findViewById(R.id.btnAlarmOn)
        val btnAlarmOff: Button = findViewById(R.id.btnAlarmOff)

        // 初期状態を設定
        updateToggleButtons(true, btnLEDOn, btnLEDOff)
        updateToggleButtons(true, btnSoundOn, btnSoundOff)
        updateToggleButtons(true, btnAlarmOn, btnAlarmOff)

        // 温度・湿度の初期値
        tvTemperature.text = "--°C"
        tvHumidity.text = "Humidity: --%"

        // Mongo DB接続処理
        btnWifiConnect.setOnClickListener {
            tvMoving.text = "Mongo DB: Connecting..."
        }

        // LED制御ボタン
        btnRed.setOnClickListener { tvMoving.text = "LED Color: Red" }
        btnBlue.setOnClickListener { tvMoving.text = "LED Color: Blue" }
        btnGreen.setOnClickListener { tvMoving.text = "LED Color: Green" }

        // ON/OFFボタン
        btnLEDOn.setOnClickListener { updateToggleButtons(true, btnLEDOn, btnLEDOff) }
        btnLEDOff.setOnClickListener { updateToggleButtons(false, btnLEDOn, btnLEDOff) }
        btnSoundOn.setOnClickListener { updateToggleButtons(true, btnSoundOn, btnSoundOff) }
        btnSoundOff.setOnClickListener { updateToggleButtons(false, btnSoundOn, btnSoundOff) }
        btnAlarmOn.setOnClickListener { updateToggleButtons(true, btnAlarmOn, btnAlarmOff) }
        btnAlarmOff.setOnClickListener { updateToggleButtons(false, btnAlarmOn, btnAlarmOff) }

        // センサーデータの定期取得開始
        startFetchingSensorData()
    }

    // センサーデータを取得して UI に表示
    private fun startFetchingSensorData() {
        handler.post(object : Runnable {
            override fun run() {
                viewModel.fetchSensorData { data ->
                    tvTemperature.text = "${data.temperature}°C"
                    tvHumidity.text = "Humidity: ${data.humidity}%"
                    tvMoving.text = if (data.motion_detected) "Motion: Detected" else "Motion: None"
                }
                handler.postDelayed(this, updateInterval) // 5秒ごとに更新
            }
        })
    }

    // ボタンの状態を更新する共通関数
    private fun updateToggleButtons(isOn: Boolean, btnOn: Button, btnOff: Button) {
        val onColor = ContextCompat.getColor(this, R.color.dark_yellow)
        val offColor = ContextCompat.getColor(this, R.color.gray)
        val textColorOn = ContextCompat.getColor(this, R.color.black)
        val textColorOff = ContextCompat.getColor(this, R.color.white)

        if (isOn) {
            btnOn.setBackgroundColor(onColor)
            btnOn.setTextColor(textColorOn)
            btnOff.setBackgroundColor(offColor)
            btnOff.setTextColor(textColorOff)
        } else {
            btnOn.setBackgroundColor(offColor)
            btnOn.setTextColor(textColorOff)
            btnOff.setBackgroundColor(onColor)
            btnOff.setTextColor(textColorOn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // ハンドラをクリア
    }
}
