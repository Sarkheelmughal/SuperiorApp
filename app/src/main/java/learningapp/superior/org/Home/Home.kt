package learningapp.superior.org.Home


import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import com.sanojpunchihewa.updatemanager.UpdateManager
import com.sanojpunchihewa.updatemanager.UpdateManager.UpdateInfoListener
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.dislike_feedack.view.*
import kotlinx.android.synthetic.main.feedback_layout.view.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.home.profile_image
import kotlinx.android.synthetic.main.menu_left_drawer.*
import kotlinx.android.synthetic.main.streak_text.view.*
import learningapp.superior.org.EntryTestInter.Mdcat
import learningapp.superior.org.Game.SubjectSelection
import learningapp.superior.org.Login.StartScreen
import learningapp.superior.org.R
import learningapp.superior.org.Reivision.Revision
import learningapp.superior.org.SideMenu.LeaderBoard
import learningapp.superior.org.SideMenu.Learning_analysis
import learningapp.superior.org.SideMenu.Profile
import learningapp.superior.org.SideMenu.Report
import learningapp.superior.org.Sliders.SliderContainer
import learningapp.superior.org.Utils.FireStoreUtils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class Home : AppCompatActivity() ,LifecycleObserver {
    private lateinit var auth: FirebaseAuth
    var stoptimer = object : CountDownTimer(0, 0) {
        override fun onFinish() {}
        override fun onTick(p0: Long) {}
    }

    var currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid
    var minute = 0
    lateinit var chapkey: String
    var mcqssubject = ""
    var classIs = ""
    var modelchapter = ""
    var name = ""
    var eng = ""
    var urdu = ""
    private lateinit var cT: CountDownTimer
    var doubleBackToExitPressedOnce = false

    private lateinit var countAppUseTime: CountDownTimer
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        Log.d("uid of user:",currentUserDataId.toString())
//        homeNameTV.setOnClickListener {
//            startActivity(Intent(this,TestingYoutubeLink::class.java))
//        }


        try {

            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        } catch (e: Exception) {
            Log.d("ProcessLifecycleOwner", "failed $e")
        }
        // Initialize the Update Manager with the Activity and the Update Mode

//      fireStoreConn()
        timerStreak()
        checkForAppUpdateLibrary()
        //----------------------------------------------------------update end

        val userr = FirebaseAuth.getInstance().currentUser

        if (userr != null) {
            if (userr.photoUrl != null) {
                profile_image.setBackgroundResource(0)
                Glide.with(this).load(userr.photoUrl).into(profile_image)
            }
        }

        val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
        val SPforNameSaved = SPforName.getString("name", "null")

        homeNameTV.setText("Hey, $SPforNameSaved.\nWhat would You \nlike to learn today?")

        // payment send  start . code working done------------------------
//        val jazzCash = JazzCash()
//        jazzCash.phoneNumber="03123456789"
//        jazzCash.amount=250.0
//        jazzCash.cinc="345678"
//       val payWithJazzCash= ViewModelProviders.of(this)[PayWithJazzCashViewModel::class.java]
//        payWithJazzCash.payWithJazzCash(Volley.newRequestQueue(applicationContext),jazzCash).observe(this,
//            androidx.lifecycle.Observer {jazz->
//
//                Log.d("homeObserver","androidx.lifecycle.Observer")
//
//
//            })

// payment get end-----------------------------------------
        streakFun()
        getChapter()
        leaderBoardData()

// selection of chapter start grade change-----------------------------

        progressReportCv.setOnClickListener {
            startActivity(Intent(this, Learning_analysis::class.java))
        }
        profile_image.setOnClickListener {
           // startActivity(Intent(this, Profile::class.java))

            val intent=Intent(this,Profile::class.java)

            intent.putExtra("comeFrom","home")
            startActivity(intent)
        }


// ------------------------------------------------------------notificationstart

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//               var channel= ("MyNotification" , "MyNotificaton" ; NotificationManager.IMPORTANCE_DEFAULT);
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                "MyNotification",
                "MyNotificaton",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
            .addOnCompleteListener { task ->
                var msg = "Success"
                if (!task.isSuccessful) {
                    msg = "fail"
                }
                Log.d("testing msg", "testing msg")
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        // ------------------------------------------------------------notificationend
        //timer ------------------start

// ----------------fun call
//        minuteCounter()

        topicCounter()

// -------------------------       fun call end

        //timer -----------------------------------------------end

        var nameTest = intent.getIntExtra("name", 0)

        val currentUsers = FirebaseAuth.getInstance().currentUser
        currentUsers?.displayName

        val PREFERENCE_FILE_KEY = "AppPreference"
        val sharedPref = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

        var Manual_name = "null"
        var KEY_USERNAME = currentUsers?.displayName.toString()
//        var KEY_USERNAME=""
        val sharedPreff = this?.getPreferences(Context.MODE_PRIVATE) ?: return
        val username = sharedPref.getString(KEY_USERNAME, currentUsers?.displayName)

        val userManualname = sharedPref.getString(Manual_name, "null")


        //------------------------------------sharepref start for menu name
        val PREFERENCE_FILE_KEY_MENU = "AppPreferenceMENU"
        val sharedPrefMenu =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_MENU, Context.MODE_PRIVATE)


        //------------------------------------sharepref end for menu name

//        val mDialogView =
//            LayoutInflater.from(this@Home).inflate(R.layout.get_name, null)
//
//        val mBuilder =     Dialog.Builder(this)
//            .setView(mDialogView)


///----------------------------------------------------------------------------old start

//        if (nameTest == 1) {           //work
////            if (nameTest!=1 || nameTest !=2) {
//            Log.d("if displayname", "fb epmty")
//            if (userManualname == "null") {
//
//                val mDialogView =
//                    LayoutInflater.from(this@Home).inflate(R.layout.get_name, null)
//
//                val mBuilder = AlertDialog.Builder(this)
//                    .setView(mDialogView)
//
//                Log.d("if userManualname==null", "first")
//                val mAlertDialog = mBuilder.show()
//                mAlertDialog.getWindow()!!.setBackgroundDrawableResource(
//                    R.drawable.feedbck_alertdialog_rounnd
//                )
//
////                mAlertDialog.window!!.setLayout(640, 560)
//
//
//                mDialogView.getNamebtn.setOnClickListener {
//
//
//                    var name = mDialogView.getNametext.text.toString()
//                    Log.d("name", name)
//
//
//                    with(sharedPref.edit()) {
//                        putString(Manual_name, name)
//                        commit()
//                    }
//
//                    val usernamee =
//                        sharedPref.getString(Manual_name, name)
//
//
//                    homeNameTV.setText("Hey, " + usernamee + "." + "\nWhat would You \nlike to learn today?")
////                    menuNameTV.setText(usernamee)
//                    with(sharedPrefMenu.edit()) {
//                        putString("menu_name", usernamee)
//                        commit()
//                    }
//                    Toast.makeText(this, "Name submitted Successfully.", Toast.LENGTH_LONG).show()
//                    mAlertDialog.dismiss()
//                }
//
//
//            }
//            else {
//                Log.d("else username==null", "third")
//                val usernamee =
//                    sharedPref.getString(Manual_name, "Name")
//
//                homeNameTV.setText("Hey, " + usernamee + "." + "\nWhat would You \nlike to learn today?")
//
//                with(sharedPrefMenu.edit()) {
//                    putString("menu_name", usernamee)
//                    commit()
//                }
//            }
////
//        }
//        else if (nameTest == 2) {
//            Log.d("first else", "else first")
//            with(sharedPref.edit()) {
//                putString(Manual_name, currentUsers!!.displayName.toString())
//                commit()
//            }
//
//            val usernameee =
//                sharedPref.getString(Manual_name, currentUsers!!.displayName.toString())
//
//            Log.d("else curent user name", "else" + currentUsers?.displayName.toString())
//
//            homeNameTV.setText("Hey, " + usernameee + "." + "\nWhat would You \nlike to learn today?")
////            menuNameTV.setText(usernameee)
//            with(sharedPrefMenu.edit()) {
//                putString("menu_name", usernameee)
//                commit()
//            }
////            homeNameTV.setText("Hey, " + currentUsers?.displayName.toString() + "." + "\nWhat would You \nlike to learn today?")
//        }
//        else {
//            homeNameTV.setText("Hey, " + userManualname + "." + "\nWhat would You \nlike to learn today?")
//            with(sharedPrefMenu.edit()) {
//                putString("menu_name", userManualname)
//                commit()
//            }
//        }
///----------------------------------------------------------------------------old end
//        lateinit var ref: DatabaseReference
//        ref = FirebaseDatabase.getInstance().getReference("mockTest")
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
////                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                for (h in p0.children) {
//                    if (p0!!.exists()) {
//
//
//                        val mcqs_chem = p0.child("totalquestion").value.toString()
//                        exam_vidz.setText(mcqs_chem + " Mcqs")
//
//
//                    }
//                }
//            }
//
//        })

        moreSubjects.setOnClickListener {
            val intent = Intent(this, Empty::class.java)

            startActivity(intent)

        }

        auth = FirebaseAuth.getInstance()
        //customViewLogout.visibility = View.INVISIBLE

//        testing drawer------------------------------------------------------------------------------------
//        nameTV.visibility=View.GONE
        val slidingRootNav = SlidingRootNavBuilder(this)

//            .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
//            .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
//            .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
//            .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
//            .addRootTransformation(customTransformation)

//                .withToolbarMenuToggle(toolbar)
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(true)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()

        this.getSupportActionBar()?.hide()//setDisplayShowTitleEnabled(false);


        logoutPic.setOnClickListener {

//            if (customViewLogout.visibility == View.INVISIBLE) {
//                customViewLogout.visibility= View.VISIBLE
//            } else {
//                customViewLogout.visibility = View.INVISIBLE
//            }
            ///// above before testing ... below is testing

            if (slidingRootNav.isMenuOpened()) {
                slidingRootNav.closeMenu();


//                Toast.makeText(this, "Close", Toast.LENGTH_SHORT).show()
            } else {
                slidingRootNav.openMenu();

//                menuNameTV.setText(menuNameShared)
//                Toast.makeText(this, "open", Toast.LENGTH_SHORT).show()
            }


        }

//        bookMarkTV.setOnClickListener {
//            startActivity(Intent(this,BookMarkScreen::class.java))
//        }
        //------------------------set menu name start
//        var menuNameShared=  sharedPrefMenu.getString("menu_name","defaultName")
//        menuNameTV.setText(menuNameShared)


        //-----------------------set menu name end

//        bioMain.setOnClickListener {
//
//            var intent=Intent(this,Empty::class.java)
//            intent.putExtra("Bio","Bio")
//            intent.putExtra("CN",2)
//            startActivity(intent)
//
//        }

//        mathMain.setOnClickListener {
//
//            var intent=Intent(this,Empty::class.java)
//            intent.putExtra("Math","Math")
//            intent.putExtra("CN",3)
//            startActivity(intent)
//
//        }
        physicsMain.setOnClickListener {

            //            startActivity(Intent(this, MainActivity2::class.java))

            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("subject", 1)
            startActivity(intent)
        }

        chemistryMain.setOnClickListener {

            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("subject", 2)
            startActivity(intent)

        }
        BioMain.setOnClickListener {

            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("subject", 3)
            startActivity(intent)

        }
        mathMain.setOnClickListener {

          //  val intent = Intent(this, MainActivity2::class.java)
           // intent.putExtra("subject", 4)
           // startActivity(intent)

        }
        quizGameCV.setOnClickListener {

            val intent = Intent(this, SubjectSelection::class.java)
            startActivity(intent)

        }


        streakStartCV.setOnClickListener {
            startActivity(Intent(this, Streak::class.java))
        }

//        askCV.setOnClickListener {
//            startActivity(Intent(this, Snap::class.java))
//        }
        cardViewSend.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            val shareSub =
                "Preparing for exams is so much more interesting with the Superior app. Personalised learning helped me master my  fundamentals. Download now for free and learn from best teachers!\n https://play.google.com/store/apps/details?id=learningapp.exambites.org"
            intent.putExtra(Intent.EXTRA_TEXT, shareSub)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share the App!"))
        }

        menuLogoutTV.setOnClickListener {
            Toast.makeText(this, "Logging out..", Toast.LENGTH_SHORT).show()
            SignOut()
            //customViewLogout.visibility = View.INVISIBLE
            startActivity(Intent(this, StartScreen::class.java))

        }
        menuNameTV.setOnClickListener {
            val intent = Intent(this, Profile::class.java)

            intent.putExtra("comeFrom","home")

            startActivity(intent)
        }

        moreSubjects.setOnClickListener {
            val intent = Intent(this, GenerateCode::class.java)

            //startActivity(intent)
        }
//        menuNameTV.setOnClickListener {
//            var intent = Intent(this, Settings::class.java)
//
//            if((currentUsers!!.displayName.toString() =="null" || currentUsers!!.displayName.toString() =="") && userManualname!!.isEmpty()){
//                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
//            }else{
//                if (userManualname!!.isEmpty()) {
//                    intent.putExtra("Name", currentUsers!!.displayName.toString())
//                } else {
//                    intent.putExtra("Name", userManualname)
//                }
//                customViewLogout.visibility = View.INVISIBLE
//
//                startActivity(intent)
//            }

        // }

        todaysChaLL.setOnClickListener {
            val intent = Intent(this, SliderContainer::class.java)

            startActivity(intent)
        }
//        FillInTheBlankTV.setOnClickListener {
//
//            startActivity(Intent(this, FillInTheBlank::class.java))
//        }
//        changeGradeTV.setOnClickListener {
//
//            startActivity(Intent(this, ChangeGrade::class.java))
//        }

        slideLearningMenu.setOnClickListener {
            startActivity(Intent(this, Learning_analysis::class.java))

        }
        sampletest.setOnClickListener {

            startActivity(Intent(this, Options::class.java))
        }
        val num = "+923365106602"

//        contactUsTv.setOnClickListener {
//            val url = "https://api.whatsapp.com/send?phone=$num"
//            val i = Intent(Intent.ACTION_VIEW)
//            i.data = Uri.parse(url)
//            startActivity(i)
//        }
        reportTV.setOnClickListener {
            startActivity(Intent(this, Report::class.java))
        }
        leaderBoardTV.setOnClickListener {


            val intent = Intent(this, LeaderBoard::class.java)

            if ((currentUsers!!.displayName.toString() == "null" || currentUsers!!.displayName.toString() == "") && userManualname!!.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                if (userManualname!!.isEmpty()) {
                    intent.putExtra("Name", currentUsers!!.displayName.toString())
                } else {
                    intent.putExtra("Name", userManualname)
                }
                //customViewLogout.visibility = View.INVISIBLE

                startActivity(intent)
            }

        }
//
//        pastPapersMain.setOnClickListener {
//            // var intent = Intent(this, Mocktest::class.java)
//            val intent = Intent(this, PastPapers::class.java)
//            startActivity(intent)
//        }

        streakCV.setOnClickListener {
            startActivity(Intent(this, Streak::class.java))
        }

        val reff: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("contents").child("numberOfVideos")
                .child("9th")


//        reff.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
////                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                for (h in p0.children) {
//                    if (p0!!.exists()) {
//
//                        videosN.setText(p0.child("Physics").value.toString() + " videos")
////                    Log.d("outside For loop", "outside from for loop")
//                        ChemVideos.setText(p0.child("Chemistry").value.toString() + " videos")
//
//                        bioVidz.setText(p0.child("Biology").value.toString() + " videos")
//                        Log.d("videos num", "videos num " + p0.child("Physics").value.toString())
//                    }
//                }
//            }
//
//        })
//---------------------------------------------------- start counting days

        var funcount = 1
        if (funcount == 1) {
            LoginDate()
            funcount++
        }
        // ye timer tab uncomment krna ha jab NET test wala session unhide kry gy app my
        //timer()
        funCountAppUseTime()

        sendAppTimeBack()


        manageStreak()
    }

    private fun manageStreak() {
        calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)

        //--------------- start get streak counter 1
        val sPofStreakCounter = getSharedPreferences("streak_pref", MODE_PRIVATE)
        val editorsPofStreakCounter = sPofStreakCounter.edit()
        val steakCount = sPofStreakCounter.getInt(
            "streak_pref",
            0
        )

        ///--------------end get streak counter

        //---------------start get last day on test given 2
        val sPofLastTest = getSharedPreferences("streak_pref_last_test", MODE_PRIVATE)
        val editorsPofLastTest = sPofLastTest.edit()
        val lastTestDay = sPofLastTest.getInt(
            "streak_pref_LT",
            0
        )
        //---------------end get last day on test given

        //---------------start get last year on test given 3
        val sPofLastTestYear = getSharedPreferences("streak_pref_last_year", MODE_PRIVATE)
        val editorsPofLastTestYear = sPofLastTestYear.edit()
        val lastTestYear = sPofLastTestYear.getInt(
            "streak_pref_LTY",
            0
        )
        //---------------end get last year on test given

        Log.d("dayTestDAY_OF_YEAR", dayOfYear.toString())
        Log.d("dayTestYear", year.toString())
        Log.d("dayTestLTD", lastTestDay.toString())
        Log.d("dayTestSteak", steakCount.toString())
        Log.d("dayTestTY", lastTestYear.toString())



        if (year - lastTestYear == 0) {
            when {
                dayOfYear - lastTestDay == 0 -> {
                   // start_now_tv.text = "Come Tomorrow!"
//                    binding.todaysHomeTV.text = "You are done for today."
                    start_now_tv.text = "Come back after"
                    streakStartOrEnd.text = "Starts in"
//                    streakStartOrEnd.text = "Streak Starts in"
                    streakStartOrEnd.visibility = View.INVISIBLE
                    timer()
                    // streakStartCV.isClickable=false
                }
                dayOfYear - lastTestDay > 1 -> {
                    editorsPofStreakCounter.putInt("streak_pref", 0)
                    editorsPofStreakCounter.apply()

                    //streakStartCV.isClickable=true

                    timer()
                }
                else -> {
                    //streakStartCV.isClickable=true

                    timer()
                }
            }
        } else if (year - lastTestYear >= 1) {
            //editorsPofStreakCounter.putInt("streak_pref",steakCount+1)
            streakStartCV.isClickable = true

            timer()
        }

    }
    private fun sendAppTimeBack() {
        calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
        val year = calendar.get(Calendar.YEAR)
        val date = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)
        val alldate = if(date-1 == 0){
            "1 : $month : $year"
        }else{
            "${date-1} : $month : $year"
        }
        val rOfLastDay = getSharedPreferences("rOfLastDay", MODE_PRIVATE)
        val editorOfLastDay = rOfLastDay.edit()
        val lastRecordDay = rOfLastDay.getInt(
            "rOfLastDay",
            0
        )


        val checker=dayOfYear-lastRecordDay
        if (checker == 0){
            Log.d("dayCheckerIF",checker.toString())

        }
        else{
            Log.d("dayCheckerELSE",checker.toString())
           val noteRef = FireStoreUtils().fireStoreDatabase.collection("DataAppUseTime")
               .document(currentUserDataId).collection(alldate)

            editorOfLastDay.putInt("rOfLastDay",dayOfYear)
            editorOfLastDay.apply()
            val countPref = getSharedPreferences("Counter", MODE_PRIVATE)
            val getDataCount = countPref.getInt("count", 0)
            val editorCountPref = countPref.edit()


            val note: MutableMap<String, Any> = HashMap()
            note["Mints"] = getDataCount

            noteRef.add(note)
                .addOnSuccessListener {
                    Log.d("saveData", "successfully")
                    editorCountPref.putInt("count", 0)
                    editorCountPref.apply()
                }
                .addOnFailureListener { e ->
                    Log.d("saveData", e.toString())
                }


        }



    }
    private fun checkForAppUpdateLibrary() {
        // Declare the UpdateManager
        // Declare the UpdateManager

        // Initialize the Update Manager with the Activity and the Update Mode
        val mUpdateManager: UpdateManager =
            UpdateManager.Builder(this).mode(UpdateManagerConstant.IMMEDIATE)
        mUpdateManager.start()

        mUpdateManager.addUpdateInfoListener(object : UpdateInfoListener {
            override fun onReceiveVersionCode(code: Int) {
                // You can get the available version code of the apk in Google Play
                // Do something here
            }

            override fun onReceiveStalenessDays(days: Int) {
                // Number of days passed since the user was notified of an update through the Google Play
                // If the user hasn't notified this will return -1 as days
                // You can decide the type of update you want to call
            }
        })


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == APP_UPDATE_REQUEST_CODE) {
//            if (resultCode != Activity.RESULT_OK) {
//                Toast.makeText(this,
//                    "App Update failed, please try again on the next app launch.",
//                    Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {

                }
                Activity.RESULT_CANCELED -> {
                    finish()
                }
//            ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
//
//            }
            }
        }
//        when (requestCode) {
//            UpdateManagerConstant.REQUEST_CODE -> {
//                when(resultCode){
//                    Activity.RESULT_OK -> {
//
//                    }
//                    Activity.RESULT_CANCELED -> {
//                        if(updateMgr?.mode == UpdateManagerConstant.IMMEDIATE) finish()
//                    }
//                    ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
//
//                    }
//                }
//            }
//        }
    }
    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 781
    }
    private fun leaderBoardData() {
        val dataEnterOrNot = getSharedPreferences("dataEnterOrNot", MODE_PRIVATE)
        val getData = dataEnterOrNot.getString("dataEnterOrNot", null)
        val getDataEditor = dataEnterOrNot.edit()

        val uidStored = getSharedPreferences("uidData", MODE_PRIVATE)
        val getDataUid = uidStored.getString("uidData", null)
        val uidEditor = uidStored.edit()

        if (getDataUid != currentUserDataId) {
            uidEditor.putString("uidData", currentUserDataId)
            getDataEditor.putString("dataEnterOrNot", null)

            uidEditor.apply()
            getDataEditor.apply()

        }

        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected", "def")


        if (getData == null) {
            val subject = if (gradeSelect.toString() == "entry") {
                "entryTest"
            } else {
                "matriculation"
            }

            val SPforName = getSharedPreferences("nameStorage", Context.MODE_PRIVATE)
            val nameOfUser = SPforName.getString("name", "null")

            val parentalUserDataRef: DatabaseReference =
                FirebaseDatabase.getInstance().reference
                    .child("LeaderBoard").child(subject).child(currentUserDataId)

            val dataMap = HashMap<Any, Any>()
            dataMap["score"] = "0"
            dataMap["name"] = nameOfUser.toString()
            parentalUserDataRef.setValue(dataMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        getDataEditor.putString("dataEnterOrNot", "yes")
                        getDataEditor.apply()


                    }
                }
        }
    }
    private fun streakFun() {
        val calendar = Calendar.getInstance()
        val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)

        val sPofStreakCounter = getSharedPreferences("streak_pref", MODE_PRIVATE)
        val steakCount=sPofStreakCounter.getInt("streak_pref",0)
        val editorsPofStreakCounter = sPofStreakCounter.edit()

        val lastDay = sPofStreakCounter.getInt(
            "streak_pref",
            0
        )

        //---------------start get last day on test given 2
        val sPofLastTest = getSharedPreferences("streak_pref_last_test", MODE_PRIVATE)
        val editorsPofLastTest = sPofLastTest.edit()
        val lastTestDay = sPofLastTest.getInt(
            "streak_pref_LT",
            0
        )

        Log.d("lastTestDaymmm",lastTestDay.toString())
        Log.d("dayOfYearmmm",dayOfYear.toString())
        //---------------end get last day on test given
        val userDataRef: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("RecordsOFStreakTests")
                .child(currentUserDataId).child("0_Streaks")


        when {
            dayOfYear - lastTestDay > 1 -> {
                streakTv.text = "Streak 0"

                editorsPofStreakCounter.putInt("streak_pref", 0)
                editorsPofStreakCounter.apply()
            }
            dayOfYear - lastTestDay == 1 || dayOfYear - lastTestDay == 0 -> {
                userDataRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val streak = snapshot.child("totalStreaks").value.toString()
    //                    streakTv.text = "Streak " + "${lastDay.toString()}"
                            streakTv.text = "Streak " + "$streak"
                            editorsPofStreakCounter.putInt("streak_pref", streak.toInt())
                            editorsPofStreakCounter.apply()
                        } else {
                            streakTv.text = "Streak " + "${steakCount}"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
            lastTestDay - dayOfYear >= 1 -> {
                Log.d("mmm","test")
                Toast.makeText(this, "Date Issue!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun timer() {
        calendar = Calendar.getInstance()
        calendar.time
        Log.d("Current time: ", calendar.time.toString())

        val hourCurrent = calendar.get(Calendar.HOUR)
        val minutCurrent = calendar.get(Calendar.MINUTE)
        val secondCurrent = calendar.get(Calendar.SECOND)

        val hourSub = 23 - hourCurrent
        val minSub = 59 - minutCurrent
        val secSub = 59 - secondCurrent


        cT = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                timerHomeTV.setText("$hourSub:$minSub:$secSub")

            }

            override fun onFinish() {
                timer()
            }
        }
        if (this::cT.isInitialized) {
            cT.start()
        }

    }
    fun timerStreak() {
        val calendar = Calendar.getInstance()
        // calendar.time
       // Log.d("CurrentTime: ", calendar.time.toString())
//        calendar
        val hourCurrent = calendar.get(Calendar.HOUR)
        val minutCurrent = calendar.get(Calendar.MINUTE)
        val secondCurrent = calendar.get(Calendar.SECOND)
        val am_pm = calendar.get(Calendar.AM_PM)

       // Log.d("CurrentTime:AM_PM ", am_pm.toString())
        //   Log.d("CurrentTime:ZONE ", am_m.toString())


        var hourSub = 23 - hourCurrent
        val minSub = 59 - minutCurrent
        val secSub = 59 - secondCurrent

        if (am_pm == 1) {
            hourSub = 11 - hourCurrent
        }


        var secZero = "0"
        secZero = if (secSub <= 9) {
            "0$secSub"
        } else {
            "$secSub"
        }

        var mintZero = "0"
        mintZero = if (minSub <= 9) {
            "0$minSub"
        } else {
            "$minSub"
        }

        var hourZero = "0"
        hourZero = if (hourSub <= 9) {
            "0$hourSub"
        } else {
            "$hourSub"
        }


        cT = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                timerStreakHomeTV.text = "$hourZero : $mintZero : $secZero"

            }

            override fun onFinish() {
                timerStreak()
            }
        }
        cT?.start()
    }
    private fun changeGrade() {


        val PREFERENCE_FILE_KEY_Grade = "AppPreferenceMenuGrade"
        val sharedPrefGrade =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade, Context.MODE_PRIVATE)
        val gradeSelect =
            sharedPrefGrade.getString("gradeSelected", "def")

        val PREFERENCE_FILE_KEY_Grade_refresh = "AppPreferenceMenuGradeRefresh"
        val sharedPrefGradeRefresh =
            this.getSharedPreferences(PREFERENCE_FILE_KEY_Grade_refresh, Context.MODE_PRIVATE)
        val gradeSelectRefresh =
            sharedPrefGradeRefresh.getString("gradeSelectedRefresh", "def")

        val entryFirstSubjectLL = findViewById<LinearLayout>(R.id.entryFirstSubjectLL)
        val entrySecondSubjectLL = findViewById<LinearLayout>(R.id.entrySecondSubjectLL)
        val linearSubjectsmore = findViewById<LinearLayout>(R.id.linearSubjectsmore)
        val linearSubjects = findViewById<LinearLayout>(R.id.linearSubjects)


//        if(gradeSelectRefresh.toString()=="2"){
//            with(sharedPrefGradeRefresh.edit()) {
//                putString("gradeSelectedRefresh", "1")
//                commit()
//            }
        if (gradeSelect.toString() == "9th") {

            linearSubjects.visibility = View.VISIBLE
            linearSubjectsmore.visibility = View.VISIBLE
            entryFirstSubjectLL.visibility = View.GONE
            entrySecondSubjectLL.visibility = View.GONE
            todaysChaLL.visibility = View.GONE
            Log.d("testingHide", "9th")
            quizGameCV.visibility = View.VISIBLE

            linearSubjctse.visibility = View.VISIBLE
            samp.visibility = View.VISIBLE
            moreSubjects.visibility = View.VISIBLE
//                sampletest.visibility=View.VISIBLE
            cardViewSend.visibility = View.VISIBLE

        } else if (gradeSelect.toString() == "entry") {
            todaysHomeTV.setText(Html.fromHtml("<b>Today's Challenge</b> <Br>Start Now"))

            entryFirstSubjectLL.visibility = View.VISIBLE
            entrySecondSubjectLL.visibility = View.VISIBLE
            todaysChaLL.visibility = View.VISIBLE

            quizGameCV.visibility = View.GONE
            linearSubjects.visibility = View.GONE
            linearSubjectsmore.visibility = View.GONE

            Log.d("testingHide", "entry")
            linearSubjctse.visibility = View.GONE
            samp.visibility = View.GONE
            moreSubjects.visibility = View.GONE
            sampletest.visibility = View.GONE
            cardViewSend.visibility = View.GONE






            nustCV.setOnClickListener {
                intent = Intent(this, Mdcat::class.java)
                intent.putExtra("subject", "net")
                startActivity(intent)
                //Toast.makeText(applicationContext,"Nust clicked",Toast.LENGTH_SHORT).show()
            }

            ecatCV.setOnClickListener {
                // Toast.makeText(applicationContext,"ecat clicked",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Options::class.java)
                intent.putExtra("mock", 2)
                startActivity(intent)

            }
            mdcatCV.setOnClickListener {
                intent = Intent(this, Mdcat::class.java)
                intent.putExtra("subject", "mdcat")
                startActivity(intent)
                // Toast.makeText(applicationContext,"mdcat clicked",Toast.LENGTH_SHORT).show()
            }


        }


//        }
    }
    fun LoginDate() {
        //        val currentDateTimeString =
//            DateFormat.getDateTimeInstance().format(Date())
//        textView.setText(currentDateTimeString)

//        val date = LocalDate.now()
//        Log.d("current date", "current day is :" + date)
//
//        var modify= date.toString().substring(8)
//        Log.d("modify date", "current day is :" + modify)


        val date = Date()
//        val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
        val formatter = SimpleDateFormat("dd")
        val modify: String = formatter.format(date)

        //--------------------------------------------functional

        val ref: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    val s = p0.child("Variable").value.toString()


                    Log.d("testing setting", "testing" + s)
//                    var codee=parent_code.text.toString()
                    val namePaste: DatabaseReference =
                        FirebaseDatabase.getInstance().reference.child("LastLoginDetail").child(s)

                    val userMap1 = HashMap<Any, Any>()

                    userMap1["Date"] = modify

                    namePaste.setValue(userMap1)
                } else {

//                    ---------------------------------------------------

                }
            }

        })

    }
    override fun onStart() {
        super.onStart()
        changeGrade()

//        funCountAppUseTime()


        Log.d("testingactivity", "onstart")

        // val optionlist: ArrayList<McqsModel> = ArrayList<McqsModel>()
        // val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("contents")
        // .child("SampleTest")


//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
////                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//
//                if (p0!!.exists()) {
//                    for (h in p0.children) {
//
//                        val opt = McqsModel(
//                            h.child("optionA").value.toString(),
//                            h.child("optionB").value.toString(),
//                            h.child("optionC").value.toString(),
//                            h.child("optionD").value.toString(),
//                            h.child("correctOption").value.toString(),
//                            h.child("question").value.toString()
//                        )
////                        Log.d("For loop", h.child("name").toString())
//                        optionlist.add(opt!!)
////                        if (a == -1) {
////                            a++
////
////                            optionlist.get(0).question
////                            sampleQuestionText.setText(optionlist.get(0).question)
////
////                            optionlist.get(0).optionA
////                            btnAa.setText(optionlist.get(0).optionA)
////
////                            optionlist.get(0).optionB
////                            btnBb.setText(optionlist.get(0).optionB)
////
////                            optionlist.get(0).optionC
////                            btnCc.setText(optionlist.get(0).optionC)
////
////                            optionlist.get(0).optionD
////                            btnDd.setText(optionlist.get(0).optionD)
////
////                            optionlist.get(0).correctOption
////
////
////                        }
//                    }
////                    Log.d("outside For loop", "outside from for loop")
//
//
//                }
//            }
//
//        })


    }
    fun funCountAppUseTime() {
        calendar = Calendar.getInstance()

        val hourCurrent = calendar.get(Calendar.HOUR)
        val mintCurrent = calendar.get(Calendar.MINUTE)
        val secondCurrent = calendar.get(Calendar.SECOND)
        incrementAppTimer()

        countAppUseTime = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {


//                val steakCount = storeTime.getInt(
//                    "store_time_all",
//                    0
//                )

              //  Log.d("funCountAppUseSec", "$hourCurrent:$mintCurrent:$secondCurrent")


            }

            override fun onFinish() {
                funCountAppUseTime()
                Log.d("funCountAppUseTime", "$hourCurrent:$mintCurrent:$secondCurrent")

            }
        }
        countAppUseTime.start()

    }
    private fun incrementAppTimer() {

        val countPref = getSharedPreferences("Counter", MODE_PRIVATE)
        val getdata = countPref.getInt("count", 0)
        val editorOpenClose = countPref.edit()
        // editorOpenClose.putInt("count", MinSecInMint)
        editorOpenClose.putInt("count", getdata + 1)
        editorOpenClose.apply()

        Log.d("funCountAppUseTimeMint", "$getdata")

    }
    private fun SignOut() {
        auth.signOut()
        LoginManager.getInstance().logOut()

        val currentUsers = FirebaseAuth.getInstance().currentUser
        currentUsers === null
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//
//
//    }

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

    fun minuteCounter() {

        var count = 1
        Log.d(
            "counter ",
            "counter  " + count
        )
        var codeValue: String
        var TotalNum: String


        var code: DatabaseReference
        code =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        Handler().postDelayed({

            minute++
            Log.d("minute log ", "log for minute finish " + minute)
            //--------------------------------------
//            var code: DatabaseReference
//            code =
//                FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

            code.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                    TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
//                    for (h in p0.children) {
                    if (p0!!.exists()) {
///-------------------
                        codeValue = p0.child("Variable").value.toString()

                        Log.d(
                            "codeValue ",
                            "codeValue  " + p0.child("Variable").value.toString()
                        )


                        var StudentcodeGet: DatabaseReference
                        StudentcodeGet =
                            FirebaseDatabase.getInstance().getReference("StudentsDataForParent")
                                .child(codeValue).child("TotalApptime")

                        StudentcodeGet.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
//                                TODO("not implemented")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
//                                    for (h in p0.children) {
                                if (p0!!.exists() && count == 1) {
//                                            if (count==1) {
                                    count++
                                    TotalNum = p0.child("TotalApptime").value.toString()

                                    Log.d(
                                        "get TotalApptime",
                                        "TotalApptime get " + p0.child("TotalApptime").value.toString()
                                    )

//                                            if(TotalNum!=null){

                                    val userDataRef: DatabaseReference =
                                        FirebaseDatabase.getInstance()
                                            .getReference("StudentsDataForParent").child(codeValue)


                                    val userMap = HashMap<Any, Any>()
                                    userMap["TotalApptime"] = TotalNum.toInt() + 1

                                    userDataRef.child("TotalApptime").setValue(userMap)

                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

//                                                            minuteCounter()
                                                // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                                            } else {

                                                //  FirebaseAuth.getInstance().signOut()
                                            }
                                        }
//                                            }

                                } else if (p0!!.exists().not()) {
//                                            count++
                                    Log.d("else", "else p0!!.exists() && count==1")
                                    val userDataRef: DatabaseReference =
                                        FirebaseDatabase.getInstance()
                                            .getReference("StudentsDataForParent").child(codeValue)


                                    val userMap = HashMap<Any, Any>()
                                    userMap["TotalApptime"] = 0

                                    userDataRef.child("TotalApptime").setValue(userMap)

                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

//                                                            minuteCounter()
                                                // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                                            } else {

                                                //  FirebaseAuth.getInstance().signOut()
                                            }
                                        }
                                }
//                                    }
                            }

                        })
                        minuteCounter()
                    }
//                    }
                }

            })
            //---------------------one end

            //------------------------------------firebae minute end
//            minuteCounter()

        }, 60000)


    }

    fun topicCounter() {
        var stoploop = 0
        var topicCount = 0
        val CodeRef =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)
        val topicCounter =
            FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
                .child(currentUserDataId).child("books")


        CodeRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                                    for (h in p0.children) {
                if (p0!!.exists()) {

                    val codeValue = p0.child("Variable").value.toString()
                    Log.d("if", "if of  CodeRef")

                    topicCounter.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
//                            TODO("not implemented")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
//                                    for (h in p0.children) {
                            if (p0!!.exists()) {
                                Log.d("if", "if of topicCounter")

                                for (h in p0.children) {
                                    for (a in h.children) {
                                        for (b in a.children) {
                                            for (c in b.children) {
                                                topicCount++
                                            }
                                        }
                                    }

                                }
                                if (stoploop == 0) {
                                    stoploop++

                                    val userDataRef: DatabaseReference =
                                        FirebaseDatabase.getInstance()
                                            .getReference("StudentsDataForParent").child(codeValue)


                                    val userMap = HashMap<Any, Any>()
                                    userMap["TotalTopicLearned"] = topicCount

                                    userDataRef.child("TotalTopicLearned").setValue(userMap)

                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {

//                                                            minuteCounter()
                                                // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                                            } else {

                                                //  FirebaseAuth.getInstance().signOut()
                                            }
                                        }
                                }

                                Log.d("increment", "value of increment :" + topicCount)
                            } else {
                                Log.d("Else", "else of topicCounter")
                            }

                        }

                    })
                } else {
                    Log.d("Else", "else of CodeRef")
                }

            }

        })
    }


    fun jump_revision(view: View) {
        if (!this::chapkey.isInitialized) {
            Toast.makeText(this, "No recommended lesson available!", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, Revision::class.java)
            intent.putExtra("chapKey", chapkey)
            intent.putExtra("classIs", classIs)
            intent.putExtra("mcqsSubject", mcqssubject)
            intent.putExtra("modelChapter", modelchapter)
            intent.putExtra("name", name)
            intent.putExtra("eng", eng)
            intent.putExtra("urdu", urdu)
            startActivity(intent)
        }
    }

    fun revzn(view: View) {
        startActivity(Intent(this, Revision::class.java))
    }

    fun go_sample(view: View) {
        startActivity(Intent(this, Options::class.java))
    }


    fun getChapter() {

        Log.d("Home","getChapter start")

       // val PREFERENCE_FILE_KEY = "AppPreferencFrag"
      //  val sharedPrefFrag = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
      ////  val Fragname = "null"
      //  val Fragmanual = sharedPrefFrag.getString(Fragname, "null")


        val ref =
            FirebaseDatabase.getInstance().getReference("RevisionTopic").child(currentUserDataId).limitToLast(1)
      //  val query = ref.orderByValue().limitToFirst(1)
        ref.keepSynced(false)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0.exists()) {
                    Log.d("Home","getChapter 01 $currentUserDataId")
                    Log.d("Home","getChapter 01 p0 ${p0.children}")


                    for (b in p0.children) {
                        classIs = b.child("grade").value.toString()
                        chapkey = b.child("chapkey").value.toString()
                        mcqssubject = b.child("mcqssubject").value.toString()
                        modelchapter = b.child("modelchapter").value.toString()
                        Log.d("Home","getChapter for 01 classIs $classIs")

                    }
                    Log.d("Home","getChapter 01 classIs $classIs")
                    Log.d("Home","getChapter 01 modelchapter $modelchapter")

///--------------------------------------------------------------------------------------------------------------------------------
                    val reff =
                        FirebaseDatabase.getInstance().getReference("contents")
                            .child("books").child(classIs)
                            .child(mcqssubject).child(modelchapter).child("topics")
                            .child(chapkey)

                    Log.d("mcqssubject", "revision mcqssubject :" + mcqssubject)
                    Log.d("chapkey", "revision chapkey :" + chapkey)
                    Log.d("modelchapter", "revision modelchapter :" + modelchapter)

                    reff.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p1: DatabaseError) {
                        }

                        override fun onDataChange(p1: DataSnapshot) {
//                Log.d("data", p0.toString())
                            if (p1.exists()) {
                                Log.d("Home","getChapter 02")

//                                    if(count==1) {

                                //---------------------------------------------------------------------------------emd
                                name = p1.child("name").value.toString()
                                eng = p1.child("urlEng").value.toString()
                                urdu = p1.child("urlUrdu").value.toString()
//
                                Log.d("testing ", "checking value of name :" + name)
                                Log.d("testing ", "checking value of eng :" + eng)
                                Log.d("testing ", "checking value of urdu :" + urdu)

//                                        chaptName.setText(name)
//                                        name.removeRange(5,1)
                                when (mcqssubject) {
                                    "subjects-1" -> {
                                        homeRevisionSubjectName.setText("Physics")
                                    }
                                    "chemistry" -> {
                                        homeRevisionSubjectName.setText("Chemistry")
                                    }
                                    "biology" -> {
                                        homeRevisionSubjectName.setText("Biology")
                                    }
                                }
//                                        name.substring(3)
                                homeRevisionTopinName.setText(name)


                            }

                        }

                    })
//                        }
//                    }//for end
                } else {
                    showToast()
                }
            }

        })

    }


    fun showToast() {
        homeRevisionTopinName.text = "Topic Name"
        homeRevisionSubjectName.text = "Subject of Topic"

        Log.d("recommendation"," no video available")
        //Toast.makeText(this, "No Recommended Video!", Toast.LENGTH_SHORT).show()
    }

    //    override fun onResume() {
//        super.onResume()
////        changeGrade()
//        getChapter()
//        Log.d("testingactivity", "onResume")
//
//    }
    override fun onResume() {
        super.onResume()
        getChapter()
        Log.d("testingactivity", "onResume")

    }

    override fun onRestart() {
        super.onRestart()
        if (this::cT.isInitialized) {
            cT.start()
        }
        getChapter()

        changeGrade()
        Log.d("testingactivity", "onRestart")

    }

    override fun onPause() {
        super.onPause()
//        Handler().removeCallbacksAndMessages(null)

        if (this::cT.isInitialized) {
            cT.cancel()
        }

        Log.d("testingactivity", "on pause yyyy")
    }

    override fun onStop() {
        super.onStop()
        if (this::cT.isInitialized) {
            cT.cancel()
        }

//        Handler().removeCallbacksAndMessages(null)
        Log.d("testingactivity", "on stop yyyy")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::cT.isInitialized) {
            cT.cancel()
        }
        Log.d("testingactivity", "on destroy yyyy")

    }


    fun isActivityVisible(): String {
        return ProcessLifecycleOwner.get().lifecycle.currentState.name
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //App in background
        countAppUseTime.cancel()

        Log.e("testingactivity", "************* backgrounded")
        Log.e("testingactivity", "************* ${isActivityVisible()}")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        countAppUseTime.start()

        Log.e("testingactivity", "************* foregrounded")
        Log.e("testingactivity", "************* ${isActivityVisible()}")
        // App in foreground
    }

    fun dislikeAlertFun(view: android.view.View) {

      //  calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)



        val mDialogView =
            LayoutInflater.from(this@Home).inflate(R.layout.dislike_feedack, null)


        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        mBuilder.setOnDismissListener {
            (mDialogView.parent as ViewGroup).removeView(mDialogView)
        }
        val mAlertDialog = mBuilder.create()

        mDialogView.sendFeedbackDisBtn.setOnClickListener {

            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            val MandY= "$month:$year"
            val currentTimestamp = System.currentTimeMillis()
            val refOfPaid = FirebaseDatabase.getInstance()
                .getReference("FeedBack").child("disLike").child(MandY).child(currentUserDataId).child(currentTimestamp.toString())
            val userMap = HashMap<Any, Any>()

            userMap["Feedback"] = mDialogView.dislikeFeedbackET.text.toString()

            if(mDialogView.dislikeFeedbackET.text.isNotEmpty()) {
                refOfPaid.setValue(userMap)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Feedback has been sent!", Toast.LENGTH_SHORT).show()
                            mAlertDialog.dismiss()

                        } else {
                            Toast.makeText(this, "Check your internet connection!", Toast.LENGTH_SHORT).show()
                            mAlertDialog.dismiss()
                        }
                    }
            }


//            val mAlertDialog = mBuilder.show()



        }

        // val mAlertDialog = mBuilder.show()
        mAlertDialog.show()

        val displayMetrics =  DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        val displayWidth = displayMetrics.widthPixels;
        val displayHeight = displayMetrics.heightPixels;

        val layoutParams =  WindowManager.LayoutParams();
        layoutParams.copyFrom(mAlertDialog.getWindow()?.getAttributes());

        val dialogWindowWidth =  (displayWidth * 0.7f);

        val dialogWindowHeight =  (displayHeight * 0.5f);

        layoutParams.width = dialogWindowWidth.toInt();
        layoutParams.height = dialogWindowHeight.toInt();
        mAlertDialog.getWindow()?.setAttributes(layoutParams);
        //mAlertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mAlertDialog.window!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )

    }

    @SuppressLint("SuspiciousIndentation")
    fun likeAlertFun(view: android.view.View) {
        val mDialogView =
            LayoutInflater.from(this@Home).inflate(R.layout.feedback_layout, null)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)



        val mBuilder = AlertDialog.Builder(this , R.style.my_dialog)
            .setView(mDialogView)

        mBuilder.setOnDismissListener {
            (mDialogView.parent as ViewGroup).removeView(mDialogView)
        }
        val mAlertDialog = mBuilder.create()


        mDialogView.sendFeedbackBtn.setOnClickListener {
            mBuilder.setOnDismissListener {
                (mDialogView.parent as ViewGroup).removeView(mDialogView)
            }

            val MandY= "$month:$year"
            val currentTimestamp = System.currentTimeMillis()
            val refOfPaid = FirebaseDatabase.getInstance()
                .getReference("FeedBack").child("Like").child(MandY).child(currentUserDataId).child(currentTimestamp.toString())
            val userMap = HashMap<Any, Any>()

            userMap["Feedback"] = mDialogView.likeFeedbackET.text.toString()

            if(mDialogView.likeFeedbackET.text.isNotEmpty())
            refOfPaid.setValue(userMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Feedback has been sent!", Toast.LENGTH_SHORT).show()
                        mAlertDialog.dismiss()

                    } else {
                        Toast.makeText(this, "Check your internet connection!", Toast.LENGTH_SHORT).show()
                        mAlertDialog.dismiss()
                    }
                }

        }

        // val mAlertDialog = mBuilder.show()
        mAlertDialog.show()


        val displayMetrics =  DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        val displayWidth = displayMetrics.widthPixels;
        val displayHeight = displayMetrics.heightPixels;

        val layoutParams =  WindowManager.LayoutParams();
        layoutParams.copyFrom(mAlertDialog.getWindow()?.getAttributes());

        val dialogWindowWidth =  (displayWidth * 0.7f);

        val dialogWindowHeight =  (displayHeight * 0.5f);

        layoutParams.width = dialogWindowWidth.toInt();
        layoutParams.height = dialogWindowHeight.toInt();
        mAlertDialog.getWindow()?.setAttributes(layoutParams);


        mAlertDialog.window!!.setBackgroundDrawableResource(
            R.drawable.feedbck_alertdialog_rounnd
        )

    }
}