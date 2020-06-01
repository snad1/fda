package com.snad.fdaproductauthenticscanner.ui.product

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        var exd = ""
        var mm = ""
        var yy = ""


        val m = arrayOf("01","02","03","04","05","06","07","08","09","10","11","12")
        val y = Array(10) { "${2019 + (it + 1)}"  }

        sm.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, m)
        sy.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, y)

        sm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                mm = m[position]
//                Toast.makeText(this@ProductAdd, m[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        sy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                yy = y[position]
                Toast.makeText(this@ProductAdd, y[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }


        btnGenerate.setOnClickListener {
            pro.name = edtName.text.trim().toString()
            pro.licenseId = edtLicense.text.trim().toString()
            pro.expireDate = mm + "/" + yy

//            if (!pro.name!!.isEmpty() && pro.licenseId!!.isEmpty() && pro.expireDate!!.isEmpty()){
            if (!(edtName.text.trim().toString().isEmpty() && edtLicense.text.trim().toString().isEmpty()) && TextUtils.isEmpty(mm) && TextUtils.isEmpty(yy)){
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
            pro.expireDate = sm.selectedItem.toString() + "/" + sy.selectedItem.toString()
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
