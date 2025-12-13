package com.example.smartshopv2.data.repository

import com.example.smartshopv2.data.local.Product
import com.example.smartshopv2.data.local.ProductDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProductRepository(
    private val dao: ProductDao,
    private val firestore: FirebaseFirestore
) {

    private val productsCollection = firestore.collection("products")

    fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts()
    }
    
    suspend fun getProductById(id: String): Product? {
        return dao.getProductById(id)
    }
    
    suspend fun addProduct(product: Product) {
        dao.insertProduct(product)
        pushToFirestore(product)
    }

    suspend fun updateProduct(product: Product) {
        dao.updateProduct(product)
        pushToFirestore(product)
    }
    suspend fun deleteProduct(product: Product) {
        dao.deleteProduct(product)
        deleteFromFirestore(product.id)
    }

    private suspend fun pushToFirestore(product: Product) {
        productsCollection
            .document(product.id)
            .set(product)
            .await()
    }

    private suspend fun deleteFromFirestore(id: String) {
        productsCollection
            .document(id)
            .delete()
            .await()
    }
    fun startListeningForRemoteChanges() {
        productsCollection.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener

            val remoteProducts = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Product::class.java)
            }
            remoteProducts.forEach { product ->
                kotlinx.coroutines.GlobalScope.launch {
                    dao.insertProduct(product)
                }
            }
        }
    }
}
