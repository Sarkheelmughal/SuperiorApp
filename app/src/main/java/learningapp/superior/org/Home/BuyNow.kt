package learningapp.superior.org.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_play_game.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.buy_now.*
import kotlinx.android.synthetic.main.incorect_code.*
import kotlinx.android.synthetic.main.incorect_code.view.*
import kotlinx.android.synthetic.main.limit_full.*
import kotlinx.android.synthetic.main.limit_full.view.*
import kotlinx.android.synthetic.main.streak_text.view.*
import learningapp.exambites.org.Utils.dayOfYear
import learningapp.exambites.org.Utils.year
import learningapp.superior.org.R

class BuyNow : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buy_now)

        val sharedPreferencesSC: SharedPreferences =
            this.getSharedPreferences("schoolCode", Context.MODE_PRIVATE)
        val editorSc: SharedPreferences.Editor = sharedPreferencesSC.edit()



        backFromBuy.setOnClickListener { onBackPressed() }

        buyNow.setOnClickListener { startActivity(Intent(this, SelectPaymentPlan::class.java)) }


        val mDialogView =
            LayoutInflater.from(this@BuyNow).inflate(R.layout.congratulations, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)



        codeCheckIV.setOnClickListener {
            progressBar_buyNow.visibility = View.VISIBLE
            val ref =
                FirebaseDatabase.getInstance().getReference("SchoolCodes")
                    .child(codeET.text.toString())

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                @SuppressLint("LogNotTimber")
                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        Log.d(
                            "checkschoolTEXTP0",
                            p0.value.toString()
                        )

                        var switch = ""
                        var total = ""
                        var used = ""

                        // for (a in p0.children) {
                        //   Log.d("keyfor1",  a.key.toString())

                        for (aa in p0.children) {

                            Log.d("keyfor2", aa.key.toString())
                            if (aa.child("switch").value.toString() != "null") {
                                Log.d("checkLoopswitch", switch)
                                switch = aa.child("switch").value.toString()// == "on"

                            }

                            if (aa.child("total").value.toString() != "null") {
                                Log.d("checkLooptotal", total)
                                total = aa.child("total").value.toString()// == "on"

                            }
                            if (aa.child("used").value.toString() != "null") {
                                Log.d("checkLoopused", used)
                                used = aa.child("used").value.toString()// == "on"


                            }


                        }
                        if (switch != "" && total != "" && used != "") {
                            checkLimits(switch, total, used, mBuilder)

                        }
                        // }


////////--------------------------------------------------------------------
//                        if (p0.child(codeET.text.toString()).value.toString() == "on") {
//                            Log.d("checkschool", "ON")
//
//                            editorSc.putString("schoolCode",codeET.text.toString())
//                            editorSc.apply()
//                            editorSc.commit()
//                            val mAlertDialog = mBuilder.show()
//                            mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
//                                R.drawable.feedbck_alertdialog_rounnd
//                            )
//
//                            progressBar_buyNow.visibility = View.GONE
//                            timer = object : CountDownTimer(3000, 1500) {
//                                override fun onTick(millisUntilFinished: Long) {
//                                }
//
//                                override fun onFinish() {
//                                    move(codeET.text.toString())
//                                }
//                            }
//                            timer.start()
//
//                        }
//
//                        else {
//                            Log.d("checkschool", "OFF")
//                            //toastFun()
//                        }

//                       }
                    } else
                        toastSwitch()
                    Log.d("elseOfRef", codeET.text.toString())
                }

            })

        }
    }
    private fun move(code: String) {
        timer.cancel()
        // pasteData(code)
        startActivity(Intent(this, Home::class.java))
    }
    private fun pasteData(code: String) {
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumberSaved = SPforNumber.getString("number", "null")
        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("LockAndUnLockChapter")
        val userMap = HashMap<Any, Any>()

        userMap[SPforNumberSaved.toString()] = "on"


        refOfPaid.child(code).child(SPforNumberSaved.toString()).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    pasteCodeInUserProfile()

                } else {
                }
            }

    }
    val currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid
    private fun pasteCodeInUserProfile() {
        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("NewUsers")
            .child(currentUserDataId)
            .child("schoolCode")
        val userMap = HashMap<Any, Any>()

        userMap["schoolCode"] = codeET.text.toString()

        refOfPaid.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                } else {
                }
            }
    }
    private fun toastFun() {
        Toast.makeText(this, "Invalid Code!", Toast.LENGTH_SHORT).show()
        progressBar_buyNow.visibility = View.GONE

    }
    private fun checkLimits(
        switch: String,
        total: String,
        used: String,
        mBuilder: AlertDialog.Builder
    ) {
        val sharedPreferencesSC: SharedPreferences =
            this.getSharedPreferences("schoolCode", Context.MODE_PRIVATE)
        val editorSc: SharedPreferences.Editor = sharedPreferencesSC.edit()

        val sharedPreferencesSCDate: SharedPreferences =
            this.getSharedPreferences("schoolCodeDate", Context.MODE_PRIVATE)
        val editorScDate: SharedPreferences.Editor = sharedPreferencesSCDate.edit()


        val ref =
            FirebaseDatabase.getInstance().getReference("SchoolCodes").child(codeET.text.toString())

        Log.d("checkLimitsswitch", switch)
        Log.d("checkLimitstotal", total)
        Log.d("checkLimitsused", used)
        if (total.toInt() == used.toInt()) {
            toastLimit()
        } else if (total.toInt() > used.toInt() && switch == "on") {
            Log.d("UsedLimitis:", used)
            val userMap = HashMap<Any, Any>()
            userMap["used"] = used.toInt() + 1
            ref.child("usedLimit").setValue(userMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        Log.d("checkschool", "ON")

                        editorSc.putString("schoolCode", codeET.text.toString())
                        editorSc.apply()

                        editorScDate.putInt("codeDate", dayOfYear)
                        editorScDate.putInt("codeYear", year)
                        editorScDate.apply()


                        val mAlertDialog = mBuilder.show()
                        mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
                            R.drawable.feedbck_alertdialog_rounnd
                        )

                        pasteData(codeET.text.toString())
                        progressBar_buyNow.visibility = View.GONE

                        timer = object : CountDownTimer(3000, 1500) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                move(codeET.text.toString())
                            }
                        }
                        timer.start()


                    } else {
                        toastTryAgain()
                    }
                }

        } else if (switch != "on") {
            toastSwitch()
        }
    }
    private fun toastSwitch() {
        progressBar_buyNow.visibility = View.GONE
        Log.d("toastBuys", "toastSwitch")
        //Toast.makeText(this, "Code is not valid!", Toast.LENGTH_SHORT).show()
        // timer.cancel()
        val mDialogView =
            LayoutInflater.from(this@BuyNow).inflate(R.layout.incorect_code, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )
        mDialogView.closeBtnRed.setOnClickListener {
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            mAlertDialog.dismiss()
        }

    }
    private fun toastTryAgain() {
        // timer.cancel()
        Log.d("toastBuys", "toastTryAgain")

        //  Toast.makeText(this, "There is an error. Try Again!", Toast.LENGTH_SHORT).show()
        progressBar_buyNow.visibility = View.GONE

        val mDialogView =
            LayoutInflater.from(this@BuyNow).inflate(R.layout.incorect_code, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )

        //this.closeBtnRed.setOnClickListener { mAlertDialog.cancel() }

        mDialogView.closeBtnRed.setOnClickListener {


//            val mAlertDialog = mBuilder.show()
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            mAlertDialog.dismiss()

        }
    }
    private fun toastLimit() {
        Log.d("toastBuys", "toastLimit")

        //  Toast.makeText(this, "Ops, Code limit full !", Toast.LENGTH_SHORT).show()
        progressBar_buyNow.visibility = View.GONE
//        timer.cancel()
        val mDialogView =
            LayoutInflater.from(this@BuyNow).inflate(R.layout.limit_full, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )

        // this.closeBtn.setOnClickListener { mAlertDialog.cancel() }

        mDialogView.closeBtn.setOnClickListener {


//            val mAlertDialog = mBuilder.show()
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            mAlertDialog.dismiss()

        }
    }
}