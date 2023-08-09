package learningapp.superior.org.Home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.payment_options_new.*
import learningapp.superior.org.R


class PaymentOption : AppCompatActivity() {
    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_payment_option)
//
//        val price=intent.getStringExtra("price")
//        textView26.text="1.Transfer $price Rs"
//        val uid = FirebaseAuth.getInstance().currentUser!!.uid
//        uidTV.setText(uid)
//        uidCopyIV.setOnClickListener {
//            val clipboard: ClipboardManager =
//                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText("uid", uid.toString())
//            clipboard.setPrimaryClip(clip)
//
//            Toast.makeText(this, "UID copied", Toast.LENGTH_SHORT).show()
//        }
//
//        val num= "+923365106602"
//
//        whatsappIV.setOnClickListener {
//            val url = "https://api.whatsapp.com/send?phone=$num"
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            startActivity(i)
//        }
//        backFromPayment.setOnClickListener { onBackPressed() }
//    }
    var isBig = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_options_new)
        backFromPM.setOnClickListener { onBackPressed() }


        val num= "+923365106602"
//
        whatsappep.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$num"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        whatsappJazz.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$num"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        imageView13.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$num"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        val heigh =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65f, resources.displayMetrics)
        val heigh1 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 430f, resources.displayMetrics)
        val heigh2 =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 330f, resources.displayMetrics)

        drop_down_info.setOnClickListener {

            list_up_info_jazz.callOnClick()
            list_up_info_ep.callOnClick()

            bank_CV.layoutParams.height = heigh1.toInt()
            list_up_info.visibility = View.VISIBLE
            //textView432.visibility= View.VISIBLE
            drop_down_info.visibility = View.GONE
        }

        list_up_info.setOnClickListener {
            bank_CV.layoutParams.height = heigh.toInt()
            drop_down_info.visibility = View.VISIBLE

            list_up_info.visibility = View.GONE

        }


        drop_down_info_jazz.setOnClickListener {
            list_up_info_jazz.visibility = View.VISIBLE

            drop_down_info_jazz.visibility = View.GONE

            list_up_info.callOnClick()
            list_up_info_ep.callOnClick()

            jazzcash_CV.layoutParams.height = heigh2.toInt()
        }

        list_up_info_jazz.setOnClickListener {
            jazzcash_CV.layoutParams.height = heigh.toInt()
            drop_down_info_jazz.visibility = View.VISIBLE

            list_up_info_jazz.visibility = View.GONE
        }


        drop_down_info_ep.setOnClickListener {

            list_up_info_jazz.callOnClick()
            list_up_info.callOnClick()
            list_up_info_ep.visibility = View.VISIBLE

            drop_down_info_ep.visibility = View.GONE
            easypaisa_CV.layoutParams.height = heigh2.toInt()
        }

        list_up_info_ep.setOnClickListener {
            easypaisa_CV.layoutParams.height = heigh.toInt()
            drop_down_info_ep.visibility = View.VISIBLE

            list_up_info_ep.visibility = View.GONE
        }

    }


}