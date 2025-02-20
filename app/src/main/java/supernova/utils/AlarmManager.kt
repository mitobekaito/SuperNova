package supernova.utils

import android.content.Context
import android.media.MediaPlayer
import supernova.ui.R

object AlarmManager {
    private var mediaPlayer: MediaPlayer? = null

    fun playAlarmSound(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alert_sound)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    fun stopAlarmSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
