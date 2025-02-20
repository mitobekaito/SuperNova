package supernova.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import supernova.network.ApiClient
import supernova.network.SensorData
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private val apiService = ApiClient.instance

    // ✅ 最新のセンサーデータを取得するメソッド
    suspend fun getLatestSensorData(): SensorData? {
        return withContext(Dispatchers.IO) {
            try {
                println("🚀 API リクエスト開始: /sensor-data")
                val dataList = apiService.getSensorData()

                if (dataList.isNotEmpty()) {
                    val latestData = dataList.first()

                    // ✅ `timestamp` を変換
                    val formattedTimestamp = formatTimestamp(latestData.timestamp)
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

    // ✅ `timestamp` を `yyyy-MM-dd HH:mm:ss` に変換する関数
    private fun formatTimestamp(timestamp: String): String {
        return try {
            // ✅ `+00:00` の形式を正しく解析するフォーマット
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

            // ✅ タイムゾーンを UTC に設定
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            // ✅ `timestamp` を `Date` に変換
            val date = sdf.parse(timestamp)

            // ✅ `Date` を `yyyy-MM-dd HH:mm:ss` にフォーマット
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(date!!)
        } catch (e: Exception) {
            "Invalid Timestamp"
        }
    }

}
