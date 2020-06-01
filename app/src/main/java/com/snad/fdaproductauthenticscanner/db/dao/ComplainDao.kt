package com.snad.fdaproductauthenticscanner.db.dao

import androidx.room.*
import com.snad.fdaproductauthenticscanner.db.entities.Complain


@Dao
interface ComplainDao {

    @Query("SELECT * FROM Complain ORDER BY id DESC")
    fun getAllComplains(): List<Complain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComplain(complain: Complain)


}