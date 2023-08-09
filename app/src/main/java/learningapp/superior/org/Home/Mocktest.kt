package learningapp.superior.org.Home


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.mocktest.*
import learningapp.superior.org.CustomOptionsForNineth.CustomMcqsNine
import learningapp.superior.org.R


class Mocktest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mocktest)

        val backBtn=findViewById(R.id.backCustomCV) as CardView
        backBtn.setOnClickListener { onBackPressed() }

        var currentValue = seekBarSB.getProgress();
        var subject = "null"

        startCustomTestCV.isEnabled = false
        seekBarSB.isEnabled = false

        physSelectCV.setOnClickListener {
            subject = "physics"
            seekBarSB.isEnabled = true
            maxNumSeekBarTV.setText("144")
            seekBarSB.max = 144
            physSelectCV.setCardBackgroundColor(Color.rgb(255, 215, 163))
            chemSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            bioSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
        }
        chemSelectCV.setOnClickListener {
            seekBarSB.isEnabled = true
            subject = "chemistry"

            maxNumSeekBarTV.setText("240")
            seekBarSB.max = 240

            chemSelectCV.setCardBackgroundColor(Color.rgb(255, 215, 163))
            physSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            bioSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))


        }
        bioSelectCV.setOnClickListener {
            seekBarSB.isEnabled = true
            subject = "biology"

            maxNumSeekBarTV.setText("140")
            seekBarSB.max = 140

            bioSelectCV.setCardBackgroundColor(Color.rgb(255, 215, 163))
            physSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
            chemSelectCV.setCardBackgroundColor(Color.rgb(255, 255, 255))
        }

        seekBarSB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentValue = progress;
                mcqsSelectTV.setText("${currentValue}")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

                startCustomTestCV.isEnabled = true
                startCustomTestCV.setCardBackgroundColor(Color.rgb(255, 168, 95))
            }
        });


        //---------------------------------------------------------------



        startCustomTestCV.setOnClickListener {
         val intent= Intent(this,CustomMcqsNine::class.java)
            intent.putExtra("subject",subject)
            intent.putExtra("showMcqs",currentValue)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}



