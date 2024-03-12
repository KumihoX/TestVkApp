package com.example.products.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.products.domain.GetProductsUseCase
import com.example.products.domain.model.Product
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class MainViewModel(private val getProductsUseCase: GetProductsUseCase) : ViewModel() {
    private val _productsState: MutableStateFlow<PagingData<Product>> =
        MutableStateFlow(value = PagingData.empty())
    val productsState: MutableStateFlow<PagingData<Product>> get() = _productsState

    private val _effect: MutableSharedFlow<MainScreenEffect> = MutableSharedFlow()
    val effect: SharedFlow<MainScreenEffect> =
        _effect.shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.onError -> viewModelScope.launch {
                _effect.emit(MainScreenEffect.Error(event.errorMessage))
            }
        }
    }

    private suspend fun getProducts() {
        try {
            getProductsUseCase()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { products ->
                    _productsState.value = products
                }
        } catch (e: Exception) {
            e.message?.let { _effect.emit(MainScreenEffect.Error(it)) }
        }
    }
}

sealed class MainScreenEffect {
    data class Error(val errorMessage: String = "") : MainScreenEffect()
}

sealed class MainScreenEvent {
    data class onError(val errorMessage: String) : MainScreenEvent()
}