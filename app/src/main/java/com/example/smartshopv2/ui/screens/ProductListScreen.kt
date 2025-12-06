package com.example.smartshopv2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshopv2.data.local.Product
import com.example.smartshopv2.ui.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = viewModel(),
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit
) {
    val products by viewModel.products.collectAsState(initial = emptyList())

    ProductListContent(
        products = products,
        onAddClick = onAddClick,
        onEditClick = onEditClick,
        onDeleteClick = { product -> viewModel.deleteProduct(product) }
    )
}


@Composable
fun ProductListContent(
    products: List<Product>,
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit,
    onDeleteClick: (Product) -> Unit
) {
    Scaffold(
        // topBar = { TopAppBar(title = { Text("Products") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(products) { product ->
                ProductRow(
                    product = product,
                    onClick = { onEditClick(product) },
                    onDeleteClick = { onDeleteClick(product) }
                )
            }
        }
    }
}

@Composable
fun ProductRow(
    product: Product,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Quantity: ${product.quantity}")
                Text(text = "Price: \$${product.price}")
            }
            Button(onClick = onDeleteClick) {
                Text("Delete")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    val sampleProducts = listOf(
        Product(id = "1", name = "iPhone 15", quantity = 5, price = 999.99),
        Product(id = "2", name = "Samsung Galaxy", quantity = 10, price = 899.99),
        Product(id = "3", name = "Headphones", quantity = 20, price = 199.50)
    )
    MaterialTheme {
        ProductListContent(
            products = sampleProducts,
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
