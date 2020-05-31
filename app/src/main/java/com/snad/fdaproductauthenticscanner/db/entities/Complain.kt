package com.snad.fdaproductauthenticscanner.db.entities

import androidx.room.*


@Entity
data class Complain(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "detail")
    val detail: String

)