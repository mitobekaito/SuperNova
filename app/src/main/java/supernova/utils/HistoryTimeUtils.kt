package supernova.utils

import java.text.SimpleDateFormat
import java.util.*

object HistoryTimeUtils {
    // ✅ `timestamp` を `M/d, yyyy, HH:mm` (例: 2/24, 2025, 14:30) に変換する関数
    fun formatTimestamp(timestamp: String): String {
        return try {
            // ✅ `+00:00` の形式を解析するフォーマット
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC") // ✅ 受け取るデータは UTC 基準

            // ✅ `timestamp` を `Date` に変換
            val date = sdf.parse(timestamp)

            // ✅ タイムゾーンを +7 時間に設定
            val outputSdf = SimpleDateFormat("M/d, yyyy, HH:mm", Locale.ENGLISH)
            outputSdf.timeZone = TimeZone.getTimeZone("GMT+7") // ✅ UTC +7 に変換

            // ✅ `Date` を `M/d, yyyy, HH:mm` 形式にフォーマット
            outputSdf.format(date!!)
        } catch (e: Exception) {
            "Invalid Timestamp"
        }
    }
}
