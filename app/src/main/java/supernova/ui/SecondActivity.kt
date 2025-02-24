package supernova.ui

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.SensorData
import supernova.ui.StarFieldView
import supernova.utils.SensorHistoryAdapter

class SecondActivity : AppCompatActivity() {

    private lateinit var btnBack: Button
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var sensorHistoryAdapter: SensorHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val imgBackground = findViewById<ImageView>(R.id.imgBackground)
        imgBackground.post {
            imgBackground.pivotX = imgBackground.width / 2f
            imgBackground.pivotY = imgBackground.height / 2f
            imgBackground.scaleX = 2.3f
            imgBackground.scaleY = 1.4f

            val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_background)
            imgBackground.startAnimation(rotateAnim)
        }

        // 星アニメーションのセットアップ
        val starFieldView = findViewById<StarFieldView>(R.id.starFieldView)
        starFieldView.post {
            starFieldView.startStarAnimation()
        }

        btnBack = findViewById(R.id.btnBack)
        recyclerHistory = findViewById(R.id.recyclerHistory)

        // 戻るボタン
        btnBack.setOnClickListener {
            finish()
        }

        // Adapter を生成して RecyclerView にセット
        sensorHistoryAdapter = SensorHistoryAdapter()
        recyclerHistory.adapter = sensorHistoryAdapter
        recyclerHistory.layoutManager = LinearLayoutManager(this)

        // データ取得を開始（リアルタイム更新例: 5秒ごと）
        startFetchingSensorHistory()
    }

    private fun startFetchingSensorHistory() {
        lifecycleScope.launch {
            while (true) {
                fetchSensorHistory()
                delay(5000) // 5秒ごとに更新
            }
        }
    }

    private suspend fun fetchSensorHistory() {
        try {
            // Retrofit等でMongoDBの全履歴を取得
            val historyList = ApiClient.instance.getAllSensorData()

            println("DEBUG: server returned ${historyList.size} items")

            // UIスレッドでAdapterに反映
            runOnUiThread {
                // ListAdapterのsubmitListに渡すと、差分更新される
                sensorHistoryAdapter.submitList(historyList)
            }
        } catch (e: Exception) {
            runOnUiThread {
                Toast.makeText(
                    this,
                    "データ取得エラー: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
