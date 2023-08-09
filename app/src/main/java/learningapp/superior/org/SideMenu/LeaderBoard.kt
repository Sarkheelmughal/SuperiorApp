package learningapp.superior.org.SideMenu

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_leader_board.*
import learningapp.superior.org.Adapter.LeaderBoardAdapter
import learningapp.superior.org.Models.LeaderBoardModel
import learningapp.superior.org.R


class LeaderBoard : AppCompatActivity() {
    private val UID: String = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var recyclerView: RecyclerView
    lateinit var namelist: ArrayList<LeaderBoardModel>

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    var myMarks="0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_leader_board)
        backFromLBCV.setOnClickListener {
            finish()
            onBackPressed() }

        checkInternet()
        getData()
    }

    private fun checkInternet() {
        var connected = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    
        if (!connected){
            Toast.makeText(this, "Make sure you are connected to an Internet!", Toast.LENGTH_LONG).show()}
    }

    fun getData() {
        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected", "def")

        var count: Int = 1
//        Log.d("value of child var", "value of child is:" + child)

        var subject = ""
        lateinit var ref: DatabaseReference

        if (gradeSelect.toString() == "entry") {
            subject = "entryTest"
            ref = FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
                .child(UID).child("books")
            Toast.makeText(this, "Ops! No Record found for Entry test.", Toast.LENGTH_LONG).show()
        } else {
            subject = "matriculation"

            ref = FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
                .child(UID).child("books")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                    if (p0.exists()) {

                        var sum = 0

                        for (a in p0.children) {
                            for (b in a.children) {
                                for (c in b.children) {
                                    for (d in c.children) {

                                        sum += d.child("watch").value.toString().toInt()
                                        Log.d("sumWatch", sum.toString())
                                    }
                                }
                            }
                        }
                        getMarks(sum, subject)
                    }
                }

            })

        }
    }

    private fun getMarks(sum: Int, subject: String) {
        val refForGameRoom: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("QuizGameMarks")
                .child("9th").child(uid)

        refForGameRoom.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {


                    Log.d("marksTotalFromFB", snapshot.child("marks").value.toString())
                    myMarks=snapshot.child("marks").value.toString()


                    val sumOfBoth=(myMarks.toInt()*10)+sum

                    getDataOfChaptersMCQS(sumOfBoth, sum , subject)
                   // pasteSum(sumOfBoth, subject)

                }else {
                   pasteSum(sum, subject, 0)

                }
            }

        })
    }

    private fun getDataOfChaptersMCQS(sumOfBoth: Int, sum:Int , subject: String) {
        val refForChapterMarks: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("UserLearningData").child(uid)


        var TrueValue=0

        refForChapterMarks.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    TrueValue=snapshot.child("truevalue").value.toString().toInt()

                    TrueValue *= 10

                    Log.d("TrueValueSum",TrueValue.toString())

                    getStreaksData(sumOfBoth, subject ,TrueValue, sum)
             //  pasteSum(sumOfBoth, subject ,TrueValue)

                }else {
                    pasteSum(sum, subject,0)

                }
            }

        })
    }

    private fun getStreaksData(sumOfBoth: Int, subject: String, TrueValue: Int, sum: Int) {
        var sumOfStreaks=0
      val  ref = FirebaseDatabase.getInstance().getReference("RecordsOFStreakTests")
          .child(UID)
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {

                    for (a in p0.children) {
                        Log.d("sumWatchStreak", a.child("trueValue").value.toString())
                        sumOfStreaks += a.child("trueValue").value.toString().toInt()
                    }


                     pasteSum(sumOfBoth + (sumOfStreaks * 10), subject ,TrueValue)
                }else {
                    pasteSum(sum, subject,0)

                }
            }

        })

    }

    private fun pasteSum(sum: Int, subject: String, TrueValue: Int) {

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val name = SPforName.getString("name", "null")

        val parentalUserDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference
                .child("LeaderBoard").child(subject).child(UID)


        val dataMap = HashMap<Any, Any>()
        dataMap["score"] = sum+TrueValue
        dataMap["name"] = name.toString()


        parentalUserDataRef.setValue(dataMap)

            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    getListOfUsers(subject)
                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {

                    //  FirebaseAuth.getInstance().signOut()
                }
            }
    }

    private fun getListOfUsers(subject: String) {

        recyclerView = findViewById(R.id.leaderBoardRV)

        namelist = ArrayList<LeaderBoardModel>()

        val recyclerViewLeader = findViewById(R.id.leaderBoardRV) as RecyclerView

         val manager = LinearLayoutManager(this@LeaderBoard)
        recyclerView.layoutManager = manager
        recyclerView.setHasFixedSize(true)

        val uid=FirebaseAuth.getInstance().currentUser!!.uid
        val adapter =
            LeaderBoardAdapter(
                applicationContext,
                namelist
            )

        val ref = FirebaseDatabase.getInstance().getReference("LeaderBoard")
            .child(subject)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {
                    leaderBoardPB.visibility=View.GONE
                    namelist.clear()

                    for (a in p0.children) {



                        Log.d("findingkey",a.key.toString())
                        val list = LeaderBoardModel(

                            a.child("name").value.toString(),
                            a.key.toString(),
                            a.child("score").value.toString().toInt()

                        )

                        namelist.add(list)
                        fun select(p: LeaderBoardModel): Int = p.score
                        namelist.sortByDescending { select(it) }



                        Log.d("listOfUsersNameScore",namelist.toString())
                        Log.d("listOfUsersName", a.child("name").value.toString())
                        Log.d("listOfUsersScore", a.child("score").value.toString())
                        recyclerViewLeader.apply {
                            recyclerViewLeader.adapter = adapter
                        }
                        recyclerView.apply {
                            recyclerView.adapter = adapter
                        }
                    }


//                val a=namelist[0].name
////                    a.split(" ")[0]
//                   a.substring(0, a.indexOf(" "))


                    firstWinnerTV.text="\uD83E\uDD47"+namelist[0].name.split(" ")[0]
                    secondWinnerTV.text="\uD83E\uDD48"+namelist[1].name.split(" ")[0]
                    thirdWinnerTV.text="\uD83E\uDD49"+namelist[2].name.split(" ")[0]

                    firstScore.text=namelist[0].score.toString()
                    secondScore.text=namelist[1].score.toString()
                    thirdScore.text=namelist[2].score.toString()


                    for (b in 0..namelist.size-1){
//                        fun select(p: LeaderBoardModel): Int = p.score
//                        namelist.sortByDescending { select(it) }
                        Log.d("uidisOFb: ",b.toString())
                        Log.d("uidisNameList: ",namelist.toString())

                        if(namelist[b].key == uid){
                            myRankTV.setText("My Rank: "+ (b+1).toString() + " ")
                            Log.d("uidis",namelist[b].key)
                            Log.d("uidisNumber",b.toString())
                            Log.d("uidisLength",(namelist.size).toString())
                        }
                    }

//                    fun select(p: LeaderBoardModel): Int = p.score
//
//                    namelist.sortByDescending { select(it) }

                    namelist.removeAt(2)
                   // namelist.drop(2)
                    namelist.removeAt(1)
                    namelist.removeAt(0)
                    Log.d("listOfUsersNameScoreRem",namelist.toString())

//                    recyclerView.adapter?.notifyDataSetChanged()
//                    adapter.notifyDataSetChanged()
                }
            }
        })
    }


}


//
//data class Product(val name: String, val price: Double /*USD*/)
//
//fun selector(p: Product): Double = p.price
//
//fun main(args : Array<String>){
//    val products = arrayOf(Product("iPhone 8 Plus 64G", 850.00),
//        Product("iPhone 8 Plus 256G", 1100.00),
//        Product("Apple iPod touch 16GB", 246.00),
//        Product("Apple iPod Nano 16GB", 234.75),
//        Product("iPad Pro 9.7-inch 32 GB", 474.98),
//        Product("iPad Pro 9.7-inch 128G", 574.99),
//        Product("Apple 42mm Smart Watch", 284.93))
//
//    products.sortByDescending({selector(it)})
//
//
//    products.forEach { println(it) }
//    /*
//        Product(name=iPhone 8 Plus 256G, price=1100.0)
//        Product(name=iPhone 8 Plus 64G, price=850.0)
//        Product(name=iPad Pro 9.7-inch 128G, price=574.99)
//        Product(name=iPad Pro 9.7-inch 32 GB, price=474.98)
//        Product(name=Apple 42mm Smart Watch, price=284.93)
//        Product(name=Apple iPod touch 16GB, price=246.0)
//        Product(name=Apple iPod Nano 16GB, price=234.75)
//     */
//}