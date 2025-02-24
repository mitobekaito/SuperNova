package supernova.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// Node.js サーバーのベースURL
private const val BASE_URL = "http://172.16.15.126:5000/"

// OkHttpClient（キャッシュ無効 & ログ出力）
private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // APIの通信ログを出力
    })
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .addNetworkInterceptor { chain ->
        val request = chain.request().newBuilder()
            .header("Cache-Control", "no-cache")
            .header("Pragma", "no-cache")
            .build()
        chain.proceed(request)
    }
    .build()

// Retrofit クライアント（API インスタンスを作成）
object ApiClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // キャッシュ無効化設定などを適用
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// API インターフェース
interface ApiService {

    // ★ 全センサーデータを返す (複数件)
    @GET("api/sensor-data")
    suspend fun getAllSensorData(): List<SensorData>

    // ★ 最新1件のセンサーデータを返す
    @GET("api/sensor-latest")
    suspend fun getLatestSensorData(): List<SensorData>

    // LED コマンドを送信
    @POST("api/led-command")
    suspend fun sendLedCommand(@Body command: LedCommand): ResponseMessage

    // Motion コマンドを送信
    @POST("api/motion-command")
    suspend fun sendMotionCommand(@Body command: MotionCommand): ResponseMessage

    // Flame コマンドを送信
    @POST("api/flame-command")
    suspend fun sendFlameCommand(@Body command: FlameCommand): ResponseMessage

    // SuperNovaコマンドを送信
    @POST("api/supernova-command")
    suspend fun sendSuperNovaCommand(@Body command: SuperNovaCommand): ResponseMessage

    // リセットコマンドを送信
    @POST("api/reset-command")
    suspend fun sendResetCommand(@Body command: RestCommand): ResponseMessage
}

// データクラスの定義
data class SensorData(
    val temperature: Double,
    val humidity: Double,
    val motion: Boolean,
    val flame: Boolean,
    val timestamp: String
)

data class ResponseMessage(val message: String)

// 各種コマンド送信用データクラス
data class LedCommand(val led_command: String)
data class MotionCommand(val motion_command: String)
data class FlameCommand(val flame_command: String)
data class SuperNovaCommand(val supernova_command: String)
data class RestCommand(val reset_command: String)
