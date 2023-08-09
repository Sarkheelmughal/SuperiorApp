package learningapp.superior.org.Home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import learningapp.superior.org.R
import learningapp.superior.org.databinding.ActivityPastPaperPdfBinding
import java.io.File

class PastPaperPDF : AppCompatActivity() {
    lateinit var binding: ActivityPastPaperPdfBinding
    lateinit var progress:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_past_paper_pdf)
        // val model = intent.getParcelableExtra<PastPaperModel>("object")
        val url = intent.getStringExtra("url")
        val name = intent.getStringExtra("name")
          progress = binding.progress

        binding.pastPaperSubjectNameTv.text=name
        progress.visibility = View.VISIBLE

        binding.backPastPaperPDF.setOnClickListener { onBackPressed() }

        loadPDF(url)
    }

    private fun loadPDF(urlOfpdf: String?) {

        val pdf_view = binding.viewdoc

        FileLoader.with(this)
            .load(urlOfpdf)
            .fromDirectory("PDFFile", FileLoader.DIR_INTERNAL)
            .asFile(object : FileRequestListener<File> {
                override fun onLoad(
                    request: FileLoadRequest?,
                    response: FileResponse<File>?
                ) {
                    progress.visibility = View.GONE
                    val pdfile = response!!.body
                    pdf_view.fromFile(pdfile)
                        ?.password(null)
                        ?.defaultPage(0)
                        ?.enableSwipe(true)
                        ?.swipeHorizontal(false)
                        ?.enableDoubletap(true)
                        ?.onDraw { canvas, pageWidth, pageHeight, displayedPage ->
                            //code
                        }?.onDrawAll { canvas, pageWidth, pageHeight, displayedPage ->
                            //code
                        }
                        ?.onPageChange { page, pageCount ->
                            //code
                        }
                        ?.onPageError { page, t ->
                            errorToast()
                        }
                        ?.onTap {
                            false
                        }
                        ?.onRender { nbPages, pageWidth, pageHeight ->
                            pdf_view.fitToWidth()
                        }
                        ?.enableAnnotationRendering(true)
                        ?.invalidPageColor(Color.RED)
                        ?.load()
                }

                override fun onError(request: FileLoadRequest?, t: Throwable?) {
                    if (t != null) {
                        toastError(t)
                        progress.visibility = View.GONE
                    }

                }
            })


    }

    private fun toastError(t: Throwable) {
        Toast.makeText(this, "" + t.message, Toast.LENGTH_LONG)
            .show()
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Error while opening page!",
            Toast.LENGTH_LONG
        ).show()
    }
}