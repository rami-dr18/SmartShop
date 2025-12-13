package com.example.smartshopv2.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartshopv2.data.local.Product
import com.example.smartshopv2.data.repository.ProductRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllProducts().collect { productList ->
                _products.value = productList
            }
        }
        repository.startListeningForRemoteChanges()
    }

    suspend fun getProductById(id: String): Product? {
        return repository.getProductById(id)
    }

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
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }
}