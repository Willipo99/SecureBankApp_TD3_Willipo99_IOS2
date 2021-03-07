package com.example.app_td3_william_fernandes_ios2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Account::class], version = 1)
abstract class AppDB : RoomDatabase() {
    abstract fun accountDao() : AccountDao
}