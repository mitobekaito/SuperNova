package supernova.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import supernova.utils.SensorDataManager
import supernova.utils.ButtonManager
import supernova.utils.LedManager
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMoving: TextView
    private lateinit var btnMongoDBConnect: Button
    private lateinit var btnLEDOn: Button
    private lateinit var btnLEDOff: Button
    private lateinit var btnSoundOn: Button
    private lateinit var btnSoundOff: Button
    private lateinit var btnFireOn: Button
    private lateinit var btnFireOff: Button
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


        // ✅ Mongo DB 接続ボタンの処理修正
        btnMongoDBConnect.setOnClickListener {
            tvMoving.text = "Mongo DB: Connecting..."
        }

    }

    // ✅ UIの初期化
    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvUpdated)
        btnMongoDBConnect = findViewById(R.id.btnMongoDBConnect)

        btnLEDOn = findViewById(R.id.btnLEDOn)
        btnLEDOff = findViewById(R.id.btnLEDOff)
        btnSoundOn = findViewById(R.id.btnSoundOn)
        btnSoundOff = findViewById(R.id.btnSoundOff)
        btnFireOn = findViewById(R.id.btnFireOn)
        btnFireOff = findViewById(R.id.btnFireOff)
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
            btnFireOn, btnFireOff,
            btnAlarmOn, btnAlarmOff
        )

        // ✅ LED ON ボタンのクリックリスナーを設定
        btnLEDOn.setOnClickListener {
            println("🟢 LED ON ボタンが押されました")
            LedManager.sendLedCommand("ON") // ✅ LED ON コマンドを送信
        }

        // ✅ LED OFF ボタンのクリックリスナーを設定
        btnLEDOff.setOnClickListener {
            println("🔴 LED OFF ボタンが押されました")
            LedManager.sendLedCommand("OFF") // ✅ LED OFF コマンドを送信
        }
    }


    // ✅ センサーデータの処理
    private fun handleSensorData(motionDetected: Boolean, flameDetected: Boolean) {
        println("🚨 モーション検知: $motionDetected, 火災検知: $flameDetected")

        // ✅ 最新データが前回のデータと異なる場合、アラートフラグをリセット
        if (motionDetected || flameDetected) {
            isAlertShown = false
        }

        // ✅ モーションまたは火災検知時のアラート処理
        if (motionDetected) {
            handleMotionAlert()
        } else if (flameDetected) {
            handleFlameAlert()
        }
    }

    // ✅ モーション検知時のアラート処理
    private fun handleMotionAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            println("🚀 モーションアラート画面を表示")
            startActivity(Intent(this, MotionAlertActivity::class.java))
        }
    }

    // ✅ 火災検知時のアラート処理
    private fun handleFlameAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            println("🔥 火災アラート画面を表示")
            startActivity(Intent(this, FlameAlertActivity::class.java))
        }
    }


}
