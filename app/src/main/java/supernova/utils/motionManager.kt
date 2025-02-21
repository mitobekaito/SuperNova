package supernova.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.network.ApiClient
import supernova.network.MotionCommand

object MotionManager {
    // ✅ Motion コマンドをサーバーに送信するメソッド
    fun sendMotionCommand(command: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println("🚀 Motion コマンド送信中: $command")
                val response = ApiClient.instance.sendMotionCommand(MotionCommand(command))
                println("✅ Motion コマンド送信成功: ${response.message}")
            } catch (e: Exception) {
                println("❌ Motion コマンド送信エラー: ${e.message}")
            }
        }
    }
}
