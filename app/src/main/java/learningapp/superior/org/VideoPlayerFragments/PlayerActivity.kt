package learningapp.superior.org.VideoPlayerFragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.ByteArrayDataSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*
import learningapp.superior.org.Download.UriByteDataHelper
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import learningapp.superior.org.Utils.VIDEO_ENCRYPTED_PASSWORD
import learningapp.superior.org.Utils.decodeFile
import learningapp.superior.org.Utils.generateKey
import learningapp.superior.org.Utils.getVideoDirPath
import java.io.File
import java.io.IOException


class PlayerActivity : AppCompatActivity() {
    private var exoPlayer: SimpleExoPlayer? = null
   private lateinit var data: ModelForTopics
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        data = intent.getParcelableExtra<ModelForTopics>("data")!! as ModelForTopics

        Log.d("playActDataOnCreate",data.toString())
    }

    private fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = exoPlayer
        Log.d("playActData","initializePlayer")

        val mediaSource = buildMediaSource(Uri.parse(data.engUrlDownload))

        exoPlayer!!.playWhenReady = playWhenReady
        exoPlayer!!.seekTo(currentWindow, playbackPosition)
        exoPlayer!!.prepare(mediaSource!!, false, false)
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        Log.d("playActData","buildMediaSource")

        data.isDownloaded=true
        if (data.isDownloaded) {
            Log.d("playActData","if (data.isDownloaded)")

            val uri= getVideoDirPath(this)
//            val file = File(data.urlDownload)
            val videoFile =
                File("${uri.absolutePath}/${data.name}_exambites.encvid")

            val file = File(videoFile.toURI())
            Log.d("playActDatafile",file.toString())

            lateinit var factory : DataSource.Factory
            lateinit var videoByteUri : Uri

            try {
                val byteArr = decodeFile(generateKey(VIDEO_ENCRYPTED_PASSWORD),file.readBytes())!!
                val dataSource = ByteArrayDataSource(byteArr)
                videoByteUri = UriByteDataHelper().getUri(file.readBytes())
                val dataSpec = DataSpec(videoByteUri)
                dataSource.open(dataSpec)
                factory = DataSource.Factory {dataSource}

            } catch (e: IOException) {
                Log.d("playActDataCatch",e.toString())
                e.printStackTrace()
            }
            factory.createDataSource()

            return ProgressiveMediaSource.Factory(factory).createMediaSource(videoByteUri)

        } else {
            Log.d("playActData","else")

            val dataSourceFactory: DataSource.Factory =
                DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this, getString(R.string.app_name))
                )
            return ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
        }
    }


    private fun hideSystemUi() {
        playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
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

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24 || exoPlayer == null) {
            initializePlayer()
        }
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

}