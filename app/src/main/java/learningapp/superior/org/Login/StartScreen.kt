package learningapp.superior.org.Login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.youtube.player.internal.e
import com.google.firebase.auth.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_bottom.*
import kotlinx.android.synthetic.main.activity_bottom.view.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_start_screen1.*
import learningapp.superior.org.Home.Home
import learningapp.superior.org.R
import learningapp.superior.org.SideMenu.Profile
import learningapp.superior.org.Utils.FireStoreUtils


class StartScreen : AppCompatActivity() {
    var a: Int = 1
    lateinit var auth: FirebaseAuth
    var callbackManager: CallbackManager = CallbackManager.Factory.create()
    lateinit var credential: AuthCredential
    val currentUser = FirebaseAuth.getInstance().currentUser
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions

    private val RC_SIGN_IN = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        setContentView(R.layout.activity_start_screen1)

        configureGoogleSignIn()
//
        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
        val bottomSheetDialog = BottomSheetDialog(this,
            R.style.BottomSheetDialog
        )



        bottomSheetDialog.setContentView(view)



        auth = FirebaseAuth.getInstance()


        //fb code1
        FacebookSdk.sdkInitialize(getApplicationContext())


        started_btn.setOnClickListener {

            if(bottomSheetDialog.isShowing){
                Log.d(" bottomSheetDialog  "," is showing")
            }else{ bottomSheetDialog.show()}

            //bottomSheetDialog.show()

        }

//        view.ccp.setOnCountryChangeListener { selectedCountry ->
////            Toast.makeText(
////                this,
////                "Updated code is " + view.ccp.selectedCountryCodeWithPlus,
////                Toast.LENGTH_SHORT
////            ).show()
//
//
//        }


        view.sen_otp_btn.setOnClickListener {


            intent = Intent(this, MobileNumber::class.java)
           startActivity(intent)

//            val mobileN: String = bottomSheetDialog.number.text.toString()
//
//
//            if (mobileN.isEmpty()) {
//                Toast.makeText(
//                    applicationContext,
//                    "Please enter valid mobile number",
//                    Toast.LENGTH_SHORT
//                ).show()
//                // bottomSheetDialog.number.setError("Please enter valid 10 digit mobile number")
//                // bottomSheetDialog.number.requestFocus()
//
//            } else {
//
//
//                bottomSheetDialog.dismiss()
//
//                intent = Intent(this, DialerOtp::class.java)
//                intent.putExtra("mobile", mobileN)
//                intent.putExtra("countryCode", view.ccp.selectedCountryCodeWithPlus.toString())
//                startActivity(intent)
//
//
//            }


        }


        view.gmailCV.setOnClickListener {
            view.progressBarSS.visibility=View.VISIBLE
            signInWithGoogle()
        }


        view.fb_login_button_Cv.setOnClickListener {
            view.login_button.performClick()
        }
        view.login_button.setOnClickListener {


            bottomSheetDialog.dismiss()
            //bottomSheetDialog.cancel()
            Log.d("fb button pressed", "fb button startSCreen")

            signIn()

        }


    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun signInWithGoogle() {

        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onDestroy() {
        super.onDestroy()

        val bottomSheetDialog = BottomSheetDialog(this,
            R.style.BottomSheetDialog
        )
        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.dismiss()
    }

    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
//        super.onBackPressed()
//        val dialog = BottomSheetDialog(this)
//        dialog.dismiss()
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 10000)
    }

    private fun updateUI(user: FirebaseUser?) {


//        Log.d("update ui fun", "function of update ui")

        FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener { void ->
            //            var user = FirebaseAuth.getInstance().currentUser


            if (currentUser != null) {
//                Log.d("facebook user not null", "updateUI")
//                Toast.makeText(this,"Inside of if",Toast.LENGTH_SHORT).show()
//                finish()
                val bottomSheetDialog = BottomSheetDialog(this,
                    R.style.BottomSheetDialog
                )
                val view = layoutInflater.inflate(R.layout.activity_bottom, null)
                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.cancel()

                startActivity(Intent(this, Home::class.java))
//                dialog.show()
            } else {
                Toast.makeText(this, "User is Null!", Toast.LENGTH_SHORT).show()
//                Log.d("else", "else updateUI")
            }
        }

    }


    private fun signIn() {
        Log.d("registercallback", "registercallabck ss1")

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("registercallback", "registercallabck onsuccess")
                    handleFacebookAccessToken(result?.accessToken)


                }

                override fun onCancel() {

                    Log.d("registercallback", "registercallabck oncancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d("fb", error.toString())

                    Log.d("registercallback", "registercallabck onerror")
                }

            })


//        Log.d("registercallback after","registercallabck after fun")

//        handleFacebookAccessToken(accessToken: AccessToken?)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        val view = layoutInflater.inflate(R.layout.activity_bottom, null)
        val bottomSheetDialog = BottomSheetDialog(this,
            R.style.BottomSheetDialog
        )
        bottomSheetDialog.setContentView(view)


        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                    view.progressBarSS.visibility=View.GONE
                    Toast.makeText(this, "Google sign in successfully done!", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                view.progressBarSS.visibility=View.GONE
                Toast.makeText(this, "Google sign in failed due to "+e, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val isNew: Boolean = it.result.additionalUserInfo?.isNewUser == true
                Log.d("MyTAGusers", "onComplete: " + if (isNew) "new user" else "old user")
                if(isNew)
                checkProfile(acct)
                else {
                    completeProfileAndMove("google")
                   // startActivity(Intent(this@StartScreen, Home::class.java))
                }
            } else {
                Log.d("google sign in failed: ","failed: " +credential.toString() )
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun completeProfileAndMove(login: String) {
        val ref =
            FirebaseDatabase.getInstance().getReference("NewUsers").child(FireStoreUtils().UID)

var name=""
var mob=""

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    Log.d("loginFromThe",login)


                    if (p0.child("mobileNumber").value.toString() != "null" && p0.child("userName").value.toString() != "null") {
                    mob=p0.child("mobileNumber").value.toString()
                        name=p0.child("userName").value.toString()
                    // toastmsg()

                        //checker = 1

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
                        SPforNumbereditor.putString("number", mob)
                        SPforNumbereditor.apply()


                        SPforNameeditor.putString("name", name)
                        SPforNameeditor.apply()

                        startActivity(Intent(this@StartScreen, Home::class.java))
                    }

                }
            }


        })

    }
    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        //Get Credentials

        Log.d("handleFacebooktoken", "handlefacebooktoken")
        credential = FacebookAuthProvider.getCredential(accessToken!!.token)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@StartScreen)
            { task ->
                if (task.isSuccessful) {
                    //Sign in success, update UI with the signed-in user's information


                    val bottomSheetDialog = BottomSheetDialog(this,
                        R.style.BottomSheetDialog
                    )
                    val view = layoutInflater.inflate(R.layout.activity_bottom, null)
                    bottomSheetDialog.setContentView(view)
                    bottomSheetDialog.dismiss()
//                    dialog.show()
                    val pd = ProgressDialog(this)
                    pd.setMessage("Signing in..")
                    pd.show()
                    //moveMainPage(task.result?.user)
//comehere




                    pd.cancel()


                    Toast.makeText(this,"Signing in through Facebook",Toast.LENGTH_LONG).show()
                    val isNew: Boolean = task.result.additionalUserInfo?.isNewUser == true
                    Log.d("MyTAGusers", "onComplete: " + if (isNew) "new user" else "old user")

                    if(isNew)
                        checkProfileFB(task.result?.user)
                    else {
                        completeProfileAndMove("fb")
                       // startActivity(Intent(this@StartScreen, Home::class.java))
                    }
//                    if (currentUsers != null) {
//                      Log.d("fb button"," currentUser!= null")


// updateUI(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()


                    //updateUI(null)
                }

//                }
            }

    }

    private fun checkProfileFB(user: FirebaseUser?) {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)

        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
           val intent=Intent(this@StartScreen, Profile::class.java)
          intent.putExtra("name",user?.displayName)
            intent.putExtra("loginWith", "fb")

            startActivity(intent)

        }
        else {
            startActivity(Intent(this@StartScreen, Home::class.java))
        }
    }


    private fun checkProfile(acct: GoogleSignInAccount) {
        val sharedPreference =  getSharedPreferences("profileBuilder", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//        editor.putString("profileComplete","yes")
//        editor.commit()
        val news= sharedPreference.getString("profileComplete","null")

        if (news=="null"){
            val intent=Intent(this@StartScreen, Profile::class.java)
            intent.putExtra("name", acct.displayName)
            intent.putExtra("acct", acct)
            intent.putExtra("loginWith", "google")

            startActivity(intent)
        }
        else {
            startActivity(Intent(this@StartScreen, Home::class.java))
        }

    }


    private fun moveMainPage(user: FirebaseUser?) {
        Log.d("moveMainPage fun", "fun")

        if (user != null) {

            val bottomSheetDialog = BottomSheetDialog(this,
                R.style.BottomSheetDialog
            )
            val view = layoutInflater.inflate(R.layout.activity_bottom, null)
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.dismiss()

            val intent = Intent(this, Home::class.java)
            intent.putExtra("name", 2)

            startActivity(intent)
            finish()
        }
    }


}