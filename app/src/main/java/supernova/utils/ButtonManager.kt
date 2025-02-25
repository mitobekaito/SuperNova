package supernova.utils

import android.app.Activity
import androidx.appcompat.widget.SwitchCompat
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.TextView
import supernova.ui.StarFieldView
import supernova.ui.R

object ButtonManager {

    fun setupButtonListeners(
        activity: Activity, tvMoving: TextView,
        switchLED: SwitchCompat, switchMotion: SwitchCompat, switchFire: SwitchCompat,
        btnSupernova: Button, btnReset: Button,
        starFieldView: StarFieldView
    ) {
        // ✅ LED トグルスイッチ
        switchLED.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                println("🟢 LED ON")
                sendCommand { LedManager.sendLedCommand("ON") }
            } else {
                println("🔴 LED OFF")
                sendCommand { LedManager.sendLedCommand("OFF") }
            }
        }

        // ✅ Motion Detected トグルスイッチ
        switchMotion.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                println("🟢 Motion Detected ON")
                sendCommand { MotionManager.sendMotionCommand("ON") }
            } else {
                println("🔴 Motion Detected OFF")
                sendCommand { MotionManager.sendMotionCommand("OFF") }
            }
        }

        // ✅ Fire Detected トグルスイッチ
        switchFire.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                println("🟢 Fire Detected ON")
                sendCommand { FlameManager.sendFlameCommand("ON") }
            } else {
                println("🔴 Fire Detected OFF")
                sendCommand { FlameManager.sendFlameCommand("OFF") }
            }
        }

        // ✅ Supernova ボタン
        btnSupernova.setOnClickListener {
            println("🔥 Supernova ボタンが押されました")
            starFieldView.stopStarAnimation()
            activity.runOnUiThread { tvMoving.text = "🔥 Supernova" }
            sendCommand { SupernovaManager.sendSupernovaCommand("SUPERNOVA") }
        }

        // ✅ Reset ボタン
        btnReset.setOnClickListener {
            println("🔄 Reset ボタンが押されました")
            starFieldView.startStarAnimation()
            activity.runOnUiThread { tvMoving.text = "🔄 RESET" }
            sendCommand { ResetManager.sendResetCommand("RESET") }
        }

    }

    // ✅ コマンドを非同期で送信する共通メソッド
    private fun sendCommand(command: suspend () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                command()
            } catch (e: Exception) {
                println("⚠️ コマンド送信エラー: ${e.message}")
            }
        }
    }

}
