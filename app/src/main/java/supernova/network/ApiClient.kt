package supernova.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


// ✅ Node.js サーバーのベースURL
private const val BASE_URL = "http://172.18.2.206:5000/"

// ✅ OkHttpClient（キャッシュ無効 & ログ出力）
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // API のリクエストとレスポンスのログを出力
    })
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .addNetworkInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Cache-Control", "no-cache") // 🚀 キャッシュ無効化
            .header("Pragma", "no-cache") // 追加のキャッシュ無効化設定
            .build()
        chain.proceed(request)
    }
    .build()

// ✅ Retrofit クライアント（API インスタンスを作成）
object ApiClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // ✅ キャッシュ無効化設定を適用
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// ✅ API インターフェース
interface ApiService {

    // 📌 センサーデータを取得
    @GET("api/sensor-data")
    suspend fun getSensorData(): List<SensorData>

    // 📌 センサーデータを送信
    @POST("api/sensor-data")
    suspend fun postSensorData(@Body data: SensorData): ResponseMessage

    // 📌 LED コマンドを送信
    @POST("api/led-command")
    suspend fun sendLedCommand(@Body command: LedCommand): ResponseMessage

    // 📌 Motion コマンドを送信
    @POST("api/motion-command")
    suspend fun sendMotionCommand(@Body command: MotionCommand): ResponseMessage

    // 📌 Flame コマンドを送信
    @POST("api/flame-command")
    suspend fun sendFlameCommand(@Body command: FlameCommand): ResponseMessage

}

// ✅ データクラスの定義

// 📌 センサーデータ
data class SensorData(
    val temperature: Double,
    val humidity: Double,
    val motion: Boolean,
    val flame: Boolean,
    val timestamp: String
)

// 📌 通常のAPIレスポンスメッセージ
data class ResponseMessage(
    val message: String
)

// 📌 LED コマンド送信用
data class LedCommand(
    val led_command: String
)

data class LedCommandResponse(
    val message: String
)

// 📌 Motion コマンド送信用
data class MotionCommand(
    val motion_command: String
)

data class MotionCommandResponse(
    val message: String
)

// 📌 Flame コマンド送信用
data class FlameCommand(
    val flame_command: String
)

data class FlameCommandResponse(
    val message: String
)