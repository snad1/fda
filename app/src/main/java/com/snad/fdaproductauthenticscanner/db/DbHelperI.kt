package com.snad.fdaproductauthenticscanner.db

import android.graphics.Bitmap
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.db.entities.QrResult


interface DbHelperI {

    fun insertQRResult(result: String): Int

    fun getQRResult(id: Int): QrResult

    fun addToFavourite(id: Int): Int

    fun removeFromFavourite(id: Int): Int

    fun deleteQrResult(id: Int): Int

    fun getAllQRScannedResult(): List<QrResult>

    fun getAllFavouriteQRScannedResult(): List<QrResult>

    fun deleteAllQRScannedResult()

    fun deleteAllFavouriteQRScannedResult()

    fun getAllProducts(): List<Product>

    fun deleteProduct(id: Int): Int

    fun insertProduct(product: Product)

    fun getProduct(id: Int): Product

    fun updateProduct(product: Product): Int

    fun checkIfProductExist(result: ByteArray): Int

    fun getAllComplains(): List<Complain>

    fun insertComplain(detail: String)
}