package learningapp.superior.org.Home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_payment_plan.*
import learningapp.superior.org.R

class SelectPaymentPlan : AppCompatActivity() {
    lateinit var price: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_payment_plan)

        backFromSPP.setOnClickListener { onBackPressed() }
        PlainText.setOnClickListener { onBackPressed() }

        buyNowPackage.setOnClickListener {
            if (this::price.isInitialized) {
                val move = Intent(this, PaymentOption::class.java)
                move.putExtra("price", price)
                startActivity(move)
            }
            else{
            Toast.makeText(this, "Please select a package before!", Toast.LENGTH_LONG)

        }
        }

        twoHCV.setOnClickListener {
            buyNowDisable.visibility= View.GONE
            buyNowPackage.visibility= View.VISIBLE
            price = "200"
            twoHCV.setCardBackgroundColor(Color.rgb(202, 209, 240))
            fiveHCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
            thousandCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
        }
        fiveHCV.setOnClickListener {
            price = "500"
            buyNowDisable.visibility= View.GONE
            buyNowPackage.visibility= View.VISIBLE
            fiveHCV.setCardBackgroundColor(Color.rgb(202, 209, 240))
            twoHCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
            thousandCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
        }
        thousandCV.setOnClickListener {
            price = "1000"
            buyNowDisable.visibility= View.GONE
            buyNowPackage.visibility= View.VISIBLE
            thousandCV.setCardBackgroundColor(Color.rgb(202, 209, 240))
            fiveHCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
            twoHCV.setCardBackgroundColor(Color.rgb(246, 234, 218))
        }
    }
}