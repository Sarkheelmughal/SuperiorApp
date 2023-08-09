package learningapp.superior.org.Home


import android.content.Intent
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
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.sample_test.*
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.R


class Options : AppCompatActivity(), View.OnTouchListener {

    lateinit var button_change2: LinearLayout
    lateinit var button_change3: LinearLayout
    lateinit var button_change4: LinearLayout
    lateinit var button_change1: LinearLayout
    lateinit var ScreenNumber: TextView
    lateinit var optionlist: ArrayList<McqsModel>
    val fulllistofqus = ArrayList<McqsModel>()
    var time = 0
    var sumTime = 0


    var dX = 0f
    var dY = 0f
    var lastAction = 0
    var floatingLayout: LinearLayout? = null

    ////Time of every scree

    var stoptimer = object : CountDownTimer(0, 0) {
        override fun onFinish() {}
        override fun onTick(p0: Long) {}
    }
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

    ///
    var a: Int = -1
    var b: Int = 1

    var trueOpt: Float = 0f
    var falseOpt: Float = 0f
    var skipOpt: Float = 10f

    lateinit var key: String
    lateinit var GetTopicName: String

    var firstTrue = 0f
    var secondTrue = 0f
    var thirdTrue = 0f
    var fourthTrue = 0f

    var firstFalse = 0f
    var secondFalse = 0f
    var thirdFalse = 0f
    var fourthFalse = 0f

    var btnCounter = 1
    var random: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )

        var mock = intent.getIntExtra(
            "mock",
            0
        ) // 3,4,5 come from SliderContainer fragments , one form each.
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_test)
//        val textTimer = findViewById(R.id.set_timer_opt) as TextView

        Log.d("mockkitesting", mock.toString())
        moveLL.setOnTouchListener(this)

        mcqsBackIV.setOnClickListener { onBackPressed() }

        sampleTextBtn.setOnClickListener {
//
            if (mock == 3 || mock == 4 || mock == 5) {
                if (btnCounter == 1) {
                    btnCounter++
                    Log.d("ifcondit ", "testingNET")

                    random?.let { it1 -> animNotifyRandom(it1) }
                } else //if (btnCounter == 2)
                {
                    moveLL.visibility = View.GONE
                    sampleTextBtn.text = "Check"
//                    btnCounter--
                    netNext(mock)
                }
            } else {
                if (btnCounter == 1) {
                    btnCounter++
                    animNotify()

                } else //if (btnCounter == 2)
                {
                    moveLL.visibility = View.GONE
                    sampleTextBtn.text = "Check"

                    result(mock)
                }
            }
        }
//

        optionlist = ArrayList<McqsModel>()


        var getTotalmcqs: Long? = null


        lateinit var ref: DatabaseReference
        if (mock == 2) {

            iqtest.text = "ECAT"
            ref = FirebaseDatabase.getInstance().getReference("contents").child("MockTest")
                .child("Ecat").child("Chemistry")
//        ScreenNumber.setText("Question 1 / 27")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
//                        for(y in p0.children){
                        for (h in p0.children) {

                            val opt = McqsModel(
                                h.child("optionA").value.toString(),
                                h.child("optionB").value.toString(),
                                h.child("optionC").value.toString(),
                                h.child("optionD").value.toString(),
                                h.child("correctOption").value.toString(),
                                h.child("question").value.toString()
                            )
//                        Log.d("For loop", h.child("name").toString())
                            optionlist.add(opt)
                            if (a == -1) {
                                a++

                                optionlist.get(0).question
                                sampleQuestionText.text = optionlist.get(0).question

                                optionlist.get(0).optionA
                                btnAa.text = optionlist.get(0).optionA

                                optionlist.get(0).optionB
                                btnBb.text = optionlist.get(0).optionB

                                optionlist.get(0).optionC
                                btnCc.text = optionlist.get(0).optionC

                                optionlist.get(0).optionD
                                btnDd.text = optionlist.get(0).optionD

                                optionlist.get(0).correctOption


                            }
                        }
//                    Log.d("outside For loop", "outside from for loop")
//                        }

                    }
                }

            })

        } else if (mock == 3 || mock == 4 || mock == 5) {
            if (mock == 3) {
                iqtest.text = "Intelligence"
                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("intelli")
            } else if (mock == 4) {
                iqtest.text = "Chemistry"

                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("chemistry")

            } else if (mock == 5) {
                iqtest.text = "English"

                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("english")//.child("Synonyms and Antonyms").child("topic")

            }


            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        for (y in p0.children) {

                            for (x in y.children) {

                                getTotalmcqs = x.childrenCount

                                Log.d("getrandomnumbers1:", getTotalmcqs.toString())
                                Log.d("getrandomnumbers2:", random.toString())

                                if (getTotalmcqs.toString() == "0") {
                                    Log.d("emptychild", "emptychild")
                                } else {
//                                        random = (0 until getTotalmcqs.toString().toInt()).random()

                                    for (h in x.children) {

                                        var opt =
                                            McqsModel(
                                                h.child("optionA").value.toString(),
                                                h.child("optionB").value.toString(),
                                                h.child("optionC").value.toString(),
                                                h.child("optionD").value.toString(),
                                                h.child("correctOption").value.toString(),
                                                h.child("question").value.toString()
                                            )
                                        fulllistofqus.add(opt)

                                    }

                                    Log.d("lengtharray", fulllistofqus.size.toString())
                                    random =
                                        (0 until fulllistofqus.size.toString().toInt() - 1).random()
                                    Log.d("lengtharrayRandom", random.toString())

                                    textSetter(random!!)
                                }
                            }

                        }

                    }
                }

            })
        } else {
            ref = FirebaseDatabase.getInstance().getReference("contents").child("SampleTest")
//            .child("class9th").child("subjects").child("physics").child("chapter1").child("topic1")

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {

                    if (p0.exists()) {
                        for (h in p0.children) {

                            val opt = McqsModel(
                                h.child("optionA").value.toString(),
                                h.child("optionB").value.toString(),
                                h.child("optionC").value.toString(),
                                h.child("optionD").value.toString(),
                                h.child("correctOption").value.toString(),
                                h.child("question").value.toString()
                            )
//                        Log.d("For loop", h.child("name").toString())
                            optionlist.add(opt)
                            if (a == -1) {
                                a++

                                optionlist.get(0).question
                                sampleQuestionText.text = optionlist.get(0).question

                                optionlist.get(0).optionA
                                btnAa.text = optionlist.get(0).optionA

                                optionlist.get(0).optionB
                                btnBb.text = optionlist.get(0).optionB

                                optionlist.get(0).optionC
                                btnCc.text = optionlist.get(0).optionC

                                optionlist.get(0).optionD
                                btnDd.text = optionlist.get(0).optionD

                                optionlist.get(0).correctOption


                            }
                        }
//                    Log.d("outside For loop", "outside from for loop")


                    }
                }

            })

        }


        stoptimer = object : CountDownTimer(1000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                set_timer_opt.text = time.toString()
                time++
//                Log.d("timer log", "log for timer" + time)

            }

            override fun onFinish() {
                //  startActivity(Intent(this@Options, ResultActivity::class.java))
//                Log.d("timer log", "log for setText")
            }
        }.start()


//        var timer = findViewById(R.id.set_timer) as TextView
        var button_background: Int = 1
        var btnDdisable: Int = 1

//        button_change1 = findViewById(R.id.btnAa) as Button
//        button_change2 = findViewById(R.id.btnBb) as Button
//        button_change3 = findViewById(R.id.btnCc) as Button
//        button_change4 = findViewById(R.id.btnDd) as Button
        button_change1 = findViewById<LinearLayout>(R.id.linearOne)
        button_change2 = findViewById<LinearLayout>(R.id.linearTwo)
        button_change3 = findViewById<LinearLayout>(R.id.linearThree)
        button_change4 = findViewById<LinearLayout>(R.id.linearFour)

        ScreenNumber = findViewById<TextView>(R.id.ScreenNumber)



        button_change1.setOnClickListener {

            btnAa.isChecked=true

            btnBb.isChecked = false
            btnCc.isChecked = false
            btnDd.isChecked = false

            stoptimer.cancel()

        }

        button_change2.setOnClickListener {
            btnBb.isChecked=true

            btnAa.isChecked = false
            btnCc.isChecked = false
            btnDd.isChecked = false

            stoptimer.cancel()

        }

        button_change3.setOnClickListener {

            btnCc.isChecked=true
            btnAa.isChecked = false
            btnBb.isChecked = false
            btnDd.isChecked = false

            stoptimer.cancel()

        }

        button_change4.setOnClickListener {

            btnDd.isChecked=true

            btnAa.setChecked(false)
            btnCc.setChecked(false)
            btnBb.setChecked(false)
            stoptimer.cancel()

        }

    }

    private fun animNotifyRandom(random: Int) {

        Log.d("animNotifyRandom", "testingNET")
        val notifyTV = findViewById<TextView>(R.id.notifyTV)
        val slide = Slide()
        slide.slideEdge = Gravity.START


        sampleTextBtn.text = "Next Question"
//        TransitionManager.beginDelayedTransition(mainSV, slide)
//        moveLL.setVisibility(View.VISIBLE)
//        notifyTV.setText("True")

        if (btnAa.isChecked == false && btnBb.isChecked == false && btnCc.isChecked == false && btnDd.isChecked == false) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "No Option selected"
        }

        if (btnAa.isChecked == true && btnAa.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.visibility = View.VISIBLE
            moveLL.setBackgroundResource(R.color.red)
            notifyTV.text = "Oops! That's not correct."
        } else if (btnBb.isChecked == true && btnBb.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.visibility = View.VISIBLE
            moveLL.setBackgroundResource(R.color.red)
            notifyTV.text = "Oops! That's not correct."
        } else if (btnCc.isChecked == true && btnCc.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.visibility = View.VISIBLE
            moveLL.setBackgroundResource(R.color.red)

            notifyTV.text = "Oops! That's not correct."
        } else if (btnDd.isChecked == true && btnDd.text != fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        }


        //

        if (btnAa.isChecked == true && btnAa.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.visibility = View.VISIBLE
            moveLL.setBackgroundResource(R.color.green)

            notifyTV.text = "Great work!"
        } else if (btnBb.isChecked == true && btnBb.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnCc.isChecked == true && btnCc.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnDd.isChecked == true && btnDd.text == fulllistofqus[random].correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)

            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
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

    var nextmcq = 0
    private fun netNext(mock: Int) {
        tickGone()

        Log.d("netNext", "testingNET")
        btnCounter = 1


//        moveLL.visibility=View.VISIBLE
        var container = ArrayList<Int>()
        Log.d("valueofrandom", random.toString())

        nextmcq++
//        if (random!! < fulllistofqus.size) {
        if (nextmcq < 10) {
            b++
            ScreenNumber.text = "Question " + b.toString() + " / 10"

            random = (0..fulllistofqus.size - 1).random()

            if (random!! in container) {
                random = (0..fulllistofqus.size - 1).random()
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            } else {
                container.add(random!!)
                textSetter(random!!)
//                    calculateResult(random!!)
                uncheckRadio()

            }

        } else {
//                Toast.makeText(this, "end", Toast.LENGTH_SHORT).show()

            moveToResult(mock)
        }
//        } else {
//            Toast.makeText(this, "eend", Toast.LENGTH_SHORT).show()
//        }
        //-----------------------down done

//        nextmcq++
//        if (random!!<fulllistofqus.size ) {
//            if (nextmcq < 3) {
//                random=(0..fulllistofqus.size-1).random()
//
//                sampleQuestionText.setText(fulllistofqus[random!!].question)
//                btnAa.setText(fulllistofqus[random!!].optionA)
//                btnBb.setText(fulllistofqus[random!!].optionB)
//                btnCc.setText(fulllistofqus[random!!].optionC)
//                btnDd.setText(fulllistofqus[random!!].optionD)
//                fulllistofqus[random!!].correctOption
//            }
//            else{
//                Toast.makeText(this, "end", Toast.LENGTH_SHORT).show()
//            }
//        }else
//        {
//            Toast.makeText(this, "eend", Toast.LENGTH_SHORT).show()
//        }


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

        Log.d("trueFalse true", trueOpt.toString())
        Log.d("trueFalse false", falseOpt.toString())

        skipOpt = 10f - (falseOpt + trueOpt)


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

//            b++

//            if (mock == 2) {
//                ScreenNumber.setText("Question " + b.toString() + " / 47")
//            } else {
//                ScreenNumber.setText("Question " + b.toString() + " / 10")
//            }


            stoptimer.cancel()
            time = 0
            stoptimer.start()

        }
    }

    private fun textSetter(random: Int) {

        sampleQuestionText.text = fulllistofqus[random].question
        btnAa.text = fulllistofqus[random].optionA
        btnBb.text = fulllistofqus[random].optionB
        btnCc.text = fulllistofqus[random].optionC
        btnDd.text = fulllistofqus[random].optionD
        fulllistofqus[random].correctOption
    }


    fun result(mock: Int) {

        tickGone()
        btnCounter = 1

        sumTime = sumTime + time
//        Log.d("testing of sum","the sum of timer is :"+sumTime)
//        var mock = intent.getIntExtra("mock", 0)

//    skipOpt=
        if (btnAa.isChecked == true && btnAa.text != optionlist.get(a).correctOption) {
            firstFalse++
        } else if (btnBb.isChecked == true && btnBb.text != optionlist.get(a).correctOption) {
            secondFalse++
        } else if (btnCc.isChecked == true && btnCc.text != optionlist.get(a).correctOption) {
            thirdFalse++
        } else if (btnDd.isChecked == true && btnDd.text != optionlist.get(a).correctOption) {
            fourthFalse++
        }

        if (btnAa.isChecked == true && btnAa.text == optionlist.get(a).correctOption) {
            firstTrue++
        } else if (btnBb.isChecked == true && btnBb.text == optionlist.get(a).correctOption) {
            secondTrue++
        } else if (btnCc.isChecked == true && btnCc.text == optionlist.get(a).correctOption) {
            thirdTrue++
        } else if (btnDd.isChecked == true && btnDd.text == optionlist.get(a).correctOption) {
            fourthTrue++
        }


        trueOpt = firstTrue + secondTrue + thirdTrue + fourthTrue

        falseOpt = firstFalse + secondFalse + thirdFalse + fourthFalse

        Log.d("mockTested", mock.toString())

        if (mock == 2) {

            skipOpt = 47f - (falseOpt + trueOpt)
        } else {

            skipOpt = 10f - (falseOpt + trueOpt)
        }


//        Log.d("value of a","value of a ouside the if is: "+a)
        a++


        if (a < optionlist.size) {


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

            b++

            if (mock == 2) {
                ScreenNumber.text = "Question " + b.toString() + " / 47"
            } else {
                ScreenNumber.text = "Question " + b.toString() + " / 10"
            }


            stoptimer.cancel()
            time = 0
            stoptimer.start()

            optionlist.get(a).question
            sampleQuestionText.text = optionlist.get(a).question

            optionlist.get(a).optionA
            btnAa.text = optionlist.get(a).optionA

            optionlist.get(a).optionB
            btnBb.text = optionlist.get(a).optionB

            optionlist.get(a).optionC
            btnCc.text = optionlist.get(a).optionC

            optionlist.get(a).optionD
            btnDd.text = optionlist.get(a).optionD


            //  Toast.makeText(this,"This is true",trueOpt,"This is false",falseOpt,Toast.LENGTH_LONG).show()
            uncheckRadio()

        } else {

            moveToResult(mock)
        }

    }

    fun moveToResult(mock: Int) {
        val intent = Intent(this, ResultActivity::class.java)
        screen10 = time
//            Log.d("value of a","value of a is :4 and value of time is :"+time)
//        var mock = intent.getIntExtra("mock", 0)

        if (mock == 2 || mock == 3) {

            intent.putExtra("ecat", 2)
        } else {

            intent.putExtra("ecat", 1)
        }

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
        if (mock == 2 || mock == 3 || mock == 4 || mock == 5) {
            intent.putExtra("entryTest", "entryTest")

        }
        Log.d("mockktestt", mock.toString())
        startActivity(intent)

        stoptimer.cancel()
    }

    private fun uncheckRadio() {

        btnAa.isChecked = false
        btnBb.isChecked = false
        btnCc.isChecked = false
        btnDd.isChecked = false

        btnBb.isClickable = true
        btnCc.isClickable = true
        btnDd.isClickable = true
        btnAa.isClickable = true
    }

    private fun tickGone() {
        tickOneIV.visibility = View.GONE
        tickTwoIV.visibility = View.GONE
        tickthreeIV.visibility = View.GONE
        tickFourIV.visibility = View.GONE
    }

    private fun animNotify() {
        val notifyTV = findViewById<TextView>(R.id.notifyTV)
        val slide = Slide()
        slide.slideEdge = Gravity.START


        sampleTextBtn.text = "Next Question"
//        TransitionManager.beginDelayedTransition(mainSV, slide)
//        moveLL.setVisibility(View.VISIBLE)
//        notifyTV.setText("True")

        if (btnAa.isChecked == false && btnBb.isChecked == false && btnCc.isChecked == false && btnDd.isChecked == false) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "No Option selected"
        }

        if (btnAa.isChecked == true && btnAa.text != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        } else if (btnBb.isChecked == true && btnBb.text != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        } else if (btnCc.isChecked == true && btnCc.text != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        } else if (btnDd.isChecked == true && btnDd.text != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Oops! That's not correct."
        }


        //

        if (btnAa.isChecked == true && btnAa.text == optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnBb.isChecked == true && btnBb.text == optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnCc.isChecked == true && btnCc.text == optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        } else if (btnDd.isChecked == true && btnDd.text == optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.visibility = View.VISIBLE
            notifyTV.text = "Great work!"
        }


        when {
            btnAa.text == optionlist.get(a).correctOption -> {
                tickOneIV.visibility = View.VISIBLE
            }
            btnBb.text == optionlist.get(a).correctOption -> {
                tickTwoIV.visibility = View.VISIBLE

            }
            btnCc.text == optionlist.get(a).correctOption -> {
                tickthreeIV.visibility = View.VISIBLE

            }
            btnDd.text == optionlist.get(a).correctOption -> {
                tickFourIV.visibility = View.VISIBLE

            }
        }
    }

    override fun onPause() {
        super.onPause()
        stoptimer.cancel()
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {


        val moveLL = findViewById<LinearLayout>(R.id.moveLL)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                moveLL.alpha = 0.5f
//                dX = view.x - event.rawX
                dY = view.y - event.rawY
                lastAction = MotionEvent.ACTION_DOWN

            }
            MotionEvent.ACTION_MOVE -> {
                moveLL.alpha = 0.5f
                view.y = event.rawY + dY
//                view.x = event.rawX + dX
                lastAction = MotionEvent.ACTION_MOVE
            }
            MotionEvent.ACTION_UP -> if (lastAction == MotionEvent.ACTION_DOWN) Toast.makeText(
                this@Options,
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
}