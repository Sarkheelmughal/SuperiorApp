package learningapp.superior.org.CustomOptionsForNineth

import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.sample_test.*
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.R
import learningapp.superior.org.Home.ResultActivity

class CustomMcqsNine : AppCompatActivity() , View.OnTouchListener{
    var childCount: Long = 0
    var pp: Long = 0
    var hh: Long = 0
    var mm: Long = 0
    val fulllistofqus = ArrayList<McqsModel>()
    var random: Int? = null
    var getTotalmcqs: Int? = null
    var btnCounter = 1
    var sumTime = 0
    var time = 0
    var firstTrue = 0f
    var secondTrue = 0f
    var thirdTrue = 0f
    var fourthTrue = 0f
    lateinit var button_change2: LinearLayout
    lateinit var button_change3: LinearLayout
    lateinit var button_change4: LinearLayout
    lateinit var button_change1: LinearLayout
    var firstFalse = 0f
    var secondFalse = 0f
    var thirdFalse = 0f
    var fourthFalse = 0f

    var trueOpt: Float = 0f
    var falseOpt: Float = 0f
    var skipOpt: Float = 10f
    var a: Int = -1
    var screen1 = 0
    var screen2 = 0
    var screen3 = 0
    var screen4 = 0
    var screen5 = 0
    var screen6 = 0
    var screen7 = 0
    var screen8 = 0
    var screen9 = 0
    var screen10 = 0
    var nextmcq = 0

    var b: Int = 1
    var dY = 0f
    var lastAction = 0
    var stoptimer = object : CountDownTimer(0, 0) {
        override fun onFinish() {}
        override fun onTick(p0: Long) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_test)
        moveLL.setOnTouchListener(this)
        mcqsBackIV.setOnClickListener { onBackPressed() }
        val subject = intent.getStringExtra("subject")
        val showMcqs = intent.getIntExtra("showMcqs", 0)

        var connected = false
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connected =
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED

        if (!connected) {
            Toast.makeText(this, "Make sure you are connected to an Internet!", Toast.LENGTH_LONG).show()
            finish()
        }


        Log.d("totalSelectedMcqs", showMcqs.toString())
        when (subject) {
            "physics" -> {
                iqtest.setText("Physics")
                getTotalmcqs = 143
            }
            "biology" -> {
                iqtest.setText("Biology")
                getTotalmcqs = 139
            }
            "chemistry" -> {
                iqtest.setText("Chemistry")
                getTotalmcqs = 239
            }
        }
        stoptimer = object : CountDownTimer(1000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                set_timer_opt.setText(time.toString())
                time++
//                Log.d("timer log", "log for timer" + time)

            }

            override fun onFinish() {
                //  startActivity(Intent(this@Options, ResultActivity::class.java))
//                Log.d("timer log", "log for setText")
            }
        }.start()


        val ref = subject?.let {
            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child("class9th").child("subjects").child(it)
        }


        Log.d("totalChildren ", "touched")
        ref?.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
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

                            Log.d("totalChildrenCounts",s.childrenCount.toString())
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
                                Log.d("totalChildrenCountQue", m.child("question").value.toString())


                            }
                        }
                    }
                    Log.d("totalChildrenCountsub", subject.toString())
                    random = (0 until (getTotalmcqs!!)).random()
                    textSetter(random!!)

                }


            }

        })

        sampleTextBtn.setOnClickListener {
            if(btnCounter == 1){
                btnCounter++
                Log.d("ifcondit ","testingNET")

                random?.let { it1 -> animNotifyRandom(it1) }
            }else //if (btnCounter == 2)
            {
                moveLL.visibility = View.GONE
                sampleTextBtn.setText("Check")
//                    btnCounter--
                netNext()
            }
        }
        button_change1 = findViewById<LinearLayout>(R.id.linearOne)
        button_change2 = findViewById<LinearLayout>(R.id.linearTwo)
        button_change3 = findViewById<LinearLayout>(R.id.linearThree)
        button_change4 = findViewById<LinearLayout>(R.id.linearFour)

        button_change1.setOnClickListener {
            uncheckSelected(1)
        }
        button_change2.setOnClickListener {
            uncheckSelected(2)
        }

        button_change3.setOnClickListener {
            uncheckSelected(3)
        }
        button_change4.setOnClickListener {
            uncheckSelected(4)
        }


        btnAa.setOnClickListener {
            uncheckSelected(1)
        }
        btnBb.setOnClickListener {
            uncheckSelected(2)
        }

        btnCc.setOnClickListener {
            uncheckSelected(3)
        }
        btnDd.setOnClickListener {
            uncheckSelected(4)
        }
    }


    private fun tickGone() {
        tickOneIV.visibility = View.GONE
        tickTwoIV.visibility = View.GONE
        tickthreeIV.visibility = View.GONE
        tickFourIV.visibility = View.GONE
    }

    private fun animNotifyRandom(random: Int) {

        Log.d("animNotifyRandom","testingNET")
        val notifyTV = findViewById<TextView>(R.id.notifyTV)
        val slide = Slide()
        slide.setSlideEdge(Gravity.START)


        sampleTextBtn.setText("Next Question")
//        TransitionManager.beginDelayedTransition(mainSV, slide)
//        moveLL.setVisibility(View.VISIBLE)
//        notifyTV.setText("True")

        if (btnAa.isChecked == false && btnBb.isChecked == false && btnCc.isChecked == false && btnDd.isChecked == false) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("No Option selected")
        }

        if (btnAa.isChecked == true && btnAa.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setVisibility(View.VISIBLE)
            moveLL.setBackgroundResource(R.color.red)
            notifyTV.setText("Oops! That's not correct.")
        } else if (btnBb.isChecked == true && btnBb.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setVisibility(View.VISIBLE)
            moveLL.setBackgroundResource(R.color.red)
            notifyTV.setText("Oops! That's not correct.")
        } else if (btnCc.isChecked == true && btnCc.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setVisibility(View.VISIBLE)
            moveLL.setBackgroundResource(R.color.red)

            notifyTV.setText("Oops! That's not correct.")
        } else if (btnDd.isChecked == true && btnDd.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Oops! That's not correct.")
        }


        //

        if (btnAa.isChecked == true && btnAa.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setVisibility(View.VISIBLE)
            moveLL.setBackgroundResource(R.color.green)

            notifyTV.setText("Great work!")
        } else if (btnBb.isChecked == true && btnBb.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        } else if (btnCc.isChecked == true && btnCc.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        } else if (btnDd.isChecked == true && btnDd.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        }


        if (btnAa.text == fulllistofqus[random].correctOption) {
            tickOneIV.visibility = View.VISIBLE
        } else if (btnBb.text == fulllistofqus[random].correctOption) {
            tickTwoIV.visibility = View.VISIBLE

        } else if (btnCc.text == fulllistofqus[random].correctOption) {
            tickthreeIV.visibility = View.VISIBLE

        } else if (btnDd.text == fulllistofqus[random].correctOption) {
            tickFourIV.visibility = View.VISIBLE

        }

        calculateResult(random)


    }
    private fun calculateResult(random: Int) {


        sumTime = sumTime + time
//        Log.d("testing of sum","the sum of timer is :"+sumTime)

//    skipOpt=
        if (btnAa.isChecked == true && btnAa.text != fulllistofqus[random].correctOption) {
            firstFalse++
        } else if (btnBb.isChecked == true && btnBb.text != fulllistofqus[random].correctOption) {
            secondFalse++
        } else if (btnCc.isChecked == true && btnCc.text != fulllistofqus[random].correctOption) {
            thirdFalse++
        } else if (btnDd.isChecked == true && btnDd.text != fulllistofqus[random].correctOption) {
            fourthFalse++
        }

        if (btnAa.isChecked == true && btnAa.text == fulllistofqus[random].correctOption) {
            firstTrue++
        } else if (btnBb.isChecked == true && btnBb.text == fulllistofqus[random].correctOption) {
            secondTrue++
        } else if (btnCc.isChecked == true && btnCc.text == fulllistofqus[random].correctOption) {
            thirdTrue++
        } else if (btnDd.isChecked == true && btnDd.text == fulllistofqus[random].correctOption) {
            fourthTrue++
        }


        trueOpt = firstTrue + secondTrue + thirdTrue + fourthTrue

        falseOpt = firstFalse + secondFalse + thirdFalse + fourthFalse

        Log.d("trueFalse true",trueOpt.toString())
        Log.d("trueFalse false",falseOpt.toString())
        val showMcqs = intent.getIntExtra("showMcqs", 0)

        skipOpt = showMcqs.toFloat() - (falseOpt + trueOpt)



//        Log.d("value of a","value of a ouside the if is: "+a)
        a++


        if (a < fulllistofqus.size) {


            if (a == 1) {
                screen1 = time
            } else if (a == 2) {
                screen2 = time
            } else if (a == 3) {
                screen3 = time
            } else if (a == 4) {
                screen4 = time
            } else if (a == 5) {
                screen5 = time
            } else if (a == 6) {
                screen6 = time
            } else if (a == 7) {
                screen7 = time
            } else if (a == 8) {
                screen8 = time
            } else if (a == 9) {
                screen9 = time
            }


            stoptimer.cancel()
            time = 0
            stoptimer.start()

        }
    }
    private fun netNext() {
        tickGone()
        val showMcqs = intent.getIntExtra("showMcqs", 0)

        Log.d("netNext","testingNET")
        btnCounter = 1


//        moveLL.visibility=View.VISIBLE
        val container = ArrayList<Int>()
        Log.d("valueofrandom", random.toString())

        nextmcq++
//        if (random!! < fulllistofqus.size) {
        if (nextmcq < showMcqs) {
            b++
            ScreenNumber.setText("Question " + b.toString() + " / "+showMcqs)

            random = (0..this!!.getTotalmcqs!!).random()

            if (random!! in container) {
                random = (0..this!!.getTotalmcqs!!).random()
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            }
            else {
                container.add(random!!)
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            }

        } else {

            moveToResult()
        }
//


    }

    private fun uncheckSelected(mcqNum:Int){
        stoptimer.cancel()
        when (mcqNum) {
            1 -> {

                btnAa.isChecked = true
                btnBb.isChecked = false
                btnCc.isChecked = false
                btnDd.isChecked = false

                btnBb.isClickable = false
                btnCc.isClickable = false
                btnDd.isClickable = false

                linearOne.isClickable=false
                linearTwo.isClickable = false
                linearThree.isClickable = false
                linearFour.isClickable = false

            }
            2 -> {
                btnBb.isChecked = true
                btnAa.isChecked = false
                btnCc.isChecked = false
                btnDd.isChecked = false

                btnAa.isClickable = false
                btnCc.isClickable = false
                btnDd.isClickable = false

                linearOne.isClickable=false
                linearTwo.isClickable = false
                linearThree.isClickable = false
                linearFour.isClickable = false
            }
            3 -> {
                btnCc.isChecked = true

                btnAa.isChecked = false
                btnBb.isChecked = false
                btnDd.isChecked = false
                btnAa.isClickable = false
                btnBb.isClickable = false
                btnDd.isClickable = false

                linearOne.isClickable=false
                linearTwo.isClickable = false
                linearThree.isClickable = false
                linearFour.isClickable = false
            }
            4 -> {

                btnDd.isChecked = true

                btnAa.isChecked = false
                btnBb.isChecked = false
                btnCc.isChecked = false
                btnAa.isClickable = false
                btnBb.isClickable = false
                btnCc.isClickable = false

                linearOne.isClickable=false
                linearTwo.isClickable = false
                linearThree.isClickable = false
                linearFour.isClickable = false
            }
        }
    }
    private fun uncheckRadio() {

        btnAa.isChecked = false;
        btnBb.isChecked = false;
        btnCc.isChecked = false;
        btnDd.isChecked = false;

        btnBb.isClickable = true
        btnCc.isClickable = true
        btnDd.isClickable = true
        btnAa.isClickable = true

        linearOne.isClickable=true
        linearTwo.isClickable = true
        linearThree.isClickable = true
        linearFour.isClickable = true
    }
    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        screen10 = time
//            Log.d("value of a","value of a is :4 and value of time is :"+time)
        var mock = intent.getIntExtra("mock", 0)



        intent.putExtra("TrueValues", trueOpt)
        intent.putExtra("FalseValues", falseOpt)
        intent.putExtra("SkipValues", skipOpt)
        intent.putExtra("b", b)
        intent.putExtra("sumTime", sumTime)

        intent.putExtra("screen1", screen1)
        intent.putExtra("screen2", screen2)
        intent.putExtra("screen3", screen3)
        intent.putExtra("screen4", screen4)
        intent.putExtra("screen5", screen5)
        intent.putExtra("screen6", screen6)
        intent.putExtra("screen7", screen7)
        intent.putExtra("screen8", screen8)
        intent.putExtra("screen9", screen9)
        intent.putExtra("screen10", screen10)

        startActivity(intent)

        stoptimer.cancel()
    }
    override fun onTouch(view: View, event: MotionEvent): Boolean {


        val moveLL = findViewById<LinearLayout>(R.id.moveLL)

        when (event!!.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                moveLL.alpha = 0.5f
//                dX = view.x - event.rawX
                dY = view.y - event!!.rawY
                lastAction = MotionEvent.ACTION_DOWN

            }
            MotionEvent.ACTION_MOVE -> {
                moveLL.alpha = 0.5f
                view.y = event.rawY + dY
//                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) Toast.makeText(
                this@CustomMcqsNine,
                "Clicked!",
                Toast.LENGTH_SHORT
            ).show()
            else -> return false
        }
        if (event.action == MotionEvent.ACTION_UP) {
            moveLL.alpha = 1f
        }

        return true
    }

    private fun textSetter(random: Int) {

        sampleQuestionText.setText(fulllistofqus[random].question)
        btnAa.setText(fulllistofqus[random].optionA)
        btnBb.setText(fulllistofqus[random].optionB)
        btnCc.setText(fulllistofqus[random].optionC)
        btnDd.setText(fulllistofqus[random].optionD)
        fulllistofqus[random].correctOption
    }
}


