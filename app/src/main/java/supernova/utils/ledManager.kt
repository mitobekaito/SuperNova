package supernova.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.LedCommand

object LedManager {
    // ✅ LED コマンドをサーバーに送信するメソッド
    fun sendLedCommand(command: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("🚀 LED コマンド送信中: $command")
                val response = ApiClient.instance.sendLedCommand(LedCommand(command))
                println("✅ LED コマンド送信成功: ${response.message}")
            } catch (e: Exception) {
                println("❌ LED コマンド送信エラー: ${e.message}")
            }
        }
    }
}
