package com.example.app_td3_william_fernandes_ios2

class BanqueResponse {
    var id : String = ""

    fun getAccount_id() : String {
        return id
    }

    var account_name : String = ""

    fun getAccount_Name() : String {
        return account_name
    }
    var amount : String = ""

    fun getAccount_amount() : String {
        return amount
    }

    var iban : String = ""

    fun getAccount_iban() : String {
        return iban
    }

    var currency : String = ""

    fun getAccount_currency() : String {
        return currency
    }
}