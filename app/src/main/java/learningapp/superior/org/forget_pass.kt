package learningapp.superior.org

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget_pass.*
import learningapp.superior.org.Login.Bottom

class forget_pass : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        auth = FirebaseAuth.getInstance()


        forget_btn.setOnClickListener {
            val email=mail2.text.toString()
            if(email.isEmpty()){
                mail2.error="Error"

            }
            else{
                forgotpasswrod(email)
            }

        }


    }

    private fun forgotpasswrod(email: String)
    {
    auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
        if(task.isComplete){
            val loginIntent= Intent(this, Bottom::class.java)
            startActivity(loginIntent)
            Toast.makeText(this,"Please check your email and Reset password & Login!",Toast.LENGTH_LONG).show()

        }
    }
    }
}
