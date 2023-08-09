package learningapp.superior.org.SideMenu

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_book_mark_screen.*
import learningapp.superior.org.Adapter.BookMarkAdapter
import learningapp.superior.org.Adapter.OnMarkClick
import learningapp.superior.org.Models.BookMArkModel
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.Config
import learningapp.superior.org.VideoPlayerFragments.VideoNew

class BookMarkScreen : AppCompatActivity(), OnMarkClick ,YouTubePlayer.PlayerStateChangeListener {

    var currentUserDataId = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var monInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var mPlayer: YouTubePlayer
    lateinit var recycleView: RecyclerView
    lateinit var chapterlist: ArrayList<BookMArkModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark_screen)

        orient.setOnClickListener {

            changeOrientation()

        }
        back_card_bookmarkCV.setOnClickListener {

            onBackPressed()

        }
        getlist()
    }

    private fun getlist() {
        recycleView = findViewById(R.id.recyclerBookMarkRV)
        chapterlist = ArrayList<BookMArkModel>()

        val manager = LinearLayoutManager(this@BookMarkScreen)
        recycleView.layoutManager = manager

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerBookMarkRV)
        var reff: DatabaseReference
        reff = FirebaseDatabase.getInstance().getReference("BookMarks")
            .child(currentUserDataId)
        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (a in snapshot.children) {


                        val chap = BookMArkModel(
                            a.child("VID").value.toString(),
                            a.child("VideoName").value.toString()

                        )
//                        Log.d("For loop", h.child("name").toString())
                        chapterlist.add(chap)

                    }

                    val adapter =
                        BookMarkAdapter(
                            applicationContext,
                            this@BookMarkScreen,
                            chapterlist
                        )
                    recycleView.adapter = adapter


                }
            }

        })

    }

    override fun onStart() {
        super.onStart()

        monInitializedListener = object : YouTubePlayer.OnInitializedListener {


            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {


                val sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(applicationContext)
                val text = sharedPreferences.getString("my", "")
                if (!wasRestored) {


                    mPlayer = player


                }


                player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                val mydef = "Sr7e0LkUbmc"
                mPlayer.loadVideo(mydef)
                mPlayer.setPlayerStateChangeListener(this@BookMarkScreen)


            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider,
                youTubeInitializationResult: YouTubeInitializationResult
            ) {
                Toast.makeText(this@BookMarkScreen, "onInitializationFailure()", Toast.LENGTH_LONG)
                    .show()
            }
        }


        val youTubePlayerFragment =
            getFragmentManager().findFragmentById(R.id.vidView) as YouTubePlayerFragment
        youTubePlayerFragment.initialize(Config.getYoutubeApiKey(), monInitializedListener)


    }


    private fun changeOrientation() {
        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT) {


            //Toast.makeText(this,"Orientation is Portarait",Toast.LENGTH_LONG).show()

            mPlayer.setFullscreen(true)


        }
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
            mPlayer.pause()
        }
    }

    override fun onAdStarted() {

    }

    override fun onLoading() {
        Toast.makeText(this, "Video is about to play", Toast.LENGTH_SHORT).show()
        Log.d("video onload", "video onload")
    }

    override fun onVideoStarted() {

        frag_lin.visibility = View.VISIBLE
        card_orient.visibility = View.VISIBLE
        def_img.visibility = View.INVISIBLE
//        Toast.makeText(this, "Video Started", Toast.LENGTH_LONG).show()
        Log.d("videodd started", "video started")
    }

    override fun onLoaded(p0: String?) {

    }

    override fun onVideoEnded() {
        Log.d("videodd ended", "video ended")
//        Toast.makeText(this, "Video Ended", Toast.LENGTH_LONG).show()


        if (getResources().getConfiguration().orientation === Configuration.ORIENTATION_LANDSCAPE) {

            mPlayer.seekToMillis(0)
            mPlayer.pause()

            mPlayer.setFullscreen(false)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        def_img.visibility = View.VISIBLE
        card_orient.visibility = View.INVISIBLE
        frag_lin.visibility = View.INVISIBLE

        (this as VideoNew?)?.setCurrentItem(1, true)
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {

    }

    override fun onItemClick(list: BookMArkModel, position: Int) {

        mPlayer.loadVideo(list.VID)
        mPlayer.setPlayerStateChangeListener(this@BookMarkScreen)

    }
}