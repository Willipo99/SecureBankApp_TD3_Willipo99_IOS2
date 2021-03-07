package com.example.app_td3_william_fernandes_ios2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import androidx.security.crypto.MasterKeys
import com.example.app_td3_william_fernandes_ios2.network.APIService
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.jvm.JvmStatic

class DisplayDataActivity : AppCompatActivity() {

    private lateinit var database : AppDB // 'lateinit' allows us to not initialize our variable here
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
        @JvmStatic
        external fun fetchURL() : String // function from the C/C++ file to fetch the API's hashed URL
        @JvmStatic
        external fun fetchKey() : String // function from the C/C++ file to fetch the secret key
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_data)

        var result : TextView = findViewById(R.id.resultApi)

        //************************ENCRYPTION******************************
        //val mainKey2 = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC) // master key
        val mainKey : String = fetchKey()
        val factory = SupportFactory(SQLiteDatabase.getBytes(mainKey.toCharArray()))
        database = Room.databaseBuilder(applicationContext, AppDB::class.java, "accountsEncryptedTest2.db").openHelperFactory(factory)
                .allowMainThreadQueries().build()
        //************************ENCRYPTION******************************

        //***************************TLS**********************************
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()

        val client = OkHttpClient.Builder()
                .connectionSpecs(Collections.singletonList(spec))
                .certificatePinner(
                        CertificatePinner.Builder()
                                .add(
                                        "mockapi.io",
                                        "sha256/a60d6f05f1a27d3dfe6f43a7822ddd752b7e0964"
                                )
                                .build()
                )
                .build()
        //***************************TLS**********************************

        //*************************API CALL*******************************
        val retrofit = Retrofit.Builder().baseUrl(String(Base64.decode(fetchURL(), Base64.DEFAULT))).client(client).
        addConverterFactory(GsonConverterFactory.create()).build()
        var apiService: APIService = retrofit.create(APIService::class.java)
        var accountCallback: Call<List<BanqueResponse>> = apiService.getAccount()

        accountCallback.enqueue(object : Callback<List<BanqueResponse>> {
            override fun onResponse(call: Call<List<BanqueResponse>>,
                                    response: Response<List<BanqueResponse>>) {
                val accounts = mutableListOf<BanqueResponse>()

                for (i in 0 until (response.body()?.size!!)) response.body()?.get(i) ?.let {
                    accounts.add(it) } // 'it' means the current element, context object
                // "get(i)" = get the element at index i // '?' = nullable // '!!' = non-null // '.?' = calls a method if non-null

                for (i in 0 until (accounts.size)) {
                    val currentAccount = Account(accounts[i].account_name, accounts[i].amount, accounts[i].currency, accounts[i].iban)

                    if (database.accountDao().exists(currentAccount.name))
                    { // we check here if it is a new account or not
                        database.accountDao().update(currentAccount)
                    } else {
                        database.accountDao().insert(currentAccount)
                    }
                }
                displayData()

                Toast.makeText(applicationContext,"Load is a success !", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<BanqueResponse>>,
                                   t: Throwable) {
                result.text = t.message
                Toast.makeText(applicationContext,"Load of data failed. Check your connection...", Toast.LENGTH_SHORT).show()
                displayData()
            }
        })
        //*************************API CALL*******************************

        val refreshButton : Button = findViewById<Button>(R.id.buttonRefresh)
        refreshButton.setOnClickListener {
            refreshData()
        }

        val logOutButton : Button = findViewById<Button>(R.id.butLogOut)
        logOutButton.setOnClickListener {
            finish()
        }
    }

    private fun displayData() {
        var data = ""
        var accountDao = database.accountDao() //link to DAO
        var accounts : List<Account> = accountDao.getAllAccounts() //link to db query

        for (a in accounts) {
            data += "Account Name: " + a.name + "\n" + "Amount : " + a.amount + a.currency + "\n" + "IBAN : " + a.iban + "\n\n"
        }

        findViewById<TextView>(R.id.resultApi).apply {
            text = "Here are your different accounts : " + "\n\n" + data
        }
    }

    private fun refreshData() {
        val retrofit = Retrofit.Builder().baseUrl(String(Base64.decode(fetchURL(), Base64.DEFAULT)))
            .addConverterFactory(GsonConverterFactory.create()).build()
        var apiService: APIService = retrofit.create(APIService::class.java)
        var accountCallback: Call<List<BanqueResponse>> = apiService.getAccount()

        accountCallback.enqueue(object : Callback<List<BanqueResponse>> {
            override fun onResponse(call: Call<List<BanqueResponse>>,
                                    response: Response<List<BanqueResponse>>) {
                val accounts = mutableListOf<BanqueResponse>()

                for (i in 0 until (response.body()?.size!!)) response.body()?.get(i) ?.let {
                    accounts.add(it) } // 'it' means the current element, context object
                // "get(i)" = get the element at index i // '?' = nullable // '!!' = non-null // '.?' = calls a method if non-null

                for (i in 0 until (accounts.size)) {
                    val currentAccount = Account(accounts[i].account_name, accounts[i].amount, accounts[i].currency, accounts[i].iban)

                    if (database.accountDao().exists(currentAccount.name))
                    { // we check here if it is a new account or not
                        database.accountDao().update(currentAccount)
                    } else {
                        database.accountDao().insert(currentAccount)
                    }
                }
                displayData()

                Toast.makeText(applicationContext,"Load is a success !", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<List<BanqueResponse>>,
                                   t: Throwable) {
                val errorMsg = "Refresh failed. Check your connection."
                Toast.makeText(applicationContext,t.message + "\n" + errorMsg , Toast.LENGTH_SHORT).show()
                displayData()
            }
        })
    }
}