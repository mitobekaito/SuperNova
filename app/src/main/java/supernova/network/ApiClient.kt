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
private const val BASE_URL = "http://172.16.15.18:5000/"

// ✅ キャッシュを無効化する OkHttpClient を作成
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

// ✅ Retrofit クライアント
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

// ✅ API 定義
interface ApiService {
    @GET("sensor-data")
    suspend fun getSensorData(): List<SensorData> // 最新のセンサーデータを取得

    @POST("sensor-data")
    suspend fun postSensorData(@Body data: SensorData): ResponseMessage // センサーデータを送信
}

// データクラスの定義
data class SensorData(
    val temperature: Double,
    val humidity: Double,
    val motion: Boolean,
    val flame: Boolean,
    val timestamp: String
)

data class ResponseMessage(
    val message: String
)
