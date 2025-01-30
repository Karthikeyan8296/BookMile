package com.example.precisepal.presentation.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.presentation.util.roundToDecimal
import java.time.LocalDate

@Composable
fun LineGraph(
    modifier: Modifier,
    bodyPartValueInstance: List<BodyPartValues>,
    pathAndCircleWidth: Float = 5f,
    helperLinesColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall
) {
    //date value should be in descending order
    val dataPointValues = bodyPartValueInstance.asReversed().map { it.value }
    //take max value and min values
    val highestValue = dataPointValues.maxOrNull() ?: 0f
    val lowestValue = dataPointValues.minOrNull() ?: 0f
    val noOfParts = 3 //dataPointValues.size
    val difference = (highestValue - lowestValue)/noOfParts
    //from that list we are creating an another list
    val valuesList = listOf(
        highestValue.roundToDecimal(),
        (highestValue - difference).roundToDecimal(),
        (lowestValue + difference).roundToDecimal(),
        lowestValue.roundToDecimal()
    )

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        val graphWidth = size.width
        val graphHeight = size.height

        valuesList.forEachIndexed{index, value ->
            val graph80PercentHeight = graphHeight * 0.8f
            val graph4PercentHeight = graphHeight * 0.04f
            val graph10PercentWidth = graphWidth * 0.1f
            val xPosition = 0f
            val yPosition = (graph80PercentHeight/noOfParts)*index

            //values text
            drawText(
                textMeasurer = textMeasurer,
                text = "$value",
                style = textStyle,
                topLeft = Offset(x = xPosition, y= yPosition)
            )
            //canvas lines
            drawLine(
                color = helperLinesColor,
                strokeWidth = pathAndCircleWidth,
                start = Offset(x = xPosition+graph10PercentWidth, y = yPosition+graph4PercentHeight),
                end = Offset(x = graphWidth, y = yPosition+graph4PercentHeight)
            )
        }
    }
}

@SuppressLint("NewApi")
@Preview(showBackground = true)
@Composable
private fun LineGraphPreview() {
    val dummyList = listOf(
        BodyPartValues(value = 72.2f, date = LocalDate.of(2023, 5, 1)),
        BodyPartValues(value = 73.0f, date = LocalDate.of(2023, 5, 2)),
        BodyPartValues(value = 72.1f, date = LocalDate.of(2023, 5, 3)),
        BodyPartValues(value = 52.2f, date = LocalDate.of(2023, 5, 4)),
        BodyPartValues(value = 65.0f, date = LocalDate.of(2023, 5, 5)),
        BodyPartValues(value = 89.1f, date = LocalDate.of(2023, 5, 6)),
        BodyPartValues(value = 12.0f, date = LocalDate.of(2023, 5, 7))
    )
    LineGraph(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(ratio = 2 / 1f),
        bodyPartValueInstance = dummyList
    )
}