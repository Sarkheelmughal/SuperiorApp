package learningapp.superior.org

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerReadyListener
import com.ct7ct7ct7.androidvimeoplayer.listeners.VimeoPlayerStateListener
import kotlinx.android.synthetic.main.vimeo_video.*


class VimeoVideoTeting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vimeo_video)
        setupView()
//        VimeoExtractor.getInstance()
//            .fetchVideoWithURL("https://https://vimeo.com/228773975", null, object : OnVimeoExtractionListener {
//                override fun onSuccess(video: VimeoVideo) {
//                    val hdStream = video.streams["720p"]
//
//                    Log.d("resultVideo ","onSuccess")
//                    hdStream?.let{
//                        playVideo(hdStream)
//                    }
//                    //...
//                }
//
//                override fun onFailure(throwable: Throwable) {
//                    //Error handling here
//                    Log.d("resultVideo ","onFail")
//
//                }
//            })
    }

    fun playVideo(videoStream: String) {
        runOnUiThread {
//            progressBar.visibility=View.GONE //hide progressBar
//            vimeoVideo.setBackgroundResource(R.drawable.logo)
//            vimeoVideo.setVideoPath(videoStream)
//            val video = Uri.parse(videoStream)
//            vimeoVideo.setVideoURI(video)
//            vimeoVideo.requestFocus()
//            vimeoVideo.setOnPreparedListener { mp ->
//                mp.isLooping = true
//                vimeoVideo.start()
//            }

           // val mediaItem: MediaItem = MediaItem.fromUri(url)
            // Set the media item to be played.
            // Set the media item to be played.
           // vimeoVideo.player.url(video)
            // Prepare the player.
            // Prepare the player.
            //vimeoVideo.player.prepare()
            // Start the playback.
            // Start the playback.
           // vimeoVideo.player.play()
        }
    }

    private fun setupView() {
        lifecycle.addObserver(vimeoPlayer)
        //228773975
//        vimeoPlayer.initialize( 59777392)
        vimeoPlayer.initialize( 228773975)
        //If video is open. but limit playing at embedded.
      //  vimeoPlayer.initialize(true, {YourPrivateVideoId}, "SettingsEmbeddedUrl")

//If video is pirvate.
       // vimeoPlayer.initialize(true, {YourPrivateVideoId},"VideoHashKey", "SettingsEmbeddedUrl")


       // vimeoPlayer.initialize(228773975, "https://video/228773975")

        vimeoPlayer.addTimeListener { second ->
            playerCurrentTimeTextView.text = getString(R.string.player_current_time, second.toString())
        }

        vimeoPlayer.addErrorListener { message, method, name ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        vimeoPlayer.addReadyListener(object : VimeoPlayerReadyListener {


            override fun onReady(title: String?, duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onReady")

            }

            override fun onInitFailed() {
                playerStateTextView.text = getString(R.string.player_state, "onInitFailed")
            }
        })

        vimeoPlayer.addStateListener(object : VimeoPlayerStateListener {
            override fun onPlaying(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPlaying")
            }

            override fun onPaused(seconds: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onPaused")
            }

            override fun onEnded(duration: Float) {
                playerStateTextView.text = getString(R.string.player_state, "onEnded")
            }
        })

        volumeSeekBar.progress = 100
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var volume = progress.toFloat() / 100
                vimeoPlayer.setVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        vimeoPlayer.addVolumeListener { volume ->
            playerVolumeTextView.text = getString(R.string.player_volume, volume.toString())
        }

        playButton.setOnClickListener {
            vimeoPlayer.play()
        }

        pauseButton.setOnClickListener {
            vimeoPlayer.pause()
        }

        getCurrentTimeButton.setOnClickListener {
            Toast.makeText(this, getString(R.string.player_current_time, vimeoPlayer.currentTimeSeconds.toString()), Toast.LENGTH_LONG).show()
        }

        loadVideoButton.setOnClickListener {
            vimeoPlayer.loadVideo(19231868)
        }

        colorButton.setOnClickListener {
            vimeoPlayer.topicColor = Color.GREEN
        }
    }
}