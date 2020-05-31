package com.snad.fdaproductauthenticscanner.ui.product

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.snad.fdaproductauthenticscanner.R
import com.snad.fdaproductauthenticscanner.db.DbHelper
import com.snad.fdaproductauthenticscanner.db.database.QrResultDataBase
import com.snad.fdaproductauthenticscanner.db.entities.Product
import kotlinx.android.synthetic.main.activity_product.*
import java.io.ByteArrayOutputStream


class ProductAdd : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(this)!!)
        val pro = Product()
        var jsonString = ""
        var bitMap: Bitmap? = null

        btnGenerate.setOnClickListener {
            pro.name = edtName.text.trim().toString()
            pro.licenseId = edtLicense.text.trim().toString()
            pro.expireDate = edtExDate.text.trim().toString()

//            if (!pro.name!!.isEmpty() && pro.licenseId!!.isEmpty() && pro.expireDate!!.isEmpty()){
            if (!edtName.text.trim().toString().isEmpty()){
                jsonString = Gson().toJson(pro)
                val bitMatrix = MultiFormatWriter().encode(jsonString, BarcodeFormat.QR_CODE, 500, 500)
                bitMap = BarcodeEncoder().createBitmap(bitMatrix)
                imgQrCode.setImageBitmap(bitMap)
            }
            else
                Toast.makeText(this, "Filled add space", Toast.LENGTH_LONG).show()
        }

        btnSave.setOnClickListener {
            pro.name = edtName.text.trim().toString()
            pro.licenseId = edtLicense.text.trim().toString()
            pro.expireDate = edtExDate.text.trim().toString()
            if(jsonString.equals(Gson().toJson(pro))){
                pro.qrCode = getBitmapAsByteArray(bitMap!!)
                dbHelperI.insertProduct(pro)
                finish()
            }
            else
                Toast.makeText(this, "Re-generate Code", Toast.LENGTH_LONG).show()
        }

    }

    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }
}
