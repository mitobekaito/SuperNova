package supernova.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // スプラッシュ画面を表示
        setContentView(R.layout.activity_splash)

        // 2秒後にメイン画面(MainActivity)に遷移
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // スプラッシュ画面を終了
        }, 2000) // 2000ミリ秒（2秒）
    }
}
