package supernova.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import supernova.network.ApiClient
import supernova.network.SensorData
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    val apiService = ApiClient.instance

    // 📌 最新のセンサーデータを取得するメソッドを追加
    suspend fun getLatestSensorData(): SensorData? {
        return withContext(Dispatchers.IO) {
            try {
                println("🚀 API リクエスト開始: /sensor-data")
                val dataList = apiService.getSensorData()
                if (dataList.isNotEmpty()) {
                    println("📩 最新データ: ${dataList.first()}")
                    dataList.first() // 最新のデータを返す
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

    fun fetchSensorData(callback: (SensorData) -> Unit) {
        viewModelScope.launch {
            try {
                println("🚀 API リクエスト開始: /sensor-data")
                val dataList = withContext(Dispatchers.IO) { apiService.getSensorData() } // ✅ 修正

                if (dataList.isEmpty()) {
                    println("⚠️ サーバーからのデータが空です")
                    return@launch
                }

                val data = dataList.firstOrNull()
                if (data != null) {
                    println("📩 取得したデータ: $data")
                    callback(data)
                } else {
                    println("⚠️ データが NULL でした")
                }
            } catch (e: Exception) {
                println("❌ データ取得エラー: ${e.message}")
            }
        }
    }

    fun sendSensorData(temperature: Double, humidity: Double, motion: Boolean, flame: Boolean) {
        viewModelScope.launch {
            try {
                val timestamp = getCurrentTimestamp()
                val sensorData = SensorData(temperature, humidity, motion, flame, timestamp)

                println("🚀 データ送信開始: $sensorData")

                val response = withContext(Dispatchers.IO) { apiService.postSensorData(sensorData) } // ✅ 修正
                println("✅ 送信結果: ${response.message}")
            } catch (e: Exception) {
                println("❌ データ送信エラー: ${e.message}")
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return sdf.format(Date())
    }
}
