package supernova.utils

import android.widget.TextView
import kotlinx.coroutines.*
import supernova.ui.MainViewModel

object SensorDataManager {

    // ✅ アプリ起動時に一度だけ最新データを取得
    fun fetchLatestSensorData(
        viewModel: MainViewModel,
        tvTemperature: TextView,
        tvHumidity: TextView,
        tvMoving: TextView,
        onMotionDetected: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val latestData = viewModel.getLatestSensorData()
                latestData?.let { data ->
                    runOnUiThread {
                        tvTemperature.text = "${data.temperature}°C"
                        tvHumidity.text = "Humidity: ${data.humidity}%"
                        tvMoving.text = "Motion: ${if (data.motion) "Detected" else "None"}"
                        onMotionDetected(data.motion)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvMoving.text = "データ取得エラー: ${e.message}"
                }
            }
        }
    }

    // ✅ 5秒ごとにデータを取得して更新
    fun startFetchingSensorData(
        viewModel: MainViewModel,
        tvTemperature: TextView,
        tvHumidity: TextView,
        tvMoving: TextView,
        onMotionDetected: (Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val data = withContext(Dispatchers.IO) { viewModel.getLatestSensorData() }
                    data?.let {
                        runOnUiThread {
                            tvTemperature.text = "${it.temperature}°C"
                            tvHumidity.text = "Humidity: ${it.humidity}%"
                            tvMoving.text = "Motion: ${if (it.motion) "Detected" else "None"}"
                            onMotionDetected(it.motion)
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        tvMoving.text = "データ取得エラー: ${e.message}"
                    }
                }
                delay(5000) // 5秒待つ
            }
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch { action() }
    }
}
