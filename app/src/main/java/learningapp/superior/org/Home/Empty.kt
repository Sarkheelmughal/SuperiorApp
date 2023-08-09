package learningapp.superior.org.Home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.empty.*
import learningapp.superior.org.R

class Empty:AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty)

        var Math=intent.getStringExtra("Math")
        var Chemistry=intent.getStringExtra("Chemistry")
        var Bio=intent.getStringExtra("Bio")

        var CN=intent.getIntExtra("CN",0)


        if (CN ==1){
            emptysubicon.setImageResource(R.drawable.chemistry_logo)
            subId.setText(Chemistry)
        }
        else if(CN == 2){

            emptysubicon.setImageResource(R.drawable.bio_logo)
//            emptysubicon.setImageDrawable(R.drawable.bio_logo)
            subId.setText(Bio)

        }
        else if(CN==3){
            subId.setText(Math)
        }


        emptybackbtn.setOnClickListener{
            onBackPressed()
        }
    }
}