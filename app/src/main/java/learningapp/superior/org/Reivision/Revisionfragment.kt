package learningapp.superior.org.Reivision

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.mcqs_option.*
import kotlinx.android.synthetic.main.mcqs_option.view.*
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.QuizVideoModel
import ru.noties.jlatexmath.JLatexMathDrawable


class Revisionfragment : Fragment() {
    var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var button_change2: RadioButton
    lateinit var button_change3: RadioButton
    lateinit var button_change4: RadioButton
    lateinit var button_change1: RadioButton
    lateinit var ScreenNumber: TextView
    lateinit var optionlist: ArrayList<McqsModel>
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
//    var screen5 = 0

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

    lateinit var ref: DatabaseReference
//    lateinit var reff: DatabaseReference
    private val model: QuizVideoModel by activityViewModels()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.mcqs_option, container, false)
        view.layout_mcq.visibility=View.INVISIBLE
        view.cover_views.visibility=View.VISIBLE
        view.optbtn.setOnClickListener {
            result()
        }
        optionlist = ArrayList<McqsModel>()
        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid
      val  reff = FirebaseDatabase.getInstance().getReference("RevisionTopic").child(uid).limitToLast(1)

       // var stoper = 1

        reff.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {
                    var chapkey = ""
                    var mcqssubject = ""
                    var modelchapter = ""
                    var grade = ""

                    for (h in p0.children) {
//                        if (stoper == 1) {
//                            stoper++
                        grade = h.child("grade").value.toString()
                            chapkey = h.child("chapkey").value.toString()
                            mcqssubject = h.child("mcqssubject").value.toString()
                            modelchapter = h.child("modelchapter").value.toString()

                            if (mcqssubject == "subjects-1") {
                                mcqssubject = "physics"
                            } else {
                            }
                            ref =
                                FirebaseDatabase.getInstance().getReference("contents")
                                    .child("topic_questions")
                                    .child(grade).child("subjects").child(mcqssubject)
                                    .child(modelchapter)
                                    .child(chapkey)

                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
//                                    TODO("not implemented")
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    if (p0!!.exists()) {
//                    var chapkey = ""
//                    var mcqssubject = ""
//                    var modelchapter = ""

                                        for (h in p0.children) {
//                        chapkey = h.child("chapkey").value.toString()
//                        mcqssubject = h.child("mcqssubject").value.toString()
//                        modelchapter = h.child("modelchapter").value.toString()
//                        var reff =
//                            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
//                                .child("class9th").child("subjects").child(mcqssubject).child(modelchapter)
//                                .child(chapkey)


                                            val opt =
                                                McqsModel(
                                                    h.child("optionA").value.toString(),
                                                    h.child("optionB").value.toString(),
                                                    h.child("optionC").value.toString(),
                                                    h.child("optionD").value.toString(),
                                                    h.child("correctOption").value.toString(),
                                                    h.child("question").value.toString()
                                                )
//                        Log.d("For loop", h.child("name").toString())
                                            optionlist.add(opt!!)
                                            if (a == -1) {
                                                a++

                                                optionlist.get(0).question
                                                questionText.setText(optionlist.get(0).question)
//                                                val drawable:JLatexMathDrawable = JLatexMathDrawable.builder("${optionlist.get(0).question}")
//                                                    .textSize(50F)
//                                                    .padding(1)
////                                  .background(0xFFffffff)
//                                                    .align(JLatexMathDrawable.ALIGN_RIGHT)
//                                                    .build();
//                                                questionText.setLatexDrawable(drawable)

                                                optionlist.get(0).optionA
                                                btnA.setText(optionlist.get(0).optionA)

                                                optionlist.get(0).optionB
                                                btnB.setText(optionlist.get(0).optionB)

                                                optionlist.get(0).optionC
                                                btnC.setText(optionlist.get(0).optionC)

                                                optionlist.get(0).optionD
                                                btnD.setText(optionlist.get(0).optionD)

                                                optionlist.get(0).correctOption


                                            }
                                        }
//                    Log.d("outside For loop", "outside from for loop")


                                    }
                                }

                            })
                      //  }//if

                    }
//                    Log.d("outside For loop", "outside from for loop")


                }
            }

        })

        //-----------------------------------------------------------------------------------------------------------------
//       ref= FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
//            .child("class9th").child("subjects").child("physics").child("chapter-1")
//            .child("topic1")
        //error above


//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//
//                if (p0!!.exists()) {
////                    var chapkey = ""
////                    var mcqssubject = ""
////                    var modelchapter = ""
//
//                    for(h in p0.children){
////                        chapkey = h.child("chapkey").value.toString()
////                        mcqssubject = h.child("mcqssubject").value.toString()
////                        modelchapter = h.child("modelchapter").value.toString()
////                        var reff =
////                            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
////                                .child("class9th").child("subjects").child(mcqssubject).child(modelchapter)
////                                .child(chapkey)
//
//
//                        val opt = McqsModel(
//                            h.child("optionA").value.toString(),
//                            h.child("optionB").value.toString(),
//                            h.child("optionC").value.toString(),
//                            h.child("optionD").value.toString(),
//                            h.child("correctOption").value.toString(),
//                            h.child("question").value.toString()
//                        )
////                        Log.d("For loop", h.child("name").toString())
//                        optionlist.add(opt!!)
//                        if (a == -1) {
//                            a++
//
//                            optionlist.get(0).question
//                            questionText.setText(optionlist.get(0).question)
//
//                            optionlist.get(0).optionA
//                            btnA.setText(optionlist.get(0).optionA)
//
//                            optionlist.get(0).optionB
//                            btnB.setText(optionlist.get(0).optionB)
//
//                            optionlist.get(0).optionC
//                            btnC.setText(optionlist.get(0).optionC)
//
//                            optionlist.get(0).optionD
//                            btnD.setText(optionlist.get(0).optionD)
//
//                            optionlist.get(0).correctOption
//
//
//                        }
//                    }
////                    Log.d("outside For loop", "outside from for loop")
//
//
//                }
//            }
//
//        })


        button_change1 = view.findViewById(R.id.btnA) as RadioButton
        button_change2 = view.findViewById(R.id.btnB) as RadioButton
        button_change3 = view.findViewById(R.id.btnC) as RadioButton
        button_change4 = view.findViewById(R.id.btnD) as RadioButton

        ScreenNumber = view.findViewById(R.id.ScreenNumber) as TextView


        button_change1.setOnClickListener {

            button_change2.setChecked(false);
            button_change4.setChecked(false);
            button_change3.setChecked(false);
            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false
            stoptimer.cancel()

            if (btnA.text == optionlist.get(a).correctOption) {
//                button_change1.setBackgroundResource(R.drawable.correct_radio_clr)

                button_change1.setTextColor(Color.parseColor("#239015")) //green


            } else {

                button_change1.setTextColor(Color.parseColor("#FF2121")) //red

                when {
                    btnB.text == optionlist.get(a).correctOption -> {
            //                    button_change2.setBackgroundResource(R.drawable.correct_radio_clr)

                        button_change2.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnC.text == optionlist.get(a).correctOption -> {
            //                    button_change3.setBackgroundResource(R.drawable.correct_radio_clr)

                        button_change3.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnD.text == optionlist.get(a).correctOption -> {
            //                    button_change4.setBackgroundResource(R.drawable.correct_radio_clr)

                        button_change4.setTextColor(Color.parseColor("#00D300")) //green
                    }
                }

//                Log.d("btnA", "BtnA text else condition")
            }
        }
        button_change2.setOnClickListener {
            button_change1.setChecked(false);
            button_change4.setChecked(false);
            button_change3.setChecked(false);
            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false
            stoptimer.cancel()
            if (btnB.text == optionlist.get(a).correctOption) {

                button_change2.setTextColor(Color.parseColor("#239015")) //green


            } else {

                button_change2.setTextColor(Color.parseColor("#FF2121")) //red
//                Log.d("btnb", "BtnB text else condition")

                when {
                    btnA.text == optionlist.get(a).correctOption -> {
            //                    button_change1.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change1.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnC.text == optionlist.get(a).correctOption -> {
            //                    button_change3.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change3.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnD.text == optionlist.get(a).correctOption -> {
            //                    button_change4.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change4.setTextColor(Color.parseColor("#239015")) //green
                    }
                }
            }
        }

        button_change3.setOnClickListener {
            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false

            button_change2.setChecked(false);
            button_change1.setChecked(false);
            button_change4.setChecked(false);


            stoptimer.cancel()
            if (btnC.text == optionlist.get(a).correctOption) {

                button_change3.setTextColor(Color.parseColor("#239015")) //green

            } else {

                button_change3.setTextColor(Color.parseColor("#FF2121")) //red
//                Log.d("btnC", "BtnC text else condition")

                when {
                    btnB.text == optionlist.get(a).correctOption -> {
            //                    button_change2.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change2.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnA.text == optionlist.get(a).correctOption -> {
            //                    button_change1.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change1.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnD.text == optionlist.get(a).correctOption -> {
            //                    button_change4.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change4.setTextColor(Color.parseColor("#239015")) //green
                    }
                }
            }
        }

        button_change4.setOnClickListener {


            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false

            button_change2.setChecked(false);
            button_change1.setChecked(false);

            button_change3.setChecked(false);
            stoptimer.cancel()
            if (button_change4.text == optionlist.get(a).correctOption) {

                button_change4.setTextColor(Color.parseColor("#239015")) //green

//


            } else {

                button_change4.setTextColor(Color.parseColor("#FF2121")) //red
//                Log.d("btnD", "BtnD text else condition")

                when {
                    btnB.text == optionlist.get(a).correctOption -> {
            //                    button_change2.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change2.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnC.text == optionlist.get(a).correctOption -> {
            //                    button_change3.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change3.setTextColor(Color.parseColor("#239015")) //green
                    }
                    btnA.text == optionlist.get(a).correctOption -> {
            //                    button_change1.setBackgroundResource(R.drawable.correct_radio_clr)
                        button_change1.setTextColor(Color.parseColor("#239015")) //green
                    }
                }
            }


        }


        return view
    }

    private fun result() {
        sumTime = sumTime + time

        if (btnA.isChecked == true && btnA.text != optionlist.get(a).correctOption) {
            firstFalse++
        } else if (btnB.isChecked == true && btnB.text != optionlist.get(a).correctOption) {
            secondFalse++
        } else if (btnC.isChecked == true && btnC.text != optionlist.get(a).correctOption) {
            thirdFalse++
        } else if (btnD.isChecked == true && btnD.text != optionlist.get(a).correctOption) {
            fourthFalse++
        }

        if (btnA.isChecked && btnA.text == optionlist.get(a).correctOption) {
            firstTrue++
        } else if (btnB.isChecked && btnB.text == optionlist.get(a).correctOption) {
            secondTrue++
        } else if (btnC.isChecked && btnC.text == optionlist.get(a).correctOption) {
            thirdTrue++
        } else if (btnD.isChecked && btnD.text == optionlist.get(a).correctOption) {
            fourthTrue++
        }


        trueOpt = firstTrue + secondTrue + thirdTrue + fourthTrue

        falseOpt = firstFalse + secondFalse + thirdFalse + fourthFalse

//        skipOpt = 4f - (falseOpt + trueOpt)

//        Log.d("value of a","value of a ouside the if is: "+a)
        a++

        if (a < optionlist.size) {


            Log.d("value of a", "value of a is: " + a)

            //if(a==0){screen1=time}

            when (a) {
                1 -> {
                    screen1 = time
                    Log.d("value of a", "value of a is :1 and value of time is :" + time)
                }
                2 -> {
                    screen2 = time
                    Log.d("value of a", "value of a is :2 and value of time is :" + time)
                }
                3 -> {
                    screen3 = time
                    Log.d("value of a", "value of a is :3 and value of time is :" + time)
                }
//                4 -> {
//                    screen4 = time
//                    Log.d("value of a", "value of a is :3 and value of time is :" + time)
//                }
            }

            b++
            ScreenNumber.setText("Question " + b.toString() + " / 4")

            optionlist.get(a).question
            questionText.setText(optionlist.get(a).question)
//            val drawable: JLatexMathDrawable = JLatexMathDrawable.builder("${optionlist.get(a).question}")
//                .textSize(50F)
//                .padding(1)
////                                  .background(0xFFffffff)
//                .align(JLatexMathDrawable.ALIGN_RIGHT)
//                .build();
//            questionText.setLatexDrawable(drawable)

            optionlist.get(a).optionA
            btnA.setText(optionlist.get(a).optionA)

            optionlist.get(a).optionB
            btnB.setText(optionlist.get(a).optionB)

            optionlist.get(a).optionC
            btnC.setText(optionlist.get(a).optionC)

            optionlist.get(a).optionD
            btnD.setText(optionlist.get(a).optionD)

            button_change2.setTextColor(Color.parseColor("#25375F"))
            button_change1.setTextColor(Color.parseColor("#25375F"))
            button_change4.setTextColor(Color.parseColor("#25375F"))
            button_change3.setTextColor(Color.parseColor("#25375F"))



            button_change2.setChecked(false);
            button_change1.setChecked(false);
            button_change4.setChecked(false);
            button_change3.setChecked(false);

            btnB.isClickable = true
            btnC.isClickable = true
            btnD.isClickable = true
            btnA.isClickable = true

//            a++
            stoptimer.cancel()
            time = 0
            stoptimer.start()
            //  Toast.makeText(this,"This is true",trueOpt,"This is false",falseOpt,Toast.LENGTH_LONG).show()


        } else {

            optbtn.setText("End Questions")
            skipOpt = a.toFloat() - (falseOpt + trueOpt)

            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false
            optbtn.isClickable = false

//            getLearning()
//            marksRecord(trueOpt,falseOpt,skipOpt)
            (activity as Revision?)?.setCurrentItems(2, true)
//            Toast.makeText(activity, "Swipe left to see your Result", Toast.LENGTH_SHORT).show()
//            val intent = Intent(getActivity(),Result::class.java)
            screen4 = time
            Log.d("value of a", "value of a is :4 and value of time is :" + time)
            stoptimer.cancel()
//

            val frag3 = FragFourNew()
            val bundlee = Bundle()
            bundlee.putString("TrueValues", "hello")
            frag3.arguments = bundlee
            val managerr = fragmentManager
            val frag_trann = managerr?.beginTransaction()
            frag_trann?.replace(R.id.score, frag3)

            Log.d("trueOpt", "trueOpt " + trueOpt)
            frag_trann?.commit()



            // passData(trueOpt)


            stoptimer.cancel()


//            val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            model.setMarks(
                a,
                trueOpt,
                falseOpt,
                skipOpt,
                screen1,
                screen2,
                screen3,
                screen4//,
            //    screen5
            )



            var conditonalValue=1
            //--------
            when (a) {
                4 -> {
                    conditonalValue=3
                }
                3 -> {
                    conditonalValue=2

                }
                2 -> {
                    conditonalValue=1

                }
                1 -> {
                    conditonalValue=0

                }
            }
           // var stoper = 1
            if (trueOpt > conditonalValue) {
                val userDataReff1 = FirebaseDatabase.getInstance().reference.child("RevisionTopic").child(uid)
                val userDataReff =
                    FirebaseDatabase.getInstance().reference.child("RevisionTopic").child(uid).limitToLast(1)
                userDataReff.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
//                        TODO("not implemented")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                        if (p0!!.exists()) {
                            var chapkey1 = ""
                            var mcqssubject1 = ""
                            var modelchapter1 = ""
                            var grade = ""

                            for (b in p0.children) {

//                                if (stoper == 1) {
//                                    stoper++
                                    chapkey1 = b.child("chapkey").value.toString()
                                grade = b.child("grade").value.toString()
                                    mcqssubject1 = b.child("mcqssubject").value.toString()
                                    modelchapter1 = b.child("modelchapter").value.toString()

                                Log.d("RevisionTopics", "remove node ${mcqssubject1 + modelchapter1 + chapkey1+grade}")

                                userDataReff1.child(mcqssubject1 + modelchapter1 + chapkey1+grade)
                                    .removeValue()
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {


                                                Log.d("RevisionTopics", "remove data revison")
//                         Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                                            } else {

                                                Toast.makeText(
                                                    getActivity(),
                                                    "Something else Went Wrong:(",
                                                    Toast.LENGTH_LONG
                                                )
                                                    .show()
                                                //  FirebaseAuth.getInstance().signOut()
                                            }
                                        }
//                                }
                            }
                        }
                    }
                })
            }

//            (activity as Revision?)?.setCurrentItems(0, true)

        }
    }

    override fun onPause() {
        super.onPause()
        stoptimer.cancel()
    }

    override fun onResume() {
        super.onResume()
        time = 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stoptimer = object : CountDownTimer(1000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //set_timer.setText(time.toString())
                time++
                Log.d("timer log", "log for timer" + time)

            }


            override fun onFinish() {
                //  startActivity(Intent(this@Options, ResultActivity::class.java))
//                Log.d("timer log", "log for setText")
                time = 0
            }
        }


//        stoptimer.start()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            stoptimer.start()
        } else {
            stoptimer.cancel()
        }
    }

}