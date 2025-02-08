package com.example.precisepal.presentation.addItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.domain.model.BodyPart
import com.example.precisepal.domain.model.predefinedBodyPart
import com.example.precisepal.presentation.components.MeasureMateDialog


@Composable
fun AddItemsScreen(
    onBackClickInstance : () -> Unit
) {

    //Add items dialog
    var isDialogAdd by rememberSaveable {
        mutableStateOf(false)

    }
    MeasureMateDialog(
        body = {
            OutlinedTextField(value = "", onValueChange = {})
        },
        title = "Add new item",
        isOpen = isDialogAdd,
        onDismiss = { isDialogAdd = false },
        onConfirm = { isDialogAdd = false },
        confirmButtonText = "Save",
        dismissButtonText = null
    )

    //Main Screen layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        //TopAppBar
        TopBar(onAddIconClick = { isDialogAdd = true },
            onBackIconClick = {onBackClickInstance()}
        )

        //responsive lazy layout
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp)
        ) {
            //if we are implementing single card at top, we can use item!
            //item {}
            //bunch of cards can be defined here, with items!
            items(predefinedBodyPart) { bodyPart ->
                ItemCard(
                    name = bodyPart.name,
                    onClick = {},
                    isChecked = true,
                    onCheckChange = {},
                    bodyPartInstance = bodyPart
                )
            }
        }
    }
}


//Header component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onAddIconClick: () -> Unit, onBackIconClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { Text("Add new item") },
        navigationIcon = {
            IconButton(onClick = { onBackIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = { onAddIconClick() }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
    )
}

//Body component//
@Composable
//the bodyPart is called from the domain
private fun ItemCard(
    bodyPartInstance: BodyPart,
    name: String,
    onClick: () -> Unit,
    isChecked: Boolean,
    onCheckChange: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = name,
                //to make sure if the text is too long it doesn't crash the app
                modifier = Modifier.weight(8f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isChecked, onCheckedChange = { onCheckChange() })
        }
    }
}

@PreviewScreenSizes
@Composable
private fun AddItemsScreenPreview() {
    AddItemsScreen(onBackClickInstance = {})
}