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
                val data = apiService.getSensorData().firstOrNull() ?: return@launch
                callback(data)
                println("📩 取得したデータ: $data")
            } catch (e: Exception) {
                println("データ取得エラー: ${e.message}")
            }
        }
    }

    fun sendSensorData(temperature: Double, motionDetected: Boolean) {
        viewModelScope.launch {
            try {
                val timestamp = getCurrentTimestamp()
                val response = apiService.postSensorData(SensorData(timestamp, temperature, motionDetected))
                println("送信結果: ${response.message}")
            } catch (e: Exception) {
                println("データ送信エラー: ${e.message}")
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        return sdf.format(Date())
    }
}
