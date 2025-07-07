package sree.ddukk.vlogapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VlogViewModel @Inject constructor(
    private val repository: VlogRepository
) : ViewModel() {

    val vlogList = repository.allVlogs
        .map { it.sortedByDescending { vlog -> vlog.id } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertVlog(vlog: VlogEntry) {
        viewModelScope.launch {
            repository.insertVlog(vlog)
        }
    }
    fun deleteVlog(vlog: VlogEntry) {
        viewModelScope.launch {
            repository.deleteVlog(vlog)
//            loadVlogs() // 🔄 refresh list if you're caching
        }
    }


}
