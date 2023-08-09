package learningapp.superior.org.Home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import learningapp.superior.org.R
import learningapp.superior.org.databinding.ActivityGenerateCodeBinding

class GenerateCode : AppCompatActivity() {
    lateinit var binding: ActivityGenerateCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_generate_code)

        binding.generateCode.setOnClickListener {



           genRandom()

        }

    }

    private fun genRandom() {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        var listofVehicleNames = arrayListOf("")

        for(i in 1..20) {

            val randomString = (1..6)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");

//            val concat ="IM"+randomString
            val concat ="TAP"+randomString
            Log.d("randomString: ", concat);
            listofVehicleNames.add(concat)
            if(i ==20 ){
                Log.d("randomString list ", listofVehicleNames.toString())

            }
           uploadCode(concat)
        }

    }

    fun uploadCode(randomString: String) {
        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("SchoolCodes").child(randomString)
        val userMap = HashMap<Any, Any>()

        userMap["switch"] = "on"
        userMap["total"] = "1"

        val userMaps = HashMap<Any, Any>()

        userMaps["used"] = "0"

        refOfPaid.child("limit").setValue(userMap)
        refOfPaid.child("usedLimit").setValue(userMaps)
    }
}