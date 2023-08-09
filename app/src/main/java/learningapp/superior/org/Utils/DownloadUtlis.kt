package learningapp.superior.org.Utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Log
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import learningapp.superior.org.VideoPlayerFragments.Fragmentone


const val VIDEO_ENCRYPTED_PASSWORD = "ASDFGHJKLASDFGHJ"
@SuppressLint("UseRequireInsteadOfGet")
fun getAppRootPath(context: Context): File {
//    val rootDirectoryPath = context.filesDir
//    val rootDirectoryPath = Environment.getExternalStorageDirectory().absolutePath
//    val rootDirectoryPath = getAppRootPath(context) //.getExternalFilesDir(null)!!
//    val rootDirectoryPath = Environment.getExternalStorageState()
//    val rootDirectoryPath = context.getDir("mydir", Context.MODE_PRIVATE);
    val rootDirectoryPath =  Fragmentone().activity?.getExternalFilesDir(null)!!
    val file = File(rootDirectoryPath.absolutePath)
    if (!file.exists())
        file.mkdir()
    return file
}

@SuppressLint("UseRequireInsteadOfGet")
fun getVideoDirPath(context: Context): File {
//    val root = Environment.getExternalStorageDirectory().toString()
//
//    val myDir = File("$root/rootfoldername/your sub folder name")
//
//    val dir = File("$root/rootfoldername/your sub folder name")
//
//    myDir.mkdirs()
//
//    dir.mkdirs()

//    val file = getAppRootPath(context)
//    val file =  File(Environment.(), "Exambites")




//
//    val rootDirectoryPath =  Fragmentone().activity?.getExternalFilesDir(null)!!
//    if (!rootDirectoryPath.exists())
//        rootDirectoryPath.mkdir()

    val file1 = File(context.getExternalFilesDir(null)!! ,"Videos")

    if (!file1.exists())
        file1.mkdir()

  //  Log.d("pathOfFolders",rootDirectoryPath.toString())
    Log.d("pathOfFoldersVideo",file1.toString())
  return file1

}

@SuppressLint("UseRequireInsteadOfGet")
fun getThumbsDirPath(context: Context): File {
//    val file = getAppRootPath(context)
//    val file =  File(Environment.(), "Exambites")



//    val rootDirectoryPath =  Fragmentone().activity?.getExternalFilesDir(null)!!
//
//
//    if (!rootDirectoryPath.exists())
//        rootDirectoryPath.mkdir()
    val file2 =  File(context.getExternalFilesDir(null)!! ,"Thumbnail")

    if (!file2.exists())
        file2.mkdir()
    return file2

}

@SuppressLint("GetInstance")
@Throws(java.lang.Exception::class)
fun encodeFile(key: ByteArray?, fileData: ByteArray?): ByteArray? {
    val skeySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
    return cipher.doFinal(fileData)
}

@SuppressLint("GetInstance")
@Throws(java.lang.Exception::class)
fun decodeFile(key: ByteArray?, fileData: ByteArray?): ByteArray? {
    val skeySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, skeySpec)
    return cipher.doFinal(fileData)
}



@Throws(java.lang.Exception::class)
fun generateKey(password: String): ByteArray? {
    return SecretKeySpec(password.toByteArray(), "AES").encoded
}

@Throws(Throwable::class)

fun generateThumbnail(videoPath: String?): Bitmap? {
    val bitmap: Bitmap?
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(
            videoPath,
            HashMap()
        )
        //   mediaMetadataRetriever.setDataSource(videoPath);
        bitmap = mediaMetadataRetriever.frameAtTime
    } catch (e: Exception) {
        e.printStackTrace()
        throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
    } finally {
        mediaMetadataRetriever?.release()
    }
    return bitmap
}
