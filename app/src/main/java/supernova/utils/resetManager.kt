package supernova.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.RestCommand

object ResetManager {
    // ✅ リセットコマンドをサーバーに送信するメソッド
    fun sendResetCommand(command: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("🚀 リセットコマンド送信中: $command")
                val response = ApiClient.instance.sendResetCommand(RestCommand(command))
                println("✅ リセットコマンド送信成功: ${response.message}")
            } catch (e: Exception) {
                println("❌ リセットコマンド送信エラー: ${e.message}")
            }
        }
    }
}