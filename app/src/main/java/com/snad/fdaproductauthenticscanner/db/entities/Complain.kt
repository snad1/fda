package com.snad.fdaproductauthenticscanner.db.entities

import androidx.room.*


@Entity
data class Complain(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "detail")
    val detail: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "pId")
    val pId: Int?,

    @ColumnInfo(name = "lat")
    val lat: Double,

    @ColumnInfo(name = "lng")
    val lng: Double,

    @ColumnInfo(name = "time")
    val time: String

)