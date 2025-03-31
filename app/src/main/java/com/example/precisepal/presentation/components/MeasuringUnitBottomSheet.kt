package com.example.precisepal.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.precisepal.domain.model.ProgressStatus
import com.example.precisepal.presentation.theme.InterFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureUnitBottomSheet(
    onDismiss: () -> Unit,
    isOpen: Boolean,
    preBuildSheetState: SheetState,
    onItemClicked: (ProgressStatus) -> Unit,
) {
    //this state will open the full bottom sheet
    if (isOpen) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = preBuildSheetState,
            containerColor = Color.White,
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(
                        text = "Your reading progress",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = InterFontFamily,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(Modifier.height(10.dp))
                    HorizontalDivider(color = Color.Gray)
                }
            },
        ) {
            LazyColumn(
                modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(ProgressStatus.entries){ unit ->
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClicked(unit) }
                        .padding(vertical = 12.dp, horizontal = 16.dp)) {
                        Text(
                            text = "${unit.code} - ${unit.label}",
                            color = Color.Black,
                            fontFamily = InterFontFamily,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}