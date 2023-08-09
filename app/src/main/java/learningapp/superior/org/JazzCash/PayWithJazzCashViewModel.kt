package com.ameerhamza.tourly.pakistan.ViewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import com.google.gson.GsonBuilder
import learningapp.superior.org.JazzCash.JazzCash
import learningapp.superior.org.JazzCash.Util
import org.json.JSONObject
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class PayWithJazzCashViewModel : ViewModel() {
    private val TAG = "PayWithJazzViewModel";
    private var jazzCash: JazzCash? = null
    private val mutableLiveData = MutableLiveData<JazzCash.JazzCashReponse>()




    public fun payWithJazzCash(
        requestQueue: com.android.volley.RequestQueue,
        pay: JazzCash
    ): LiveData<JazzCash.JazzCashReponse> {

        val dateandtime = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
        val dexpiredate = SimpleDateFormat("yyyyMMddHHmmss").format(
            (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10))
        );

        val values: Array<String> = pay.amount.toString().split("\\.".toRegex()).toTypedArray()
        var price = values[0]
        price = price + "00"
        val tre = "T" + dateandtime;
        val pp_Amount = price;
        val pp_BillReference = "billRef";
        val pp_Description = "Description";
        val pp_SubMerchantID = ""
        val pp_Language = "EN";
        val pp_MerchantID ="MC19566"
        val pp_Password = "s2095s35ze"

        val pp_ReturnURL = "https://sandbox.jazzcash.com.pk/ApplicationAPI/API/2.0/Purchase/DoMWalletTransaction"
        val pp_ver = "2.0";
        val pp_TxnCurrency = "PKR";
        val pp_TxnDateTime = dateandtime.toString();
        val pp_TxnExpiryDateTime = dexpiredate.toString();
        val pp_TxnRefNo = tre.toString();
        val pp_TxnType = "MWALLET";
        val pp_MobileNumber = pay!!.phoneNumber;
        val pp_CNIC = pay.cinc
        val IntegeritySalt = "w4t98vesu2"
        val and = '&';
        val superdata =
            IntegeritySalt + and +
                    pp_Amount + and +
                    pp_BillReference + and +
                    pp_CNIC + and +
                    pp_Description + and +
                    pp_Language + and +
                    pp_MobileNumber + and +
                    pp_MerchantID + and +
                    pp_Password + and +
                    pp_ReturnURL + and +
                    pp_SubMerchantID + and +
                    pp_TxnCurrency + and +
                    pp_TxnDateTime + and +
                    pp_TxnExpiryDateTime + and +
                    pp_TxnRefNo + and +
                    pp_TxnType + and +
                    pp_ver

        ;

        val IntegeritySaltUTF8ByteArray =
            IntegeritySalt.toString().toByteArray(Charset.forName("UTF-8"))
        val superdataUTF8ByteArray = superdata.toByteArray(Charset.forName("UTF-8"))

        val resultString = Util.getHMACSha1(superdataUTF8ByteArray, IntegeritySaltUTF8ByteArray)

//        var url = firebaseRemoteConfig.getString("https://sandbox.jazzcash.com.pk/ApplicationAPI/API/2.0/Purchase/DoMWalletTransaction");

        var url="https://sandbox.jazzcash.com.pk/ApplicationAPI/API/2.0/Purchase/DoMWalletTransaction"
        val mapDate = mapOf(
            "pp_Version" to pp_ver,
            "pp_TxnType" to pp_TxnType,
            "pp_Language" to pp_Language,
            "pp_MerchantID" to pp_MerchantID,
            "pp_Password" to pp_Password,
            "pp_TxnRefNo" to tre,
            "pp_Amount" to pp_Amount,
            "pp_TxnCurrency" to pp_TxnCurrency,
            "pp_TxnDateTime" to dateandtime,
            "pp_BillReference" to pp_BillReference,
            "pp_Description" to pp_Description,
            "pp_TxnExpiryDateTime" to dexpiredate,
            "pp_ReturnURL" to pp_ReturnURL,
            "pp_SecureHash" to resultString,
            "pp_CNIC" to pp_CNIC,
            "pp_SubMerchantID" to pp_SubMerchantID,
            "pp_MobileNumber" to pp_MobileNumber
        )

        val jsonObject = JSONObject(mapDate)

        //Utils.Log(TAG,"payLoad "+jsonObject.toString())
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonObject,
            Response.Listener { response ->
                try {
                    Log.d(TAG, "respsnse" + response.toString())
                    val gson = GsonBuilder().create()
                    val jazzCashReponse = gson.fromJson<JazzCash.JazzCashReponse>(
                        response.toString(),
                        JazzCash.JazzCashReponse::class.java
                    )
                    mutableLiveData.value = jazzCashReponse
                } catch (e: Exception) {
                    e.printStackTrace()
                    mutableLiveData.value = null
                }

            },
            Response.ErrorListener { error -> // TODO: Handle error
                error.printStackTrace()
                Log.d(TAG, "error : " + error.message)
                mutableLiveData.value = null
            })



        jsonObjectRequest.setRetryPolicy(
            DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
        )
        requestQueue.add(jsonObjectRequest)
        return mutableLiveData
    }





}