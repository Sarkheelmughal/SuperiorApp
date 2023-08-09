package learningapp.superior.org.Game

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_play_with_code.*
import kotlinx.android.synthetic.main.activity_play_with_code.checkTextBtn
import kotlinx.android.synthetic.main.activity_play_with_code.headerTV
import kotlinx.android.synthetic.main.activity_play_with_code.mcqFourRb
import kotlinx.android.synthetic.main.activity_play_with_code.mcqOneRb
import kotlinx.android.synthetic.main.activity_play_with_code.mcqThreeRb
import kotlinx.android.synthetic.main.activity_play_with_code.mcqTwoRb
import kotlinx.android.synthetic.main.activity_play_with_code.parentLL
import kotlinx.android.synthetic.main.activity_play_with_code.questionMainText
import kotlinx.android.synthetic.main.activity_play_with_code.screenNumber
import kotlinx.android.synthetic.main.activity_play_with_code.set_timer_opt_game
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.R

class PlayWithCode : AppCompatActivity() {
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    var stopper = 0
    var pp: Long = 0
    var hh: Long = 0
    var mm: Long = 0
    var mcqCounter = 0
    var fulllistofqus = ArrayList<McqsModel>()
    var listToDisplay = ArrayList<McqsModels>()
    var storeRandomofFull = ArrayList<McqsModel>(fulllistofqus.size)
    var codeRandom: Int? = null
    var getTotalmcqs: Int? = null
    var skipMcq: Int = 0
    var trueMcq: Int = 0
    var falseMcq: Int = 0
    var statusChecker = 0
    var counter = 1
//    var sumOfCode = 0
    var code: Int = 0
//   lateinit var valueListner : ValueEventListener() // = FirebaseDatabase.getInstance().reference
    var stopAutoboot = 0
    var autoboot: String = "code"

    var refForGameRoom: DatabaseReference =
        FirebaseDatabase.getInstance().reference
    var timer: CountDownTimer = object : CountDownTimer(21000, 1500) {
        override fun onTick(millisUntilFinished: Long) {
//            time--
//            set_timer_opt_game.text = time.toString()
        }

        override fun onFinish() {
//            checkTextBtn.performClick()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_with_code)
        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val name = SPforName.getString("name", "null")
        val subject = intent.getStringExtra("subject")
        hideView()
        codeRandom=(1000 until 9999).random()
       refForGameRoom =
            FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
                .child("GameRoomWithCode").child(subject!!)
        codeForGameTV.text=codeRandom.toString()

        codeCv.setOnClickListener {   val intent = Intent()
            intent.action = Intent.ACTION_SEND
            val shareSub =codeRandom.toString() + " paste this code to play with "+ name
                   intent.putExtra(Intent.EXTRA_TEXT, shareSub)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share the Code!")) }


      //  goForMatch(userDataRef,)

        var connected = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

        if (!connected) {
            Toast.makeText(this, "Make sure you are connected to an Internet!", Toast.LENGTH_LONG)
                .show()

            finish()
        }
        else {

            checkIfOtherJoin(subject)
            getAllMcqs()

            checkTextBtn.setOnClickListener {
                timer.cancel()


                Log.d("mcqCounterCheckText", mcqCounter.toString())
                Log.d("marksTestingskip", skipMcq.toString())
                Log.d("marksTestingtrue", trueMcq.toString())
                Log.d("marksTestingfalse", falseMcq.toString())

                if (mcqCounter < 4) {
                    checkMcqs()
                    pasteEmptyResults(subject)
                    textsetter()
                    pasteResults(subject)


                    Log.d("marksTestingif", "if")

                } else {
                    Log.d("marksTestingif", "else")

                    pasteEmptyResults(subject)
                    checkMcqs()


                    //  skipMcq=5 -(trueMcq+falseMcq)

                    pasteResults(subject)
                    val myMarks = intent.getStringExtra("myMarks")

//                Toast.makeText(this, "Test End", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, GameResult::class.java)
                    intent.putExtra("trueMcq", trueMcq)
                    intent.putExtra("falseMcq", falseMcq)
                    intent.putExtra("skipMcq", skipMcq)
                    intent.putExtra("subject", subject)
                    intent.putExtra("myMarks", myMarks)
                    intent.putExtra("sumOfCode", codeRandom)
                    intent.putExtra("autoboot", autoboot)
                    timer.cancel()
                    startActivity(intent)
                }
//            mcqCounter++

            }
//            waitingRoom(subject, code)
//
//            checkIfGameStartWithOther(subject)

            Handler().postDelayed({

                if (statusChecker == 0) {
                    Log.i("funplay", "checkOnlinePlayers 125")
                    if(stopAutoboot==0) {
                        statusChecker++
//                        checkOnlinePlayers(userDataRef, code)
                    }
                    Log.d("handler", "handler")
                    //Toast.makeText(this, "handler", Toast.LENGTH_SHORT).show()
                }
            }, 10000)

            mcqOneRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqFourRb.isChecked = false

                mcqTwoRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqTwoRb.setOnClickListener {
                mcqOneRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqFourRb.isChecked = false

                mcqOneRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqThreeRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqOneRb.isChecked = false
                mcqFourRb.isChecked = false
                mcqTwoRb.isClickable = false
                mcqOneRb.isClickable = false
                mcqFourRb.isClickable = false
            }
            mcqFourRb.setOnClickListener {
                mcqTwoRb.isChecked = false
                mcqThreeRb.isChecked = false
                mcqOneRb.isChecked = false
                mcqTwoRb.isClickable = false
                mcqThreeRb.isClickable = false
                mcqOneRb.isClickable = false
            }

        }
        cardView2.setOnClickListener { onBackPressed() }

        startGameBtn.setOnClickListener {
val code=codePasteET.text.toString()
            if(code.isNotEmpty() && codePasteET.text.toString().length == 4) {
                connectWithPlayer(code)
            }else Toast.makeText(this, "Please enter 4 digit code!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectWithPlayer(code: String) {
        codeRandom=code.toInt()
        informJoin()
        getMcqsToDisplay()

    }

    private fun informJoin() {
        val subject = intent.getStringExtra("subject")

        val refForGameRoomWaiting=  FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
            .child("GameRoomWithCode").child(subject.toString()).child(codeRandom.toString()).child("Join")
        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val name = SPforName.getString("name", "null")

        val userMap = HashMap<Any, Any>()
        userMap["name"] = name.toString()

        refForGameRoomWaiting.setValue(userMap)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()

     //   refForGameRoom.removeEventListener(valueListner)
    }

    private fun getAllMcqs() {
        Log.i("playwithcode", "getAllMcqs")
        val subject = intent.getStringExtra("subject")
        var ref = FirebaseDatabase.getInstance().getReference("contents")



        var refSubject = "null"
        when (subject) {
            "Bio" -> {
                refSubject = "biology"
                getTotalmcqs = 139

            }
            "Physics" -> {
                refSubject = "physics"
                getTotalmcqs = 143

            }
            "Chemistry" -> {
                refSubject = "chemistry"
                getTotalmcqs = 239

            }
        }

        if (refSubject != "null") {

            ref =
                FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                    .child("class9th").child("subjects").child(refSubject)
        } else {
            Toast.makeText(this, "Path is incomplete!", Toast.LENGTH_SHORT).show()
        }


            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        Log.d("totalChildrenEXits", "totalChildren")
                        pp = p0.childrenCount
                        Log.d("totalChildrenCountpp :", pp.toString())

                        for (h in p0.children) {
                            hh = h.childrenCount
                            Log.d("totalChildrenCounthh :", hh.toString())
                            for (s in h.children) {

                                Log.d("totalChildrenCounts", s.childrenCount.toString())
                                for (m in s.children) {
                                    mm = m.childrenCount
                                    Log.d("totalChildrenCountmm :", mm.toString())

                                    val opt =
                                        McqsModel(
                                            m.child("optionA").value.toString(),
                                            m.child("optionB").value.toString(),
                                            m.child("optionC").value.toString(),
                                            m.child("optionD").value.toString(),
                                            m.child("correctOption").value.toString(),
                                            m.child("question").value.toString()
                                        )
                                    fulllistofqus.add(opt)
                                    Log.d(
                                        "totalChildrenCountQue",
                                        m.child("question").value.toString()
                                    )
                                }
                            }
                        }
                        Log.d("totalChildrenCountsub", subject.toString())
                        Log.d("fulllistofqus", fulllistofqus.toString())
//                        if (statusChecker==0) {
                        pasteAllMcqsToBackend()
//                        }
//                        else{
//                            getMcqsToDisplay(refForGameRoom,timestamp,sumOfCode)

//                        }

                    }

                    else{
                        Log.d("playwithcode"," else getAllMcqs")
                    }

                }

            })



    }



    private fun checkIfOtherJoin(subject: String) {
       val refForGameRoomWaiting=  FirebaseDatabase.getInstance().reference.child("QuizGame").child("9th")
            .child("GameRoomWithCode").child(subject).child(codeRandom.toString()).child("Join")
        refForGameRoomWaiting.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("playwithcode", "joined")
                    getMcqsToDisplay()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        )
    }

    private fun pasteAllMcqsToBackend(
//        refForGameRoom: DatabaseReference,
//        timestamp: Long,
//        sumOfCode: Int
    ) {
//        random = (0 until 5).random()
        Log.i("playwithcode", "pasteAllMcqsToBackend")

        for (a in 1..5) {
            val random = (0 until (getTotalmcqs!!)).random()

            Log.d("random1", random.toString())
            Log.d("random2", fulllistofqus.size.toString())
            storeRandomofFull.add(fulllistofqus[random!!])

        }
        Log.d("storeRandomofFulllist1", storeRandomofFull.size.toString())
        Log.d("storeRandomofFulllist2", storeRandomofFull.toString())


        val userMap = HashMap<Any, Any>()
        userMap["McqsList"] = storeRandomofFull

      // valueListner=
           refForGameRoom.child(codeRandom.toString()).child("Mcqs").setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    //getMcqsToDisplay()

                } else {
//                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun getMcqsToDisplay(
//        refForGameRoom: DatabaseReference,
//        timestamp: Long,
//        sumOfCode: Int
    ) {
        Log.i("playwithcode", "getMcqsToDisplay")

        refForGameRoom.child(codeRandom.toString()).child("Mcqs")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {


                    if (snapshot.exists()) {

                        snapshot.child("McqsList").value.toString()
                        Log.d("listToDisplay5", snapshot.child("McqsList").value.toString())
                        Log.d(
                            "listToDisplay6",
                            snapshot.child("McqsList").child("0")
                                .child("optionA").value.toString()
                        )

                        for (n in snapshot.children) {

                            for (m in n.children) {
                                val mcqData = McqsModels(
                                    m.child("optionA").value.toString(),
                                    m.child("optionB").value.toString(),
                                    m.child("optionC").value.toString(),
                                    m.child("optionD").value.toString(),
                                    m.child("correctOption").value.toString(),
                                    m.child("question").value.toString()
                                )
                                listToDisplay.add(mcqData!!)
                                Log.d("listToDisplay", mcqData.toString())

                            }
                        }
//                   var mcqData:McqsModels?= snapshot.child("McqsList").getValue(McqsModels::class.java)
                        Log.d("listToDisplay1", listToDisplay.toString())

                        //removePlayerFromWaiting(a, userDataRef)
                        textsetter()

                        Log.d("listToDisplay2", listToDisplay.toString())
                    } else {
                        Log.i("elseOfFunData", "getMcqsToDisplay")
                    }
                }

            })
    }


    private fun textsetter() {
        Log.i("playwithcode", "textsetter")

        Log.d("mcqCounterTextSetter", mcqCounter.toString())
        unHideView()
        timerText()
        val subject = intent.getStringExtra("subject")

        mcqTwoRb.isClickable = true
        mcqThreeRb.isClickable = true
        mcqOneRb.isClickable = true
        mcqFourRb.isClickable = true


        if (mcqCounter < 5) {
            questionMainText.setText(listToDisplay[mcqCounter].question)
            mcqOneRb.setText(listToDisplay[mcqCounter].optionA)
            mcqTwoRb.setText(listToDisplay[mcqCounter].optionB)
            mcqThreeRb.setText(listToDisplay[mcqCounter].optionC)
            mcqFourRb.setText(listToDisplay[mcqCounter].optionD)

        }

    }

    fun hideView() {
        Log.i("playwithcode", "hideView")
        headerTV.visibility = View.GONE
        screenNumber.visibility = View.GONE
        parentLL.visibility = View.GONE
    }

    fun unHideView() {
        Log.i("playwithcode", "unHideView")
        codeCv.visibility = View.GONE
        codeTypeCv.visibility = View.GONE
        headerTV.visibility = View.VISIBLE
        screenNumber.visibility = View.VISIBLE
        parentLL.visibility = View.VISIBLE
        //  pleaseWaitTV.visibility = View.GONE
    }

    private fun uncheckRadioBtn() {
        Log.i("playwithcode", "uncheckRadioBtn")

        mcqOneRb.isChecked = false
        mcqTwoRb.isChecked = false
        mcqThreeRb.isChecked = false
        mcqFourRb.isChecked = false
    }
    private fun timerText() {
        set_timer_opt_game
        var time = 21
        timer = object : CountDownTimer(21000, 1500) {
            override fun onTick(millisUntilFinished: Long) {
                time--
                set_timer_opt_game.text = time.toString()
            }

            override fun onFinish() {
                checkTextBtn.performClick()
            }
        }
        timer.start()
    }
    private fun checkMcqs() {
        Log.i("funplay", "checkMcqs")

        Log.d("correctAns", listToDisplay[mcqCounter].correctOption)
        Log.d("checkMcqTest", "checkMcqTest")

        counter++
        Log.d("questionCounter", counter.toString())
        screenNumber.setText("Question $counter")


        if (mcqOneRb.isChecked == false && mcqTwoRb.isChecked == false && mcqThreeRb.isChecked == false && mcqFourRb.isChecked == false) {
            skipMcq++
        }

        if (mcqOneRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqOneRb.text) {
            trueMcq++
        } else if (mcqTwoRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqTwoRb.text) {
            trueMcq++
        } else if (mcqThreeRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqThreeRb.text) {
            trueMcq++
        } else if (mcqFourRb.isChecked == true && listToDisplay[mcqCounter].correctOption == mcqFourRb.text) {
            trueMcq++
        } else if (mcqOneRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqOneRb.text) {
            falseMcq++
        } else if (mcqTwoRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqTwoRb.text) {
            falseMcq++
        } else if (mcqThreeRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqThreeRb.text) {
            falseMcq++
        } else if (mcqFourRb.isChecked == true && listToDisplay[mcqCounter].correctOption != mcqFourRb.text) {
            falseMcq++
        }
        Log.d("checkMcqTrue", trueMcq.toString())
        Log.d("checkMcqFalse", falseMcq.toString())
        Log.d("checkMcqSkip", skipMcq.toString())
        Log.d("checkMcqQuestion", listToDisplay[mcqCounter].question)
        Log.d("checkMcqCorrect", listToDisplay[mcqCounter].correctOption)

        uncheckRadioBtn()
        mcqCounter++

    }
    private fun pasteEmptyResults(subject: String?) {
        Log.i("funplay", "pasteEmptyResults")

        // skipMcq=5 -(trueMcq+falseMcq)


        val userMap = HashMap<Any, Any>()
        userMap["true"] = trueMcq
        userMap["false"] = falseMcq
        userMap["skip"] = skipMcq

        refForGameRoom.child(codeRandom.toString()).child("Mcqs").child("Results").child(uid)
            .setValue(userMap)


    }
    private fun pasteResults(subject: String?) {
        Log.i("funplay", "pasteResults")

        // skipMcq=5 -(trueMcq+falseMcq)


        val userMap = HashMap<Any, Any>()
        userMap["true"] = trueMcq
        userMap["false"] = falseMcq
        userMap["skip"] = skipMcq

        refForGameRoom.child(codeRandom.toString()).child("Mcqs").child("Results").child(uid)
            .setValue(userMap)


    }

}