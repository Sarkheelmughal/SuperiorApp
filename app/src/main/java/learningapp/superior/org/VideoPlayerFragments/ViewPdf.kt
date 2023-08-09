package learningapp.superior.org.VideoPlayerFragments

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.github.barteksc.pdfviewer.PDFView
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import kotlinx.android.synthetic.main.activity_view_pdf.*
import learningapp.superior.org.R
import java.io.File


class ViewPdf : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        val pdf_view = findViewById<PDFView>(R.id.viewdoc)
        fbtn.setOnClickListener {
            onBackPressed()
        }

        val viewTyp = intent.getStringExtra("view")


        if (viewTyp !== null) {
            progress.visibility = View.VISIBLE
            val sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(applicationContext)
            FileLoader.with(this@ViewPdf)
                .load("https://firebasestorage.googleapis.com/v0/b/exambites-application.appspot.com/o/Notes9th%2FBiology%2FBiology%20Chapter%201.pdf?alt=media&token=50dd3d84-3d11-4d43-b220-3a6e65db9570")
                    .fromDirectory("PDFFile", FileLoader.DIR_INTERNAL)
                .asFile(object : FileRequestListener<File> {
                    override fun onLoad(
                        request: FileLoadRequest?,
                        response: FileResponse<File>?
                    ) {
                        progress.visibility = View.GONE
                        val pdfile = response!!.body
                        pdf_view.fromFile(pdfile)
                            .password(null)
                            .defaultPage(0)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                                //code
                            }.onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->
                                //code
                            }
                            .onPageChange { page, pageCount ->
                                //code
                            }
                            .onPageError { page, t ->
                                Toast.makeText(
                                    this@ViewPdf,
                                    "Error while opening page!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .onTap {
                                false
                            }
                            .onRender { nbPages, pageWidth, pageHeight ->
                                pdf_view.fitToWidth()
                            }
                            .enableAnnotationRendering(true)
                            .invalidPageColor(Color.RED)
                            .load()
                    }

                    override fun onError(request: FileLoadRequest?, t: Throwable?) {
                        if (t != null) {
                            Toast.makeText(this@ViewPdf, "" + t.message, Toast.LENGTH_SHORT)
                                .show()
                            progress.visibility = View.GONE
                        }

                    }
                })

        }


    }
}

