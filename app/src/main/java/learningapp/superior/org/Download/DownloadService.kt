package learningapp.superior.org.Download

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import learningapp.superior.org.Utils.*
import java.net.URL
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException


class DownloadService : Service() {
    private val mBinder = LocalBinder()

    private val downloadingVideoIdList = mutableListOf<String>()
    private val percentageBackList = mutableListOf<(progress: Float, total: Int) -> Unit>()

    override fun onBind(intent: Intent?): IBinder? = mBinder
    override fun onCreate() {
        super.onCreate()
        downloadingVideoIdList.clear()
        notificationIdKeys.clear()
        notificationIds.clear() }
    inner class LocalBinder : Binder() {
        val serviceInstance: DownloadService
            get() = this@DownloadService }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    @SuppressLint("LogNotTimber")
    fun test(){
        Thread{
            Log.d("serviceTest","run")
        }.start()
    }
    lateinit var context :Context
    fun downloadFile(
        ctx: Context,
        videoData: ModelForTopics,
        percentageBack: (progress: Float, total: Int) -> Unit,
        callback: (Int) -> Unit
    ) {
        context=ctx
        Log.d("downloadingVideoTry", "out ThreadStart")
        Thread {
            Log.d("downloadingVideoTry", "in ThreadStart")

            val videoParentFile = getVideoDirPath(ctx)
            val thumbsParentFile = getThumbsDirPath(ctx)

            val videoFile =
//                File("${videoParentFile.absolutePath}/${videoData.name}###${videoData.videoID}.encvid")
                File("${videoParentFile.absolutePath}/${videoData.name}_exambites.encvid")

            val videoFileUrdu =
                File("${videoParentFile.absolutePath}/${videoData.name}_UrduExambites.encvid")


            Log.d("pathOfVideoFileDS",videoFile.toString())

            val thumbFile =
//                File("${thumbsParentFile.absolutePath}/${videoData.name}###${videoData.videoID}.jpg")
                File("${thumbsParentFile.absolutePath}/${videoData.name}_exambites.jpg")

            if (videoFile.exists() && thumbFile.exists() && videoFileUrdu.exists()) {
                if (downloadingVideoIdList.isEmpty()) {
                    stopSelf()
                }
                callback(-1)
                return@Thread
            }


            if (downloadingVideoIdList.contains(videoData.name)) {
                callback(0)
                return@Thread
            }

            downloadingVideoIdList.add(videoData.name)
            percentageBackList.add(percentageBack)

            callback(2)
            val notificationBuilder = showDownloadingNotification(videoData)

            val downloadingVideoFile =
                File("${videoParentFile.absolutePath}/${videoData.name}_exambites.downvid")
            val downloadingVideoFileUrdu =
                File("${videoParentFile.absolutePath}/${videoData.name}_UrduExambites.downvid")

            val downloadThumbFile =
                File("${thumbsParentFile.absolutePath}/${videoData.name}_exambites.downthumb")

            if (thumbFile.exists())
                thumbFile.delete()

            if (downloadThumbFile.exists())
                downloadThumbFile.delete()


            try {
                if (!videoFile.exists()) {
                    Log.d("resultCallBackVideo","try if !videoFile.exists()")
                    val url = URL(videoData.engUrlDownload)
                    Log.d("resultCallBackVideoMore",url.toString())
                    val httpUrlConn = url.openConnection()
                    httpUrlConn.connect()

                    val bufferedInputStream = BufferedInputStream(httpUrlConn.getInputStream())

                    lateinit var fileOutputStream: FileOutputStream
                    var skippedBytes = 0L
                    if (downloadingVideoFile.exists()) {
                        skippedBytes = downloadingVideoFile.length()
                        Log.d("resultCallBackVideoMore","downloadingVideoFile.exists()")

                        var s00 = bufferedInputStream.skip(skippedBytes)

                        var skip00 = skippedBytes - s00

                        while (s00 != skippedBytes) {
                            val s22 = bufferedInputStream.skip(skip00)
                            s00 += s22
                            skip00 -= s22
                        }

                        if (s00 == 0L) {
                            skippedBytes = 0
                            downloadingVideoFile.delete()
                            downloadThumbFile.delete()
                        }

                        fileOutputStream = FileOutputStream(downloadingVideoFile, true)
                    }
                    else {
                        Log.d("resultCallBackVideoMore","downloadingVideoFile.exists() else")

                        fileOutputStream = FileOutputStream(downloadingVideoFile)
                    }

                    val totalLen = httpUrlConn.contentLength
                    var downloaded = (0 + skippedBytes).toFloat()
                    var downloadedPercentage: Int
                    var i = 0
                    var isWantToSend = 50

                    downloadedPercentage = ((downloaded / totalLen) * 100).toInt()

                    percentageBack(downloaded, totalLen)
                    updateProgressOnNotification(
                        notificationBuilder.setContentTitle("Downloading ${videoData.name} | $downloadedPercentage%"),
                        videoData,
                        downloaded.toInt(),
                        totalLen
                    )

                    val buffer = ByteArray(1024)
                    while (run {
                            i = bufferedInputStream.read(buffer)
                            i
                        } != -1) {
                        fileOutputStream.write(buffer, 0, i)
                        downloaded += i
                        downloadedPercentage = ((downloaded.toDouble() / totalLen) * 100).toInt()

                        if (isWantToSend == 0) {
                            percentageBack(downloaded, totalLen)
                            updateProgressOnNotification(
                                notificationBuilder.setContentTitle("Downloading ${videoData.name} | $downloadedPercentage%"),
                                videoData,
                                downloaded.toInt(),
                                totalLen
                            )
                            isWantToSend = 25
                        } else
                            isWantToSend--

                    }

                    bufferedInputStream.close()
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    val encoded = encodeFile(
                        generateKey(VIDEO_ENCRYPTED_PASSWORD),
                        downloadingVideoFile.readBytes()
                    )

                    val bufferedOutputStream =
                        BufferedOutputStream(FileOutputStream(downloadingVideoFile))
                    bufferedOutputStream.write(encoded!!)
                    bufferedOutputStream.flush()
                    bufferedOutputStream.close()

                    downloadingVideoFile.renameTo(videoFile)
                }
                val url = URL(videoData.urduUrlDownload)

                if (!videoFileUrdu.exists() && (url.toString() != "" || url.toString() != null || url.toString().isNotEmpty())) {
                    Log.d("resultCallBackVideo","try if !videoFile.exists()")
                    val url = URL(videoData.urduUrlDownload)


                    Log.d("resultCallBackVideoMore",url.toString())
                    val httpUrlConn = url.openConnection()
                    httpUrlConn.connect()

                    val bufferedInputStream = BufferedInputStream(httpUrlConn.getInputStream())

                    lateinit var fileOutputStream: FileOutputStream
                    var skippedBytes = 0L
                    if (downloadingVideoFileUrdu.exists()) {
                        skippedBytes = downloadingVideoFileUrdu.length()
                        Log.d("resultCallBackVideoMore","downloadingVideoFileUrdu.exists()")

                        var s00 = bufferedInputStream.skip(skippedBytes)

                        var skip00 = skippedBytes - s00

                        while (s00 != skippedBytes) {
                            val s22 = bufferedInputStream.skip(skip00)
                            s00 += s22
                            skip00 -= s22
                        }

                        if (s00 == 0L) {
                            skippedBytes = 0
                            downloadingVideoFileUrdu.delete()
                            downloadThumbFile.delete()
                        }

                        fileOutputStream = FileOutputStream(downloadingVideoFileUrdu, true)
                    }
                    else {
                        Log.d("resultCallBackVideoMore","downloadingVideoFileUrdu.exists() else")

                        fileOutputStream = FileOutputStream(downloadingVideoFileUrdu)
                    }

                    val totalLen = httpUrlConn.contentLength
                    var downloaded = (0 + skippedBytes).toFloat()
                    var downloadedPercentage: Int
                    var i = 0
                    var isWantToSend = 50

                    downloadedPercentage = ((downloaded / totalLen) * 100).toInt()

                    percentageBack(downloaded, totalLen)
                    updateProgressOnNotification(
                        notificationBuilder.setContentTitle("Downloading ${videoData.name} | $downloadedPercentage%"),
                        videoData,
                        downloaded.toInt(),
                        totalLen
                    )

                    val buffer = ByteArray(1024)
                    while (run {
                            i = bufferedInputStream.read(buffer)
                            i
                        } != -1) {
                        fileOutputStream.write(buffer, 0, i)
                        downloaded += i
                        downloadedPercentage = ((downloaded.toDouble() / totalLen) * 100).toInt()

                        if (isWantToSend == 0) {
                            percentageBack(downloaded, totalLen)
                            updateProgressOnNotification(
                                notificationBuilder.setContentTitle("Downloading ${videoData.name} | $downloadedPercentage%"),
                                videoData,
                                downloaded.toInt(),
                                totalLen
                            )
                            isWantToSend = 25
                        } else
                            isWantToSend--

                    }

                    bufferedInputStream.close()
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    val encoded = encodeFile(
                        generateKey(VIDEO_ENCRYPTED_PASSWORD),
                        downloadingVideoFileUrdu.readBytes()
                    )

                    val bufferedOutputStream =
                        BufferedOutputStream(FileOutputStream(downloadingVideoFileUrdu))
                    bufferedOutputStream.write(encoded!!)
                    bufferedOutputStream.flush()
                    bufferedOutputStream.close()

                    downloadingVideoFileUrdu.renameTo(videoFileUrdu)
                }
              else{
                  Toast.makeText(this, "Urdu video is not available !", Toast.LENGTH_SHORT).show()
                }
                percentageBack(100f, 100)
                updateProgressOnNotification(
                    notificationBuilder.setContentTitle("Downloading is almost done.."),
                    videoData,
                    100,
                    100
                )

//                thumbnail working fine just commented because not needed.. code1 start
//                if (videoData.videoThumbUri == null || videoData.videoThumbUri == "null") {
//                    val fos = FileOutputStream(downloadThumbFile)
//                    generateThumbnail(videoData.videoThumbUri)?.compress(
//                        Bitmap.CompressFormat.PNG,
//                        90,
//                        fos
//                    )
//                    fos.close()
//                    downloadThumbFile.renameTo(thumbFile)
//                }
//                else {
//                    val thumbUrl = URL(videoData.videoThumbUri)
//                    val urlConn = thumbUrl.openConnection()
//                    urlConn.connect()
//
//                    val thumbInputStream = BufferedInputStream(urlConn.getInputStream())
//
//                    val thumbOutputStream = FileOutputStream(downloadThumbFile)
//
//                    var thumbI = 0
//                    val thumbBuffer = ByteArray(1024)
//
//                    while ({ thumbI = thumbInputStream.read(thumbBuffer);thumbI }() != -1) {
//                        thumbOutputStream.write(thumbBuffer, 0, thumbI)
//                    }
//
//                    thumbInputStream.close()
//                    thumbOutputStream.flush()
//                    thumbOutputStream.close()
//
//                    downloadThumbFile.renameTo(thumbFile)
//                }
//------thumbnail working fine just commented because not needed.. code1 end
                updateProgressOnNotification(
                    notificationBuilder.apply{
                        setContentTitle("Download Success")
                        setContentText(videoData.name)
                        setOngoing(false)
                    },
                    videoData,
                    0,
                    0
                )

                percentageBackList.removeAt(downloadingVideoIdList.indexOf(videoData.name))
                downloadingVideoIdList.remove(videoData.name)

                callback(1)

                if (downloadingVideoIdList.isEmpty()) {
                    stopSelf()
                }

            }
            catch (e: Exception) {
                Log.d("resultCallBackVideo -2",e.toString())
                callback(-2)
                clearNotification(videoData)
                percentageBackList.removeAt(downloadingVideoIdList.indexOf(videoData.name))
                downloadingVideoIdList.remove(videoData.name)
                if (downloadingVideoIdList.isEmpty()) {
                    stopSelf()
                }
            }
        }.start()

//        Thread{
//            Log.d("openingFolder","yes")
//            val videoParentFile = getVideoDirPath(ctx)
//            val thumbsParentFile = getThumbsDirPath(ctx)
//
////            val intent = Intent(Intent.ACTION_VIEW)
////            val mydir = Uri.parse("file://$videoParentFile")
////            intent.setDataAndType(mydir, "application/*") // or use */*
////
////            startActivity(intent)
//            Log.d("pathOfVideo",videoParentFile.toString())
//        }.start()
    }

    fun downloadTest(){
        Log.d("serviceTest","downloadTest()")
        Thread{
            Log.d("serviceTest","downloadTest")
        var u: URL? = null
        var iss: InputStream? = null

        try {
           // u = URL("https://r1---sn-ug5onfvgaq-aixs.googlevideo.com/videoplayback?expire=1635172940&ei=7G12Yd73CI2dWoXCk_AG&ip=203.215.181.201&id=o-AL0yJoiGxDJSwjDDmHeo-RXZc0TOdCc7QyA7LC2C82mF&itag=22&source=youtube&requiressl=yes&mh=G5&mm=31%2C29&mn=sn-ug5onfvgaq-aixs%2Csn-hju7en7r&ms=au%2Crdu&mv=m&mvi=1&pl=24&initcwndbps=1642500&vprv=1&mime=video%2Fmp4&ns=tPbwSehxnuLyT4Ft17IM2_MG&ratebypass=yes&dur=37.778&lmt=1596705592418957&mt=1635151016&fvip=1&fexp=24001373%2C24007246&c=WEB&txp=6216222&n=8bDA8e3jcfZbtemhgnT&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRQIgQSwnd_TZlPr9HR-0fSkwEy0tEEgTpfEdWC-dYVZFwM4CIQD1h2lAYsJFx_Lxjw4mUGxF3U-fP8dp4ujW3d-2Uhu0FQ%3D%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRgIhALOogfDuS7bUj_WvFSJf4lUSzu7lZNgSVQ5mvyK4NuUAAiEAwr5A7O8hdr2w4EUyyc2kEnUXLRUxpVjEq2LkrCxmeY0%3D")
            u = URL("https://r2---sn-ug5onfvgaq-aixl.googlevideo.com/videoplayback?expire=1635174473&ei=6XN2YZekHIS2WOStg9AI&ip=203.215.181.201&id=o-APr9JxLcKfEs97N5NEJFKjAeLEDRvVNd0NipEyhqLXdj&itag=22&source=youtube&requiressl=yes&mh=yH&mm=31%2C29&mn=sn-ug5onfvgaq-aixl%2Csn-hju7en7r&ms=au%2Crdu&mv=m&mvi=2&pl=24&initcwndbps=547500&vprv=1&mime=video%2Fmp4&ns=9Zi60rqb1C1dVRYE0hYRvyQG&ratebypass=yes&dur=55.054&lmt=1596705671722022&mt=1635152201&fvip=2&fexp=24001373%2C24007246&c=WEB&txp=6216222&n=4VIzeh2wfZQqjpMLdti&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Cns%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRgIhAK21ihflNad65cKrCiSlOJI3232mogx0G3kx-Bu6Ip0GAiEAz1AoftKIddu9rNTRFKWvfKLE9G70mSvNG2hdTiI0_0M%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRQIgdOx8jp1Z_6nUfu_AShHGj6AD_lISNLqjFVOZVCq5vwwCIQDIEk85TOeSKgHQXpmTu3b5M2XTm8mdfnrq3MoPPbXiOw%3D%3D")
                iss = u.openStream()
            val huc: HttpURLConnection =
                u.openConnection() as HttpURLConnection //to know the size of video
            val size: Int = huc.getContentLength()
            if (huc != null) {
                val fileName = "FILE3.mp4"

                val storagePath = getVideoDirPath(this)
                val f = File(storagePath.absolutePath, fileName)
                val fos = FileOutputStream(f)

                val buffer = ByteArray(1024)
                var len1 = 0
                if (iss != null) {
                    while (iss.read(buffer).also { len1 = it } > 0) {
                        fos!!.write(buffer, 0, len1)
                        Log.d("serviceTest","downloading")
                    }
                }
                if (fos != null) {
                    fos.close()
                    Log.d("serviceTest","close")

                }
            }
        } catch (mue: MalformedURLException) {
            mue.printStackTrace()
            Log.d("serviceTest","mue")

        } catch (ioe: IOException) {
            Log.d("serviceTest","ioe")

            ioe.printStackTrace()
        } finally {
            try {
                Log.d("serviceTest","Ftry")

                iss?.close()
            } catch (ioe: IOException) {
                Log.d("serviceTest","Fioe")

                // just going to ignore this one
            }
        }
    }.start()
    }

    private val notificationIdKeys = mutableListOf<String>()
    private val notificationIds = mutableListOf<Int>()

    private fun getNotificationId(key: String): Int {
        return if (notificationIdKeys.contains(key)) {
            notificationIds[notificationIdKeys.indexOf(key)]
        } else {
            notificationIdKeys.add(key)
            if (notificationIds.isNotEmpty())
                notificationIds.add(notificationIds[notificationIds.size - 1] + 1)
            else
                notificationIds.add(0)

            notificationIds[notificationIds.size - 1]
        }
    }

    private fun removeNotificationId(key: String): Boolean =
        notificationIds.remove(notificationIds[notificationIdKeys.indexOf(key)]) && notificationIdKeys.remove(
            key
        )


    private fun showDownloadingNotification(videoData: ModelForTopics): NotificationCompat.Builder {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val downloadChannel = NotificationChannel(
                "DownloadingChannel",
                "Downloader Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(downloadChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, "DownloadingChannel")
            .setContentTitle("Downloading ${videoData.name} | 0%")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(100, 0, false)

        notificationManager?.notify(
            getNotificationId(videoData.name),
            notificationBuilder.build()
        )
        return notificationBuilder
    }

    private fun updateProgressOnNotification(
        notification: NotificationCompat.Builder,
        videoData: ModelForTopics,
        progress: Int,
        total: Int
    ) {
        notification.setProgress(total, progress, false)

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.notify(
            getNotificationId(videoData.name),
            notification.build()
        )
    }

    private fun clearNotification(videoData: ModelForTopics) {


        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            getNotificationId(videoData.name)
        )
      //  removeNotificationId(videoData.videoID)
    }


}