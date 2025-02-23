package supernova.ui


import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import supernova.utils.ButtonManager
import supernova.utils.SensorDataManager


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
    private lateinit var btnSupernova: Button
    private lateinit var btnReset: Button


    private var isAlertShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val imgBackground = findViewById<ImageView>(R.id.imgBackground)

        // レイアウトが完了して ImageView のサイズが確定してから拡大＆アニメーションを適用
        imgBackground.post {
            // たとえば1.3倍に拡大して隙間を防ぐ
            imgBackground.pivotX = imgBackground.width / 2f
            imgBackground.pivotY = imgBackground.height / 2f
            imgBackground.scaleX = 2.3f
            imgBackground.scaleY = 1.4f

            // 回転アニメーションを読み込み＆開始
            val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_background)
            imgBackground.startAnimation(rotateAnim)
        }

        // 星アニメのカスタムView
        val starFieldView = findViewById<StarFieldView>(R.id.starFieldView)
        // レイアウトが確定したあと(= onSizeChanged後)に startStarAnimation するのが安全
        starFieldView.post {
            starFieldView.startStarAnimation()
        }


        setupUI()
        setupButtonListeners(starFieldView)

        // ✅ アプリ起動時に最新データを取得
        SensorDataManager.fetchLatestSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }

        // ✅ 5秒ごとにセンサーデータを更新
        SensorDataManager.startFetchingSensorData(viewModel, tvTemperature, tvHumidity, tvMoving) { motionDetected, flameDetected ->
            handleSensorData(motionDetected, flameDetected)
        }
    }

    private fun setupUI() {
        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvMoving = findViewById(R.id.tvUpdated)
        btnSupernova = findViewById(R.id.btnSupernova)
        btnReset = findViewById(R.id.btnReset)
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

    private fun setupButtonListeners(starFieldView: StarFieldView) {
        ButtonManager.setupButtonListeners(
            this,
            tvMoving,
            btnLEDOn, btnLEDOff,
            btnSoundOn, btnSoundOff,
            btnFireOn, btnFireOff,
            btnSupernova,btnReset,
            starFieldView = starFieldView
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
