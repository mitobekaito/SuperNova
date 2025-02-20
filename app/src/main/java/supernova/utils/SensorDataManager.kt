package supernova.utils

import android.widget.TextView
import kotlinx.coroutines.*
import supernova.ui.MainViewModel
import supernova.network.SensorData

object SensorDataManager {

    private var lastFetchedData: SensorData? = null  // ✅ 前回のデータを保存
    private var alarmTriggered = false  // ✅ アラームが一度鳴ったかを管理

    // ✅ アプリ起動時に一度だけ最新データを取得
    fun fetchLatestSensorData(
        viewModel: MainViewModel,
        tvTemperature: TextView,
        tvHumidity: TextView,
        tvUpdated: TextView,
        onDataReceived: (motionDetected: Boolean, flameDetected: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val latestData = viewModel.getLatestSensorData()
                latestData?.let { data ->
                    runOnUiThread {
                        updateUI(tvTemperature, tvHumidity, tvUpdated, data)
                        handleNewData(data, onDataReceived)
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvUpdated.text = "データ取得エラー: ${e.message}"
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
        onDataReceived: (motionDetected: Boolean, flameDetected: Boolean) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val data = withContext(Dispatchers.IO) { viewModel.getLatestSensorData() }
                    data?.let {
                        runOnUiThread {
                            updateUI(tvTemperature, tvHumidity, tvMoving, it)
                            handleNewData(it, onDataReceived)
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

    // ✅ UI を更新する共通関数
    private fun updateUI(tvTemperature: TextView, tvHumidity: TextView, tvUpdated: TextView, data: SensorData) {
        tvTemperature.text = "${data.temperature}°C"
        tvHumidity.text = "Humidity: ${data.humidity}%"
        tvUpdated.text = "${data.timestamp}"
    }

    // ✅ データの更新をチェックし、アラームを制御
    private fun handleNewData(data: SensorData, onDataReceived: (Boolean, Boolean) -> Unit) {
        // ✅ データが変わったかどうかを厳密にチェック
        val isNewData = lastFetchedData == null || lastFetchedData != data

        if (isNewData) {
            println("🔄 最新データが変更されました: $data")

            // ✅ アラームをリセット
            alarmTriggered = false

            lastFetchedData = data // 前回のデータを更新
        }

        // 🔥 `motion` または `flame` が `true` ならアラームを鳴らす
        if ((data.motion || data.flame) && !alarmTriggered) {
            println("🚨 アラームを発動！")
            alarmTriggered = true  // ✅ 一度アラームを鳴らしたらフラグを立てる
            onDataReceived(data.motion, data.flame)
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch { action() }
    }
}
