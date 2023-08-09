package learningapp.superior.org.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.CallbackManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import learningapp.superior.org.R

class Bottom : AppCompatActivity() {
    //    val codePicker=findViewById<CountryCodePicker>(R.id.ccp)
//    val phoneNumber=findViewById<EditText>(R.id.number)
    var user = FirebaseAuth.getInstance().currentUser
    lateinit var auth: FirebaseAuth
    val currentUsers = FirebaseAuth.getInstance().currentUser
    lateinit var credential: AuthCredential
    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom)


    }
}

