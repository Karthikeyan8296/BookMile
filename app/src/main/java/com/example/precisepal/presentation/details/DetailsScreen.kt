package com.example.precisepal.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.TimeRange
import com.example.precisepal.presentation.components.LineGraph
import com.example.precisepal.presentation.components.MeasureMateDialog
import com.example.precisepal.presentation.components.MeasureUnitBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(modifier: Modifier = Modifier) {
    //Dialog
    var isDeleteDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    MeasureMateDialog(
        isOpen = isDeleteDialogOpen,
        onDismiss = { isDeleteDialogOpen = false },
        onConfirm = { isDeleteDialogOpen = false },
        title = "delete Body Part",
        body = { Text("Are you sure, want to delete?") },
        confirmButtonText = "Delete",
        dismissButtonText = "Cancel"
    )
    //Bottom sheet
    var isBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val preBuildSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    MeasureUnitBottomSheet(preBuildSheetState = preBuildSheetState,
        isOpen = isBottomSheetOpen,
        onDismiss = { isBottomSheetOpen = false },
        //this will update the state and close the bottom sheet too!
        onItemClicked = {
            scope.launch { preBuildSheetState.hide() }.invokeOnCompletion {
                if (!preBuildSheetState.isVisible) {
                    isBottomSheetOpen = false
                }
            }
        })

    var selectedTimeRange by rememberSaveable {
        mutableStateOf(TimeRange.LAST_7_DAYS)
    }

    //Main Layout
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        DetailsTopBar(
            onDeleteIconClick = { isDeleteDialogOpen = true },
            onBackIconClick = {},
            onUnitIconClick = { isBottomSheetOpen = true },
            bodyPartInstance = BodyPart(
                name = "Shoulder",
                measuringUnit = "cm",
                isActive = true,
                bodyPartId = "xxx",
                latestValue = null
            )
        )
        ChartTimeRangeButton(
            onButtonClick = { selectedTimeRange = it },
            selectedTimeRange = selectedTimeRange
        )
    }
}

//Header component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    onDeleteIconClick: () -> Unit,
    onBackIconClick: () -> Unit,
    onUnitIconClick: () -> Unit,
    bodyPartInstance: BodyPart?,
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = bodyPartInstance?.name ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onDeleteIconClick() }) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
            }
            Spacer(Modifier.width(8.dp))
            Text(text = bodyPartInstance?.measuringUnit ?: "")
            IconButton(onClick = { onUnitIconClick() }) {
                Icon(imageVector = Icons.Rounded.ArrowDropDown, contentDescription = null)
            }
        },
    )
}


//this is the outer layout of that button
@Composable
private fun ChartTimeRangeButton(
    selectedTimeRange: TimeRange,
    onButtonClick: (TimeRange) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 24.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //we are getting the data from enum class
        TimeRange.entries.forEach { timeRange ->
            TimeRangeSelectionButton(
                label = timeRange.label,
                labelTextStyle = if (timeRange == selectedTimeRange) {
                    MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                backgroundColor = if (timeRange == selectedTimeRange) {
                    MaterialTheme.colorScheme.surface
                } else {
                    Color.Transparent
                },
                onClick = { onButtonClick(timeRange) }
            )
        }
    }
}

//this is the inner button
//TimeRangeSelectionButton
@Composable
private fun TimeRangeSelectionButton(
    label: String,
    labelTextStyle: TextStyle,
    backgroundColor: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .width(104.dp)
            .clickable(
                //to remove that clickable effect shadow
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .background(backgroundColor, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailsScreenPreview() {
    DetailsScreen()
}