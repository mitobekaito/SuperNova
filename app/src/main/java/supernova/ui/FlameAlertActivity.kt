package supernova.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import supernova.utils.AlarmManager

class FlameAlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_flame) // 🔥 火災用のレイアウト

        // ✅ AlarmManager を使用してアラーム音を再生
        AlarmManager.playAlarmSound(this)

        // ボタンで警告を解除
        val btnDismiss: Button = findViewById(R.id.btnDismissFire)
        btnDismiss.setOnClickListener {
            finish() // ✅ アクティビティを閉じる（onDestroy() で音を止める）
        }

        // 一定時間後に自動で閉じる（例: 10秒後）
        Handler(Looper.getMainLooper()).postDelayed({
            finish() // ✅ 10秒後に自動で閉じる（onDestroy() で音が止まる）
        }, 10000)
    }

    override fun onPause() {
        super.onPause()
        AlarmManager.stopAlarmSound() // ✅ 画面が閉じる前に音を止める
    }

    override fun onDestroy() {
        super.onDestroy()
        AlarmManager.stopAlarmSound() // ✅ 念のため onDestroy でも音を止める
    }
}
