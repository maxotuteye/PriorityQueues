import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skija.Font
import org.jetbrains.skija.Paint
import org.jetbrains.skija.Typeface
import kotlin.math.pow

class CustomCanvas(val modifier: Modifier, val priorityQueue: CustomPriorityQueue<Item>) {
    private val x = IntArray(priorityQueue.size() + 1)
    private val y = IntArray(priorityQueue.size() + 1)
    private val lhf = 500
    private val layerHeight = lhf / (logBase2(priorityQueue.size() - 1) + 1)
    private var textX = 0
    private var textY = 0

    //    val fontFamily = FontFamily(
//        Font(
//            resource = "font.ttf",
//            weight = FontWeight.W400,
//            style = FontStyle.Normal
//        )
//    )
    private val font = Font(Typeface.makeDefault())
    private val paint = Paint().setColor(0xFF00FF00.toInt())

    @Composable
    fun drawCanvas() {
        Canvas(
            modifier = modifier.fillMaxHeight().fillMaxWidth(),
        ) {
            drawRoundRect(
                color = Color.Black,
                topLeft = Offset.Zero,
                cornerRadius = CornerRadius(15f, 15f),
            )
            for (i in 1 until priorityQueue.size()) {
                val item = priorityQueue.arrayList[i] as Item
                x[i] =
                    (lhf / (1 + 2.0.pow(logBase2(i).toDouble())) *
                            (i + 1 - 2.0.pow(logBase2(i).toDouble()))).toInt()
                y[i] = 100 + layerHeight * logBase2(i)

                textX =
                    (lhf / (1 + 2.0.pow(logBase2(i).toDouble())) * (i + 1 - 2.0.pow(logBase2(i).toDouble()))).toInt()
                textY = (120 + layerHeight * logBase2(i))
                drawIntoCanvas {
                    it.nativeCanvas.drawString(
                        "${item.patientName}: ${item.severity}",
                        textX.toFloat(),
                        textY.toFloat(),
                        font,
                        paint
                    )
                }
            }

            for (j in priorityQueue.size() - 1 downTo 2) {
                drawLine(
                    color = Color(0xffdce775),
                    start = Offset((x[j] + 5).toFloat(), (y[j] - 10).toFloat()),
                    end = Offset((x[j / 2] + 5).toFloat(), (y[j / 2] + 30).toFloat()),
                    strokeWidth = 2.5f,
                )
            }
        }
    }
}