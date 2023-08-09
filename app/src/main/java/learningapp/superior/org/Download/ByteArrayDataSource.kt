package learningapp.superior.org.Download

import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Assertions
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.*
import kotlin.math.min

class ByteArrayDataSource(data: ByteArray) : DataSource {
    private val data: ByteArray
    private var uri: Uri? = null
    private var readPosition = 0
    private var bytesRemaining = 0L

    @Throws(IOException::class)
    override fun open(dataSpec: DataSpec): Long {
        uri = dataSpec.uri
        readPosition = dataSpec.position.toInt()
        bytesRemaining =
            (if (dataSpec.length == C.LENGTH_UNSET.toLong()) data.size - dataSpec.position else dataSpec.length)
        if (bytesRemaining <= 0 || readPosition + bytesRemaining > data.size) {
            throw IOException(
                "Unsatisfiable range: [" + readPosition + ", " + dataSpec.length
                        + "], length: " + data.size
            )
        }
        return bytesRemaining
    }

    @Throws(IOException::class)
    override fun read(buffer: ByteArray?, offset: Int, readLen: Int): Int {
        var readLength = readLen
        if (readLength == 0) {
            return 0
        } else if (bytesRemaining == 0L) {
            return C.RESULT_END_OF_INPUT
        }
        readLength = min(readLength, bytesRemaining.toInt())
        System.arraycopy(data, readPosition, buffer!!, offset, readLength)
        readPosition += readLength
        bytesRemaining -= readLength
        return readLength
    }

    override fun getUri(): Uri? {
        return uri
    }

    @Throws(IOException::class)
    override fun close() {
        uri = null
    }

    override fun addTransferListener(transferListener: TransferListener?) {

    }

    init {
        Assertions.checkNotNull(data)
        Assertions.checkArgument(data.isNotEmpty())
        this.data = data
    }
}


internal class UriByteDataHelper {
    fun getUri(data: ByteArray?): Uri {
        return try {
            val url = URL(null, "bytes:///" + "video", BytesHandler(data))
            Uri.parse(url.toURI().toString())
        } catch (e: MalformedURLException) {
            throw RuntimeException(e)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    internal inner class BytesHandler(private var mData: ByteArray?) :
        URLStreamHandler() {
        @Throws(IOException::class)
        override fun openConnection(u: URL?): URLConnection {
            return ByteUrlConnection(u, mData)
        }

    }

    internal inner class ByteUrlConnection(url: URL?, private var mData: ByteArray?) :
        URLConnection(url) {
        @Throws(IOException::class)
        override fun connect() {
        }

        override fun getInputStream(): InputStream {
            return ByteArrayInputStream(mData)
        }
    }
}