package learningapp.superior.org.VideoPlayerFragments


import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.getField
import kotlinx.android.synthetic.main.mcqs_option.*
import kotlinx.android.synthetic.main.mcqs_option.view.*
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.Models.Model
import learningapp.superior.org.R
import learningapp.superior.org.Utils.FireStoreUtils
import ru.noties.jlatexmath.JLatexMathDrawable
import ru.noties.jlatexmath.JLatexMathView


class Fragmenttwo : Fragment() {


    //    lateinit var dataPasser2: Fragmenttwo.OnDataPasss
    lateinit var button_change2: RadioButton
    lateinit var button_change3: RadioButton
    lateinit var button_change4: RadioButton
    lateinit var button_change1: RadioButton
    lateinit var ScreenNumber: TextView
    lateinit var optionlist: ArrayList<McqsModel>
    var time = 0
    var sumTime = 0
    ///Time of every scree
    //written code my


    //....mine
    var stoptimer = object : CountDownTimer(0, 0) {
        override fun onFinish() {}
        override fun onTick(p0: Long) {}
    }
    var screen1 = 0
    var screen2 = 0
    var screen3 = 0
    var screen4 = 0
//    var screen5 = 0
//    var screen6 = 0

    ///
    var a: Int = -1
    var b: Int = 1

    var trueOpt: Float = 0f
    var falseOpt: Float = 0f
    var skipOpt: Float = 4f

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
    private val model: QuizVideoModel by activityViewModels()


    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )


            : View {

        val view: View = inflater.inflate(R.layout.mcqs_option, container, false)
        view.quiz_star.setOnClickListener {
            layout_mcq.visibility = View.INVISIBLE
            cover_views.visibility = View.VISIBLE
        }

//       val questionText: JLatexMathView= view.findViewById(R.id.questionText)
        // model= ViewModelProviders.of(requireActivity()).get(QuizVideoModel::class.java)


        optionlist = ArrayList()
        val modl = requireArguments().getParcelable<McqsModel>("obj")
        val model = requireArguments().getParcelable<Model>("object")


        val sharepref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val edit = sharepref.edit()
        if (model?.key == null) {
            Log.d("if nothing to change", "if frag2")
        } else {
            edit.putString("model", model.key)
            edit.apply()
        }
        val modelchapter = sharepref.getString("model", "0").toString()


//        stoptimer.start()

        //   key=getActivity()!!.intent.getStringExtra("key")
        //  GetTopicName=getActivity()!!.intent.getStringExtra("GetTopicName")


        val textTimer = view.findViewById(R.id.set_timer) as TextView
//        lateinit var ref: DatabaseReference
        val chapkey = requireArguments().getString("items.key").toString()
        val subject = requireArguments().getInt("subject", 0)
        var mcqssubject: String = "null"
        val grade = requireArguments().getString("grade").toString()


        view.optbtn.setOnClickListener {
            try{ resNext(grade)  }catch (exceptionType: Exception){

            }
        }

        val sharepreff = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editt = sharepreff.edit()
        if (subject == 0) {
            Log.d("if nothing to change", "if frag2")
        } else {
            editt.putInt("subject", subject)
            editt.apply()
        }
        val subjectt = sharepreff.getInt("subject", 0)
//

        when (subjectt) {
            1 -> {
                Log.d("Physics clicked ", "Physics clicked frag2")
                mcqssubject = "physics"
            }

            2 -> {
                Log.d("Chemis clicked", "Chemis clicked frag2")
                mcqssubject = "chemistry"
            }

            3 -> {
                Log.d("biology clicked", "biology clicked frag2")
                mcqssubject = "biology"
            }
        }


        Log.d("grade", " fra2 grade :" + grade.toString())
        Log.d("mcqssubject", " fra2 mcqssubject :" + mcqssubject.toString())
        Log.d("chapkey", "fra2 chapkey :" + chapkey.toString())
        Log.d("modelchapter", "fra2 modelchapter :" + modelchapter.toString())

        ref = if (requireArguments().getString("items.key") != null) {

            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child(grade).child("subjects").child(mcqssubject).child(modelchapter)
                .child(chapkey)
        } else {
            FirebaseDatabase.getInstance().getReference("contents").child("topic_questions")
                .child(grade).child("subjects").child(mcqssubject).child(modelchapter)
                .child("topic1")
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0!!.exists()) {
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
//                        optionlist.add(opt)
                        if (optionlist.size < 4) {
                            optionlist.add(opt)
                        } else {
                            break // Stop adding elements if the optionlist has 4 items
                        }

                        if (a == -1) {
                            a++

                            optionlist.get(0).question
                            questionText.setText(optionlist.get(0).question)
//                            val drawable:JLatexMathDrawable = JLatexMathDrawable.builder("${optionlist.get(0).question}")
//                                .textSize(50F)
//                                .padding(1)
////                                  .background(0xFFffffff)
//                                .align(JLatexMathDrawable.ALIGN_RIGHT)
//                                .build();
//                            questionText.setLatexDrawable(drawable)

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


//        stoptimer = object : CountDownTimer(1000000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                textTimer.setText(time.toString())
//                time++
//                Log.d("timer log", "log for timer" + time)
//
//            }
//
//            override fun onFinish() {
//                //  startActivity(Intent(this@Options, ResultActivity::class.java))
////                Log.d("timer log", "log for setText")
//                time=0
//            }
//        }.start()


//        var timer = view.findViewById(R.id.set_timer) as TextView
        var button_background: Int = 1
        var btnDisable: Int = 1

        button_change1 = view.findViewById(R.id.btnA) as RadioButton
        button_change2 = view.findViewById(R.id.btnB) as RadioButton
        button_change3 = view.findViewById(R.id.btnC) as RadioButton
        button_change4 = view.findViewById(R.id.btnD) as RadioButton

        ScreenNumber = view.findViewById(R.id.ScreenNumber) as TextView


        button_change1.setOnClickListener {

            //            btnA.isClickable = false
            button_change2.isClickable = false
            button_change3.isClickable = false
            button_change4.isClickable = false
            // button_background=2

            button_change2.setChecked(false);

            button_change4.setChecked(false);
            button_change3.setChecked(false);

            stoptimer.cancel()

            try {


            if (btnA.text == optionlist.get(a).correctOption) {
//                button_change1.setBackgroundResource(R.drawable.correct_radio_clr)

                button_change1.setTextColor(Color.parseColor("#239015")) //green
//                trueOpt++
//                skipOpt--
                ///////////
//                firstTrue++
                ///////

            }
            else {


//                falseOpt++
//                skipOpt--

//                button_change1.setBackgroundResource(R.drawable.wrong_radio_clr)
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
            }  }catch (exceptionType: Exception){

            }
        }


        button_change2.setOnClickListener {
            // button_background=2

//            btnA.isClickable = false
//            btnB.isClickable = false
//            btnC.isClickable = false
//            btnD.isClickable = false
            button_change1.isClickable = false
            button_change3.isClickable = false
            button_change4.isClickable = false

            button_change1.setChecked(false);
            button_change4.setChecked(false);
            button_change3.setChecked(false);

            stoptimer.cancel()
            try{
            if (btnB.text == optionlist.get(a).correctOption) {
                // if(button_background==2){
//                button_change2.setBackgroundResource(R.drawable.correct_radio_clr)
                // button_change.setBackgroundResource(R.drawable.correct_option)
                button_change2.setTextColor(Color.parseColor("#239015")) //green

//                trueOpt++
//                skipOpt--


//                secondTrue++
                ////


            }
            else {


//                falseOpt++
//                skipOpt--
//                button_change2.setBackgroundResource(R.drawable.wrong_radio_clr)
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
            }  }catch (exceptionType: Exception){

            }
        }

        button_change3.setOnClickListener {
            // button_background=2

//            btnA.isClickable = false
//            btnB.isClickable = false
//            btnC.isClickable = false
//            btnD.isClickable = false
            button_change2.isClickable = false
            button_change1.isClickable = false
            button_change4.isClickable = false

            button_change2.setChecked(false);
            button_change1.setChecked(false);
            button_change4.setChecked(false);


            stoptimer.cancel()
            try{
            if (btnC.text == optionlist.get(a).correctOption) {
                // if(button_background==2){
//                button_change3.setBackgroundResource(R.drawable.correct_radio_clr)
                //button_change.setBackgroundResource(R.drawable.correct_option)

                button_change3.setTextColor(Color.parseColor("#239015")) //green

//                trueOpt++
//                skipOpt--


//                thirdTrue++
                ////


            }
            else {


//                falseOpt++
//                skipOpt--
//                button_change3.setBackgroundResource(R.drawable.wrong_radio_clr)
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
            }  }catch (exceptionType: Exception){

            }
        }

        button_change4.setOnClickListener {
            // button_background=2

//            btnA.isClickable = false
//            btnB.isClickable = false
//            btnC.isClickable = false
//            btnD.isClickable = false
            button_change2.isClickable = false
            button_change3.isClickable = false
            button_change1.isClickable = false

            button_change2.setChecked(false);
            button_change1.setChecked(false);

            button_change3.setChecked(false);
            stoptimer.cancel()
            try{
            if (button_change4.text == optionlist.get(a).correctOption) {
                // if(button_background==2){
                //  button_change3.setBackgroundResource(R.drawable.wrong_option)
//                button_change4.setBackgroundResource(R.drawable.correct_radio_clr)
                button_change4.setTextColor(Color.parseColor("#239015")) //green

//                trueOpt++
//                skipOpt--


//                fourthTrue++
                ////


            }
            else {


//                falseOpt++
//                skipOpt--
//                button_change4.setBackgroundResource(R.drawable.wrong_radio_clr)
                button_change4.setTextColor(Color.parseColor("#FF2121")) //red
//                Log.d("btnD", "BtnD text else condition")

                if (btnB.text == optionlist.get(a).correctOption) {
//                    button_change2.setBackgroundResource(R.drawable.correct_radio_clr)
                    button_change2.setTextColor(Color.parseColor("#239015")) //green
                } else if (btnC.text == optionlist.get(a).correctOption) {
//                    button_change3.setBackgroundResource(R.drawable.correct_radio_clr)
                    button_change3.setTextColor(Color.parseColor("#239015")) //green
                } else if (btnA.text == optionlist.get(a).correctOption) {
//                    button_change1.setBackgroundResource(R.drawable.correct_radio_clr)
                    button_change1.setTextColor(Color.parseColor("#239015")) //green
                }
            }  }catch (exceptionType: Exception){

            }


        }
        return view
    }


    //    val fAns = pref.edit()
    fun resNext(grade: String) {

        sumTime = sumTime + time
//             Log.d("testing of sum","the sum of timer is :"+sumTime)


//    skipOpt=
        if (btnA.isChecked && btnA.text != optionlist.get(a).correctOption) {
            firstFalse++
        } else if (btnB.isChecked && btnB.text != optionlist.get(a).correctOption) {
            secondFalse++
        } else if (btnC.isChecked && btnC.text != optionlist.get(a).correctOption) {
            thirdFalse++
        } else if (btnD.isChecked && btnD.text != optionlist.get(a).correctOption) {
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
            Log.d("value of a", "value of optionlist.size is:  ${optionlist.size.toString()}")

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

//                5 -> {
//                    screen5 = time
//                    Log.d("value of a", "value of a is :3 and value of time is :" + time)
//                }
//
//                6 -> {
//                    screen6 = time
//                    Log.d("value of a", "value of a is :3 and value of time is :" + time)
//                }
            }


            b++
            ScreenNumber.setText("Question " + b.toString())
//            ScreenNumber.setText("Question " + b.toString() + " / 4")

            optionlist.get(a).question
            questionText.setText(optionlist.get(a).question)
//            val drawable:JLatexMathDrawable = JLatexMathDrawable.builder("${optionlist.get(a).question}")
//                .textSize(50F)
//                .padding(1)
////                                  .background(0xFFffffff)
//                .align(JLatexMathDrawable.ALIGN_CENTER)
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

            button_change2.isChecked = false;
            button_change1.isChecked = false;
            button_change4.isChecked = false;
            button_change3.isChecked = false;

            btnB.isClickable = true
            btnC.isClickable = true
            btnD.isClickable = true
            btnA.isClickable = true

//            a++
            stoptimer.cancel()
            time = 0
            stoptimer.start()
            //  Toast.makeText(this,"This is true",trueOpt,"This is false",falseOpt,Toast.LENGTH_LONG).show()


        }
        else {
            skipOpt = a.toFloat() - (falseOpt + trueOpt)

            optbtn.setText("End Questions")
            btnA.isClickable = false
            btnB.isClickable = false
            btnC.isClickable = false
            btnD.isClickable = false
            optbtn.isClickable = false
            getLearning()
            marksRecord(trueOpt, falseOpt, skipOpt, grade) // store data for every quiz

            subjectWiseDataForProgressReport(trueOpt, falseOpt, skipOpt) // uncoment it


            screen4 = time
            Log.d("value of a", "value of a is :4 and value of time is :" + time)
            stoptimer.cancel()


            stoptimer.cancel()

            model.setMarks(
                a,
                trueOpt,
                falseOpt,
                skipOpt,
                screen1,
                screen2,
                screen3,
                screen4
            )

            (activity as VideoNew?)?.setCurrentItem(2, true)


        }

    }

    private fun subjectWiseDataForProgressReport(trueOpt: Float, falseOpt: Float, skipOpt: Float) {
        val model = requireArguments().getParcelable<Model>("object")
        val sharepref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val edit = sharepref.edit()

        if (model?.key == null) {
            Log.d("if nothing to change", "if frag2")
        } else {
            edit.putString("model", model.key)
            edit.apply()
        }
        val modelchapter = sharepref.getString("model", "0").toString()
//
        val chapkey = requireArguments().getString("items.key").toString()
        val subject = requireArguments().getInt("subject", 0)
        var mcqssubject: String = "null"


        val sharepreff = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editt = sharepreff.edit()
        if (subject == 0) {
            Log.d("if nothing to change", "if frag2")
        } else {
            editt.putInt("subject", subject)
            editt.apply()
        }
        val subjectt = sharepreff.getInt("subject", 0)
//

        when (subjectt) {
            1 -> {
                Log.d("Physics clicked ", "Physics clicked frag2")
                mcqssubject = "physics"
            }

            2 -> {
                Log.d("Chemis clicked", "Chemis clicked frag2")
                mcqssubject = "chemistry"
            }

            3 -> {
                mcqssubject = "biology"
            }
        }

        ///------------------------------------------------------

        val getref = FireStoreUtils().fireStoreDatabase.collection("ProgressReport")
            .document(FireStoreUtils().UID).collection(mcqssubject).document("details")


        getref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    var backendTrue = 0f
                    var backendFalse = 0f
                    var backendSkip = 0f
                    if (documentSnapshot.getField<Any>("trueOpt") != null || documentSnapshot.getField<Any>(
                            "trueOpt"
                        ) != "null"
                    ) {
                        //backendTrue=documentSnapshot.getString("trueOpt").toString().toFloat()
                        backendTrue = documentSnapshot.getField<Any>("trueOpt").toString().toFloat()

                    }
                    if (documentSnapshot.getField<Any>("falseOpt") != null || documentSnapshot.getField<Any>(
                            "falseOpt"
                        ) != "null"
                    ) {
                        backendFalse =
                            documentSnapshot.getField<Any>("falseOpt").toString().toFloat()
                    }
                    if (documentSnapshot.getField<Any>("skipOpt") != null || documentSnapshot.getField<Any>(
                            "skipOpt"
                        ) != "null"
                    ) {
                        backendSkip = documentSnapshot.getField<Any>("skipOpt").toString().toFloat()
                    }
                    val trueResult = trueOpt + backendTrue
                    val falseResult = falseOpt + backendFalse
                    val skipResult = skipOpt + backendSkip
                    //Map<String, Object> note = documentSnapshot.getData();
                    val checker = 1
                    pasteDataSubjectWise(trueResult, falseResult, skipResult, checker, getref)
                } else {
                    val checker = 2
                    pasteDataSubjectWise(trueOpt, falseOpt, skipOpt, checker, getref)
                }
            }
            .addOnFailureListener { e ->

                Log.d("gettingSubjectData", e.toString())
            }

    }

    private fun pasteDataSubjectWise(
        trueOpt: Float,
        falseOpt: Float,
        skipOpt: Float,
        checker: Int,
        getref: DocumentReference
    ) {
        if (checker == 2) {
            val note: MutableMap<String, Any> = HashMap()
            note["trueOpt"] = trueOpt
            note["falseOpt"] = falseOpt
            note["skipOpt"] = skipOpt
            getref.set(note)
                .addOnSuccessListener {
                    Log.d("fireStoreSetData", "done")


                }
                .addOnFailureListener { e ->
                    Log.d("fireStoreSetData", e.toString())
                }
        } else if (checker == 1) {
            getref.update("trueOpt", trueOpt)
            getref.update("falseOpt", falseOpt)
            getref.update("skipOpt", skipOpt)

        }
        ////////////////////////// above ok below testing

//        val note: MutableMap<String, Any> = HashMap()
//
//        for (a in 0..100) {
//
//            note["trueOpt$a"] = trueOpt+a
//            note["falseOpt$a"] = falseOpt+a
//            note["skipOpt$a"] = skipOpt+a
//        }
//        val getref=FireStoreUtils().fireStoreDatabase.collection("ProgressReport").document(FireStoreUtils().UID).collection("physics").document("details")
//
//        getref.set(note)
//                .addOnSuccessListener {
//                    Log.d("fireStoreSetData", "done")
//
//
//                }
//                .addOnFailureListener { e ->
//                    Log.d("fireStoreSetData", e.toString())
//                }


    }


//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        dataPasser2 = context as OnDataPasss
//    }
//
//    fun passData(data: Float) {
//        dataPasser2.onDataPass(data)
//    }
//
//    interface OnDataPasss {
//        fun onDataPass(data: Float)
////        fun passData(data: String)
//    }

    var falseSum: Float = 0f
    var trueSum: Float = 0f
    var skipSum: Float = 0f

    fun marksRecord(tAns: Float, fAns: Float, UnAns: Float, grade: String) {
        val model = requireArguments().getParcelable<Model>("object")
        val sharepref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val edit = sharepref.edit()

        if (model?.key == null) {
            Log.d("if nothing to change", "if frag2")
        } else {
            edit.putString("model", model.key)
            edit.apply()
        }
        val modelchapter = sharepref.getString("model", "0").toString()
//
        val chapkey = requireArguments().getString("items.key").toString()
        val subject = requireArguments().getInt("subject", 0)
        var mcqssubject: String = "null"


        val sharepreff = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editt = sharepreff.edit()
        if (subject == 0) {
            Log.d("if nothing to change", "if frag2")
        } else {
            editt.putInt("subject", subject)
            editt.apply()
        }
        val subjectt = sharepreff.getInt("subject", 0)
//

        when (subjectt) {
            1 -> {
                Log.d("Physics clicked ", "Physics clicked frag2")
                mcqssubject = "physics"
            }

            2 -> {
                Log.d("Chemis clicked", "Chemis clicked frag2")
                mcqssubject = "chemistry"
            }

            3 -> {
                mcqssubject = "biology"
            }
        }

        ///------------------------------------------------------
//        var subject = arguments!!.getInt("subject", 0)
//        var subjectt = sharepreff.getInt("subject", 0)
        Log.d("marksRecord", "marksRecord " + ref)
        Log.d("marksRecord", "marksRecord subjectss" + " and " + subject)
        val uid: String = FirebaseAuth.getInstance().currentUser!!.uid

        val path = if (requireArguments().getString("items.key") != null) {

            FirebaseDatabase.getInstance().getReference("UserLearningDataLast").child(uid)
                .child("subjects").child(mcqssubject).child(modelchapter)
                .child(chapkey)
        } else {
            FirebaseDatabase.getInstance().getReference("UserLearningDataLast").child(uid)
                .child("subjects").child(mcqssubject).child(modelchapter)
                .child("topic1")
        }

        val userMap = HashMap<Any, Any>()
        userMap["truevalue"] = tAns
        userMap["falsevalue"] = fAns
        userMap["skipvalue"] = UnAns

        path.child("last").setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(getActivity(), "Something Went Wrong:(", Toast.LENGTH_LONG)
                        .show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }
        //-------------------------------------------------------------------------------------------------
        val userDataReff: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("RevisionTopic")

        var subOfmcq = ""
        when (mcqssubject) {
            "physics" -> {
                subOfmcq = "subjects-1"
            }

            "chemistry" -> {
                subOfmcq = "chemistry"
            }

            "biology" -> {
                subOfmcq = "biology"
            }
        }
        val userMapp = HashMap<Any, Any>()

        userMapp["mcqssubject"] = subOfmcq
        userMapp["modelchapter"] = modelchapter
        userMapp["chapkey"] = chapkey
        userMapp["grade"] = grade

//        val shareprefCount = activity!!.getPreferences(Context.MODE_PRIVATE)
//        val editCount = shareprefCount.edit()
//        editCount.putInt("counter", 0)
//
//         var count=0
//        count=shareprefCount.getInt("counter",0)
//        count++
//        editCount.putInt("counter",count++)
//        editCount.apply()

        if (chapkey.isEmpty() || chapkey == null || chapkey == "null") {
            Log.d("one of these are null", "subOfmcq!=null && modelchapter!=null && chapkey!=null")
        } else {

            if (fAns >= 2 || UnAns >= 2) {
                userDataReff.child(uid).child(subOfmcq + modelchapter + chapkey + grade)
                    .setValue(userMapp)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                        } else {

                            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_LONG)
                                .show()

                        }

                    }
            } else {
                Log.d("else of", "else of UnAns >= 2 ")
                userDataReff.child(uid).child(subOfmcq + modelchapter + chapkey).setValue(null)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

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

            }

        }

    }

    fun getLearning() {
        var count: Int = 1
        val child: String = FirebaseAuth.getInstance().currentUser!!.uid
//        Log.d("value of child var", "value of child is:" + child)

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UserLearningData").child(child)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {


                    if (count == 1) {
                        count++

                        p0.child("truevalue").value.toString()
                        p0.child("falsevalue").value.toString()
                        p0.child("skipvalue").value.toString()

                        trueSum =
                            trueOpt.toFloat() + (p0.child("truevalue").value.toString().toFloat())
                        falseSum =
                            falseOpt.toFloat() + (p0.child("falsevalue").value.toString().toFloat())
                        skipSum =
                            (skipOpt.toFloat()) + (p0.child("skipvalue").value.toString().toFloat())

                        sumLearning(trueSum, falseSum, skipSum)
//                        Log.d("value of trueSum","value of trueSum"+trueSum)

                    }
//                    for (h in p0.children) {

                } else {
                    sumLearning(trueSum, falseSum, skipSum)
                }


            }

        })


    }

    var currentUserDataId = FireStoreUtils().UID

    fun sumLearning(tAns: Float, fAns: Float, UnAns: Float) {

        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("UserLearningData")

        val userMap = HashMap<Any, Any>()
        userMap["uid"] = currentUserDataId
        userMap["truevalue"] = tAns
        userMap["falsevalue"] = fAns
        userMap["skipvalue"] = UnAns

        userDataRef.child(currentUserDataId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(activity, "Something Went Wrong:(", Toast.LENGTH_LONG)
                        .show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }

        ///--------------parental data start

        val parentalref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        parentalref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {

                    val s = p0.child("Variable").value.toString()

                    Log.d("testing setting", "testing" + s)


                    val parentalUserDataRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("StudentsDataForParent")
                            .child(s).child("results")

                    val ParentaluserMap = HashMap<Any, Any>()
                    ParentaluserMap["truevalue"] = tAns
                    ParentaluserMap["falsevalue"] = fAns
                    ParentaluserMap["skipvalue"] = UnAns

                    parentalUserDataRef.setValue(ParentaluserMap)

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                            } else {

                                //  FirebaseAuth.getInstance().signOut()
                            }
                        }
                } else {


                }
            }

        })
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


    //    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//
//        if(isVisibleToUser)
//        {
//         time=0
//         stoptimer.start()
//            clearFindViewByIdCache()
//        }
//
//
//
//    }
    override fun onStart() {
        super.onStart()
    }
//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//
//
//        if (getFragmentManager() != null) {
//
//            getFragmentManager()!!
//                .beginTransaction()
//                .detach(this)
//                .attach(this)
//                .commit();
//        }
//    }
}