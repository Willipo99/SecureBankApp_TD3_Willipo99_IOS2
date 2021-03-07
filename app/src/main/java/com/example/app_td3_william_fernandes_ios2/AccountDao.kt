package com.example.app_td3_william_fernandes_ios2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AccountDao {
    @Query("select * from account")
    fun getAllAccounts() : List<Account>

    @Insert
    fun insert(account: Account)

    @Update
    fun update(account: Account)

    @Query("select exists(select * from account where name = :name)")
    fun exists(name: String): Boolean
}