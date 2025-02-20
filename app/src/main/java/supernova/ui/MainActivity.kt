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

    private var isMotionDetectionEnabled = false  // ✅ Motion Detected の ON/OFF 状態を管理
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

        // ✅ ボタンの初期状態を設定
        ButtonManager.setupButtonListeners(
            this,
            btnLEDOn, btnLEDOff,
            btnSoundOn, btnSoundOff,
            btnFireOn, btnFireOff,
            btnAlarmOn, btnAlarmOff
        )

        // ✅ Mongo DB 接続ボタンの処理修正
        btnMongoDBConnect.setOnClickListener {
            tvMoving.text = "Mongo DB: Connecting..."
        }

    }

    // ✅ UIの初期化
    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvMoving)
        btnMongoDBConnect = findViewById(R.id.btnMongoDBConnect)

        btnLEDOn = findViewById(R.id.btnLEDOn)
        btnLEDOff = findViewById(R.id.btnLEDOff)
        btnSoundOn = findViewById(R.id.btnSoundOn)
        btnSoundOff = findViewById(R.id.btnSoundOff)
        btnFireOn = findViewById(R.id.btnFireOn)
        btnFireOff = findViewById(R.id.btnFireOff)
        btnAlarmOn = findViewById(R.id.btnAlarmOn)
        btnAlarmOff = findViewById(R.id.btnAlarmOff)

        // ✅ `btnSoundOn` , 'btnFireOn' の初期状態を ON に設定
        btnSoundOn.tag = "ON"
        btnSoundOff.tag = "OFF"
        btnFireOn.tag = "ON"
        btnFireOff.tag = "OFF"

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

        // ✅ Motion Detected ボタンのリスナー設定（追加）
        btnSoundOn.setOnClickListener {
            isMotionDetectionEnabled = true  // ✅ ON にする
            updateMotionButtonState()       // ✅ ボタンの見た目を更新
        }
        btnSoundOff.setOnClickListener {
            isMotionDetectionEnabled = false // ✅ OFF にする
            updateMotionButtonState()        // ✅ ボタンの見た目を更新
        }
    }

    // ✅ Motion Detected ボタンの UI 更新（追加）
    private fun updateMotionButtonState() {
        if (isMotionDetectionEnabled) {
            btnSoundOn.setBackgroundColor(resources.getColor(R.color.dark_yellow))
            btnSoundOn.setTextColor(resources.getColor(R.color.black))
            btnSoundOff.setBackgroundColor(resources.getColor(R.color.gray))
            btnSoundOff.setTextColor(resources.getColor(R.color.white))
        } else {
            btnSoundOn.setBackgroundColor(resources.getColor(R.color.gray))
            btnSoundOn.setTextColor(resources.getColor(R.color.white))
            btnSoundOff.setBackgroundColor(resources.getColor(R.color.dark_yellow))
            btnSoundOff.setTextColor(resources.getColor(R.color.black))
        }
    }

    // ✅ センサーデータの処理
    private fun handleSensorData(motionDetected: Boolean, flameDetected: Boolean) {
        println("🚨 モーション検知: $motionDetected, 火災検知: $flameDetected")

        // ✅ Motion Detected / Fire Detected ボタンの ON 状態を判定
        val isMotionAlertOn = btnSoundOn.tag == "ON"
        val isFireAlertOn = btnFireOn.tag == "ON"

        if (motionDetected && isMotionAlertOn) {
            handleMotionAlert()
        } else if (flameDetected && isFireAlertOn) {
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
