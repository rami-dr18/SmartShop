package com.example.smartshopv2.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshopv2.data.local.Product
import com.example.smartshopv2.ui.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = viewModel(),
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit,
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
                    onDeleteClick = { onDeleteClick(product) },
                    onEditClick = { onEditClick(product) }
                )
            }
        }
    }
}

@Composable
fun ProductRow(
    product: Product,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Product info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Quantity: ${product.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Price: $${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit product"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }

                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete product"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
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
