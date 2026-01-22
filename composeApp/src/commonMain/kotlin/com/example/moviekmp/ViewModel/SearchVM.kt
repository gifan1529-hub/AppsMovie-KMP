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
    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isEmpty()) {
            _searchResults.value = SearchState.EmptyQuery
        }
    }
}