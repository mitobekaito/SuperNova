package supernova.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

// Node.js サーバーのベースURL
private const val BASE_URL = "http://172.20.10.2:5000/"

// Retrofit クライアントの作成
object ApiClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON を Kotlin オブジェクトに変換
            .build()
            .create(ApiService::class.java)
    }
}

// サーバーとの通信を定義
interface ApiService {
    @GET("sensor-data")
    suspend fun getSensorData(): List<SensorData> // 最新のセンサーデータを取得

    @POST("sensor-data")
    suspend fun postSensorData(@Body data: SensorData): ResponseMessage // センサーデータを送信
}

// データクラスの定義
data class SensorData(
    val timestamp: String,
    val temperature: Double,
    val motion_detected: Boolean
)

data class ResponseMessage(
    val message: String
)
