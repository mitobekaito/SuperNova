package supernova.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var btnGoToSecond: Button // 🚀 SecondActivity へ遷移するボタンを追加

    private var isAlertShown = false // 警告画面の連続表示防止フラグ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupButtonListeners()

        // ✅ アプリ起動時に一度最新データを取得
        SensorDataManager.fetchLatestSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }

        // ✅ 5秒ごとにセンサーデータを更新
        SensorDataManager.startFetchingSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }

    }

    // ✅ UIの初期化
    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvMoving)

        btnLEDOn = findViewById(R.id.btnLEDOn)
        btnLEDOff = findViewById(R.id.btnLEDOff)
        btnSoundOn = findViewById(R.id.btnSoundOn)
        btnSoundOff = findViewById(R.id.btnSoundOff)
        btnAlarmOn = findViewById(R.id.btnAlarmOn)
        btnAlarmOff = findViewById(R.id.btnAlarmOff)

        // 🚀 SecondActivity に遷移するボタンを追加
        btnGoToSecond = findViewById(R.id.btnGoToSecond)

        // ボタンを押すと `SecondActivity` に遷移
        btnGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
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
// ✅ センサーデータの処理
    private fun handleSensorData(motionDetected: Boolean, flameDetected: Boolean) {
        println("🚨 モーション検知: $motionDetected, 火災検知: $flameDetected")
        if (motionDetected) {
            handleMotionAlert()
        } else if (flameDetected) {
            handleFlameAlert()
        } else {
            isAlertShown = false // 🚀 モーションも火災も検知されなくなったらリセット
        }
    }


    // ✅ モーション検知時のアラート処理
    private fun handleMotionAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            startActivity(Intent(this, MotionAlertActivity::class.java)) // 🚀 モーション検知の画面へ
        }
    }

    // ✅ 火災検知時のアラート処理
    private fun handleFlameAlert() {
        println("🔥 火災アラート画面を表示")
        if (!isAlertShown) {
            isAlertShown = true
            startActivity(Intent(this, FlameAlertActivity::class.java)) // 🔥 火災検知の画面へ
        }
    }

}
