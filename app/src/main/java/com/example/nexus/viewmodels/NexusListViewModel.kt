package com.example.nexus.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nexus.data.db.ListEntity
import com.example.nexus.data.repositories.ListRepository
import com.example.nexus.data.web.ListEntry
import com.example.nexus.ui.routes.list.ListCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NexusListViewModel  @Inject constructor(private val repo: ListRepository) : ViewModel(){

    private val selectedCategory = mutableStateOf(ListCategory.ALL)

    private val searchQuery = MutableStateFlow("")

    fun onSelectedCategoryChanged(category: ListCategory){
        selectedCategory.value = category
    }

    fun getSelectedCategory(): ListCategory {
        return selectedCategory.value
    }

    fun wipeDatabase() {
        viewModelScope.launch { repo.wipeDatabase() }
    }

    fun storeBackendGamesInDb(){
        repo.getBackendGames().forEach { game ->
            storeListEntry(game)
        }
    }

    val allGames: StateFlow<List<ListEntity>> by lazy {
        repo.allGames.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    val playing: StateFlow<List<ListEntity>> by lazy {
        repo.playing.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    val completed: StateFlow<List<ListEntity>> by lazy {
        repo.completed.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    val planned: StateFlow<List<ListEntity>> by lazy {
        repo.planned.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    val dropped: StateFlow<List<ListEntity>> by lazy {
        repo.dropped.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun getCategory(category: String): StateFlow<List<ListEntity>> {
        val games: StateFlow<List<ListEntity>> by lazy {
            if (category == ListCategory.ALL.value){
                repo.allGames.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
            } else{
                repo.getCategory(category).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
            }
        }
        return games
    }

    fun storeListEntry(entry: ListEntry) = viewModelScope.launch { repo.storeListEntry(entry) }

    fun deleteListEntry(entity: ListEntity) = viewModelScope.launch { repo.deleteListEntry(entity) }

    private val _backendGames = MutableStateFlow(repo.getBackendGames())
    val backendGames: StateFlow<List<ListEntry>> = _backendGames

    var testList = MutableStateFlow(ArrayList<ListEntry>())

//    fun firestoreTest(){
//        viewModelScope.launch {
//            testList = repo.getTest() as MutableStateFlow<ArrayList<ListEntry>>
//        }
//    }

//    fun getTestList(): StateFlow<ArrayList<ListEntry>> {
//        firestoreTest()
//        return testList
//    }

//    val test: StateFlow<ArrayList<ListEntry>> by lazy {
//        repo.getTest().stateIn(viewModelScope, SharingStarted.Lazily, ArrayList())
//    }
//    fun firestoreTest(): StateFlow<Iterable<QueryDocumentSnapshot>?> {
//        val test = repo.firestoreTest.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//        return test
//    }

    fun writeTest() = repo.writeTest()
}