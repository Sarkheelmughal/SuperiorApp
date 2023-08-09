package learningapp.superior.org.SideMenu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.getField
import kotlinx.android.synthetic.main.learning_analysis.*
import learningapp.superior.org.Home.Home
import learningapp.superior.org.R
import learningapp.superior.org.Utils.FireStoreUtils
import ro.alexmamo.firestore_document.FirestoreDocument
import java.util.ArrayList


class Learning_analysis : AppCompatActivity() {

    var trueper: Float = 0f
    var falski: Float = 0f
    var skiped: Float = 0f
var selectedChap=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.learning_analysis)

        AllBtn.setOnClickListener {
            AllBtn.setBackgroundResource(R.drawable.select_board_orange)
            AllBtn.setTextColor(Color.parseColor("#ffffff"))

            PhysicsBtn.setBackgroundResource(R.drawable.select_board_white)
            PhysicsBtn.setTextColor(Color.parseColor("#25375F"))

            ChemistryBtn.setBackgroundResource(R.drawable.select_board_white)
            ChemistryBtn.setTextColor(Color.parseColor("#25375F"))

            BioBtn.setBackgroundResource(R.drawable.select_board_white)
          BioBtn.setTextColor(Color.parseColor("#25375F"))
            getData()
        }
        PhysicsBtn.setOnClickListener {
            selectedChap="physics"
            progBar.visibility = View.VISIBLE
            PhysicsBtn.setBackgroundResource(R.drawable.select_board_orange)
            PhysicsBtn.setTextColor(Color.parseColor("#ffffff"))

            AllBtn.setBackgroundResource(R.drawable.select_board_white)
            AllBtn.setTextColor(Color.parseColor("#25375F"))

            ChemistryBtn.setBackgroundResource(R.drawable.select_board_white)
            ChemistryBtn.setTextColor(Color.parseColor("#25375F"))

            BioBtn.setBackgroundResource(R.drawable.select_board_white)
            BioBtn.setTextColor(Color.parseColor("#25375F"))

            getPhysicsData()
        }
        ChemistryBtn.setOnClickListener {
            progBar.visibility = View.VISIBLE
            selectedChap="chemistry"
            ChemistryBtn.setBackgroundResource(R.drawable.select_board_orange)
            ChemistryBtn.setTextColor(Color.parseColor("#ffffff"))

            PhysicsBtn.setBackgroundResource(R.drawable.select_board_white)
            PhysicsBtn.setTextColor(Color.parseColor("#25375F"))

            AllBtn.setBackgroundResource(R.drawable.select_board_white)
            AllBtn.setTextColor(Color.parseColor("#25375F"))

            BioBtn.setBackgroundResource(R.drawable.select_board_white)
            BioBtn.setTextColor(Color.parseColor("#25375F"))

            getChemistryData()
        }
        BioBtn.setOnClickListener {
            progBar.visibility = View.VISIBLE
            selectedChap="bio"
            BioBtn.setBackgroundResource(R.drawable.select_board_orange)
            BioBtn.setTextColor(Color.parseColor("#ffffff"))

            PhysicsBtn.setBackgroundResource(R.drawable.select_board_white)
            PhysicsBtn.setTextColor(Color.parseColor("#25375F"))

            ChemistryBtn.setBackgroundResource(R.drawable.select_board_white)
            ChemistryBtn.setTextColor(Color.parseColor("#25375F"))

            AllBtn.setBackgroundResource(R.drawable.select_board_white)
            AllBtn.setTextColor(Color.parseColor("#25375F"))

            getBioData()
        }

        getData()

        backbtn.setOnClickListener() {
            startActivity(Intent(this, Home::class.java))
        }
        val dialogBuilder = AlertDialog.Builder(this)
        val alert = dialogBuilder.create()

        clearDataCV.setOnClickListener() {
            //clearData()


            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("Clear Data!")
            //set message for alert dialog
            builder.setMessage("Are you sure to delete the data?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Delete") { dialogInterface, which ->
                //Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()

                clearData()
            }
            //performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                //Toast.makeText(applicationContext,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
            }
            //performing negative action
//            builder.setNegativeButton("No"){dialogInterface, which ->
//                Toast.makeText(applicationContext,"clicked No",Toast.LENGTH_LONG).show()
//            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    private fun getPhysicsData() {
        val ref=FireStoreUtils().fireStoreDatabase
            .collection("ProgressReport")
            .document(FireStoreUtils().UID).collection("physics").document("details")
// get real time data using firestore
        ref.addSnapshotListener(MetadataChanges.INCLUDE) { documentSnapshot, e ->
            if (documentSnapshot != null) {
                if (documentSnapshot.exists()) {
                    trueper= documentSnapshot.getField<Any>("trueOpt").toString().toFloat()
                    falski= documentSnapshot.getField<Any>("falseOpt").toString().toFloat()
                    skiped= documentSnapshot.getField<Any>("skipOpt").toString().toFloat()

                    val firestoreDocument = FirestoreDocument.getInstance();
                   // val siz= documentSnapshot.getResult()
                    val size= firestoreDocument.getSize(documentSnapshot)

                    Log.d("chechdocsize",size.toString())

                    val clear = "falseP"
                    plotData(clear)

                } else {
                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
                        .show()
                    trueper= 0f
                    falski= 0f
                    skiped= 0f

                    val clear = "true"
                    plotData(clear)
                }
            }
        }
        //------------------------------------------ below ok . upper testing
//        ref.get()
//            .addOnSuccessListener  { documentSnapshot ->
//                if (documentSnapshot.exists()) {
//                trueper= documentSnapshot.getField<Any>("trueOpt").toString().toFloat()
//                    falski= documentSnapshot.getField<Any>("falseOpt").toString().toFloat()
//                    skiped= documentSnapshot.getField<Any>("skipOpt").toString().toFloat()
//
//                    val clear = "falseP"
//                    plotData(clear)
//
//                } else {
//                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
//                        .show()
//                    trueper= 0f
//                    falski= 0f
//                    skiped= 0f
//
//                    val clear = "true"
//                    plotData(clear)
//                }
//            }

    }
    private fun getChemistryData() {
        val ref=FireStoreUtils().fireStoreDatabase
            .collection("ProgressReport")
            .document(FireStoreUtils().UID).collection("chemistry").document("details")

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    trueper= documentSnapshot.getField<Any>("trueOpt").toString().toFloat()
                    falski= documentSnapshot.getField<Any>("falseOpt").toString().toFloat()
                    skiped= documentSnapshot.getField<Any>("skipOpt").toString().toFloat()

                    val clear = "falseC"
                    plotData(clear)
                } else {
                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
                        .show()
                    trueper= 0f
                    falski= 0f
                    skiped= 0f

                    val clear = "true"
                    plotData(clear)

                }
            }
    }
    private fun getBioData() {
        val ref=FireStoreUtils().fireStoreDatabase
            .collection("ProgressReport")
            .document(FireStoreUtils().UID).collection("biology").document("details")

        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    trueper= documentSnapshot.getField<Any>("trueOpt").toString().toFloat()
                    falski= documentSnapshot.getField<Any>("falseOpt").toString().toFloat()
                    skiped= documentSnapshot.getField<Any>("skipOpt").toString().toFloat()

                    val clear = "falseB"
                    plotData(clear)
                } else {
                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
                        .show()
                    trueper= 0f
                    falski= 0f
                    skiped= 0f

                    val clear = "true"
                    plotData(clear)
                }
            }
    }
    private fun clearData() {
        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected", "def")

        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid

        val ref: DatabaseReference = if (gradeSelect.toString() == "entry") {
            FirebaseDatabase.getInstance().getReference("LearningAnalysisEntryTest").child(uid)

        } else {
            FirebaseDatabase.getInstance().getReference("UserLearningData").child(uid)
        }

        val userMap = HashMap<Any, Any>()
        userMap["falsevalue"] = "0"
        userMap["truevalue"] = "0"
        userMap["skipvalue"] = "0"
        userMap["uid"] = uid

        val clear = "true"

        when (selectedChap) {
            "physics" -> {
                val ref=FireStoreUtils().fireStoreDatabase
                    .collection("ProgressReport")
                    .document(FireStoreUtils().UID).collection("physics").document("details")
                ref.delete()
                    .addOnSuccessListener {
                        plotData(clear)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@Learning_analysis, "Connection Error!", Toast.LENGTH_SHORT).show()
                        Log.d("errorLA", e.toString())
                    }
            }
            "chemistry" -> {
                val ref=FireStoreUtils().fireStoreDatabase
                    .collection("ProgressReport")
                    .document(FireStoreUtils().UID).collection("chemistry").document("details")
                ref.delete()
                    .addOnSuccessListener {
                        plotData(clear)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@Learning_analysis, "Connection Error!", Toast.LENGTH_SHORT).show()
                        Log.d("errorLA", e.toString())
                    }
            }
            "bio" -> {
                val ref=FireStoreUtils().fireStoreDatabase
                    .collection("ProgressReport")
                    .document(FireStoreUtils().UID).collection("biology").document("details")
                ref.delete()
                    .addOnSuccessListener {
                        plotData(clear)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@Learning_analysis, "Connection Error!", Toast.LENGTH_SHORT).show()
                        Log.d("errorLA", e.toString())
                    }
            }
            else -> {
                ref.setValue(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            plotData(clear)
                            // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                            //  FirebaseAuth.getInstance().signOut()
                        }
                    }

            }
        }
    }

    fun getData() {
        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected", "def")

        var count: Int = 1
        val child: String = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.d("value of child var", "value of child is:" + child)

        val ref: DatabaseReference = if (gradeSelect.toString() == "entry") {
            FirebaseDatabase.getInstance().getReference("LearningAnalysisEntryTest").child(child)

        } else {
            FirebaseDatabase.getInstance().getReference("UserLearningData").child(child)
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    trueper = p0.child("truevalue").value.toString().toFloat()
                    falski = p0.child("falsevalue").value.toString().toFloat()
                    skiped = p0.child("skipvalue").value.toString().toFloat()

                    val clear = "false"
                    plotData(clear)

                } else
                {
                    Toast.makeText(this@Learning_analysis, "No data available!", Toast.LENGTH_SHORT)
                        .show()
                    trueper= 0f
                    falski= 0f
                    skiped= 0f

                    val clear = "true"
                    plotData(clear)
                }
            }

        })
    }

    fun plotData(clear: String) {
        progBar.visibility = View.GONE

        val pieChart = findViewById<PieChart>(R.id.piechartLearning1)
        val corectLearing = findViewById<TextView>(R.id.corectLearing)
        val incorrectLearning = findViewById<TextView>(R.id.incorrectLearning)
        val unansweredLearning = findViewById<TextView>(R.id.unansweredLearning)


        corectLearing.setText(trueper.toInt().toString() + " Correct")
        incorrectLearning.setText(falski.toInt().toString() + " Incorrect")
        unansweredLearning.setText(skiped.toInt().toString() + " Unanswered")


        val NoOfEmp1 = ArrayList<PieEntry>()


        NoOfEmp1.add(PieEntry(trueper))
        NoOfEmp1.add(PieEntry(falski))
        NoOfEmp1.add(PieEntry(skiped))

        val dataSet = PieDataSet(NoOfEmp1, "Number Of Employees")


        val data1 = PieData(dataSet)
        pieChart.data = data1


        val MY_COLORS = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0),
            Color.rgb(255, 156, 0)
        )
        val colors = ArrayList<Int>()

        for (b in MY_COLORS) colors.add(b)

        dataSet.colors = colors
        dataSet.setDrawValues(false)


        pieChart.animateXY(2000, 2000)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false

        pieChart.holeRadius = 65f


        pieChart.centerText =
            (trueper.toInt().toString() + "/" + (trueper + falski + skiped).toInt()
                .toString()).toString()

        pieChart.setCenterTextColor(Color.rgb(162, 162, 162))

        //-------------------------------piechart2----------------------------------------------------


        val pieChart1 = findViewById<PieChart>(R.id.piechartLearning2)

        val NoOfEmp2 = ArrayList<PieEntry>()

        val percentage = (trueper / (falski + skiped + trueper)) * 100

        NoOfEmp2.add(PieEntry(trueper))
        NoOfEmp2.add(PieEntry(falski + skiped))

        val dataSet1 = PieDataSet(NoOfEmp2, "Number Of Employees")


        val data2 = PieData(dataSet1)
        pieChart1.data = data2


        val MY_COLORS1 = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0)
        )
        val colors1 = ArrayList<Int>()

        for (a in MY_COLORS1) colors1.add(a)

        dataSet1.colors = colors1
        dataSet1.setDrawValues(false)


        pieChart1.animateXY(2000, 2000)
        pieChart1.legend.isEnabled = false
        pieChart1.description.isEnabled = false

        pieChart1.holeRadius = 65f


        pieChart1.centerText = (percentage.toInt().toString() + "%").toString()

        pieChart1.setCenterTextColor(Color.rgb(162, 162, 162))

        //--------------------------------------pie3-----------------------------------------------


        val pieChart3 = findViewById<PieChart>(R.id.piechartLearning3)

        val NoOfEmp3 = ArrayList<PieEntry>()
        var avg = 0f
        avg = when (clear) {
            "true" -> {
                0f

            }
            "falseP" -> {
                4f
            }
            "falseC" -> {
                5f
            }
            "falseB" -> {
                7f
            }
            else -> {

                8f
            }
        }
        NoOfEmp3.add(PieEntry(1f))


        val dataSet3 = PieDataSet(NoOfEmp3, "Number Of Employees")


        val data3 = PieData(dataSet3)
        pieChart3.data = data3


        val MY_COLORS3 = intArrayOf(
            Color.rgb(253, 114, 246)

        )
        val colors3 = ArrayList<Int>()

        for (f in MY_COLORS3) colors3.add(f)

        dataSet3.colors = colors3
        dataSet3.setDrawValues(false)


        pieChart3.animateXY(2000, 2000)
        pieChart3.legend.isEnabled = false
        pieChart3.description.isEnabled = false

        pieChart3.holeRadius = 65f


        pieChart3.centerText = (avg.toInt().toString() + " Second").toString()

        pieChart3.setCenterTextColor(Color.rgb(162, 162, 162))


    }

}


