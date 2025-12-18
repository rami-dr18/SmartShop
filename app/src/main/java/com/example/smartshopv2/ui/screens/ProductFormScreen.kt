package com.example.smartshopv2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartshopv2.ui.viewmodel.ProductViewModel
import com.example.smartshopv2.data.local.Product

// 1. The Stateful Screen (Logic & Database) - Kept Logic Intact
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF6200EE))
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

// 2. The Stateless Content (Redesigned UI)
@OptIn(ExperimentalMaterial3Api::class)
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

    val primaryPurple = Color(0xFF6200EE)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC)), // Matching background color
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Section
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                imageVector = if (productToEdit != null) Icons.Default.Edit else Icons.Default.Add,
                contentDescription = null,
                tint = primaryPurple,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = if (productToEdit != null) "Edit Product" else "Add New Product",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name") },
                        leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity in Stock") },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price ($)") },
                        leadingIcon = { Text("$  ", modifier = Modifier.padding(start = 12.dp), fontWeight = FontWeight.Bold, color = primaryPurple) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancelClick,
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, primaryPurple)
                        ) {
                            Text("Cancel", color = primaryPurple)
                        }

                        Button(
                            onClick = {
                                onSaveClick(name, quantity.toInt(), price.toDouble())
                            },
                            enabled = isValid,
                            modifier = Modifier.weight(1f).height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryPurple,
                                disabledContainerColor = Color.LightGray
                            )
                        ) {
                            Text(if (productToEdit != null) "Update" else "Add")
                        }
                    }
                }
            }
        }
    }
}

// 3. Previews
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