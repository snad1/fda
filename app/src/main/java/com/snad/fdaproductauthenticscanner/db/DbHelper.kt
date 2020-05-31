package com.snad.fdaproductauthenticscanner.db

import android.graphics.Bitmap
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import com.snad.fdaproductauthenticscanner.db.entities.Product
import com.snad.fdaproductauthenticscanner.db.entities.QrResult
import java.util.*


class DbHelper(var qrResultDataBase: QrResultDataBase) : DbHelperI {

    override fun insertQRResult(result: String): Int {
        val time = Calendar.getInstance()
        val resultType = findResultType(result)
        val qrResult = QrResult(result = result, resultType = resultType, calendar = time, favourite = false)
        return qrResultDataBase.getQrDao().insertQrResult(qrResult).toInt()
    }

    override fun getQRResult(id: Int): QrResult {
        return qrResultDataBase.getQrDao().getQrResult(id)
    }

    override fun addToFavourite(id: Int): Int {
        return qrResultDataBase.getQrDao().addToFavourite(id)
    }

    override fun removeFromFavourite(id: Int): Int {
        return qrResultDataBase.getQrDao().removeFromFavourite(id)
    }

    override fun deleteQrResult(id: Int): Int {
        return qrResultDataBase.getQrDao().deleteQrResult(id)
    }

    override fun getAllQRScannedResult(): List<QrResult> {
        return qrResultDataBase.getQrDao().getAllScannedResult()
    }

    override fun getAllFavouriteQRScannedResult(): List<QrResult> {
        return qrResultDataBase.getQrDao().getAllFavouriteResult()
    }

    override fun deleteAllQRScannedResult() {
        qrResultDataBase.getQrDao().deleteAllScannedResult()
    }

    override fun deleteAllFavouriteQRScannedResult() {
        qrResultDataBase.getQrDao().deleteAllFavouriteResult()
    }

    override fun getAllProducts(): List<Product> {
//        return emptyList()
        return qrResultDataBase.getProDao().getAllProducts()
    }

    override fun deleteProduct(id: Int): Int {
        return qrResultDataBase.getProDao().deleteProduct(id)
    }

    override fun insertProduct(product: Product) {
        qrResultDataBase.getProDao().insertProduct(product)
    }

    override fun getProduct(id: Int): Product {
        return qrResultDataBase.getProDao().getProduct(id)
    }

    override fun updateProduct(product: Product): Int {
        return qrResultDataBase.getProDao().updateProduct(product.id!!, product.name!!)
    }

    override fun checkIfProductExist(result: ByteArray): Int {
        return qrResultDataBase.getProDao().checkIfProductExist(result)
    }

    override fun getAllComplains(): List<Complain> {
        return qrResultDataBase.getComDao().getAllComplains()
    }

    override fun insertComplain(detail: String) {
        qrResultDataBase.getComDao().insertComplain(Complain(detail= detail))
    }

    /*
    * This feature will add in future
    * */
    private fun findResultType(result: String): String {
        return "TEXT"
    }



}
