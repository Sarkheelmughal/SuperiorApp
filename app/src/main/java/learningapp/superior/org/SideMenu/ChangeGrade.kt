package learningapp.superior.org.SideMenu

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_grade.*
import learningapp.superior.org.R


class ChangeGrade : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_grade)

        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade = this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected","def")

        val PREFERENCE_FILE_KEY_Grade_refresh = "AppPreferenceMenuGradeRefresh"
        val sharedPrefGradeRefresh = this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade_refresh, Context.MODE_PRIVATE)

        backFromGradeCV.setOnClickListener {
            onBackPressed()
        }

        ninethGradeCV.setOnClickListener {
            entryTestPrepTV.setTextColor((Color.rgb(255,186,95)))
            entryTestPrepCV.setCardBackgroundColor(Color.WHITE)
            ninethGradeCV.setCardBackgroundColor(Color.rgb(255,186,95))
            ninethGradeTV.setTextColor(Color.WHITE)

            with(sharedPrefGrade.edit()) {
                putString("gradeSelected", "9th")
                commit()
            }

            with(sharedPrefGradeRefresh.edit()) {
                putString("gradeSelectedRefresh", "2")
                commit()
            }

            onBackPressed()
        }
        entryTestPrepCV.setOnClickListener {
            ninethGradeTV.setTextColor((Color.rgb(255,186,95)))
            ninethGradeCV.setCardBackgroundColor(Color.WHITE)
            entryTestPrepCV.setCardBackgroundColor(Color.rgb(255,186,95))
            entryTestPrepTV.setTextColor(Color.WHITE)

            with(sharedPrefGrade.edit()) {
                putString("gradeSelected", "entry")
                commit()
            }

            with(sharedPrefGradeRefresh.edit()) {
                putString("gradeSelectedRefresh", "2")
                commit()
            }


            onBackPressed()
        }

        if (gradeSelect=="9th"){
            entryTestPrepTV.setTextColor((Color.rgb(255,186,95)))
            entryTestPrepCV.setCardBackgroundColor(Color.WHITE)
            ninethGradeCV.setCardBackgroundColor(Color.rgb(255,186,95))
            ninethGradeTV.setTextColor(Color.WHITE)
        }else{
            ninethGradeTV.setTextColor((Color.rgb(255,186,95)))
            ninethGradeCV.setCardBackgroundColor(Color.WHITE)
            entryTestPrepCV.setCardBackgroundColor(Color.rgb(255,186,95))
            entryTestPrepTV.setTextColor(Color.WHITE)
        }

//        tenthGradeCV.setOnClickListener {
//            val gradeSelect =
//                sharedPrefGrade.getString("gradeSelected","def")
//            val gradeSelectRefresh =
//                sharedPrefGradeRefresh.getString("gradeSelectedRefresh","def")
//
////            finish();
////            overridePendingTransition( 0, 0);
////            startActivity(getIntent());
////            overridePendingTransition( 0, 0);
//
//            Log.d("gradeSelected",gradeSelect.toString())
//            Log.d("gradeSelected",gradeSelectRefresh.toString())
//
//        }
    }
}