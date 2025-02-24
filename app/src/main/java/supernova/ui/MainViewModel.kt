package supernova.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import supernova.network.ApiClient
import supernova.network.SensorData
import supernova.utils.TimeUtils


class MainViewModel : ViewModel() {

    private val apiService = ApiClient.instance

    // ✅ 最新のセンサーデータを取得するメソッド
    suspend fun getLatestSensorData(): SensorData? {
        return withContext(Dispatchers.IO) {
            try {
                println("🚀 API リクエスト開始: /sensor-latest")
                val dataList = apiService.getLatestSensorData()

                if (dataList.isNotEmpty()) {
                    val latestData = dataList.first()

                    // ✅ `timestamp` を `+7時間` に変換
                    val formattedTimestamp = TimeUtils.formatTimestamp(latestData.timestamp)
                    println("📩 最新データ: $latestData")
                    latestData.copy(timestamp = formattedTimestamp)
                } else {
                    println("⚠️ サーバーからのデータが空です")
                    null
                }
            } catch (e: Exception) {
                println("❌ データ取得エラー: ${e.message}")
                null
            }
        }
    }
}