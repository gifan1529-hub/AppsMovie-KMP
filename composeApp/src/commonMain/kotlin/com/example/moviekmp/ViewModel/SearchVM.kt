package com.example.moviekmp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviekmp.Domain.Usecase.SearchState
import com.example.moviekmp.Domain.Usecase.SearchUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * viewModel dari search
 */
class SearchVM (
    private val searchUC: SearchUC
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<SearchState>(SearchState.EmptyQuery)
    val searchResults: StateFlow<SearchState> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    init {
        setupSearch()
    }

    /**
     * ngatur setup search
     * nunggu 500 ms sebelum melakukan pencarian
     * nge cegah spam pas pencarian
     * hanya nyari pas ada query yang di ketik
     */
    private fun setupSearch() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotEmpty() }
                .collect { query ->
                    _isSearching.value = true
                    searchUC.execute(query).collect { state->
                        _searchResults.value = state
                        _isSearching.value = false
                    }
                }
        }
    }

    /**
     * memperbarui state search query sesuai sama yang user ketik
     * jika query kosong maka akan menampilkan empty query
     */
    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) {
            _searchResults.value = SearchState.EmptyQuery
        }
    }
}