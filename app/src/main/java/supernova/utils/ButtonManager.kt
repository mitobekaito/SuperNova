package supernova.utils

import android.app.Activity
import android.widget.Button
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import supernova.utils.LedManager
import supernova.utils.MotionManager
import supernova.utils.FlameManager
import supernova.utils.SupernovaManager
import supernova.utils.ResetManager
import supernova.ui.R

object ButtonManager {

    fun setupButtonListeners(
        activity: Activity,
        btnLEDOn: Button, btnLEDOff: Button,
        btnSoundOn: Button, btnSoundOff: Button,
        btnFireOn: Button, btnFireOff: Button,
        btnSupernova: Button, btnReset: Button
    ) {
        setInitialButtonState(activity, btnLEDOn, btnLEDOff)
        setInitialButtonState(activity, btnSoundOn, btnSoundOff)
        setInitialButtonState(activity, btnFireOn, btnFireOff)

        // ✅ LED ON
        btnLEDOn.setOnClickListener {
            println("🟢 LED ON ボタンが押されました")
            updateToggleButtons(activity, true, btnLEDOn, btnLEDOff)
            sendCommand { LedManager.sendLedCommand("ON") }
        }

        // ✅ LED OFF
        btnLEDOff.setOnClickListener {
            println("🔴 LED OFF ボタンが押されました")
            updateToggleButtons(activity, false, btnLEDOn, btnLEDOff)
            sendCommand { LedManager.sendLedCommand("OFF") }
        }

        // ✅ Motion ON
        btnSoundOn.setOnClickListener {
            println("🟢 Motion ON ボタンが押されました")
            updateToggleButtons(activity, true, btnSoundOn, btnSoundOff)
            sendCommand { MotionManager.sendMotionCommand("ON") }
        }

        // ✅ Motion OFF
        btnSoundOff.setOnClickListener {
            println("🔴 Motion OFF ボタンが押されました")
            updateToggleButtons(activity, false, btnSoundOn, btnSoundOff)
            sendCommand { MotionManager.sendMotionCommand("OFF") }
        }

        // ✅ Flame ON
        btnFireOn.setOnClickListener {
            println("🟢 Flame ON ボタンが押されました")
            updateToggleButtons(activity, true, btnFireOn, btnFireOff)
            sendCommand { FlameManager.sendFlameCommand("ON") }
        }

        // ✅ Flame OFF
        btnFireOff.setOnClickListener {
            println("🔴 Flame OFF ボタンが押されました")
            updateToggleButtons(activity, false, btnFireOn, btnFireOff)
            sendCommand { FlameManager.sendFlameCommand("OFF") }
        }

        // ✅ Supernova
        btnSupernova.setOnClickListener {
            println("🚀 Supernova ボタンが押されました")
            sendCommand { SupernovaManager.sendSupernovaCommand("Supernova") }
        }

        // ✅ Reset
        btnReset.setOnClickListener {
            println("🔄 Reset ボタンが押されました")
            sendCommand { ResetManager.sendResetCommand("Reset") }
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

    fun setInitialButtonState(activity: Activity, btnOn: Button, btnOff: Button) {
        val onColor = ContextCompat.getColor(activity, R.color.dark_yellow)
        val offColor = ContextCompat.getColor(activity, R.color.gray)
        val textColorOn = ContextCompat.getColor(activity, R.color.black)
        val textColorOff = ContextCompat.getColor(activity, R.color.white)

        btnOn.setBackgroundColor(onColor)
        btnOn.setTextColor(textColorOn)
        btnOff.setBackgroundColor(offColor)
        btnOff.setTextColor(textColorOff)

        btnOn.tag = "ON"
        btnOff.tag = "OFF"
    }

    private fun updateToggleButtons(activity: Activity, isOn: Boolean, btnOn: Button, btnOff: Button) {
        val onColor = ContextCompat.getColor(activity, R.color.dark_yellow)
        val offColor = ContextCompat.getColor(activity, R.color.gray)
        val textColorOn = ContextCompat.getColor(activity, R.color.black)
        val textColorOff = ContextCompat.getColor(activity, R.color.white)

        if (isOn) {
            btnOn.setBackgroundColor(onColor)
            btnOn.setTextColor(textColorOn)
            btnOff.setBackgroundColor(offColor)
            btnOff.setTextColor(textColorOff)
            btnOn.tag = "ON"
            btnOff.tag = "OFF"
        } else {
            btnOn.setBackgroundColor(offColor)
            btnOn.setTextColor(textColorOff)
            btnOff.setBackgroundColor(onColor)
            btnOff.setTextColor(textColorOn)
            btnOn.tag = "OFF"
            btnOff.tag = "ON"
        }
    }
}
