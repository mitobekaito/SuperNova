package supernova.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.SensorData

class SecondActivity : AppCompatActivity() {
    private lateinit var tableLayout: TableLayout
    private lateinit var btnBack: Button
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        tableLayout = findViewById(R.id.tableLayout)
        btnBack = findViewById(R.id.btnBack)

        // 戻るボタン処理
        btnBack.setOnClickListener {
            finish()
        }

        // データ取得を開始（リアルタイム更新）
        startFetchingSensorHistory()
    }

    private fun startFetchingSensorHistory() {
        lifecycleScope.launch {
            while (true) {
                fetchSensorHistory()
                delay(5000) // 5秒ごとにデータ取得
            }
        }
    }

    private fun fetchSensorHistory() {
        lifecycleScope.launch {
            try {
                val historyList = ApiClient.instance.getSensorData()
                runOnUiThread {
                    updateTable(historyList)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@SecondActivity, "データ取得エラー: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTable(historyList: List<SensorData>) {
        // 最大表示行数（例: 最新100件のみ表示）
        val maxRows = 100

        // 🔹 最新のデータを取得（`historyList` の最後のデータ）
        val latestData = historyList.lastOrNull() ?: return

        // 🔹 新しい行を作成
        val row = TableRow(this)

        // タイムスタンプ（日付と時間を分割して改行表示）
        val timestampParts = latestData.timestamp.split(" ")
        val formattedTimestamp = if (timestampParts.size == 2) {
            "${timestampParts[0]}\n${timestampParts[1]}"
        } else {
            latestData.timestamp // フォーマットが異常な場合はそのまま
        }

        val timeText = TextView(this).apply {
            text = formattedTimestamp
            textSize = 14f
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setSingleLine(false) // ✅ 自動改行を許可
            setLines(2) // ✅ 2行固定
        }
        row.addView(timeText)

        // 温度
        val tempText = TextView(this).apply {
            text = "${latestData.temperature}°C"
            textSize = 14f
            setPadding(8, 16, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        row.addView(tempText)

        // 湿度
        val humidityText = TextView(this).apply {
            text = "${latestData.humidity}%"
            textSize = 14f
            setPadding(8, 16, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        row.addView(humidityText)

        // Motion
        val motionText = TextView(this).apply {
            text = if (latestData.motion) "Detected" else "None"
            textSize = 14f
            setPadding(8, 16, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        row.addView(motionText)

        // Fire
        val fireText = TextView(this).apply {
            text = if (latestData.flame) "Detected" else "None"
            textSize = 14f
            setPadding(8, 16, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        row.addView(fireText)

        // 🔹 最新のデータをヘッダーの直下に追加（行の先頭に挿入）
        tableLayout.addView(row, 1)

        // 🔹 最大行数を超えたら古いデータを削除
        if (tableLayout.childCount > maxRows + 1) { // ヘッダー分を考慮
            tableLayout.removeViewAt(tableLayout.childCount - 1) // 一番古い行を削除
        }
    }
}
