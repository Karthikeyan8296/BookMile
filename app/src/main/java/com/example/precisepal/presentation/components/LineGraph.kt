package com.example.precisepal.presentation.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.copy
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.presentation.theme.InterFontFamily
import com.example.precisepal.presentation.util.changeLocalDateToGraphDate
import com.example.precisepal.presentation.util.roundToDecimal
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LineGraph(
    modifier: Modifier,
    bodyPartValueInstance: List<BodyPartValues>,
    pathAndCircleWidth: Float = 5f,
    pathAndCircleColor: Color = Color(0xFF314AF3),
    graphTextColor: Color = Color(0xFF5863BD),
    helperLinesColor: Color = Color.LightGray,
    textStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.SemiBold
    ),
) {
    //value should be in descending order
    val dataPointValues = bodyPartValueInstance.asReversed().map { it.value }
    //dates value
    val dates = bodyPartValueInstance.asReversed().map { it.date.changeLocalDateToGraphDate() }

    //take max value and min values
    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfParts = 3 //dataPointValues.size
    val difference = (highestValue - lowestValue) / noOfParts

    //from that list we are creating an another value list
    val valuesList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    //taking first to last date values
    val firstDate = dates.firstOrNull() ?: ""
    val secondDate = dates.getOrNull((dates.size * 0.33).toInt()) ?: ""
    val thirdDate = dates.getOrNull((dates.size * 0.67).toInt()) ?: ""
    val lastDate = dates.lastOrNull() ?: ""

    //from that list we are creating an another date list
    val dateList = listOf(firstDate, secondDate, thirdDate, lastDate)

    val textMeasurer = rememberTextMeasurer()

    //Animation for the graph
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(key1 = Unit) {
        animationProgress.animateTo(1f, tween(durationMillis = 3000))
    }

    Canvas(modifier = modifier) {
        val graphWidth = size.width
        val graphHeight = size.height
        val points = calculatePoints(dataPointValues, graphWidth, graphHeight)
        val path = createPath(points)
        val filledPath = createFilledPath(path, graphHeight, graphWidth)

        //this is for valueList
        valuesList.forEachIndexed { index, value ->
            val graph80PercentHeight = graphHeight * 0.8f
            val graph4PercentHeight = graphHeight * 0.04f
            val graph10PercentWidth = graphWidth * 0.1f
            val xPosition = 0f
            val yPosition = (graph80PercentHeight / noOfParts) * index

            //values text
            drawText(
                textMeasurer = textMeasurer,
                text = "$value",
                style = textStyle.copy(color = graphTextColor),
                topLeft = Offset(x = xPosition, y = yPosition)
            )
            //canvas lines
            drawLine(
                color = helperLinesColor,
                strokeWidth = pathAndCircleWidth,
                start = Offset(
                    x = xPosition + graph10PercentWidth,
                    y = yPosition + graph4PercentHeight
                ),
                end = Offset(x = graphWidth, y = yPosition + graph4PercentHeight)
            )
        }

        //this is for date list
        dateList.forEachIndexed { index, date ->
            val graph10PercentWidth = graphWidth * 0.1f
            val graph77PercentWidth = graphWidth * 0.77f
            val xPosition = (graph77PercentWidth / noOfParts) * index + graph10PercentWidth
            val yPosition = graphHeight * 0.9f
            drawText(
                textMeasurer = textMeasurer,
                text = date,
                style = textStyle.copy(color = graphTextColor),
                topLeft = Offset(x = xPosition, y = yPosition)
            )
        }

        //it will clip the rectangle from the right side
        clipRect(right = graphWidth * animationProgress.value) {

            //this is for points list
            points.forEach { point ->
                drawCircle(
                    color = pathAndCircleColor,
                    radius = pathAndCircleWidth,
                    center = point
                )
            }

            //this is for points path
            drawPath(
                path = path,
                color = pathAndCircleColor,
                style = Stroke(3f)
            )

            //this is for gradient filled path
            if (dataPointValues.isNotEmpty()) {
                drawPath(
                    path = filledPath,
                    brush = Brush.verticalGradient(
                        listOf(pathAndCircleColor.copy(alpha = 0.4f), Color.Transparent)
                    )
                )
            }
        }
    }
}

//creates points
private fun calculatePoints(
    dataPoints: List<Float>,
    graphWith: Float,
    graphHeight: Float,
): List<Offset> {
    val graph90PercentWidth = graphWith * 0.9f
    val graph10PercentWidth = graphWith * 0.1f
    val graph80PercentHeight = graphHeight * 0.78f
    val graph5PercentHeight = graphHeight * 0.05f

    val highestValue = dataPoints.maxOrNull() ?: 0f
    val lowestValue = dataPoints.minOrNull() ?: 0f
    val valueRange = highestValue - lowestValue

    //calculating x-coordinates
    val xPosition = dataPoints.indices.map { index ->
        (graph90PercentWidth / (dataPoints.size - 1)) * index + graph10PercentWidth
    }
    //calculating y-position
    val yPosition = dataPoints.map { value ->
        //normalized each data point from 0 to 1
        //it will be in 0s and 1s
        val normalizedValue = (value - lowestValue) / valueRange
        val yPosition = graph80PercentHeight * (1 - normalizedValue) + graph5PercentHeight
        //returning
        yPosition
    }
    return xPosition.zip(yPosition) { x, y -> Offset(x, y) }
}

//for path line creating
private fun createPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isNotEmpty()) {
        //starting
        path.moveTo(points[0].x, points[0].y)
        //for each
        for (i in 1 until points.size) {
            val currentPoint = points[i]
            val previewPoint = points[i - 1]
            val controlPointX = (previewPoint.x + currentPoint.x) / 2f
            //cubicTo - creates an smooth path
            path.cubicTo(
                controlPointX, previewPoint.y,
                controlPointX, currentPoint.y,
                currentPoint.x, currentPoint.y
            )
        }
    }
    return path
}

//creates gradient colors
private fun createFilledPath(path: Path, graphHeight: Float, graphWith: Float): Path {
    val filledPath = Path()
    val graph80PercentHeight = graphHeight * 0.84f
    val graph10PercentWidth = graphWith * 0.1f

    filledPath.addPath(path)
    filledPath.lineTo(graphWith, graph80PercentHeight)
    filledPath.lineTo(graph10PercentWidth, graph80PercentHeight)
    filledPath.close()
    return filledPath
}

@SuppressLint("NewApi")
@Preview(showBackground = true)
@Composable
private fun LineGraphPreview() {
    val dummyList = listOf(
        BodyPartValues(value = 71.5f, date = LocalDate.of(2023, 7, 1)),
        BodyPartValues(value = 72.3f, date = LocalDate.of(2023, 7, 2)),
        BodyPartValues(value = 70.8f, date = LocalDate.of(2023, 7, 3)),
        BodyPartValues(value = 69.4f, date = LocalDate.of(2023, 7, 4)),
        BodyPartValues(value = 68.9f, date = LocalDate.of(2023, 7, 5)),
        BodyPartValues(value = 73.2f, date = LocalDate.of(2023, 7, 6)),
        BodyPartValues(value = 71.7f, date = LocalDate.of(2023, 7, 7)),
        BodyPartValues(value = 70.1f, date = LocalDate.of(2023, 7, 8)),
        BodyPartValues(value = 69.8f, date = LocalDate.of(2023, 7, 9)),
        BodyPartValues(value = 72.5f, date = LocalDate.of(2023, 7, 10))
    )
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 2 / 1f)
            .padding(16.dp),
        bodyPartValueInstance = dummyList
    )
}