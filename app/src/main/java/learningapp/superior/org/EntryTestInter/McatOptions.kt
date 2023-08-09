package learningapp.superior.org.EntryTestInter


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
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
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.sample_test.*
import kotlinx.android.synthetic.main.sample_test.btnAa
import kotlinx.android.synthetic.main.sample_test.btnBb
import kotlinx.android.synthetic.main.sample_test.btnCc
import kotlinx.android.synthetic.main.sample_test.btnDd
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.R
import learningapp.superior.org.Home.ResultActivity


class McatOptions : AppCompatActivity(), View.OnTouchListener {


    lateinit var button_change2: LinearLayout
    lateinit var button_change3: LinearLayout
    lateinit var button_change4: LinearLayout
    lateinit var button_change1: LinearLayout
    lateinit var ScreenNumber: TextView
    lateinit var optionlist: ArrayList<McqsModel>
    lateinit var ref: DatabaseReference
    var time = 0
    var sumTime = 0

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

    var getTotalMcqOfSubj: Long? = null
    var btnCounter = 1

    var dX = 0f
    var dY = 0f
    var lastAction = 0
    override fun onCreate(savedInstanceState: Bundle?) {
//        getWindow().setFlags(
//            WindowManager.LayoutParams.FLAG_SECURE,
//            WindowManager.LayoutParams.FLAG_SECURE
//        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_test)
//        val textTimer = findViewById(R.id.set_timer_opt) as TextView

        mcqsBackIV.setOnClickListener { onBackPressed() }

//        optbcakbtn.setOnClickListener(){
//            onBackPressed()
//        }
        moveLL.setOnTouchListener(this)
        optionlist = ArrayList<McqsModel>()

//        key=intent.getStringExtra("key")
//
//        GetTopicName=intent.getStringExtra("GettopicName")
//

        sampleTextBtn.setOnClickListener {
            if (btnCounter == 1) {
                btnCounter++
                animNotify()

            } else //if (btnCounter == 2)
            {
                moveLL.visibility = View.GONE
                sampleTextBtn.setText("Check")

                result()
            }

        }


        var sub = intent.getIntExtra("mcat", 0)

        if (sub == 1) {
            iqtest.setText("Physics")
            ref = FirebaseDatabase.getInstance().getReference("contents").child("MockTest")
                .child("Mdcat").child("physics")
        } else if (sub == 2) {
            iqtest.setText("Chemistry")
            ref = FirebaseDatabase.getInstance().getReference("contents").child("MockTest")
                .child("Mdcat").child("chemistry")

            // Toast.makeText(this,"Chemistry pressed",Toast.LENGTH_SHORT).show()
        } else if (sub == 3) {
            iqtest.setText("Biology")
            ref = FirebaseDatabase.getInstance().getReference("contents").child("MockTest")
                .child("Mdcat").child("biology")

            //Toast.makeText(this,"Chemistry pressed",Toast.LENGTH_SHORT).show()
        } else if (sub == 4) {
            iqtest.setText("English")
            ref = FirebaseDatabase.getInstance().getReference("contents").child("MockTest")
                .child("Mdcat").child("english")

            // Toast.makeText(this,"Chemistry pressed",Toast.LENGTH_SHORT).show()
        }
       else  if (sub == 5) {

            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val a = sharedPreferences.getString("id", "")
            val b = sharedPreferences.getString("chap", "")
            iqtest.text = b
            var subj = intent.getIntExtra("net", 0)
            // Log.d("netsubject",name_chap.toString())

            if (subj == 1) {


                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("chemistry").child(a.toString()).child("topics")
            }

            if (subj == 2) {


                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("physics").child(a.toString()).child("topics")
            }
            if (subj == 3) {
                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("english").child(a.toString()).child("topics")
            }
            if (subj == 4) {
                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("intelli").child(a.toString()).child("topics")
            }
            if (subj == 5) {


                ref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                    .child("maths").child(a.toString()).child("topics")
            }


        }


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {

                    getTotalMcqOfSubj = p0.childrenCount
                    Log.d("getTotalMcqOfSubj", getTotalMcqOfSubj.toString())

                    for (h in p0.children) {

                        val opt = McqsModel(
                            h.child("optionA").value.toString(),
                            h.child("optionB").value.toString(),
                            h.child("optionC").value.toString(),
                            h.child("optionD").value.toString(),
                            h.child("correctOption").value.toString(),
                            h.child("question").value.toString()

                        )


//
//                        Log.d("For loop", h.child("name").toString())
                        optionlist.add(opt!!)
                        if (a == -1) {
                            a++
                            val optio = optionlist.get(0).question


                            optionlist.get(0).question
                            if(optio.startsWith("https://"))
                            {
                                optionlist.get(0).optionA
                                Glide.with(applicationContext).load(optionlist.get(0).question)
                                    .into(question_mcq)
                            }
                            else
                            {
                                sampleQuestionText.setText(optionlist.get(0).question)
                            }

                            val option1 = optionlist.get(0).optionA
                            val option2 = optionlist.get(0).optionB
                            val option3 = optionlist.get(0).optionC
                            val option4 = optionlist.get(0).optionD

                            if(option1.startsWith("https://"))
                            {

                                optionlist.get(0).optionA
                                Glide.with(applicationContext).load(optionlist.get(0).optionA)
                                    .into(image_optiona)
                            }
                            else
                            {
                                optionlist.get(0).optionA
                                btnAa.text = optionlist.get(0).optionA
                            }
                            if(option2.startsWith("https://"))
                            {

                                optionlist.get(0).optionB
                                Glide.with(applicationContext).load(optionlist.get(0).optionB)
                                    .into(image_optionb)
                            }
                            else
                            {
                                optionlist.get(0).optionB
                                btnBb.text = optionlist.get(0).optionB
                            }
                            if(option3.startsWith("https://"))
                            {
                                optionlist.get(0).optionC
                                Glide.with(applicationContext).load(optionlist.get(0).optionC)
                                    .into(image_optionc)
                            }
                            else
                            {
                                optionlist.get(0).optionC
                                btnCc.text = optionlist.get(0).optionC
                            }
                            if(option4.startsWith("https://"))
                            {
                                optionlist.get(0).optionD
                                Glide.with(applicationContext).load(optionlist.get(0).optionD)
                                    .into(image_optiond)
                            }
                            else
                            {
                                optionlist.get(0).optionD
                                btnDd.text = optionlist.get(0).optionD
                            }



                            optionlist.get(0).correctOption


                        }
                    }



                }
            }

        })


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


//        var timer = findViewById(R.id.set_timer) as TextView
        var button_background: Int = 1
        var btnDdisable: Int = 1
        ScreenNumber = findViewById(R.id.ScreenNumber) as TextView

        button_change1 = findViewById<LinearLayout>(R.id.linearOne)
        button_change2 = findViewById<LinearLayout>(R.id.linearTwo)
        button_change3 = findViewById<LinearLayout>(R.id.linearThree)
        button_change4 = findViewById<LinearLayout>(R.id.linearFour)

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


    override fun onBackPressed() {
        super.onBackPressed()

    }

    private fun animNotify() {
        val notifyTV = findViewById<TextView>(R.id.notifyTV)
        val slide = Slide()
        slide.setSlideEdge(Gravity.START)


        sampleTextBtn.setText("Next Question")
//        TransitionManager.beginDelayedTransition(mainSV, slide)
//        moveLL.setVisibility(View.VISIBLE)
//        notifyTV.setText("True")

//         var op1=optionlist.get(a).optionA
//            if(op1.startsWith("https://"))
//            {
//                image_optiona.visibility=View.VISIBLE
//
//                btnAa.setText("")
//                optionlist.get(a).optionA
//                Glide.with(applicationContext).load(optionlist.get(a).optionA)
//                    .into(image_optiona)
//            }

        if (btnAa.isChecked == false && btnBb.isChecked == false && btnCc.isChecked == false && btnDd.isChecked == false) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("No Option selected")
        }

        if (btnAa.isChecked == true && optionlist.get(a).optionA != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Oops! That's not correct.")
        } else if (btnBb.isChecked == true && optionlist.get(a).optionB != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Oops! That's not correct.")
        } else if (btnCc.isChecked == true && optionlist.get(a).optionC != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Oops! That's not correct.")
        } else if (btnDd.isChecked == true && optionlist.get(a).optionD != optionlist.get(a).correctOption) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.red)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Oops! That's not correct.")
        }


        //

        if (btnAa.isChecked == true && optionlist.get(a).correctOption == optionlist.get(a).optionA) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        } else if (btnBb.isChecked == true && optionlist.get(a).correctOption == optionlist.get(a).optionB) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        } else if (btnCc.isChecked == true && optionlist.get(a).correctOption == optionlist.get(a).optionC) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        } else if (btnDd.isChecked == true && optionlist.get(a).correctOption == optionlist.get(a).optionD) {
            TransitionManager.beginDelayedTransition(mainSV, slide)
            moveLL.setBackgroundResource(R.color.green)
            moveLL.setVisibility(View.VISIBLE)
            notifyTV.setText("Great work!")
        }


        when {
            optionlist.get(a).optionA == optionlist.get(a).correctOption -> {
                tickOneIV.visibility = View.VISIBLE

            }
            optionlist.get(a).optionB == optionlist.get(a).correctOption -> {
                tickTwoIV.visibility = View.VISIBLE


            }
            optionlist.get(a).optionC == optionlist.get(a).correctOption -> {
                tickthreeIV.visibility = View.VISIBLE


            }
            optionlist.get(a).optionD == optionlist.get(a).correctOption -> {
                tickFourIV.visibility = View.VISIBLE

            }
        }
    }

    fun result() {
        imageGone()
        tickGone()
        sumTime = sumTime + time
//        Log.d("testing of sum","the sum of timer is :"+sumTime)
        btnCounter = 1

//    skipOpt=
        if (btnAa.isChecked == true && optionlist.get(a).optionA != optionlist.get(a).correctOption) {
            firstFalse++
        } else if (btnBb.isChecked == true && optionlist.get(a).optionB != optionlist.get(a).correctOption) {
            secondFalse++
        } else if (btnCc.isChecked == true && optionlist.get(a).optionC != optionlist.get(a).correctOption) {
            thirdFalse++
        } else if (btnDd.isChecked == true && optionlist.get(a).optionD != optionlist.get(a).correctOption) {
            fourthFalse++
        }

        if (btnAa.isChecked == true && optionlist.get(a).optionA == optionlist.get(a).correctOption) {
            firstTrue++
        } else if (btnBb.isChecked == true && optionlist.get(a).optionB == optionlist.get(a).correctOption) {
            secondTrue++
        } else if (btnCc.isChecked == true && optionlist.get(a).optionC == optionlist.get(a).correctOption) {
            thirdTrue++
        } else if (btnDd.isChecked == true && optionlist.get(a).optionD == optionlist.get(a).correctOption) {
            fourthTrue++
        }


        trueOpt = firstTrue + secondTrue + thirdTrue + fourthTrue

        falseOpt = firstFalse + secondFalse + thirdFalse + fourthFalse

        skipOpt = (getTotalMcqOfSubj?.toFloat() ?: 10f) - (falseOpt + trueOpt)


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

            ScreenNumber.setText("Question ${b.toString()} / ${getTotalMcqOfSubj.toString()}")
            var qu=optionlist.get(a).question
            if(qu.startsWith("https://")) {

                question_mcq.visibility=View.VISIBLE
                sampleQuestionText.setText("")
                optionlist.get(a).question
                Glide.with(applicationContext).load(optionlist.get(a).question)
                    .into(question_mcq)


            }
            else
            {
                optionlist.get(a).question
                sampleQuestionText.setText(optionlist.get(a).question)
            }




             var op1=optionlist.get(a).optionA
            if(op1.startsWith("https://"))
            {
                image_optiona.visibility=View.VISIBLE

                btnAa.setText("")
                optionlist.get(a).optionA
                Glide.with(applicationContext).load(optionlist.get(a).optionA)
                    .into(image_optiona)
            }
            else
            {
                optionlist.get(a).optionA
                btnAa.text = optionlist.get(a).optionA
            }


            val op2=optionlist.get(a).optionB
            if(op2.startsWith("https://"))
            {
                image_optionb.visibility=View.VISIBLE
                btnBb.setText("")
                optionlist.get(a).optionB
                Glide.with(applicationContext).load(optionlist.get(a).optionB)
                    .into(image_optionb)
            }
            else
            {
                optionlist.get(a).optionB
                btnBb.setText(optionlist.get(a).optionB)
            }

            val op3=optionlist.get(a).optionC
            if(op3.startsWith("https://"))
            {
                image_optionc.visibility=View.VISIBLE
                btnCc.setText("")
                optionlist.get(a).optionC
                Glide.with(applicationContext).load(optionlist.get(a).optionC)
                    .into(image_optionc)
            }
            else
            {
                optionlist.get(a).optionC
                btnCc.setText(optionlist.get(a).optionC)
            }
            val op4=optionlist.get(a).optionD
            if(op4.startsWith("https://"))
            {
                image_optiond.visibility=View.VISIBLE
                btnDd.setText("")
                optionlist.get(a).optionD
                Glide.with(applicationContext).load(optionlist.get(a).optionD)
                    .into(image_optiond)
            }
            else
            {
                optionlist.get(a).optionD
                btnDd.setText(optionlist.get(a).optionD)
            }



            btnAa.setChecked(false);
            btnBb.setChecked(false);
            btnCc.setChecked(false);
            btnDd.setChecked(false);

            btnBb.isClickable = true
            btnCc.isClickable = true
            btnDd.isClickable = true
            btnAa.isClickable = true


            stoptimer.cancel()
            time = 0
            stoptimer.start()
            //  Toast.makeText(this,"This is true",trueOpt,"This is false",falseOpt,Toast.LENGTH_LONG).show()

        } else {
            val intent = Intent(this, ResultActivity::class.java)
            screen10 = time
//            Log.d("value of a","value of a is :4 and value of time is :"+time)

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
            intent.putExtra("entryTest", "entryTest")


//            intent.putExtra("key",key)

//            intent.putExtra("GetTopicName",GetTopicName)
            startActivity(intent)

            stoptimer.cancel()
        }

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
                this@McatOptions,
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

    override fun onPause() {
        super.onPause()
        stoptimer.cancel()
    }

    private fun tickGone() {
        tickOneIV.visibility = View.GONE
        tickTwoIV.visibility = View.GONE
        tickthreeIV.visibility = View.GONE
        tickFourIV.visibility = View.GONE
    }
    private fun imageGone() {
       image_optiona.visibility = View.GONE
        image_optionb.visibility = View.GONE
        image_optionc.visibility = View.GONE
        image_optiond.visibility = View.GONE
        question_mcq.visibility=View.GONE
    }
}