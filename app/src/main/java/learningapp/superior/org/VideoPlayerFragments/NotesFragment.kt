package learningapp.superior.org.VideoPlayerFragments

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import learningapp.superior.org.Models.Model
import learningapp.superior.org.R
import java.io.File


class NotesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        val model = requireArguments().getParcelable<Model>("object")
        val subject = requireArguments().getInt("subject", 0)
        var grade = requireArguments().getString("grade").toString()



        Log.d("modelAll", model.toString())
        val id = model?.id?.lowercase()
        Log.d("idAll", id.toString())

        val chapeterKey = "chapter$id"
        var subjectNode = ""
        var urlOfChpater = ""
        when (subject) {
            1 -> {
                subjectNode = "physics"

//                if(grade=="class11th"){
//                    grade="class12th"
//                }
            }
            2 -> {
                subjectNode = "chemistry"
            }
            3 -> {
                subjectNode = "bio"
            }
        }





        val ref = FirebaseDatabase.getInstance().getReference("contents").child("notes")
            .child(grade).child(subjectNode).child(chapeterKey)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    urlOfChpater = p0.value.toString()
                    Log.d("urlOfChpater", urlOfChpater)

                    try {
                        loadPDF(urlOfChpater)

                    }catch (
                        exceptionType: Exception
                    ){
                    //    Toast.makeText(this, "An error occurs", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        //.child("biology").child(model.key).child("topics")


        // val viewNotesBtn=view.findViewById<Button>(R.id.viewNotesBtn)

//        viewNotesBtn.setOnClickListener {
//            val intent= Intent(requireActivity(), ViewPdf::class.java)
//            intent.putExtra("view","internet")
//            startActivity(intent)
//        }


        return view

    }



    private fun loadPDF(urlOfChpater: String) {

        val pdf_view = view?.findViewById<PDFView>(R.id.viewdoc)
        val progress = view?.findViewById<ProgressBar>(R.id.progress)

        if (progress != null) {
            progress.visibility = View.VISIBLE
        }
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireActivity())
        FileLoader.with(requireActivity())
            .load(urlOfChpater)
            .fromDirectory("PDFFile", FileLoader.DIR_INTERNAL)
            .asFile(object : FileRequestListener<File> {
                override fun onLoad(
                    request: FileLoadRequest?,
                    response: FileResponse<File>?
                ) {
                    if (progress != null) {
                        progress.visibility = View.GONE
                    }
                    val pdfile = response!!.body
                    pdf_view?.fromFile(pdfile)
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
                            Toast.makeText(
                                requireActivity(),
                                "Error while opening page!",
                                Toast.LENGTH_LONG
                            ).show()
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
                        Toast.makeText(requireActivity(), "" + t.message, Toast.LENGTH_LONG)
                            .show()
                        progress?.visibility = View.GONE
                    }

                }
            })


    }


}