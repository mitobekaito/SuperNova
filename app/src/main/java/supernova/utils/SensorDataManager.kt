package supernova.utils

import android.util.Log
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
        onDataReceived: (motionDetected: Boolean, flameDetected: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val latestData = withContext(Dispatchers.IO) { viewModel.getLatestSensorData() }

                latestData?.let { data ->
                    Log.d("SensorDataManager", "📩 最新データ: $data") // ✅ ログで確認

                    tvTemperature.text = "${data.temperature}°C"
                    tvHumidity.text = "Humidity: ${data.humidity}%"
                    tvMoving.text = "Motion: ${if (data.motion) "Detected" else "None"}"

                    onDataReceived(data.motion, data.flame)
                }
            } catch (e: Exception) {
                tvMoving.text = "データ取得エラー: ${e.message}"
                Log.e("SensorDataManager", "❌ データ取得エラー: ${e.message}")
            }
        }
    }

    // ✅ 5秒ごとにデータを取得して更新
    fun startFetchingSensorData(
        viewModel: MainViewModel,
        tvTemperature: TextView,
        tvHumidity: TextView,
        tvMoving: TextView,
        onDataReceived: (motionDetected: Boolean, flameDetected: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                try {
                    val data = withContext(Dispatchers.IO) { viewModel.getLatestSensorData() }

                    data?.let {
                        Log.d("SensorDataManager", "🔄 最新データ更新: $it") // ✅ デバッグログ

                        tvTemperature.text = "${it.temperature}°C"
                        tvHumidity.text = "Humidity: ${it.humidity}%"
                        tvMoving.text = "Motion: ${if (it.motion) "Detected" else "None"}"

                        onDataReceived(it.motion, it.flame)
                    }
                } catch (e: Exception) {
                    tvMoving.text = "データ取得エラー: ${e.message}"
                    Log.e("SensorDataManager", "❌ データ取得エラー: ${e.message}")
                }
                delay(5000) // 5秒ごとに更新
            }
        }
    }
}
