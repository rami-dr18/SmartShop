package com.example.smartshopv2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: Int = 0,
    val price: Double = 0.0,
    val updatedAt: Long = System.currentTimeMillis()
)
