package supernova.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import supernova.utils.AlarmManager
import supernova.utils.SensorDataManager
import supernova.utils.ButtonManager

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMoving: TextView
    private lateinit var btnLEDOn: Button
    private lateinit var btnLEDOff: Button
    private lateinit var btnSoundOn: Button
    private lateinit var btnSoundOff: Button
    private lateinit var btnAlarmOn: Button
    private lateinit var btnAlarmOff: Button

    private var isAlertShown = false // 警告画面の連続表示防止フラグ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupButtonListeners()

        // ✅ アプリ起動時に一度最新データを取得
        SensorDataManager.fetchLatestSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected ->
            handleSensorData(motionDetected)
        }

        // ✅ 5秒ごとにセンサーデータを更新
        SensorDataManager.startFetchingSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected ->
            handleSensorData(motionDetected)
        }
    }

    // ✅ UIの初期化
    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvMoving)

        btnLEDOn = findViewById(R.id.btnLedOn)
        btnLEDOff = findViewById(R.id.btnLedOff)
        btnSoundOn = findViewById(R.id.btnSoundOn)
        btnSoundOff = findViewById(R.id.btnSoundOff)
        btnAlarmOn = findViewById(R.id.btnAlarmOn)
        btnAlarmOff = findViewById(R.id.btnAlarmOff)
    }

    // ✅ ボタンのリスナーを設定
    private fun setupButtonListeners() {
        ButtonManager.setupButtonListeners(
            this,
            btnLEDOn, btnLEDOff,
            btnSoundOn, btnSoundOff,
            btnAlarmOn, btnAlarmOff
        )
    }

    // ✅ センサーデータの処理
    private fun handleSensorData(motionDetected: Boolean) {
        if (motionDetected) {
            handleAlert()
        } else {
            isAlertShown = false // 🚀 モーションが検知されなくなったらリセット
        }
    }

    // ✅ 警告画面と音の処理
    private fun handleAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            startActivity(Intent(this, AlertActivity::class.java)) // 🚀 修正
        }
    }
}

