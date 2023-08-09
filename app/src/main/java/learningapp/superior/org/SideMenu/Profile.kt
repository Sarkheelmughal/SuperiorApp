package learningapp.superior.org.SideMenu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import kotlinx.android.synthetic.main.activity_dialer_otp.*
import kotlinx.android.synthetic.main.activity_mobile_number.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.number
import kotlinx.android.synthetic.main.profile_v_code.*
import kotlinx.android.synthetic.main.settings.*
import learningapp.superior.org.Home.Home
import learningapp.superior.org.R
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit


class Profile : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    val currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid


    lateinit var auth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var verificationIde = ""
    private val KEY_VERIFICATION_ID = "key_verification_id"

    val currentUser = FirebaseAuth.getInstance().currentUser
    var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val loginWith = intent.getStringExtra("loginWith")
        profile_image.setOnClickListener {
            launchGallery()

        }

        back_profile_CV.setOnClickListener {
            finish()
            onBackPressed()

        }

        auth = FirebaseAuth.getInstance()
        auth.useAppLanguage()
        fillProfile()
        spinnersFun()
        // ParentalCode()
        val userr = FirebaseAuth.getInstance().currentUser

        if (userr != null) {
            if (userr.photoUrl != null) {

                profile_image.setBackgroundResource(0)

//                    Picasso.get(this).load(userr.photoUrl).into(imageView)
                Glide.with(this).load(userr.photoUrl).into(profile_image)

            }
        }

//        if (number.text.isEmpty() || nameET.text.isEmpty()) {
//            saveProfile.isClickable = false
//            saveProfile.isEnabled = false
//
//
//        } else {
//            saveProfile.isClickable = true
//            saveProfile.isEnabled = true
//        }


        //---------------shared files start------------------------------
        // builder inform profile is complete
        val sharedPreference = getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()


        // to store number
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumbereditor = SPforNumber.edit()


        // to store name

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameeditor = SPforName.edit()

        // to store grade

        val SPforGrade = getSharedPreferences("gradeStorage", Context.MODE_PRIVATE)
        val SPforGradeeditor = SPforGrade.edit()


        // to store gender

        val SPforGender = getSharedPreferences("genderStorage", Context.MODE_PRIVATE)
        val SPforGendereditor = SPforGender.edit()
        //---------------shared files end

        ParentalCode()


        dialog = BottomSheetDialog(this@Profile)

        dialog.setContentView(R.layout.profile_v_code)

        dialog.sentProfile.setOnClickListener {

            if (dialog.pinET.text.toString().length <= 5) {
                Toast.makeText(this, "Enter OTP code!", Toast.LENGTH_SHORT).show()
            }
            //else
             verifyVerificationCode(dialog.pinET.text.toString())
        }

        saveProfile.setOnClickListener {
            val ccp = findViewById<CountryCodePicker>(R.id.ccp)
            if (number.text.isEmpty() || nameET.text.isEmpty()) {
                Toast.makeText(this, "Please add Number & Name to proceed!", Toast.LENGTH_SHORT)
                    .show()


            } else {
//                val dialog = BottomSheetDialog(this@Profile)
//                dialog.setContentView(R.layout.profile_v_code)
//
//                dialog.show()
//
                val number = findViewById<EditText>(R.id.number)
                ccp.setOnCountryChangeListener {
////            Toast.makeText(
////                this,
////                "Updated code is " + view.ccp.selectedCountryCodeWithPlus,
////                Toast.LENGTH_SHORT
////            ).show()
//
//
                }
                Log.d("countryCodeP", number.toString())
                Log.d("countryCodePhoneP", ccp.toString())

                    saveDataInSharePref()
//                if(numberVar==number.text.toString()){
//                   // Toast.makeText(this, "number not changed!", Toast.LENGTH_SHORT).show()
//                }else {
//                    dialog.show()
//
//                    sendVerificationCode(
//                        number.text.toString(),
//                        ccp.selectedCountryCodeWithPlus.toString()
//                    )
//                }

//                editor.putString("profileComplete", "yes")
//                editor.apply()
//
//                SPforNumbereditor.putString("number", number.text.toString())
//                SPforNumbereditor.apply()
//
//
//                SPforNameeditor.putString("name", nameET.text.toString())
//                SPforNameeditor.apply()
//
//
//                SPforGradeeditor.putString("grade", gradeSpinner.selectedItemPosition.toString())
//                SPforGradeeditor.apply()
//
//
//                SPforGendereditor.putString("gender", genderSpinner.selectedItemPosition.toString())
//                SPforGendereditor.apply()
//
//                saveDataToFireBase()
//
//
//                if (loginWith == "google" || loginWith == "fb" || loginWith == "mobile") {
//                    startActivity(Intent(this@Profile, Home::class.java))
//
//                } else {
//
//                    Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show()
//                    if (checker == 0)
//                        onBackPressed()
//                }

            }

        }
    }

    private fun saveDataToFireBase() {
        val sharedPreferencesSC: SharedPreferences =
            this.getSharedPreferences("schoolCode", Context.MODE_PRIVATE)
        //val editorSc: SharedPreferences.Editor = sharedPreferencesSC.edit()
        val getSchoolCode = sharedPreferencesSC.getString("schoolCode", "null")
//-----------------
        var gender = ""
        val SPforGender = getSharedPreferences("genderStorage", Context.MODE_PRIVATE)
        val SPforGenderSaved = SPforGender.getString("gender", "null")

        if (SPforGenderSaved != "null") {
            if (SPforGenderSaved != null) {
                gender = when {
                    SPforGenderSaved.toInt() == 0 -> {
                        "male"
                    }
                    SPforGenderSaved.toInt() == 1 -> {
                        "female"
                    }
                    else -> "Other"
                }
            }
        }
        //________________________________
        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("NewUsers")
        val userMap = HashMap<Any, Any>()

        userMap["mobileNumber"] = number.text.toString()
        userMap["userName"] = nameET.text.toString()
        userMap["parentalCode"] = parentalCodeTv.text.toString()
        userMap["gender"] = gender
        userMap["S_code"] = getSchoolCode.toString()


        refOfPaid.child(currentUserDataId).setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {


                } else {
                }
            }


    }

    private fun ParentalCode() {

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameSaved = SPforName.getString("name", "null")


        val ref =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {

                    val s = p0.child("Variable").value.toString()
                    profilePB.visibility = View.GONE
                    parentalCodeTv.setText(s)
                    Log.d("testing setting", "testing" + s)
//                    var codee=parent_code.text.toString()
                    val namePaste: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("StudentsDataForParent")
                            .child(s).child("Name")

                    val userMap1 = HashMap<Any, Any>()

                    userMap1["Name"] = SPforNameSaved.toString()

                    namePaste.setValue(userMap1)

                    val comeFrom = intent.getStringExtra("comeFrom")
                    Log.d("profileLogis", comeFrom.toString())
                    if (comeFrom != "home") {
                        completeProfileAndMove()
                    }
                } else {
                    getRandomString(6)

                    val userDataRef: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("ParentalCode")

                    val userMap = HashMap<Any, Any>()
                    userMap["Variable"] = parentalCodeTv.text.toString()
                    userMap["Name"] = SPforNameSaved.toString()

                    userDataRef.child(currentUserDataId).setValue(userMap)

                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                profilePB.visibility = View.GONE

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

    private fun completeProfileAndMove() {
        val ref =
            FirebaseDatabase.getInstance().getReference("NewUsers").child(currentUserDataId)



        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {


                    if (p0.child("mobileNumber").value.toString() != "null" && p0.child("userName").value.toString() != "null") {
                        number.setText(p0.child("mobileNumber").value.toString())
                        nameET.setText(p0.child("userName").value.toString())
                        //toastmsg()

                        checker = 1
                        saveProfile.performClick()
                    }

                }
            }


        })

    }

    var checker = 0
    private fun toastmsg() {
        Toast.makeText(this, "Profile auto completed!", Toast.LENGTH_SHORT).show()

    }

    fun getRandomString(length: Int): String {
        val allowedChars = "abcdefghijklmnopqrstuvwxyz0123456789"


        parentalCodeTv.setText((1..length)
            .map { allowedChars.random() }
            .joinToString(""))

        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun spinnersFun() {
        val Grade = resources.getStringArray(R.array.Grade)

        // access the spinner
        val spinner = findViewById<Spinner>(R.id.gradeSpinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, Grade
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
//                    Toast.makeText(this@Profile,
//                        getString(R.string.selected_item) + " " +
//                                "" + Grade[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        //--------------------------------------

        val Gender = resources.getStringArray(R.array.Gender)

        // access the spinner
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)
        if (genderSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, Gender
            )
            genderSpinner.adapter = adapter

            genderSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
//                    Toast.makeText(this@Profile,
//                        getString(R.string.selected_item) + " " +
//                                "" + Gender[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        val SPforGender = getSharedPreferences("genderStorage", Context.MODE_PRIVATE)
        val SPforGenderSaved = SPforGender.getString("gender", "null")
        if (SPforGenderSaved != "null") {
            if (SPforGenderSaved != null) {
                genderSpinner.setSelection(SPforGenderSaved.toInt(), true)
//                genderSpinner.setSelection(SPforGenderSaved.toInt())
            }
        }
//        genderSpinner.setSelection(2,true)

    }
var numberVar=""
    private fun fillProfile() {
        val currentUsers = FirebaseAuth.getInstance()?.currentUser
//        val Grade = resources.getStringArray(R.array.Grade)
//
//        val adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.Gender,
//            android.R.layout.simple_spinner_dropdown_item
//        )
//
//        genderSpinner.setAdapter(adapter)
//        genderSpinner.setSelection(1)

        val loginWith = intent.getStringExtra("loginWith")
        val name = intent.getStringExtra("name")
        val phoneNo = intent.getStringExtra("phoneNo")

        //---------------shared files start------------------------------
//246460
        // to store number
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumberSaved = SPforNumber.getString("number", "null")


        // to store name

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameSaved = SPforName.getString("name", "null")

        // to store grade

        val SPforGrade = getSharedPreferences("gradeStorage", Context.MODE_PRIVATE)
        val SPforGradeSaved = SPforGrade.getString("grade", "null")


        // to store gender

        //---------------shared files end

        when (loginWith) {
            "google" -> {
                nameET.setText(name)
            }
            "fb" -> {
                nameET.setText(name)

            }
            "mobile" -> {
                number.setText(phoneNo)
            }


        }

        if (SPforNumberSaved != "null") {
            number.setText(SPforNumberSaved)
            numberVar=SPforNumberSaved.toString()
        }
        if (SPforNameSaved != "null") {
            nameET.setText(SPforNameSaved)
        }
         if (SPforGradeSaved != "null") {
            if (SPforGradeSaved != null) {
                gradeSpinner.setSelection(SPforGradeSaved.toInt())
            }
        }
//

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
// PICK_IMAGE_REQUEST
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                profile_image.setImageBitmap(bitmap)
                handleimg(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun handleimg(bitmap: Bitmap?) {

        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseStorage.getInstance().reference
            .child("profileImages")
            .child(uid + ".jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener { p0 ->
                getDownloadUrl(ref)
            }
            .addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()

            }


    }

    private fun getDownloadUrl(reference: StorageReference) {
        reference.downloadUrl.addOnSuccessListener { uri: Uri ->
            setUserProfileUrl(uri)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user?.updateProfile(request)
            ?.addOnSuccessListener {

                Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT)
                    .show()

            }
            ?.addOnFailureListener { p0 ->
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
            }
    }


    private fun sendVerificationCode(phoneNo: String, countryCode: String) {
        verificationCallbacks()
        // val phoneNo=findViewById<EditText>(R.id.number)

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            countryCode + phoneNo, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            // TaskExecutors.MAIN_THREAD,
            mCallbacks
        ) // OnVerificationStateChangedCallbacks
    }

    lateinit var dialog: BottomSheetDialog

    private fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //Getting the code sent by SMS
                val cod: String? = credential.smsCode

                Log.d("codeTestingVCBOutSide", cod.toString())



                if (cod != null) {
                    dialog.pinET.setText(cod)
                    //verifying the code
                    Log.d("codeTestingVCB", cod)
                    verifyVerificationCode(cod)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_SHORT).show()
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(applicationContext, "Invalid request", Toast.LENGTH_SHORT).show()
                    // ...
                } else if (p0 is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(
                        applicationContext,
                        "SMS quota for the project has been exceeded",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onCodeSent(
                verification: String,
                p1: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verification, p1)
                //storing the verification id that is sent to the user
                verificationIde = verification
                mResendToken = p1
                Toast.makeText(
                    applicationContext,
                    "OTP has been sent on your phone!",
                    Toast.LENGTH_SHORT
                ).show()
                dialog.show()
                Log.d("codeTestingOCS", verification)
                Log.d("codeTestingOCS", p1.toString())


            }
        }
    }

    fun verifyVerificationCode(code: String) {
        //creating the credential
        if (verificationIde != "" || verificationIde.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationIde, code)
            //signing the user
            Log.d("codeTestingVVC", credential.toString())
            signInWithPhoneAuthCredential(credential)
        } else Toast.makeText(
            this,
            "Try Again! Don't close app after getting OTP!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Your Phone number has been registered successfully!",
                        Toast.LENGTH_SHORT
                    ).show();
                    //verification successful we will start the profile activity


                    dialog.dismiss()
                    saveDataInSharePref()


//                    val intent = Intent(this, Home::class.java)
//                    intent.putExtra("name",1)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
//                    Toast.makeText(this, "Wrong Code!", Toast.LENGTH_SHORT).show()
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(
                            this,
                            "verification code entered was invalid!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun saveDataInSharePref() {
        //---------------shared files start------------------------------
        // builder inform profile is complete
        val sharedPreference = getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()


        // to store number
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumbereditor = SPforNumber.edit()


        // to store name

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameeditor = SPforName.edit()

        // to store grade

        val SPforGrade = getSharedPreferences("gradeStorage", Context.MODE_PRIVATE)
        val SPforGradeeditor = SPforGrade.edit()


        // to store gender

        val SPforGender = getSharedPreferences("genderStorage", Context.MODE_PRIVATE)
        val SPforGendereditor = SPforGender.edit()
        //---------------shared files end

        editor.putString("profileComplete", "yes")
        editor.apply()

        SPforNumbereditor.putString("number", number.text.toString())
        SPforNumbereditor.apply()


        SPforNameeditor.putString("name", nameET.text.toString())
        SPforNameeditor.apply()


        SPforGradeeditor.putString("grade", gradeSpinner.selectedItemPosition.toString())
        SPforGradeeditor.apply()


        SPforGendereditor.putString("gender", genderSpinner.selectedItemPosition.toString())
        SPforGendereditor.apply()

        saveDataToFireBase()
        val loginWith = intent.getStringExtra("loginWith")


        if (loginWith == "google" || loginWith == "fb" || loginWith == "mobile") {
            startActivity(Intent(this@Profile, Home::class.java))

        } else {

            Toast.makeText(this, "Changes Saved!", Toast.LENGTH_SHORT).show()
            if (checker == 0)
                onBackPressed()
        }
    }

}