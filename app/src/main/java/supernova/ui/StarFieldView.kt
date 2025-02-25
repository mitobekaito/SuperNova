package supernova.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * 動きの種類を表す
 */
enum class MotionType {
    STRAIGHT,
    SWIRL
}

/**
 * 星(パーティクル)1つ分の情報
 */
data class Star(
    var x: Float,
    var y: Float,
    var radius: Float,
    var dx: Float,
    var dy: Float,
    var color: Int,

    // 円軌道用
    var centerX: Float,
    var centerY: Float,
    var angle: Float,
    var rotationSpeed: Float,

    var motionType: MotionType
)

/**
 * 星をランダムに生成し、画面内を動かすカスタムView
 */
class StarFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // 星のリスト
    private val stars = mutableListOf<Star>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // 星の数
    private val starCount = 1000

    // アニメーションループ用フラグ
    private var running = false

    /**
     * パステル系のランダム色を生成
     */
    private fun randomColor(): Int {
        val a = Random.nextInt(128, 256)
        val r = Random.nextInt(128, 256)
        val g = Random.nextInt(128, 256)
        val b = Random.nextInt(128, 256)
        return Color.argb(a, r, g, b)
    }

    /**
     * 星を初期生成する
     */
    private fun initStars() {
        stars.clear()

        val w = width.toFloat()
        val h = height.toFloat()

        for (i in 0 until starCount) {
            // ランダムに動き方を決める
            val motion = if (Random.nextBoolean()) MotionType.STRAIGHT else MotionType.SWIRL
            when (motion) {
                MotionType.STRAIGHT -> {
                    // 直進パターン
                    val x = Random.nextFloat() * w
                    val y = Random.nextFloat() * h
                    val dx = Random.nextFloat() * 4f - 2f
                    val dy = Random.nextFloat() * 4f - 2f
                    val radius = Random.nextFloat() * 6f + 2f
                    val color = randomColor()
                    stars.add(
                        Star(
                            x, y, radius, dx, dy, color,
                            centerX = 0f, centerY = 0f,
                            angle = 0f, rotationSpeed = 0f,
                            motionType = MotionType.STRAIGHT
                        )
                    )
                }
                MotionType.SWIRL -> {
                    // 円軌道パターン
                    val cx = w / 2f
                    val cy = h / 2f
                    val angle = Random.nextFloat() * 360f
                    val r = Random.nextFloat() * (w / 2f) // 画面中心からの半径
                    val rotationSpeed = Random.nextFloat() * 2f - 1f // -1 ~ +1
                    val color = randomColor()

                    // 初期位置として円周上に配置
                    val rad = Math.toRadians(angle.toDouble())
                    val x = cx + r * cos(rad).toFloat()
                    val y = cy + r * sin(rad).toFloat()
                    val radius = Random.nextFloat() * 6f + 2f

                    stars.add(
                        Star(
                            x, y, radius,
                            dx = 0f, dy = 0f, color = color,
                            centerX = cx, centerY = cy,
                            angle = angle,
                            rotationSpeed = rotationSpeed,
                            motionType = MotionType.SWIRL
                        )
                    )
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Viewがレイアウトされ、幅・高さが確定したら星を初期生成
        initStars()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 星を描画
        for (star in stars) {
            paint.color = star.color
            canvas.drawCircle(star.x, star.y, star.radius, paint)
        }
    }

    /**
     * 星を移動して画面外に出たら再度位置を調整する等のロジック
     */
    private fun updateStars() {
        val w = width.toFloat()
        val h = height.toFloat()

        for (star in stars) {
            when (star.motionType) {
                MotionType.STRAIGHT -> {
                    // 直進
                    star.x += star.dx
                    star.y += star.dy

                    // 画面外に行ったら再配置(ランダム)
                    if (star.x < -star.radius || star.x > w + star.radius
                        || star.y < -star.radius || star.y > h + star.radius) {
                        star.x = Random.nextFloat() * w
                        star.y = Random.nextFloat() * h
                    }
                }

                MotionType.SWIRL -> {
                    // 円軌道
                    star.angle += star.rotationSpeed
                    if (star.angle > 360f) star.angle -= 360f
                    else if (star.angle < 0f) star.angle += 360f

                    // 半径rを (現在位置 - 中心) の距離として計算し直す
                    val r = sqrt(
                        (star.x - star.centerX).pow(2) +
                                (star.y - star.centerY).pow(2)
                    )
                    val rad = Math.toRadians(star.angle.toDouble())
                    star.x = star.centerX + r * cos(rad).toFloat()
                    star.y = star.centerY + r * sin(rad).toFloat()
                }
            }
        }
    }

    /**
     * アニメーション開始
     */
    fun startStarAnimation() {
        if (running) return
        running = true

        // だいたい 60FPS (16msおき) に update → invalidate する簡易ループ
        post(object : Runnable {
            override fun run() {
                if (!running) return

                updateStars()
                invalidate()

                postDelayed(this, 16)
            }
        })
    }

    /**
     * アニメーション停止
     */
    fun stopStarAnimation() {
        running = false
    }
}
