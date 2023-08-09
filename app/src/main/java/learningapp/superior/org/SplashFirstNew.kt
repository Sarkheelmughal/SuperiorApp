package learningapp.superior.org

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashFirstNew : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashFirstNew, SplashScreen::class.java))

        // close splash activity

        // close splash activity
        finish()
    }
}