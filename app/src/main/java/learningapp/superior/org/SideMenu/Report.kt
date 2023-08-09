package learningapp.superior.org.SideMenu

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report.*
import kotlinx.android.synthetic.main.buy_now.*
import learningapp.superior.org.R
import java.util.*
import kotlin.collections.HashMap

class Report : AppCompatActivity() {
    val calendar=  Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        back_reportCV.setOnClickListener { onBackPressed() }

        val Grade = resources.getStringArray(R.array.Report)

        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val MandY= "$month:$year"
        val currentTimestamp = System.currentTimeMillis()
        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("FeedBack").child("Slider").child(MandY)
            .child(uid).child(currentTimestamp.toString())


        val userMap = HashMap<Any, Any>()
        val mDialogView =
            LayoutInflater.from(this@Report).inflate(R.layout.alert_send_report, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        //val currentTimestamp = System.currentTimeMillis()
        sendReport.setOnClickListener {
            var connected = false
            val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            connected =
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

            if (!connected) {
                Toast.makeText(
                    this,
                    "Make sure you are connected to an Internet!",
                    Toast.LENGTH_LONG
                ).show()
            } else if (reportDetailET.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please briefly explain the issue!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
//                userMap["subject"] = reportSubjectSpinner.selectedItem
               // userMap["subject"] = reportSubjectSpinner.selectedItem
                userMap["feedback"] = reportDetailET.text.toString()


                refOfPaid.child(currentTimestamp.toString()).setValue(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val mAlertDialog = mBuilder.show()
                            mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
                                R.drawable.feedbck_alertdialog_rounnd
                            )

                            val timer = object : CountDownTimer(3000, 1500) {
                                override fun onTick(millisUntilFinished: Long) {
                                }

                                override fun onFinish() {
                                    finish()
                                }
                            }
                            timer.start()


                        } else {
                        }
                    }

            }
        }
        // access the spinner
//        val spinner = findViewById<Spinner>(R.id.reportSubjectSpinner)
//        if (spinner != null) {
//            val adapter = ArrayAdapter(
//                this,
//                android.R.layout.simple_spinner_dropdown_item, Grade
//            )
//            spinner.adapter = adapter
//
//            spinner.onItemSelectedListener = object :
//                AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View, position: Int, id: Long
//                ) {
////                    Toast.makeText(this@Profile,
////                        getString(R.string.selected_item) + " " +
////                                "" + Grade[position], Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    // write code to perform some action
//                }
//            }
//        }

    }
}