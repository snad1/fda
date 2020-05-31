package com.snad.fdaproductauthenticscanner.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.snad.fdaproductauthenticscanner.db.dao.QrResultDao
import com.snad.fdaproductauthenticscanner.db.entities.QrResult
import androidx.room.Room
import com.snad.fdaproductauthenticscanner.db.dao.ComplainDao
import com.snad.fdaproductauthenticscanner.db.dao.ProductDao
import com.snad.fdaproductauthenticscanner.db.entities.Complain
import com.snad.fdaproductauthenticscanner.db.entities.Product


@Database(entities = [QrResult::class, Product::class, Complain::class], version = 1,exportSchema = false)
abstract class QrResultDataBase : RoomDatabase() {
    abstract fun getQrDao(): QrResultDao
    abstract fun getProDao(): ProductDao
    abstract fun getComDao(): ComplainDao

    companion object {
        private const val DB_NAME = "FDADb"
        private var qrResultDataBase: QrResultDataBase? = null
        fun getAppDatabase(context: Context): QrResultDataBase? {
            if (qrResultDataBase == null) {
                qrResultDataBase =
                    Room.databaseBuilder(context.applicationContext, QrResultDataBase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .build()
            }
            return qrResultDataBase
        }

        fun destroyInstance() {
            qrResultDataBase = null
        }
    }
}