package com.example.products.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.products.common.Constants.PAGE_PRODUCT_LIMIT
import com.example.products.domain.model.Product
import com.example.products.ui.cards.LoadingCard
import com.example.products.ui.cards.ProductCard
import com.example.products.ui.theme.ProductsTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsTheme {
                val viewModel: MainViewModel by viewModel()
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is MainScreenEffect.Error -> snackBarHostState.showSnackbar(effect.errorMessage)
            }
        }
    }

    val productPagingItems: LazyPagingItems<Product> =
        viewModel.productsState.collectAsLazyPagingItems()

    MainScreenStateless(
        snackBarHostState = remember(snackBarHostState) { snackBarHostState },
        products = productPagingItems,
        onError = { errorMessage ->
            viewModel.onEvent(MainScreenEvent.onError(errorMessage))
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenStateless(
    snackBarHostState: SnackbarHostState,
    products: LazyPagingItems<Product>,
    onError: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { SnackbarError(it) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(2),
            content = {
                items(products.itemCount) { index ->
                    val product = products[index]
                    product?.let {
                        ProductCard(
                            title = product.title,
                            description = product.description,
                            thumbnail = product.thumbnail
                        )
                    }
                }
                products.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            items(PAGE_PRODUCT_LIMIT) {
                                LoadingCard()
                            }
                        }

                        loadState.refresh is LoadState.Error -> {
                            val error = products.loadState.refresh as LoadState.Error
                            onError(error.error.localizedMessage!!)
                        }

                        loadState.append is LoadState.Loading -> {
                            items(PAGE_PRODUCT_LIMIT) {
                                LoadingCard()
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            val error = products.loadState.append as LoadState.Error
                            onError(error.error.localizedMessage!!)
                        }
                    }
                }
            }
        )
    }
}


