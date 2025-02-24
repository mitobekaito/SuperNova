package supernova.ui


import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import supernova.utils.ButtonManager
import supernova.utils.SensorDataManager
import supernova.ui.StarFieldView


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var tvMoving: TextView
    private lateinit var switchLED: SwitchCompat
    private lateinit var switchMotion: SwitchCompat
    private lateinit var switchFire: SwitchCompat
    private lateinit var btnSupernova: Button
    private lateinit var btnReset: Button
    private lateinit var btnGoToSecond: Button

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
        switchLED = findViewById(R.id.switchLED)
        switchMotion = findViewById(R.id.switchMotion)
        switchFire = findViewById(R.id.switchFire)
        btnSupernova = findViewById(R.id.btnSupernova)
        btnReset = findViewById(R.id.btnReset)
        btnGoToSecond = findViewById(R.id.btnGoToSecond)

        btnGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // ✅ すべてのスイッチをONに設定（初期状態）
        switchLED.isChecked = true
        switchMotion.isChecked = true
        switchFire.isChecked = true
    }

    private fun setupButtonListeners(starFieldView: StarFieldView) {
        ButtonManager.setupButtonListeners(
            this,
            tvMoving,
            switchLED, switchMotion, switchFire,
            btnSupernova, btnReset,
            starFieldView = starFieldView
        )
    }

    private fun handleSensorData(motionDetected: Boolean, flameDetected: Boolean) {
        val isMotionAlertOn = switchMotion.isChecked
        val isFireAlertOn = switchFire.isChecked

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
