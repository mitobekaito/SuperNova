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
import supernova.utils.FlameManager
import supernova.utils.MotionManager
import supernova.ui.MotionAlertActivity
import supernova.ui.FlameAlertActivity
import androidx.constraintlayout.widget.ConstraintSet.Motion


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMoving: TextView
    private lateinit var btnLEDOn: Button
    private lateinit var btnLEDOff: Button
    private lateinit var btnSoundOn: Button
    private lateinit var btnSoundOff: Button
    private lateinit var btnFireOn: Button
    private lateinit var btnFireOff: Button
    private lateinit var btnGoToSecond: Button
    private lateinit var btnsupernova: Button
    private lateinit var btnRESET: Button


    private var isAlertShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupButtonListeners()

        // ✅ アプリ起動時に最新データを取得
        SensorDataManager.fetchLatestSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }

        // ✅ 5秒ごとにセンサーデータを更新
        SensorDataManager.startFetchingSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }

        btnsupernova.setOnClickListener {
            tvMoving.text = "FIRE"
        }
    }

    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvUpdated)
        btnsupernova = findViewById(R.id.btnsupernova)

        btnLEDOn = findViewById(R.id.btnLEDOn)
        btnLEDOff = findViewById(R.id.btnLEDOff)
        btnSoundOn = findViewById(R.id.btnSoundOn)
        btnSoundOff = findViewById(R.id.btnSoundOff)
        btnFireOn = findViewById(R.id.btnFireOn)
        btnFireOff = findViewById(R.id.btnFireOff)

        btnGoToSecond = findViewById(R.id.btnGoToSecond)

        btnGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // ✅ すべてのボタンの初期状態を ON に設定
        ButtonManager.setInitialButtonState(this, btnLEDOn, btnLEDOff)
        ButtonManager.setInitialButtonState(this, btnSoundOn, btnSoundOff)
        ButtonManager.setInitialButtonState(this, btnFireOn, btnFireOff)
    }

    private fun setupButtonListeners() {
        ButtonManager.setupButtonListeners(
            this,
            btnLEDOn, btnLEDOff,
            btnSoundOn, btnSoundOff,
            btnFireOn, btnFireOff,
            btnRESET,btnsupernova,
        )
    }

    private fun handleSensorData(motionDetected: Boolean, flameDetected: Boolean) {
        val isMotionAlertOn = btnSoundOn.tag == "ON"
        val isFireAlertOn = btnFireOn.tag == "ON"

        if (motionDetected && isMotionAlertOn) {
            handleMotionAlert()
        } else if (flameDetected && isFireAlertOn) {
            handleFlameAlert()
        }
    }

    private fun handleMotionAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            startActivity(Intent(this, MotionAlertActivity::class.java))
        }
    }

    private fun handleFlameAlert() {
        if (!isAlertShown) {
            isAlertShown = true
            startActivity(Intent(this, FlameAlertActivity::class.java))
        }
    }
}
