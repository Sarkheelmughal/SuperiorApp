package learningapp.superior.org.SideMenu

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_fill_in_the_blank.*
import learningapp.superior.org.R

class FillInTheBlank : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_in_the_blank)
// 6 5 4 3
        var question =
            "Physics is the branch of ______ concerned with the nature and _____ of matter and ____. The subject matter of physics includes ___ , heat, light and other radiation, sound, electricity, magnetism, and the structure of atoms"
        // testing()
        sampleQuestionFind.setText(question)
        val correctans = "2431"

        BtnFirst.setText("mechanics")
        BtnSecond.setText("science")
        BtnThird.setText("energy")
        BtnFourth.setText("properties")

        var ans = ""

        var mcqNumber = 1

        var selectionNumber = 1
        linearOne.setOnClickListener {

            Log.d("mcqNumber",mcqNumber.toString())

            val firstText= "<b>"+"<font color='#FFBA5F'> ${BtnFirst.text} </font> "+ "</b> "

            if (mcqNumber == 1) {


                question=question.replace("______",firstText.toString())
                sampleQuestionFind.setText(Html.fromHtml(question))

            }else if(mcqNumber == 2){

                question=question.replace("_____",firstText.toString())
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 3){

                question=question.replace("____",firstText.toString())
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 4){

                question=question.replace("___",firstText.toString())
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }
            mcqNumber++
            Log.d("questionISThis",question)
            Log.d("questionFill",BtnFirst.text.toString())

            //  BtnFirst.visibility=View.GONE

            selectionNumberingOneTv.visibility = View.VISIBLE
            selectionNumberingOneTv.text = selectionNumber.toString()
            selectionNumber++
            ans = ans + "1"

            BtnFirst.setTextColor(Color.parseColor("#FFBA5F"))

            BtnFirst.isClickable = false
        }
        linearTwo.setOnClickListener { //BtnSecond.visibility=View.GONE

            val SecondText="<b>"+"<font color='#FFBA5F'> ${BtnSecond.text} </font> "+ "</b> "


            Log.d("mcqNumber",mcqNumber.toString())

            if (mcqNumber == 1) {

                question=question.replace("______",SecondText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 2){

                question=question.replace("_____",SecondText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 3){

                question=question.replace("____",SecondText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 4){

                question=question.replace("___",SecondText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }
            mcqNumber++
            Log.d("questionISThis",question)
            Log.d("questionFill",BtnSecond.text.toString())

            BtnSecond.setTextColor(Color.parseColor("#FFBA5F"))
            BtnSecond.isClickable = false

            selectionNumberingTwoTv.visibility = View.VISIBLE
            selectionNumberingTwoTv.text = selectionNumber.toString()
            selectionNumber++

            ans = ans + "2"
        }
        linearThree.setOnClickListener {// BtnThird.visibility=View.GONE


            Log.d("mcqNumber",mcqNumber.toString())

            val ThirdText= "<b>"+"<font color='#FFBA5F'> ${BtnThird.text} </font> "+ "</b> "



            if (mcqNumber == 1) {

                question=question.replace("______",ThirdText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 2){

                question=question.replace("_____",ThirdText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 3){

                question=question.replace("____",ThirdText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 4){

                question=question.replace("___",ThirdText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }

            mcqNumber++
            Log.d("questionISThis",question)
            Log.d("questionFill",BtnThird.text.toString())


            ans = ans + "3"
            BtnThird.setTextColor(Color.parseColor("#FFBA5F"))
            BtnThird.isClickable = false


            selectionNumberingThreeTv.visibility = View.VISIBLE
            selectionNumberingThreeTv.text = selectionNumber.toString()
            selectionNumber++
        }
        linearFour.setOnClickListener { //BtnFourth.visibility=View.GONE

            Log.d("mcqNumber",mcqNumber.toString())
            val FourthText= "<b>"+"<font color='#FFBA5F'> ${BtnFourth.text} </font> "+ "</b> "




            if (mcqNumber == 1) {

                question=question.replace("______",FourthText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 2){

                question=question.replace("_____",FourthText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 3){

                question=question.replace("____",FourthText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }else if(mcqNumber == 4){

                question=question.replace("___",FourthText)
//                sampleQuestionFind.setText(question)
                sampleQuestionFind.setText(Html.fromHtml(question))
            }
            mcqNumber++


            Log.d("questionFill",BtnFourth.text.toString())
            Log.d("questionISThis",question)

            ans = ans + "4"
            BtnFourth.setTextColor(Color.parseColor("#FFBA5F"))
            BtnFourth.isClickable = false

            selectionNumberingFourTv.visibility = View.VISIBLE
            selectionNumberingFourTv.text = selectionNumber.toString()
            selectionNumber++
        }

        mcqsBackIV.setOnClickListener{
            onBackPressed()
        }
        checkBtn.setOnClickListener {
            Log.d("ansfillinis", ans)

            if (correctans == ans) {
                Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "False", Toast.LENGTH_SHORT).show()

            }
        }

    }

    private fun testing() {
        var a = "5 _____ 4 ____ 3 ___ "

//       Log.d("stringtest1",a.contains("--",ignoreCase = true).toString())
//       Log.d("stringtest2",a.contains("bac",ignoreCase = true).toString())
//       Log.d("stringtest3",a.contains("-----",ignoreCase = true).toString())

        var b = a.replace("_____", "hello")

        Log.d("stringtest1", b.toString())

        b = b.replace("____", "I'm")


        Log.d("stringtest2", b.toString())

        b = b.replace("___", "Sarkheel")


        Log.d("stringtest3", b.toString())


    }
}