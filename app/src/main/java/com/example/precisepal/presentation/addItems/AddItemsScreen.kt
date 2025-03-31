package com.example.precisepal.presentation.addItems

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.precisepal.domain.model.predefinedBooks
import com.example.precisepal.presentation.components.AddBookDialog
import com.example.precisepal.presentation.theme.InterFontFamily
import com.example.precisepal.presentation.util.UIEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


@Composable
fun AddItemsScreen(
    onBackClickInstance: () -> Unit,
    paddingValuesInstance: PaddingValues,
    snackbarHostStateInstanceScreen: SnackbarHostState,
    state: AddItemsState,
    onEvent: (AddItemEvent) -> Unit,
    uiEvent: Flow<UIEvent>,
) {

    //snack bar
    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    snackbarHostStateInstanceScreen.showSnackbar(event.message)
                }

                UIEvent.HideBottomSheet -> {}
                UIEvent.NavigateBack -> {}
            }
        }
    }
    //Add items dialog
    var isDialogAdd by rememberSaveable {
        mutableStateOf(false)

    }

    val value = state.textFieldValue
    var inputError by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    inputError = when {
        value.isBlank() -> "Please enter your book name"
        else -> null
    }

    AddBookDialog(
        body = {
            Column(modifier = Modifier) {
                Text(text = "Manage your collection by adding a new book or updating an existing one.")
                Spacer(Modifier.size(16.dp))
                OutlinedTextField(
                    placeholder = { Text(text = "Log your book") },
                    value = value,
                    textStyle = TextStyle(color = Color.Black),
                    onValueChange = { onEvent(AddItemEvent.OnTextFieldValueChange(it)) },
                    //error message will show if it has error
                    isError = inputError != null && value.isNotBlank(),
                    singleLine = true,
                    supportingText = { Text(text = inputError.orEmpty(), color = Color.Gray) },
                )
            }
        },
        isError = inputError == null,
        title = "Add or Update Book",
        isOpen = isDialogAdd,
        onDismiss = {
            isDialogAdd = false
            onEvent(AddItemEvent.OnAddItemDialogDismiss)
        },
        onConfirm = {
            isDialogAdd = false
            onEvent(AddItemEvent.UpsertItem)
        },
        confirmButtonText = "Save",
        dismissButtonText = null
    )

    //Main Screen layout
    Column(
        modifier = Modifier
            .padding(paddingValuesInstance)
            .fillMaxSize()
            .background(color = Color(0xFFF6F6F6))
    ) {
        //TopAppBar
        TopBar(onAddIconClick = { isDialogAdd = true },
            onBackIconClick = { onBackClickInstance() }
        )

        //responsive lazy layout
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp)
        ) {
            //if we are implementing single card at top, we can use item!
            //item {}
            //bunch of cards can be defined here, with items!
            items(state.books) { bodyPart ->
                ItemCard(
                    name = bodyPart.name,
                    onClick = {
                        isDialogAdd = true
                        onEvent(AddItemEvent.OnItemClick(bodyPart))
                    },
                    isChecked = bodyPart.isActive,
                    onCheckChange = { onEvent(AddItemEvent.OnItemIsActiveChange(bodyPart)) },
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF6F6F6),
            titleContentColor = Color(0xFF5863BD),
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        ),
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Your Library",
                fontWeight = FontWeight.SemiBold,
                fontFamily = InterFontFamily
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBackIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null,
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
                    color = Color.Black,
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
    AddItemsScreen(
        onBackClickInstance = {},
        paddingValuesInstance = PaddingValues(0.dp),
        snackbarHostStateInstanceScreen = SnackbarHostState(),
        state = AddItemsState(books = predefinedBooks),
        uiEvent = flowOf(),
        onEvent = {}
    )
}