package com.snad.fdaproductauthenticscanner.db.dao

import androidx.room.*
import com.snad.fdaproductauthenticscanner.db.entities.Product


@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

    @Query("DELETE FROM Product WHERE id = :id")
    fun deleteProduct(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Query("SELECT * FROM Product WHERE id = :id")
    fun getProduct(id: Int): Product

    @Query("UPDATE Product SET name = :name  WHERE id = :id")
    fun updateProduct(id: Int, name: String): Int

    @Query("SELECT * FROM Product WHERE qr_code = :result ")
    fun checkIfProductExist(result: ByteArray): Int


}