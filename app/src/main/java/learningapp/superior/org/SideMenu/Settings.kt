package learningapp.superior.org.SideMenu

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.settings.*
import learningapp.superior.org.R

class Settings : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val name=intent.getStringExtra("Name")

        Log.d("testofnamenew",name.toString())

        user_name.setText(name)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val phone_number: String? = FirebaseAuth.getInstance().currentUser?.phoneNumber

        if (firebaseUser != null) {
            for (userInfo in firebaseUser.providerData) {
                if (userInfo.providerId == "facebook.com") {
                    Log.d("TAG", "User is signed in with Facebook")
                    phone.setText(getString(R.string.LoginThroughFB))
                }
                else{
                    phone.setText(phone_number.toString())
                }
            }
        }
//        if(phone_number!!.isEmpty()){
//            phone.setText(getString(R.string.LoginThroughFB))
//        }
//        else{
//            phone.setText(phone_number.toString())
//        }
        settingBack.setOnClickListener {
            onBackPressed()
        }
        val currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid

        lateinit var ref: DatabaseReference
        ref = FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    val s = p0.child("Variable").value.toString()

                    parent_code.setText(s)
                    Log.d("testing setting", "testing" + s)
//                    var codee=parent_code.text.toString()
                    val namePaste: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("StudentsDataForParent").child(s).child("Name")

                    val userMap1 = HashMap<Any, Any>()

                    userMap1["Name"] = name.toString()

                    namePaste.setValue(userMap1)
                } else {
                    getRandomString(6)

                    val userDataRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("ParentalCode")

                    val userMap = HashMap<Any, Any>()
                    userMap["Variable"] = parent_code.text.toString()
                    userMap["Name"] = name.toString()

                    userDataRef.child(currentUserDataId).setValue(userMap)

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                            } else {

                                //  FirebaseAuth.getInstance().signOut()
                            }
                        }
//                    ---------------------------------------------------

                }
            }

        })



    }


    fun getRandomString(length: Int): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789"


        parent_code.setText((1..length)
            .map { allowedChars.random() }
            .joinToString(""))

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}