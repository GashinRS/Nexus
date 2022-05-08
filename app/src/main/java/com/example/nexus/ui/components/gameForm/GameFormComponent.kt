package com.example.nexus.ui.components.gameForm

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexus.data.dataClasses.ListEntry
import com.example.nexus.ui.routes.ListCategory
import com.example.nexus.ui.routes.ListCategoryColors
import com.example.nexus.viewmodels.games.NexusGameDetailViewModel

@Composable
fun GameFormComponent(
    onGameFormOpenChanged: (Boolean) -> Unit,
    getListEntry: () -> ListEntry,
    storeListEntry: (ListEntry) -> Unit,
    deleteListEntry: (ListEntry) -> Unit,
    getGameScore: () -> Int,
    setGameScore: (Int) -> Unit,
    getGameStatus: () -> String,
    setGameStatus: (String) -> Unit,
    getHours: () -> String,
    setHours: (String) -> Unit,
    getMinutes: () -> String,
    setMinutes: (String) -> Unit,
    onShowErrorPopupChanged: (Boolean) -> Unit,
    onShowDeleteWarningChanged: (Boolean) -> Unit,
    setCurrentListEntryMinutes: (Int) -> Unit,
    getEditOrAddGames: () -> String,
    getShowErrorPopup: () -> Boolean,
    getShowDeleteWarning: () -> Boolean,
    setEditOrAddGames: (String) -> Unit
){
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .fillMaxSize()) {
        Row(){
            IconButton(onClick = { onGameFormOpenChanged(false)
                                    focusManager.clearFocus()}) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "close game form",
                )
            }
        }
        Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()) {
            Text(text = getListEntry().title, fontSize = 20.sp)
        }

        //score input
        Row(modifier = Modifier.padding(5.dp)) {
            Text(text = "Your score: ")
            var expanded by remember { mutableStateOf(false) }
            val scoreText = if (getGameScore() == 0){
                "No score"
            } else {
                getGameScore().toString()
            }
            var text by remember { mutableStateOf(scoreText)}
            OutlinedButton(onClick = { expanded = !expanded }) {
                Text(text = text, color=MaterialTheme.colors.onBackground)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = { expanded = false
                                            text = "No score"
                                            setGameScore(0)}) {
                    Text(text = "No score")
                }
                listOf(1,2,3,4,5,6,7,8,9,10).forEach { score ->
                    DropdownMenuItem(onClick = { expanded = false
                                                text = score.toString()
                                                setGameScore(score)}) {
                        Text(text = score.toString())
                    }
                }
            }
        }

        //status input
        Row(modifier = Modifier.padding(5.dp)){
            Text(text = "Status: ")
            var expanded by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf(getGameStatus())}
            OutlinedButton(onClick = { expanded = !expanded }) {
                ListCategoryColors[text]?.let { Text(text = text, color= it) }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                listOf(ListCategory.PLAYING, ListCategory.PLANNED, ListCategory.COMPLETED,
                ListCategory.DROPPED).forEach { status ->
                    DropdownMenuItem(onClick = { expanded = false
                                                text = status.value
                                                setGameStatus(status.value)}) {
                        ListCategoryColors[status.value]
                            ?.let { Text(text = status.value, color = it) }
                    }
                }
            }
        }

        //hours input
        TimeInput(focusManager = focusManager, text = "Hours played: ", getTime = { getHours() },
            setTime = {hours -> setHours(hours)})

        //minutes input
        TimeInput(focusManager = focusManager, text = "Minutes played: ", getTime = { getMinutes() },
            setTime = {minutes -> setMinutes(minutes)})


        Row(modifier = Modifier.padding(5.dp)){
            GameSaveButton(focusManager = focusManager, getHours(), getMinutes(),
                {b:Boolean -> onShowErrorPopupChanged(b)},
                {m:Int -> setCurrentListEntryMinutes(m)},
                {l:ListEntry -> storeListEntry(l)},
                getListEntry(),
                {b:Boolean -> onGameFormOpenChanged(b)}
            )

            GameDeleteButton(getEditOrAddGames()) { b: Boolean -> onShowDeleteWarningChanged(b) }
        }

        if(getShowErrorPopup()){
            AlertDialog(
                onDismissRequest = { },
                confirmButton = {
                    TextButton(onClick = {onShowErrorPopupChanged(false)})
                    { Text(text = "OK", color=MaterialTheme.colors.onBackground) }
                },
                title = { Text(text = "wrong time") },
                text = { Text(text = "Please provide a valid time (integers only)") }
            )
        }

        if(getShowDeleteWarning()){
            AlertDialog(onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    deleteListEntry(getListEntry())
                    setEditOrAddGames(NexusGameDetailViewModel.GameFormButton.ADD.value)
                    onGameFormOpenChanged(false)
                    focusManager.clearFocus()
                    onShowDeleteWarningChanged(false)}) {
                    Text(text = "Confirm", color=MaterialTheme.colors.onBackground)
                }
            },
                dismissButton = {
                    TextButton(onClick = {
                    onShowDeleteWarningChanged(false) }) {
                    Text(text = "Cancel", color=MaterialTheme.colors.onBackground)
                }},
            text = { Text(text = "Are you sure you want to delete this game from your list?")}
            )
        }
    }
}
