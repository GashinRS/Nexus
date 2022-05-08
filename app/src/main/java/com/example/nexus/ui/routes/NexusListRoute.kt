package com.example.nexus.ui.routes

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nexus.viewmodels.NexusListViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.navigation.NavHostController
import com.example.nexus.ui.components.NexusTopBar
import com.example.nexus.ui.components.list.ListCategoryComponent
import com.example.nexus.ui.components.list.ListTopNavigationBar
import com.example.nexus.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalAnimationApi
@Composable
fun NexusListRoute(
    vM: NexusListViewModel,
    navController: NavHostController,
    onOpenGameDetails : (gameId: Long) -> Unit,
){
    Scaffold(
        topBar = { NexusTopBar(navController = navController, canPop = false) }
    ) {
        Scaffold(topBar = {
            ListTopNavigationBar(vM.getSelectedCategory(), vM::onSelectedCategoryChanged)
        }){
            ListCategoryComponent(
                category = vM.getSelectedCategory(),
                getCategoryByName = { category -> vM.getCategoryByName(category) },
                toggleDescendingOrAscendingIcon = {vM.toggleDescendingOrAscendingIcon()},
                getDescendingOrAscendingIcon = {vM.getDescendingOrAscendingIcon()},
                getSelectedCategory = {vM.getSelectedCategory()},
                setSortOption = {s -> vM.setSortOption(s)},
                getSortOption = {vM.getSortOption()},
                onOpenGameDetails = onOpenGameDetails)
        }
    }

}

enum class ListCategory(val value: String){
    ALL("All"),
    PLAYING("Playing"),
    COMPLETED("Completed"),
    PLANNED("Planned"),
    DROPPED("Dropped")
}

fun getAllListCategories(): List<ListCategory> =
    listOf(ListCategory.ALL, ListCategory.PLAYING, ListCategory.COMPLETED,
        ListCategory.PLANNED, ListCategory.DROPPED
    )

val ListCategoryColors = mapOf(ListCategory.PLAYING.value to Playing,
    ListCategory.COMPLETED.value to Completed,
    ListCategory.DROPPED.value to Dropped,
    ListCategory.PLANNED.value to Planned
)