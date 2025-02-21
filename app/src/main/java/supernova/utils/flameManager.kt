package supernova.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.FlameCommand

object FlameManager {
    // ✅ Flame コマンドをサーバーに送信するメソッド
    fun sendFlameCommand(command: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("🚀 Flame コマンド送信中: $command")
                val response = ApiClient.instance.sendFlameCommand(FlameCommand(command))
                println("✅ Flame コマンド送信成功: ${response.message}")
            } catch (e: Exception) {
                println("❌ Flame コマンド送信エラー: ${e.message}")
            }
        }
    }
}