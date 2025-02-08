package com.example.precisepal.presentation.details

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.BodyPartValues
import com.example.precisepal.domain.model.TimeRange
import com.example.precisepal.presentation.components.LineGraph
import com.example.precisepal.presentation.components.MeasureMateDialog
import com.example.precisepal.presentation.components.MeasureUnitBottomSheet
import com.example.precisepal.presentation.components.NewValueInputBar
import com.example.precisepal.presentation.components.datePicker
import com.example.precisepal.presentation.util.changeLocalDateToFullDate
import com.example.precisepal.presentation.util.changeMillisToGraphDate
import com.example.precisepal.presentation.util.pastOrPresentSelectableDates
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    windowSizeInstance: WindowWidthSizeClass,
    bodyPartIDInstance: String,
    onBackClickInstance: () -> Unit,
    paddingValuesInstance: PaddingValues,
) {
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
    MeasureUnitBottomSheet(
        preBuildSheetState = preBuildSheetState,
        isOpen = isBottomSheetOpen,
        onDismiss = { isBottomSheetOpen = false },
        //this will update the state and close the bottom sheet too!
        onItemClicked = {
            scope.launch { preBuildSheetState.hide() }.invokeOnCompletion {
                if (!preBuildSheetState.isVisible) {
                    isBottomSheetOpen = false
                }
            }
        }
    )

    var selectedTimeRange by rememberSaveable {
        mutableStateOf(TimeRange.LAST_7_DAYS)
    }

    //input state
    var inputValue by remember {
        mutableStateOf("")
    }
    //focus manager - for IME actions
    val focusManager = LocalFocusManager.current

    //for hiding state
    var isInputValueCardVisible by rememberSaveable {
        mutableStateOf(true)
    }

    //date picker
    var isDatePickerOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState(
        //this will set the current date as default
        initialSelectedDateMillis = System.currentTimeMillis(),
        //restricting the user, not to select the future dates
        //we need to create an object
        selectableDates = pastOrPresentSelectableDates
    )
    datePicker(
        isOpen = isDatePickerOpen,
        onConfirm = { isDatePickerOpen = false },
        onDismissReq = { isDatePickerOpen = false },
        state = datePickerState
    )
    val dummyBodyPart = BodyPart(
        name = "Shoulder: $bodyPartIDInstance",
        measuringUnit = "cm",
        isActive = true,
        bodyPartId = "xxx",
        latestValue = null
    )

    when (windowSizeInstance) {
        WindowWidthSizeClass.Compact -> {
            //Screen UI
            Box(
                modifier = modifier
                    .padding(paddingValuesInstance)
//                    .consumeWindowInsets(contentPadding)
//                    .imePadding()
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                //Main Layout
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    //header
                    DetailsTopBar(
                        onDeleteIconClick = { isDeleteDialogOpen = true },
                        onBackIconClick = { onBackClickInstance() },
                        onUnitIconClick = { isBottomSheetOpen = true },
                        bodyPartInstance = dummyBodyPart
                    )
                    //toggle button component
                    ChartTimeRangeButton(
                        onButtonClick = { selectedTimeRange = it },
                        selectedTimeRange = selectedTimeRange
                    )
                    Spacer(Modifier.height(16.dp))
                    //graph component
                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 2 / 1f)
                            .padding(16.dp),
                        bodyPartValueInstance = dummyList
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HistorySection(
                        bodyPartInstance = dummyList,
                        onDeleteIconClick = {},
                        measuringUnitCode = "cm"
                    )
                }

                //input field
                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    value = inputValue,
                    //it will display the date over here
                    date = datePickerState.selectedDateMillis.changeMillisToGraphDate()
                        .changeLocalDateToFullDate(),
                    onDoneClick = {},
                    onValueChange = { inputValue = it },
                    onCalenderIconClick = { isDatePickerOpen = true },
                    onDoneImeActionClick = {
                        focusManager.clearFocus()
                    },
                    isInputValueCardVisible = isInputValueCardVisible
                )
                //hide
                InputCardHideIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 6.dp),
                    isInputValueCardVisible = isInputValueCardVisible,
                    onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                )
            }
        }
        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                //header
                DetailsTopBar(
                    onDeleteIconClick = { isDeleteDialogOpen = true },
                    onBackIconClick = {},
                    onUnitIconClick = { isBottomSheetOpen = true },
                    bodyPartInstance = dummyBodyPart
                )
                Row(modifier = modifier.fillMaxSize()) {
                    Column(
                        modifier = modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        //toggle button component
                        ChartTimeRangeButton(
                            onButtonClick = { selectedTimeRange = it },
                            selectedTimeRange = selectedTimeRange
                        )
                        Spacer(Modifier.height(16.dp))
                        //graph component
                        LineGraph(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 2 / 1f)
                                .padding(16.dp),
                            bodyPartValueInstance = dummyList
                        )
                    }
                    Box(
                        modifier = modifier
                            .fillMaxHeight()
                            .weight(1f),
                    ) {
                        HistorySection(
                            bodyPartInstance = dummyList,
                            onDeleteIconClick = {},
                            measuringUnitCode = "cm"
                        )
                        //input field
                        NewValueInputBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            value = inputValue,
                            //it will display the date over here
                            date = datePickerState.selectedDateMillis.changeMillisToGraphDate()
                                .changeLocalDateToFullDate(),
                            onDoneClick = {},
                            onValueChange = { inputValue = it },
                            onCalenderIconClick = { isDatePickerOpen = true },
                            onDoneImeActionClick = {
                                focusManager.clearFocus()
                            },
                            isInputValueCardVisible = isInputValueCardVisible
                        )
                        //hide
                        InputCardHideIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 6.dp),
                            isInputValueCardVisible = isInputValueCardVisible,
                            onClick = { isInputValueCardVisible = !isInputValueCardVisible }
                        )
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
val dummyList = listOf(
    BodyPartValues(value = 71.5f, date = LocalDate.of(2023, 7, 1)),
    BodyPartValues(value = 72.3f, date = LocalDate.of(2023, 7, 2)),
    BodyPartValues(value = 70.8f, date = LocalDate.of(2023, 7, 3)),
    BodyPartValues(value = 69.4f, date = LocalDate.of(2023, 8, 4)),
    BodyPartValues(value = 68.9f, date = LocalDate.of(2023, 8, 5)),
    BodyPartValues(value = 73.2f, date = LocalDate.of(2023, 9, 6)),
    BodyPartValues(value = 71.7f, date = LocalDate.of(2023, 9, 7)),
    BodyPartValues(value = 70.1f, date = LocalDate.of(2023, 9, 8)),
    BodyPartValues(value = 69.8f, date = LocalDate.of(2023, 10, 9)),
    BodyPartValues(value = 72.5f, date = LocalDate.of(2023, 10, 10))
)


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
        windowInsets = WindowInsets(0, 0, 0 , 0),
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

//History lazy component
@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistorySection(
    bodyPartInstance: List<BodyPartValues>,
    measuringUnitCode: String?,
    onDeleteIconClick: () -> Unit,
) {
    LazyColumn {
        //it will take the groups of months
        val grouped = bodyPartInstance.groupBy { it.date.month }

        item {
            Text(
                text = "History", style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 12.dp)
            )
        }
        grouped.forEach { (month, bodyPartInstance) ->
            //for months display
            stickyHeader {
                Text(
                    text = month.name, style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                )
            }
            items(bodyPartInstance) { bodyPartInstance ->
                HistoryCard(
                    bodyPartInstance = bodyPartInstance,
                    measuringUnitCode = measuringUnitCode,
                    onDeleteIconClick = onDeleteIconClick
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

//history card
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartInstance: BodyPartValues,
    measuringUnitCode: String?,
    onDeleteIconClick: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.DateRange, contentDescription = "")
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = bodyPartInstance.date.changeLocalDateToFullDate(),
                modifier = modifier.weight(8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = bodyPartInstance.value.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = measuringUnitCode.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(onClick = { onDeleteIconClick() }) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = ""
                )
            }
        }

    }
}

@Composable
fun InputCardHideIcon(
    modifier: Modifier = Modifier,
    isInputValueCardVisible: Boolean,
    onClick: () -> Unit,
) {
    IconButton(modifier = modifier, onClick = { onClick() }) {
        Icon(
            imageVector = if (isInputValueCardVisible) Icons.Rounded.KeyboardArrowDown
            else Icons.Rounded.KeyboardArrowUp,
            contentDescription = null
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@PreviewScreenSizes
@Composable
private fun DetailsScreenPreview() {
    DetailsScreen(
        windowSizeInstance = WindowWidthSizeClass.Compact,
        bodyPartIDInstance = "",
        onBackClickInstance = {},
        paddingValuesInstance = PaddingValues(0.dp)
    )
}