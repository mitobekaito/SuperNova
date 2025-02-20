package supernova.utils

import android.app.Activity
import android.widget.Button
import androidx.core.content.ContextCompat
import supernova.ui.R

object ButtonManager {

    fun setupButtonListeners(
        activity: Activity,
        btnLEDOn: Button, btnLEDOff: Button,
        btnSoundOn: Button, btnSoundOff: Button,
        btnFireOn: Button, btnFireOff: Button,
        btnAlarmOn: Button, btnAlarmOff: Button
    ) {
        setInitialButtonState(activity, btnLEDOn, btnLEDOff)
        setInitialButtonState(activity, btnSoundOn, btnSoundOff)
        setInitialButtonState(activity, btnFireOn, btnFireOff)
        setInitialButtonState(activity, btnAlarmOn, btnAlarmOff)

        btnLEDOn.setOnClickListener { updateToggleButtons(activity, true, btnLEDOn, btnLEDOff) }
        btnLEDOff.setOnClickListener { updateToggleButtons(activity, false, btnLEDOn, btnLEDOff) }
        btnSoundOn.setOnClickListener { updateToggleButtons(activity, true, btnSoundOn, btnSoundOff) }
        btnSoundOff.setOnClickListener { updateToggleButtons(activity, false, btnSoundOn, btnSoundOff) }
        btnFireOn.setOnClickListener { updateToggleButtons(activity, true, btnFireOn, btnFireOff) }
        btnFireOff.setOnClickListener { updateToggleButtons(activity, false, btnFireOn, btnFireOff) }
        btnAlarmOn.setOnClickListener { updateToggleButtons(activity, true, btnAlarmOn, btnAlarmOff) }
        btnAlarmOff.setOnClickListener { updateToggleButtons(activity, false, btnAlarmOn, btnAlarmOff) }
    }

    private fun setInitialButtonState(activity: Activity, btnOn: Button, btnOff: Button) {
        val initialOnColor = ContextCompat.getColor(activity, R.color.dark_yellow)
        val initialOffColor = ContextCompat.getColor(activity, R.color.gray)
        val initialTextColorOn = ContextCompat.getColor(activity, R.color.black)
        val initialTextColorOff = ContextCompat.getColor(activity, R.color.white)

        btnOn.setBackgroundColor(initialOnColor) // ✅ ONボタンの背景を黄色に設定
        btnOn.setTextColor(initialTextColorOn)   // ✅ ONボタンの文字色を黒に設定
        btnOff.setBackgroundColor(initialOffColor) // OFFボタンの背景をグレー
        btnOff.setTextColor(initialTextColorOff) // OFFボタンの文字色を白
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
        } else {
            btnOn.setBackgroundColor(offColor)
            btnOn.setTextColor(textColorOff)
            btnOff.setBackgroundColor(onColor)
            btnOff.setTextColor(textColorOn)
        }
    }
}
