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
        btnAlarmOn: Button, btnAlarmOff: Button
    ) {
        btnLEDOn.setOnClickListener { updateToggleButtons(activity, true, btnLEDOn, btnLEDOff) }
        btnLEDOff.setOnClickListener { updateToggleButtons(activity, false, btnLEDOn, btnLEDOff) }
        btnSoundOn.setOnClickListener { updateToggleButtons(activity, true, btnSoundOn, btnSoundOff) }
        btnSoundOff.setOnClickListener { updateToggleButtons(activity, false, btnSoundOn, btnSoundOff) }
        btnAlarmOn.setOnClickListener { updateToggleButtons(activity, true, btnAlarmOn, btnAlarmOff) }
        btnAlarmOff.setOnClickListener { updateToggleButtons(activity, false, btnAlarmOn, btnAlarmOff) }
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
