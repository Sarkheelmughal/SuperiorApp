package learningapp.superior.org.Home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.coming_soon_past_papers.*
import kotlinx.android.synthetic.main.coming_soon_past_papers.view.*
import learningapp.superior.org.R
import learningapp.superior.org.databinding.ActivityPastPapersBinding

class PastPapers : AppCompatActivity() {
    lateinit var binding: ActivityPastPapersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // var checker = 0

        binding = DataBindingUtil.setContentView(this, R.layout.activity_past_papers)


        binding.backPastPaperCv.setOnClickListener {
            onBackPressed()
        }

        val mDialogView =
            LayoutInflater.from(this@PastPapers).inflate(R.layout.coming_soon_past_papers, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        mBuilder.setOnDismissListener {
            (mDialogView.parent as ViewGroup).removeView(mDialogView)
        }

        binding.physicsMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "physics")
            startActivity(intent)
//            when (checker) {
//                0 -> {
//
//                }
//                1 -> {
//
//                    callAlert()
//
//                }
//                2 -> {
//                    callAlert()
//
//                }
//            }


        }
        binding.englishCv.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "english")
            startActivity(intent)
//            when (checker) {
//                0 -> {
//                    startActivity(intent)
//                }
//                1 -> {
//                    callAlert()
//
////                    mAlertDialog.dismiss()
////                    mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    // )
//                }
//                2 -> {
//                    callAlert()
//
////                    mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    //  )
//                }
//            }


        }
        binding.bioPaperCv.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "bio")
            startActivity(intent)
//            when (checker) {
//                0 -> {
//                    startActivity(intent)
//                }
//                1 -> {
//                    callAlert()
//
////                    mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    //)
//                }
//                2 -> {
//                    callAlert()
//
////                    mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    //)
//                }
//            }
        }
        binding.chemistryMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "chemistry")
            startActivity(intent)
//            when (checker) {
//                0 -> {
//                    startActivity(intent)
//                }
//                1 -> {
//                    callAlert()
//
////                    mAlertDialog.window!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    // )
//                }
//                2 -> {
//                    callAlert()
//
////                    mAlertDialog.window!!.setBackgroundDrawableResource(
////                        R.drawable.feedbck_alertdialog_rounnd
//                    //)
//                }
//            }
        }
        binding.urduMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "urdu")
            startActivity(intent)
        }
        binding.islamyatMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "islamyat")
            startActivity(intent)
        }
        binding.mathMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "maths")
            startActivity(intent)
        }
        binding.computerMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "computer")
            startActivity(intent)
        }
        binding.pakStudiesMain.setOnClickListener {
            val intent = Intent(this, PastPaperSubjectSelected::class.java)
            intent.putExtra("subject", "pak_study")
            startActivity(intent)
        }


        binding.quizCustomeGameCV.setOnClickListener {
            val intent = Intent(this, Mocktest::class.java)
            startActivity(intent)
        }

//        binding.lahoreBtn.setOnClickListener {
//            checker = 0
//            binding.lahoreBtn.setBackgroundResource(R.drawable.select_board_orange)
//            binding.lahoreBtn.setTextColor(Color.parseColor("#ffffff"))
//
//            binding.federalBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.federalBtn.setTextColor(Color.parseColor("#25375F"))
//
//            binding.sindhBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.sindhBtn.setTextColor(Color.parseColor("#25375F"))
//
//
//        }
//        binding.federalBtn.setOnClickListener {
//            checker = 1
//            binding.federalBtn.setBackgroundResource(R.drawable.select_board_orange)
//            binding.federalBtn.setTextColor(Color.parseColor("#ffffff"))
//
//            binding.lahoreBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.lahoreBtn.setTextColor(Color.parseColor("#25375F"))
//
//            binding.sindhBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.sindhBtn.setTextColor(Color.parseColor("#25375F"))
//
//
//        }
//        binding.sindhBtn.setOnClickListener {
//            checker = 2
//            binding.sindhBtn.setBackgroundResource(R.drawable.select_board_orange)
//            binding.sindhBtn.setTextColor(Color.parseColor("#ffffff"))
//
//            binding.federalBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.federalBtn.setTextColor(Color.parseColor("#25375F"))
//
//            binding.lahoreBtn.setBackgroundResource(R.drawable.select_board_white)
//            binding.lahoreBtn.setTextColor(Color.parseColor("#25375F"))
//
//
//        }
    }

    private fun callAlert() {
        val mDialogView =
            LayoutInflater.from(this@PastPapers).inflate(R.layout.coming_soon_past_papers, null)

        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        mBuilder.setOnDismissListener {
            (mDialogView.parent as ViewGroup).removeView(mDialogView)
        }
        val mAlertDialog = mBuilder.create()

        mDialogView.crossIv.setOnClickListener {

//            val mAlertDialog = mBuilder.show()
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            mAlertDialog.dismiss()

        }

       // val mAlertDialog = mBuilder.show()
        mAlertDialog.show()
        mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


    }
}