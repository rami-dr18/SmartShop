package com.example.smartshopv2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshopv2.ui.viewmodel.ProductViewModel
import com.example.smartshopv2.data.local.Product

// 1. The Stateful Screen (Logic & Database)
@Composable
fun ProductFormScreen(
    viewModel: ProductViewModel = viewModel(),
    productId: String? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    var productToEdit by remember { mutableStateOf<Product?>(null) }
    
    LaunchedEffect(productId) {
        if (productId != null) {
            val product = viewModel.getProductById(productId)
            productToEdit = product
        }
    }

    if (productId != null && productToEdit == null) {
        // Loading state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        ProductFormContent(
            productToEdit = productToEdit,
            onSaveClick = { name, quantity, price ->
                if (productToEdit != null) {
                    val updatedProduct = productToEdit!!.copy(
                        name = name,
                        quantity = quantity,
                        price = price
                    )
                    viewModel.updateProduct(updatedProduct)
                } else {
                    viewModel.addProduct(name, quantity, price)
                }
                onSave()
            },
            onCancelClick = onCancel
        )
    }
}

// 2. The Stateless Content (UI Only - Safe to Preview)
@Composable
fun ProductFormContent(
    productToEdit: Product? = null,
    onSaveClick: (String, Int, Double) -> Unit,
    onCancelClick: () -> Unit
) {
    var name by remember(productToEdit) { mutableStateOf(productToEdit?.name ?: "") }
    var quantity by remember(productToEdit) { mutableStateOf(productToEdit?.quantity?.toString() ?: "") }
    var price by remember(productToEdit) { mutableStateOf(productToEdit?.price?.toString() ?: "") }

    val isValid = name.isNotBlank() &&
            quantity.toIntOrNull() != null && quantity.toInt() >= 0 &&
            price.toDoubleOrNull() != null && price.toDouble() > 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
            
            Button(
                onClick = {
                    onSaveClick(name, quantity.toInt(), price.toDouble())
                },
                enabled = isValid,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (productToEdit != null) "Update" else "Add")
            }
        }
    }
}

// 3. The Previews

@Preview(showBackground = true, name = "New Product Form")
@Composable
fun ProductFormNewPreview() {
    MaterialTheme {
        ProductFormContent(
            productToEdit = null,
            onSaveClick = { _, _, _ -> },
            onCancelClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Product Form")
@Composable
fun ProductFormEditPreview() {
    // Mock data for editing
    val sampleProduct = Product(
        id = "1",
        name = "Gaming Mouse",
        quantity = 5,
        price = 49.99
    )

    MaterialTheme {
        ProductFormContent(
            productToEdit = sampleProduct,
            onSaveClick = { _, _, _ -> },
            onCancelClick = {}
        )
    }
}
