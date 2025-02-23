package supernova.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.SuperNovaCommand

object SupernovaManager {
    // Supernova コマンドをサーバーに送信するメソッド
    fun sendSupernovaCommand(command: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("🚀 Supernova コマンド送信中: $command")
                val response = ApiClient.instance.sendSuperNovaCommand(SuperNovaCommand(command))
                println("✅ Supernova コマンド送信成功: ${response.message}")
            } catch (e: Exception) {
                println("❌ Supernova コマンド送信エラー: ${e.message}")
            }
        }
    }
}