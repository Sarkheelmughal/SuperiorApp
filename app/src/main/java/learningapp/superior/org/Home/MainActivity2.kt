package learningapp.superior.org.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.buyNow
import kotlinx.android.synthetic.main.buy_now.*
import learningapp.superior.org.Adapter.CustomeAdapter
import learningapp.superior.org.Adapter.OnChapterClick
import learningapp.superior.org.Models.Model
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.VideoNew


class  MainActivity2 : AppCompatActivity(),
    OnChapterClick {


    lateinit var ref: DatabaseReference
    lateinit var recycleView: RecyclerView
    lateinit var chapterlist: ArrayList<Model>
    var chapter: String = "null"
    var classIs="class9th"

    // ham school code hony per sab ko logoff krwa skty ha / single student ko b logoff krwa skty ha
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        back_home.setOnClickListener {
            onBackPressed()

        }


        buyNow.setOnClickListener { startActivity(Intent(this, BuyNow::class.java)) }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        val subject = intent.getIntExtra("subject", 0)

        headerDetail()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val reff: DatabaseReference = FirebaseDatabase.getInstance("https://superior-school-4287a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("contents").child("numberOfVideos")
            .child("9th")


//        reff.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
////                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                for (h in p0.children) {
//                    if (p0!!.exists()) {
//
//                        when (subject) {
//                            1 -> {
//                                Nofvideos.setText(p0.child("Physics").value.toString() + " videos")
//                            }
//                            2 -> {
//                                Nofvideos.setText(p0.child("Chemistry").value.toString() + " videos")
//                            }
//                            3 -> {
//                                Nofvideos.setText(p0.child("Biology").value.toString() + " videos")
//                            }  4 -> {
//                                Nofvideos.setText(p0.child("Math").value.toString() + " videos")
//                            }
//                        }
////                    Log.d("outside For loop", "outside from for loop")
//
////                        Log.d("videos num", "videos num " + p0.child("Physics").value.toString())
//                    }
//                }
//            }
//
//        })

        val uid = FirebaseAuth.getInstance().currentUser!!.uid


        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumberSaved = SPforNumber.getString("number", "null")
        val sharedPreferencesSC: SharedPreferences =
            this.getSharedPreferences("schoolCode", Context.MODE_PRIVATE)
        //val editorSc: SharedPreferences.Editor = sharedPreferencesSC.edit()
        val getSchoolCode = sharedPreferencesSC.getString("schoolCode", "null")
    val SPforGrade = getSharedPreferences("gradeStorage", Context.MODE_PRIVATE)
    val getClassGrade = SPforGrade.getString("grade", "null")



    Log.d("MainActivity2","getClassGrade "+getClassGrade.toString())

        Log.d("MainActivity2","callAllVideos out $getClassGrade")

        callAllVideos(getClassGrade)
        //---------------------------------------------------------------------store that student is paid or not ..start01

        val savePAidOrNot = getSharedPreferences("save", Context.MODE_PRIVATE)
        val savePAidOrNotData = savePAidOrNot.getString("save", "null")
        val editor=  savePAidOrNot.edit()
        //---------------------------------------------------------------------store that student is paid or not ..end01
        //.child(SPforNumberSaved.toString())

//        val refOfCheckSchool = FirebaseDatabase.getInstance("https://superior-school-4287a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("SchoolCodes").child(getSchoolCode.toString())
//
//        refOfCheckSchool.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//            @SuppressLint("SuspiciousIndentation")
//            override fun onDataChange(p0: DataSnapshot) {
//
//                if (p0.exists()) {
//                    Log.d(
//                        "checkschoolTEXTP0",
//                        p0.value.toString()
//                    )
//
//                    var switch = ""
//
//                    for (aa in p0.children) {
//
//                        Log.d("keyfor2",  aa.key.toString())
//                        if (aa.child("switch").value.toString() != "null") {
//                            switch = aa.child("switch").value.toString()// == "on"
//                            Log.d("checkLoopswitch", switch)
//
//                        }
//
//
//
//
//                    }
//                    if (switch == "on"){
//                        val refOfPaid = FirebaseDatabase.getInstance("https://superior-school-4287a-default-rtdb.asia-southeast1.firebasedatabase.app")
//                            .getReference("LockAndUnLockChapter").child(getSchoolCode.toString())
//
//                        refOfPaid.addValueEventListener(object : ValueEventListener {
//                            override fun onCancelled(p0: DatabaseError) {
//                            }
//
//                            override fun onDataChange(p0: DataSnapshot) {
//                                if (p0.exists()) {
//                                    //for (h in p0.children) {
//
//                                    if (p0.child(SPforNumberSaved.toString())
//                                            .child(SPforNumberSaved.toString()).value.toString() == "on"
//                                    ) {
//                                        Log.d("paidOrNot", p0.child("paid").value.toString())
//                                        // if (p0.child("paid").value.toString() == "yes") {
//                                        buyCV.visibility = View.GONE
//
//                                        recyclerView.visibility = View.VISIBLE
//                                        editor.putString("save","paid")
//                                        editor.apply()
//                                        editor.commit()
//                                        callAllVideos(getClassGrade)
//
//
//                                    } else {
//                                        recyclerView.visibility = View.VISIBLE
//                                        editor.putString("save","nonpaid")
//                                        editor.apply()
//                                        editor.commit()
//                                        buyCV.visibility = View.VISIBLE
//                                        callTwoVideos(getClassGrade)
//                                        // notPaid.visibility = View.VISIBLE
//                                    }
//                                    // }
//
//                                }
//                                else {
//                                    recyclerView.visibility = View.VISIBLE
//                                    editor.putString("save","nonpaid")
//                                    editor.apply()
//                                    editor.commit()
//                                    buyCV.visibility = View.VISIBLE
//                                    callTwoVideos(getClassGrade)
//
//                                }
//                            }
//                        })
//
//
//                    }
//
//                    else
//                        recyclerView.visibility = View.VISIBLE
//                    editor.putString("save","nonpaid")
//                    editor.apply()
//                    editor.commit()
//                    buyCV.visibility = View.VISIBLE
//                    callTwoVideos(getClassGrade)
//                }
//                else
//                recyclerView.visibility = View.VISIBLE
//                editor.putString("save","nonpaid")
//                editor.apply()
//                editor.commit()
//                buyCV.visibility = View.VISIBLE
//                callTwoVideos(getClassGrade)
//            }
//
//        })

        //------------------



        //

    }



    fun headerDetail() {

        when (intent.getIntExtra("subject", 0)) {
            1 -> {
                chapter = "subjects-1"
            }
            2 -> {
                chapter = "chemistry"
                emptysubicon.setImageResource(R.drawable.chemistry_logo)
                subId.setText("Chemistry")

            }
            3 -> {
                chapter = "biology"
                emptysubicon.setImageResource(R.drawable.biology)
                subId.setText("Biology")
            }
        }

    }

    private fun callAllVideos(getClassGrade: String?) {
        recyclerView.visibility = View.VISIBLE
        Log.d("MainActivity2","callAllVideos in $getClassGrade")
        when (getClassGrade) {
            "0" -> {
                classIs="class9th"
            }
            "1" -> {
                classIs="class10th"
            }
            "2" -> {
                classIs="class11th"
            }
            "3" -> {
                classIs="class12th"
            }
        }

        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.setLayoutManager(GridLayoutManager(this, 2))
//        recyclerView

       // val ProgressBar = findViewById<com.github.ybq.android.spinkit.SpinKitView>(R.id.progress_bar_rv)



        recycleView = findViewById(R.id.recyclerView)
        chapterlist = ArrayList<Model>()
        ref = FirebaseDatabase.getInstance("https://superior-school-4287a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("contents").child("books").child(classIs)
            .child(chapter)//.child("subjects-1")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {
                    chapterlist.clear()

                    // notPaid.visibility = View.GONE
                    progress_bar_rv.visibility = View.INVISIBLE
                    for (h in p0.children) {
                        val chap = Model(
                            h.child("name").value.toString(),
                            h.child("description").value.toString(),
                            h.key!!,
                            h.child("icon").value.toString(),
                            h.child("id").value.toString()
                        )
//                        Log.d("For loop", h.child("name").toString())
                        chapterlist.add(chap!!)

                    }
//                    Log.d("outside For loop", "outside from for loop")


                    val adapter =
                        CustomeAdapter(
                            applicationContext,
                            this@MainActivity2,
                            chapterlist
                        )
                    recycleView.adapter = adapter


                }
            }

        })

    }

    private fun callTwoVideos(getClassGrade: String?) {
        when (getClassGrade) {
            "0" -> {
                classIs="class9th"
            }
            "1" -> {
                classIs="class10th"
            }
            "2" -> {
                classIs="class11th"
            }
            "3" -> {
                classIs="class12th"
            }
        }
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.setLayoutManager(GridLayoutManager(this, 2))
//        recyclerView

       // val ProgressBar = findViewById<com.github.ybq.android.spinkit.SpinKitView>(R.id.progress_bar_rv)



        recycleView = findViewById(R.id.recyclerView)
        chapterlist = ArrayList<Model>()

        //chapterlist.remove()

        ref = FirebaseDatabase.getInstance("https://superior-school-4287a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("contents").child("books").child(classIs)
            .child(chapter)//.child("subjects-1")


        ref.limitToFirst(2)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                    if (p0!!.exists()) {
                        chapterlist.clear()
                        //notPaid.visibility = View.GONE
                        progress_bar_rv.visibility = View.INVISIBLE
                        for (h in p0.children) {
                            val chap = Model(
                                h.child("name").value.toString(),
                                h.child("description").value.toString(),
                                h.key!!,
                                h.child("icon").value.toString(),
                                h.child("id").value.toString()
                            )
//                        Log.d("For loop", h.child("name").toString())
                            chapterlist.add(chap!!)
                        }
//                    Log.d("outside For loop", "outside from for loop")


                        val adapter =
                            CustomeAdapter(
                                applicationContext,
                                this@MainActivity2,
                                chapterlist
                            )
                        recycleView.adapter = adapter


                    }
                }

            })

    }

    override fun onItemClick(list: Model, position: Int) {


        val subject = intent.getIntExtra("subject", 0)

//        var intent = Intent(this, RecycleTopics::class.java)
        val intent = Intent(this, VideoNew::class.java)

        intent.putExtra("grade",classIs)
        intent.putExtra("object", list)
        intent.putExtra("subject", subject)
        startActivity(intent)

//        val bundle = Bundle()
//        val myMessage = "Stack Overflow is cool!"
//        bundle.putString("message", myMessage)
//        val fragInfo = Fragmentone()
//        fragInfo.setArguments(bundle)


//        Log.d("error not", "onItemclick log d")

    }

    override fun onBackPressed() {
        super.onBackPressed()

//        startActivity(Intent(this, Home::class.java))
    }



}