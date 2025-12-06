package com.example.smartshopv2.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshopv2.data.local.Product
import com.example.smartshopv2.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    // --- UI State: products list
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        // Observe local Room database
        viewModelScope.launch {
            repository.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }

        // Start Firestore listener
        repository.startListeningForRemoteChanges()
    }

    // --- Add product
    fun addProduct(name: String, quantity: Int, price: Double) {
        if (name.isBlank() || quantity < 0 || price <= 0.0) return

        val product = Product(
            name = name,
            quantity = quantity,
            price = price
        )

        viewModelScope.launch {
            repository.addProduct(product)
        }
    }

    // --- Update product
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    // --- Delete product
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}
