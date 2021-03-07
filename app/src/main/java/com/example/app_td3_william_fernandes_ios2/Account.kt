package com.example.app_td3_william_fernandes_ios2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account (
    @PrimaryKey(autoGenerate = false) val name: String,
    @ColumnInfo(name = "amount") val amount: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "iban") val iban: String
)