package learningapp.superior.org.VideoPlayerFragments


import Fragmentthree
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_topics_row.*
import kotlinx.android.synthetic.main.video_new.*
import learningapp.superior.org.Models.BookMArkModel
import learningapp.superior.org.Models.McqsModel
import learningapp.superior.org.Models.Model
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import learningapp.superior.org.ViewPagerAdapter
import java.util.*
import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.view.WindowManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import kotlinx.android.synthetic.main.activity_player.*
import learningapp.superior.org.Download.DownloadService
import learningapp.superior.org.Download.UriByteDataHelper
import learningapp.superior.org.Utils.VIDEO_ENCRYPTED_PASSWORD
import learningapp.superior.org.Utils.decodeFile
import learningapp.superior.org.Utils.generateKey
import learningapp.superior.org.Utils.getVideoDirPath
import java.io.File
import java.io.IOException


class VideoNew : AppCompatActivity(),
    Fragmentone.OnDataPass {
//    Fragmentone.OnDataPass, YouTubePlayer.PlayerStateChangeListener {


    private var mgr: DownloadManager? = null
    private val STORAGE_PERMISSION_CODE: Int = 1000
    lateinit var reff: DatabaseReference
    lateinit var ref: DatabaseReference

    //  var fullscreen = false
    var a: Int = 0
    var aa = 0
    var subjname: String = "null"
    var subjnamee: String = "null"
    var currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid
    var minute = 0
    private var fullscreen: Boolean = false

    //    lateinit var monInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var mPlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

    var link = "null"
    var linkName = "null"
    var VIDChecker = true

    var listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {}
    lateinit var youTubePlayerFragment: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_new)
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        card_orient.visibility=View.INVISIBLE
        isMyServiceRunning(DownloadService::class.java)
        //screenshoot
        // window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        val videeo =
            ("https://firebasestorage.googleapis.com/v0/b/exambites-application.appspot.com/o/9th%2Fchemistry%2Fchapter1b%2Fenglish%2FC1%20A11%20History%20of%20element%20E.m4v?alt=media&token=6bb9e51d-2fa0-464b-aece-e863182aa61b")

//        ImageSwi()


        //controls.player = playerView2.player
        ///--------------------------------------bookmark start
//        BookMarkIV.setOnClickListener {
//            BookMarkIV.setColorFilter(ContextCompat.getColor(this, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
//
//        }

        //-----------------------------------------------------------bootm mark end
        val model = intent.getParcelableExtra<Model>("object")
        val video =
            ("https://firebasestorage.googleapis.com/v0/b/exambites-application.appspot.com/o/naturevideo.mp4?alt=media&token=61a4df7f-cb7f-4965-b714-bbbfecaf4af7")
        val modl = intent.getParcelableExtra<McqsModel>("obj")

        val adapter =
            ViewPagerAdapter(supportFragmentManager)


        bookmarkVideoEmptyIV.setOnClickListener {
            bookmarkVideoFillIV.visibility = View.VISIBLE
            bookmarkVideoEmptyIV.visibility = View.GONE

            AddBookMark()
        }
        bookmarkVideoFillIV.setOnClickListener {
            bookmarkVideoFillIV.visibility = View.GONE
            bookmarkVideoEmptyIV.visibility = View.VISIBLE

            DeleteBookMark()
        }

        video_back.setOnClickListener {
            onBackPressed()

        }
        orient.setOnClickListener {

            changeOrientation()

        }
//        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//                val my_view = findViewById<View>(R.id.orient2)
//                my_view.visibility = View.VISIBLE
//
//        }
        val subject = intent.getIntExtra("subject", 0)
        when (subject) {
            1 -> {
                Log.d("Physics clicked ", "Physics clicked video")
                subjname = "physics"
                subjnamee = "subjects-1"
            }

            2 -> {
                Log.d("Chemis clicked", "Chemis clicked video")
                subjname = "chemistry"
                subjnamee = "chemistry"
            }

            3 -> {
                Log.d("biology clicked", "biology clicked video")
                subjname = "biology"
                subjnamee = "biology"
            }
        }

        val grade = intent.getStringExtra("grade")


        val bundle = Bundle()
        bundle.putParcelable("object", model)
        bundle.putInt("subject", subject)
        bundle.putString("grade", grade)

        val fragInfo =
            Fragmentone()
        fragInfo.setArguments(bundle)
        adapter.addFragment(fragInfo, "VIDEOS")

        bundle.putParcelable("obj", modl)
//        bundle.putString("modelchapter", modelchapter)
        val fragInfo2 =
            Fragmenttwo()
        fragInfo2.setArguments(bundle)
        adapter.addFragment(fragInfo2, "QUIZ")


        val fragInfo3 =
            Fragmentthree()
        fragInfo3.setArguments(bundle)
        adapter.addFragment(fragInfo3, "STATS")


        val fragInfo4 =
            NotesFragment()
        fragInfo4.setArguments(bundle)
        adapter.addFragment(fragInfo4, "NOTES")



        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
//        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                viewPager.adapter!!.notifyDataSetChanged()
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {}
//        })

        // viewPager.getAdapter()?.notifyDataSetChanged()

//        videoView.setOrientationListener {
//            dolayout()
//        }

        var totalVideos = "0"
        reff = FirebaseDatabase.getInstance().getReference("contents").child("books")
            .child(subjnamee).child(model!!.key)


        var count = 1

        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {
                    totalVideos = p0.child("totalVideos").value.toString()
                    Log.d("totalvideos videonew", "totalvideos" + totalVideos)

                    val UID: String = FirebaseAuth.getInstance().currentUser!!.uid

                    ref =
                        FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
                            .child(UID).child("books")
                            .child(subjname).child(model!!.key).child("topicWatch")
//


                    ref.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
//                            TODO("not implemented")
                        }

                        override fun onDataChange(p1: DataSnapshot) {
//                Log.d("data", p0.toString())
                            if (p1.exists()) {

                                if (count == 1) {
                                    count++

                                    for (h in p1.children) {
                                        aa++
                                    }

                                    Log.d("totalvideos videonew aa", "totalvideos" + totalVideos)
                                    Log.d("value of aa", "value of aa " + aa)

                                    val percentage = ((100 * aa) / totalVideos.toInt())
                                    Log.d("percentage ", " of videos " + percentage.toString())

                                    val b = totalVideos.toInt() - aa

//     working done
                                    val text = findViewById<TextView>(R.id.textView16)
                                    text?.text = "$percentage% Complete, $b Videos Remaining"

//    working done
                                    if (percentage <= 0) {

                                    } else {
                                        progress_bar_1.progress = percentage

                                    }

                                }


                            } else {
//                                textView16.setText("0% Complete, " + totalVideos.toString() +(" Videos Remaining"))

                            }


                        }


                    })


                }
            }

        })

        listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                // We're using pre-made custom ui

                Log.d("onready", "hello on ready ")
                mPlayer = youTubePlayer
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
                Log.d("LOG", "hello onVideoDuration")

            }

            override fun onVideoLoadedFraction(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                loadedFraction: Float
            ) {
                super.onVideoLoadedFraction(youTubePlayer, loadedFraction)
//                Log.d("LOG", "hello onVideoLoadedFraction" )

            }

        }

        releasePlayer()
        frag_lin.visibility = View.VISIBLE
        card_orient.visibility = View.VISIBLE
        def_img.visibility = View.INVISIBLE
        playerView2.visibility = View.GONE

        //Toast.makeText(this, "Video Started", Toast.LENGTH_LONG).show()
        Log.d("videodd started", "video started")
    }

    private fun checkBookMarkBackend() {
//        val bookmarkEmptyIV = view?.findViewById<ImageView>(R.id.bookmarkEmptyIV)
//        bookmarkEmptyIV?.visibility = View.VISIBLE
//
//        val bookmarkFillIV = view?.findViewById<ImageView>(R.id.bookmarkFillIV)
//        bookmarkFillIV?.visibility = View.GONE
        val bookmarkVideoEmptyIV = findViewById<ImageView>(R.id.bookmarkVideoEmptyIV)
        val bookmarkVideoFillIV = findViewById<ImageView>(R.id.bookmarkVideoFillIV)

        val refOfBookmark = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(currentUserDataId)

        refOfBookmark.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (a in snapshot.children) {
                        val detail = BookMArkModel(
                            a.child("VID").value.toString(),
                            a.child("VideoName").value.toString()
                        )

                        Log.d("detailsKiVideoDetail", detail.VID.toString())
                        Log.d("detailsKiVideoLink", link)

                        // bookMarklist.add(detail)
                        if (detail.VID == link) {
                            bookmarkVideoEmptyIV?.visibility = View.GONE

                            Log.d("detail.VIDVideo == link", link)
                            VIDChecker = false
                            bookmarkVideoFillIV?.visibility = View.VISIBLE
                        } else if (detail.VID != link && VIDChecker) {
                            Log.d("detail.VIDVideo != link", link)

                            bookmarkVideoEmptyIV?.visibility = View.VISIBLE
                            bookmarkVideoFillIV?.visibility = View.GONE
                        }

                    }


                }
            }
        })


    }

    private fun AddBookMark() {
        val refOfBook = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(currentUserDataId).child(link)
        val userMap = HashMap<Any, Any>()
        userMap["VID"] = link
        userMap["VideoName"] = linkName

        refOfBook.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Something Went Wrong:(", Toast.LENGTH_LONG)
                        .show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }

    }

    private fun DeleteBookMark() {
        val refOfBook = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(currentUserDataId).child(link)


        refOfBook.setValue(null)
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
                R.id.vidView
            )

//            getFragmentManager().findFragmentById(R.id.vidView) as YouTubePlayerFragment


        youTubePlayerFragment.enableAutomaticInitialization =
            false // We set it to false because we init it manually

//        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), monInitializedListener)

        // Disable iFrame UI
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        youTubePlayerFragment.initialize(listener, options)
    }

    private fun changeOrientation() {
        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT) {


            //Toast.makeText(this,"Orientation is Portarait",Toast.LENGTH_LONG).show()

//            mPlayer.setFullscreen(true)


        }
    }


//    override fun onConfigurationChanged(newConfig:Configuration) {
//        super.onConfigurationChanged(newConfig)
//        // Checks the orientation of the screen
//
//    }


    override fun onDataPass(data: String, name: String, items: ModelForTopics, lang: String) {


//        val youTubeView = findViewById<YouTubePlayerView>(R.id.vidView)
//        frag_lin.visibility=View.VISIBLE

        if (data != "true") {
            Log.d("LOG", "hello 1 data " + data)
//            if (mPlayer != null ) {
            if (::mPlayer.isInitialized) {
//                mPlayer.loadVideo(data)

                mPlayer.cueVideo(data, 0f)

//                mPlayer.setPlayerStateChangeListener(this)
                link = data
                linkName = name
             //   VIDChecker == true
               // checkBookMarkBackend()

            }
            else{
                Toast.makeText(this, "Youtube Player is not isInitialized", Toast.LENGTH_SHORT).show()
            }



            releasePlayer()
            frag_lin.visibility = View.VISIBLE
            card_orient.visibility = View.VISIBLE
            def_img.visibility = View.INVISIBLE
            playerView2.visibility = View.GONE

            //Toast.makeText(this, "Video Started", Toast.LENGTH_LONG).show()
            Log.d("videodd started", "video started")
        } else {
            playerView2.visibility = View.VISIBLE

            def_img.visibility = View.INVISIBLE
            releasePlayer()
            dataOfModel = items
            initializePlayer(lang)
            // Toast.makeText(this, "downloaded", Toast.LENGTH_SHORT).show()
        }

        val myData = data
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val editor = sharedPreferences.edit()
        editor.putString("my", data)
        editor.apply()


        incrementWatchTime()


    }


    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var dataOfModel: ModelForTopics
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private fun initializePlayer(lang: String) {
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView2.player = exoPlayer
        Log.d("playActData", "initializePlayer")
        frag_lin.visibility = View.GONE
        // releasePlayer()
        val mediaSource = buildMediaSource(Uri.parse(dataOfModel.engUrlDownload), lang)

        exoPlayer!!.playWhenReady = playWhenReady
        exoPlayer!!.seekTo(currentWindow, playbackPosition)
        exoPlayer!!.prepare(mediaSource!!, false, false)
    }

    private fun buildMediaSource(uri: Uri, lang: String): MediaSource? {
        Log.d("playActData", "buildMediaSource")

        // releasePlayer()
        dataOfModel.isDownloaded = true
        if (dataOfModel.isDownloaded) {
            Log.d("playActData", "if (data.isDownloaded)")

            val uri = getVideoDirPath(this)
//            val file = File(data.urlDownload)
            val videoFile = if (lang == "urdu") {
                File("${uri.absolutePath}/${dataOfModel.name}_UrduExambites.encvid")
            } else {
                File("${uri.absolutePath}/${dataOfModel.name}_exambites.encvid")
            }
            card_orient.visibility = View.GONE
            val file = File(videoFile.toURI())
            Log.d("playActDatafile", file.toString())

            lateinit var factory: DataSource.Factory
            lateinit var videoByteUri: Uri

            try {
                val byteArr = decodeFile(generateKey(VIDEO_ENCRYPTED_PASSWORD), file.readBytes())!!
                val dataSource = ByteArrayDataSource(byteArr)
                videoByteUri = UriByteDataHelper().getUri(file.readBytes())
                val dataSpec = DataSpec(videoByteUri)
                dataSource.open(dataSpec)
                factory = DataSource.Factory { dataSource }

            } catch (e: IOException) {
                Log.d("playActDataCatch", e.toString())
                e.printStackTrace()
            }
            factory.createDataSource()

            return ProgressiveMediaSource.Factory(factory).createMediaSource(videoByteUri)

        } else {
            Log.d("playActData", "else")

            val dataSourceFactory: DataSource.Factory =
                DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, getString(R.string.app_name))
                )
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }

    private fun releasePlayer() {

        if (exoPlayer != null) {
            playWhenReady = exoPlayer!!.playWhenReady
            playbackPosition = exoPlayer!!.currentPosition
            currentWindow = exoPlayer!!.currentWindowIndex
            exoPlayer!!.release()
            exoPlayer = null
        }
    }

    fun incrementWatchTime() {


        var count = 1
        Log.d(
            "counter ",
            "counter  " + count
        )
        var codeValue: String
        var TotalNum: String


        val code: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        Log.d("minute log ", "log for minute finish " + minute)
        //--------------------------------------
//            var code: DatabaseReference
//            code =
//                FirebaseDatabase.getInstance().getReference("ParentalCode").child(currentUserDataId)

        code.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
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


                    val StudentcodeGet: DatabaseReference =
                        FirebaseDatabase.getInstance().getReference("StudentsDataForParent")
                            .child(codeValue).child("TotalVideoWatchTime")

                    StudentcodeGet.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
//                            TODO("not implemented")
                        }

                        override fun onDataChange(p0: DataSnapshot) {
//                                    for (h in p0.children) {
                            if (p0!!.exists() && count == 1) {
//                                            if (count==1) {
                                count++
                                TotalNum = p0.child("TotalVideoWatchTime").value.toString()

                                Log.d(
                                    "get TotalVideoWatchTime",
                                    "TotalVideoWatchTime get " + p0.child("TotalVideoWatchTime").value.toString()
                                )

//                                            if(TotalNum!=null){

                                val userDataRef: DatabaseReference =
                                    FirebaseDatabase.getInstance()
                                        .getReference("StudentsDataForParent").child(codeValue)


                                val userMap = HashMap<Any, Any>()
                                userMap["TotalVideoWatchTime"] = TotalNum.toInt() + 2

                                userDataRef.child("TotalVideoWatchTime").setValue(userMap)

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
                                userMap["TotalVideoWatchTime"] = 0

                                userDataRef.child("TotalVideoWatchTime").setValue(userMap)

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
                }
//                    }
            }

        })
    }


//    fun onFullscreen(isFullScreen: Boolean) {
//        fullscreen = isFullScreen
//        dolayout()
//    }

//    private fun dolayout() {
//        val videoViewParams =
//            videoView.layoutParams
//        if (fullscreen) {
//            // When in fullscreen, the visibility of all other views than the player should be set to
//            // GONE and the player should be laid out across the whole screen.
//            videoViewParams.width = WindowManager.LayoutParams.FLAG_FULLSCREEN
//            videoViewParams.height = WindowManager.LayoutParams.FLAG_FULLSCREEN
//
//            val myview = findViewById<View>(R.id.video_back)
////            myview.visibility =
////                if (resources.configuration.orientation == SENSOR_LANDSCAPE) View.GONE else View.VISIBLE
//
//            val tab = findViewById<View>(R.id.tabs)
//            tab.visibility =
//                if (resources.configuration.orientation == SENSOR_LANDSCAPE) View.GONE else View.VISIBLE
//
//            val topicName = findViewById<TextView>(R.id.textView3)
//            topicName.visibility =
//                if (resources.configuration.orientation == SENSOR_LANDSCAPE) View.GONE else View.VISIBLE
////            topicName.setText(model!!.name+" (9th-1)")
//            val topcs = findViewById<RecyclerView>(R.id.recyclerViewTopics)
//            topcs.visibility =
//                if (resources.configuration.orientation == SENSOR_LANDSCAPE) View.GONE else View.VISIBLE
//
//
//        }
//
//
//    }

    //private var paramsNotFullscreen: ConstraintLayout.LayoutParams? = null

    //    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) //To fullscreen
//        {
//
//            val myview = findViewById<View>(R.id.video_back)
////            myview.visibility =
////                if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) View.GONE else View.VISIBLE
//
//
////
//            //paramsNotFullscreen = videoView.getLayoutParams() as ConstraintLayout.LayoutParams?
//
//            val params: ConstraintLayout.LayoutParams =
//                ConstraintLayout.LayoutParams(paramsNotFullscreen)
//
//            params.setMargins(0, 0, 0, 0)
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT
//
//            videoView.setLayoutParams(params)
//        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
//            videoView.layoutParams = paramsNotFullscreen
//
////            video_back.visibility = if (newConfig.orientation !== Configuration.ORIENTATION_LANDSCAPE) View.VISIBLE else View.VISIBLE
//
//        }
//    }
//

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        viewPager.setCurrentItem(item, smoothScroll)
    }

    override fun onDestroy() {
        releasePlayer()

        super.onDestroy()

    }

    override fun onBackPressed() {
        val orientation = this.resources.configuration.orientation
        if (orientation === Configuration.ORIENTATION_LANDSCAPE) {


            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            // Toast.makeText(this, "Rotate the screen to go back", Toast.LENGTH_SHORT).show()
//            dolayout()
//            onConfigurationChanged(Configuration.ORIENTATION_LANDSCAPE)
        } else {

            super.onBackPressed()
            if (this::mPlayer.isInitialized) {
                mPlayer.pause()
            }
            finish()
            releasePlayer()
        }

    }

//    override fun onAdStarted() {
//
//    }

//    override fun onLoading() {
//        Toast.makeText(this, "Video is about to play", Toast.LENGTH_SHORT).show()
//        Log.d("video onload", "video onload")
//    }

//     override fun onVideoStarted() {
//        releasePlayer()
//        frag_lin.visibility = View.VISIBLE
//        card_orient.visibility = View.VISIBLE
//        def_img.visibility = View.INVISIBLE
//        playerView2.visibility = View.GONE
//
//        //Toast.makeText(this, "Video Started", Toast.LENGTH_LONG).show()
//        Log.d("videodd started", "video started")
//    }
//
//    override fun onLoaded(p0: String?) {
//        Log.d("video onloaded", "video onloaded")
//
//    }
//
//    override fun onVideoEnded() {
//        Log.d("videodd ended", "video ended")
////        Toast.makeText(this, "Video Ended", Toast.LENGTH_LONG).show()
//
//
//        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_LANDSCAPE) {
//
//            mPlayer.seekToMillis(0)
//            mPlayer.pause()
//
//            mPlayer.setFullscreen(false)
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//
//        def_img.visibility = View.VISIBLE
//        card_orient.visibility = View.INVISIBLE
//        frag_lin.visibility = View.INVISIBLE
//
//
//        (this as VideoNew?)?.setCurrentItem(1, true)
//    }
//
//    override fun onError(p0: YouTubePlayer.ErrorReason?) {
//
//    }
//

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.d("testingService", "true")
                return true
            }
        }
        Log.d("testingService", "false")

        return false
    }


    override fun onPause() {
        super.onPause()

        releasePlayer()

    }

    override fun onStop() {
        super.onStop()

        releasePlayer()

    }
}
