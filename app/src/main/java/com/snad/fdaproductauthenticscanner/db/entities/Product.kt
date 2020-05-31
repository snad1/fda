package com.snad.fdaproductauthenticscanner.db.entities

import android.graphics.Bitmap
import androidx.room.*
import com.snad.fdaproductauthenticscanner.db.converters.DateTimeConverters
import java.util.*


@Entity
@TypeConverters(DateTimeConverters::class)
data class Product(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String? = "",

    @ColumnInfo(name = "license_id")
    var licenseId: String? = "",

    @ColumnInfo(name = "expire_date")
    var expireDate: String? = "",

    @ColumnInfo(name = "qr_code")
    var qrCode: ByteArray? = null

){}