package learningapp.superior.org.Reivision

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.jarvanmo.exoplayerview.media.SimpleMediaSource
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import kotlinx.android.synthetic.main.activity_revision.*
import kotlinx.android.synthetic.main.fragment_frag_one_new.*
import learningapp.superior.org.*
import learningapp.superior.org.Home.Home

import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.Config


class Revision : AppCompatActivity()    ,
//    YouTubePlayer.PlayerStateChangeListener,
    FragOneNew.OnDataPas{
    lateinit var monInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var mPlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

    var uid: String = FirebaseAuth.getInstance().currentUser!!.uid


    var chapKey = ""
    var classIs = ""
    var mcqsSubject = ""
    var modelChapter = ""
    var name = ""
    var eng = ""
    var urdu=""
    lateinit var youTubePlayerFragment: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
    var  listener: YouTubePlayerListener =object : AbstractYouTubePlayerListener(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revision)

        val adapter =
            ViewPagerAdapter(supportFragmentManager)

         chapKey= intent.getStringExtra("chapKey").toString()
        classIs= intent.getStringExtra("classIs").toString()
         mcqsSubject=intent.getStringExtra("mcqsSubject").toString()
        modelChapter=intent.getStringExtra("modelChapter").toString()
         name=intent.getStringExtra("name").toString()
        eng=intent.getStringExtra("eng").toString()
        urdu=intent.getStringExtra("urdu").toString()

        Log.d("reciveToRevChapKey",chapKey)
        Log.d("reciveToRevMcqsSubject",mcqsSubject)
        Log.d("reciveToRevModelChapter",modelChapter)
        Log.d("reciveToRevName",name)
        Log.d("reciveToRevEng",eng)
        Log.d("reciveToRevUrdu",urdu)
//        val a= intent.getIntExtra("name",0)
//        Log.d("vale","value of a is :"+a)
//
//        val q=intent.getIntExtra("name",0)
//        if(q==1) {
//           // getChapter()
//            Log.d("name","value of q name if :"+q)
//        }
//        else{
//            Log.d("name","value of q name else :"+q)
//        }

//        val PREFERENCE_FILE_KEY = "AppPreferencFrag"
//        val sharedPrefFrag = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
//        val Fragname = "null"
//        val Fragmanual = sharedPrefFrag.getString(Fragname, "null")

//        Log.d("above bundle", "checking value share of name :" + Fragmanual)
        val bundle = Bundle()
       // bundle.putInt("nameInt", a)

        bundle.putString("chapKey", chapKey)
        bundle.putString("classIs", classIs)
        bundle.putString("mcqsSubject", mcqsSubject)
        bundle.putString("modelChapter", modelChapter)
        bundle.putString("eng", eng)
        bundle.putString("urdu", urdu)
        bundle.putString("name", name)


        //frag1
        val fragInf = FragOneNew()
        fragInf.arguments = bundle
        adapter.addFragment(fragInf, "REVIEW")

        //frag2
//        val fragInf2 = FragTwoNew()
//        adapter.addFragment(fragInf2, "VIDEO")
        //frag3
        val fragInf3 = FragThreeNew()
        adapter.addFragment(fragInf3, "QUIZ")
        //frag4
        val fragInf4 = FragFourNew()
        adapter.addFragment(fragInf4, "STATS")

        viewPager2.adapter = adapter
        tabs2.setupWithViewPager(viewPager2)

//        orient2.setOnClickListener {
//            changeOrientation()
//        }
        goBackRevision.setOnClickListener {
            onBackPressed()
        }


        listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                // We're using pre-made custom ui

                Log.d("onready", "hello on ready ")
                mPlayer= youTubePlayer
                val defaultPlayerUiController =
                    DefaultPlayerUiController(youTubePlayerFragment, youTubePlayer)
                defaultPlayerUiController.showFullscreenButton(true)

                // When the video is in full-screen, cover the entire screen
                defaultPlayerUiController.setFullScreenButtonClickListener {
                    if (youTubePlayerFragment.isFullScreen()) {
                        youTubePlayerFragment.exitFullScreen()
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                        // Show ActionBar
                        if (supportActionBar != null) {
                            supportActionBar!!.show()
                        }
                    } else {
                        youTubePlayerFragment.enterFullScreen()
                        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Hide ActionBar
                        if (supportActionBar != null) {
                            supportActionBar!!.hide()
                        }
                    }
                }


                youTubePlayerFragment.setCustomPlayerUi(defaultPlayerUiController.rootView)

//                val videoId =data
//                    val videoId = "YE7VzlLtp-4"
//                youTubePlayer.cueVideo(videoId, 0f)

            }

            override fun onVideoId(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                videoId: String
            ) {
                super.onVideoId(youTubePlayer, videoId)
//                Log.d("LOG", "hello onVideoId" )

            }

            override fun onStateChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
//                Log.d("LOG", "hello onStateChange"  )

            }

            override fun onPlaybackQualityChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                playbackQuality: PlayerConstants.PlaybackQuality
            ) {
                super.onPlaybackQualityChange(youTubePlayer, playbackQuality)
//                Log.d("LOG", "hello onPlaybackQualityChange"  )

            }

            override fun onPlaybackRateChange(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                playbackRate: PlayerConstants.PlaybackRate
            ) {
                super.onPlaybackRateChange(youTubePlayer, playbackRate)
//                Log.d("LOG", "hello onPlaybackRateChange"  )

            }

            override fun onError(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                super.onError(youTubePlayer, error)
//                Log.d("LOG", "hello onError"  )

            }

            override fun onApiChange(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onApiChange(youTubePlayer)
//                Log.d("LOG", "hello onApiChange"  )

            }

            override fun onCurrentSecond(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                second: Float
            ) {
                super.onCurrentSecond(youTubePlayer, second)
//                Log.d("LOG", "hello onCurrentSecond" )

            }

            override fun onVideoDuration(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                duration: Float
            ) {
                super.onVideoDuration(youTubePlayer, duration)
                Log.d("LOG", "hello onVideoDuration"  )

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                loadedFraction: Float
            ) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction)
//                Log.d("LOG", "hello onVideoLoadedFraction" )

            }

        }

    }
    override fun onStart() {
        super.onStart()

//        monInitializedListener = object : OnInitializedListener {
//
//
//            override fun onInitializationSuccess(
//                provider: YouTubePlayer.Provider,
//                player: YouTubePlayer,
//                wasRestored: Boolean
//            ) {
//
//
//                val sharedPreferences =
//                    PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                val text = sharedPreferences.getString("my", "")
//                if (!wasRestored) {
//
//
//                    mPlayer = player
//                  //  player.loadVideo(YoutubeLinkRequest.getYouTubeID(youTubeURL), currentPlaying);
//
//                }
//
//
//                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
//                val mydef = "Sr7e0LkUbmc"
//                mPlayer.loadVideo(mydef)
//
//
//              //  Toast.makeText(this@VideoNew, "onInitializationSuccess()", Toast.LENGTH_LONG).show()
//
//
//            }
//
//            override fun onInitializationFailure(
//                provider: YouTubePlayer.Provider,
//                youTubeInitializationResult: YouTubeInitializationResult
//            ) {
//                Toast.makeText(this@VideoNew, "onInitializationFailure()", Toast.LENGTH_LONG).show()
//            }
//        }


        youTubePlayerFragment =
            findViewById<com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView>(
                R.id.vidView)

//            getFragmentManager().findFragmentById(R.id.vidView) as YouTubePlayerFragment


        youTubePlayerFragment.enableAutomaticInitialization =
            false // We set it to false because we init it manually

//        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), monInitializedListener)

        // Disable iFrame UI
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerFragment.initialize(listener, options)
    }

//    private fun changeOrientation() {
//        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT)
//        {
//
//            //Toast.makeText(this,"Orientation is Portarait",Toast.LENGTH_LONG).show()
//            mPlayer.setFullscreen(true)
//
//        }
//    }

    override fun onBackPressed() {
        val orientation = this.resources.configuration.orientation
        if (orientation === Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else {

            super.onBackPressed()
            //mPlayer.pause()
        }
    }





//
//    override fun onAdStarted() {
//
//    }
//
//    override fun onLoading() {
////        Toast.makeText(this, "Video onload", Toast.LENGTH_LONG).show()
//        Log.d("videodd onload","video onload")
//    }
//
//    override fun onVideoStarted() {
////        Toast.makeText(this, "Video Started", Toast.LENGTH_LONG).show()
//        Log.d("videodd started","video started")
//    }
//
//    override fun onLoaded(p0: String?) {
//
//    }
//
//    override fun onVideoEnded() {
//        Log.d("videodd ended","video ended")
////        Toast.makeText(this, "Video Ended", Toast.LENGTH_LONG).show()
//        def_im.visibility=View.VISIBLE
////        card_orient2.visibility=View.INVISIBLE
//
//    }
//
//    override fun onError(p0: YouTubePlayer.ErrorReason?) {
//
//    }
    fun go_home(view: View) {
        startActivity(Intent(this, Home::class.java))
    }

//    fun getChapter() {
//        lateinit var name:String
//        var count=1
//val chaptName=findViewById<TextView>(R.id.chapName)
//
//        val PREFERENCE_FILE_KEY = "AppPreferencFrag"
//        val sharedPrefFrag = this.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)
//        val Fragname = "null"
//        val Fragmanual = sharedPrefFrag.getString(Fragname, "null")
//
//        var q=intent.getIntExtra("name",0)
//
//        val ref =
//            FirebaseDatabase.getInstance().getReference("RevisionTopic").child(uid)
//
//        ref.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
////                TODO("not implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
////                Log.d("data", p0.toString())
//                if (p0!!.exists()) {
//                    var chapkey = ""
//                    var mcqssubject = ""
//                    var modelchapter = ""
//                    var classIs = ""
//                    var stoper=1
//                    for (b in p0.children) {
//
//                        if(stoper==1){
//                            stoper++
//                            chapkey = b.child("chapkey").value.toString()
//                            classIs = b.child("classIs").value.toString()
//                            mcqssubject = b.child("mcqssubject").value.toString()
//                            modelchapter = b.child("modelchapter").value.toString()
//
//
/////--------------------------------------------------------------------------------------------------------------------------------
//                            val reff =
//                                FirebaseDatabase.getInstance().getReference("contents").child("books").child(classIs)
//                                    .child(mcqssubject).child(modelchapter).child("topics")
//                                    .child(chapkey)
//
//                            Log.d("mcqssubject", "revision mcqssubject :" + mcqssubject)
//                            Log.d("chapkey", "revision chapkey :" + chapkey)
//                            Log.d("modelchapter", "revision modelchapter :" + modelchapter)
//                            var x = 1
//                            Log.d("c", "fra1new :" + x++)
//
//                            reff.addListenerForSingleValueEvent(object : ValueEventListener {
//                                override fun onCancelled(p1: DatabaseError) {
////                                    TODO("not implemented")
//                                }
//
//                                override fun onDataChange(p1: DataSnapshot) {
////                Log.d("data", p0.toString())
//                                    if (p1.exists()) {
//
////                                    if(count==1) {
//
//                                        //---------------------------------------------------------------------------------emd
//                                        name = p1.child("name").value.toString()
//                                        val eng = p1.child("urlEng").value.toString()
//                                        val urdu = p1.child("urlUrdu").value.toString()
//
//                                        Log.d("testing ", "checking value of name :" + name)
//                                        Log.d("testing ", "checking value of eng :" + eng)
//                                        Log.d("testing ", "checking value of urdu :" + urdu)
//
//                                        // error yaha ha
//                                        if(name!=null) {
//                                            chaptName.setText(name)
//                                        }
//                                        //textTopic.setText(name)
//
//                                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
//                                        val editor = sharedPreferences.edit()
//                                        editor.putString("getmine",eng)
//                                        editor.apply()
//
//
//
//
//                                        chaptName.setOnClickListener {
//
//
//                                            val dialogBuilder =
//                                                AlertDialog.Builder(this@Revision)
//                                            dialogBuilder.setMessage("Please choose a language below.")
//                                                .setPositiveButton(
//                                                    "URDU",
//                                                    DialogInterface.OnClickListener { dialog, id ->
//
//                                                        val mediaSource: SimpleMediaSource =
//                                                            SimpleMediaSource(urdu)
//
//                                                        mPlayer.loadVideo(urdu)
//
//                                                    })
//
//                                                .setNegativeButton(
//                                                    "ENGLISH",
//                                                    DialogInterface.OnClickListener { dialog, id ->
//
//
//                                                        val mediaSource: SimpleMediaSource =
//                                                            SimpleMediaSource(eng)
//                                                        mPlayer.loadVideo(eng)
//
//                                                    })
//
//                                            val alert = dialogBuilder.create()
//                                            alert.show()
//
//
//                                        }
//
//
//                                    }
//
//
//                                }
//
//
//
//                            })
//                        }
//                    }//for end
//                }
//
//
//            }
//
//        })
//
//    }
    fun setCurrentItems(item: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(item, smoothScroll)
    }

    override fun onDataPas(lang: String) {
        if (mPlayer != null) {
//            frag_lin.visibility=View.VISIBLE
//            card_orient.visibility=View.VISIBLE
//            def_img.visibility= View.INVISIBLE
            mPlayer.cueVideo(lang, 0f)


//            mPlayer.setPlayerStateChangeListener(this)
        }
    }

//    override fun onBackPressed() {
//        val orientation = this.resources.configuration.orientation
//        if (orientation === Configuration.ORIENTATION_LANDSCAPE) {
//
//
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//            // Toast.makeText(this, "Rotate the screen to go back", Toast.LENGTH_SHORT).show()
////            dolayout()
////            onConfigurationChanged(Configuration.ORIENTATION_LANDSCAPE)
//        } else {
//
//            super.onBackPressed()
//            mPlayer.pause()
//        }
//    }

}