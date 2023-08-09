package learningapp.superior.org

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.maxcom.util.Log
import learningapp.superior.org.Home.Home
import learningapp.superior.org.Login.StartScreen
import learningapp.superior.org.SideMenu.Profile


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
//        enablePersistence()

        FirebaseApp.initializeApp(this)
        val currentUsers = FirebaseAuth.getInstance().currentUser
        Handler().postDelayed({
            //start activity
            if(currentUsers != null){
//                startActivity(Intent(this@SplashScreen,
//                    Home::class.java))

                checkProfile()
            }
            else{

                startActivity(Intent(this@SplashScreen,
                    StartScreen::class.java))
                finish()
            }
            //end activity

        },1800)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val database = FirebaseDatabase.getInstance()
    }
    private fun checkProfile() {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//        editor.putString("profileComplete","yes")
//        editor.commit()
        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
            val intent=Intent(this@SplashScreen, Profile::class.java)


            startActivity(intent)
            finish()

        }
        else {
            startActivity(Intent(this@SplashScreen, Home::class.java))

            finish()

        }

    }


}
private fun enablePersistence() {
    try{
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
    catch (e : Exception){
        Log.d("enablePersistence","app crash $e")
    }
    return
}