package supernova.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import supernova.network.ApiClient
import supernova.network.SensorData
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    val apiService = ApiClient.instance

    fun fetchSensorData(callback: (SensorData) -> Unit) {
        viewModelScope.launch {
            try {
                println("🚀 API リクエスト開始: /sensor-data")
                val dataList = apiService.getSensorData()

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


    fun sendSensorData(temperature: Double, humidity: Double, motionDetected: Boolean) {
        viewModelScope.launch {
            try {
                val timestamp = getCurrentTimestamp()
                val sensorData = SensorData(timestamp, humidity, temperature, motionDetected)

                println("🚀 データ送信開始: $sensorData")

                val response = apiService.postSensorData(sensorData)
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
