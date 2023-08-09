package learningapp.superior.org.Reivision

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.Adapter.OnTopicClick
import learningapp.superior.org.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragOneNew : Fragment(), OnTopicClick {
    lateinit var dataPasser: OnDataPas
    var uid: String = FirebaseAuth.getInstance().currentUser!!.uid
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        var name = arguments!!.getString("name")
//        chaptName.setText(name)
//        Log.d("name","name in onviewcreated fra1" + name)


    }

    //    lateinit var recycleView: RecyclerView
//    lateinit var items: ArrayList<ModelForTopics>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_frag_one_new, container, false)
       // var nameInt = requireArguments().getInt("nameInt", 2)

        val name = requireArguments().getString("name")
        val eng = requireArguments().getString("eng")
        val urdu = requireArguments().getString("urdu")


        val chapName=view.findViewById<TextView>(R.id.chapName)

        chapName.text=name

        chapName.setOnClickListener {
//            val dialogBuilder = AlertDialog.Builder(requireActivity())
//
//            dialogBuilder.setMessage("Please choose a language below.")
//
//                .setNegativeButton("ENGLISH", DialogInterface.OnClickListener { dialog, id ->

                    passDataToRev(eng.toString())
//                })
//
//                .setPositiveButton("Urdu", DialogInterface.OnClickListener{dialog, id ->
//
//                    passDataToRev(urdu.toString())
//                })
//
//            val alert = dialogBuilder.create()
//            alert.show()
        }
        //------------------------------------start recycle -----------------------------------------------------
//        val view: View = inflater.inflate(R.layout.main_topics_row, container, false)
//        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewTopics)
//        recyclerView?.layoutManager = LinearLayoutManager(activity)
//        val users = ArrayList<ModelForTopics>()
//        recycleView = view!!.findViewById(R.id.recyclerViewTopics)
//        items = ArrayList<ModelForTopics>()
//------------------end---------------------------------------
//        var count = 1
//
//        if (count == 1) {
//            getChapter(view)
//            count++
//        } else {
//            Log.d("start", "start the fragonenew")
//        }
        return view
    }




    fun getChapter(view: View) {
        val chaptName=view.findViewById<TextView>(R.id.chapName)

        var nameInt = requireArguments().getInt("nameInt", 2)
        val shareprefCount = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editCount = shareprefCount.edit()
        editCount.putInt("counter", 0)
        var count = shareprefCount.getInt("counter", 0)
//----------

        val shareprefName = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val shareName = shareprefName.edit()
        shareName.putString("name", "Loading")
//        shareName.apply()

        // ------------------
//
        editCount.apply()



//        Log.d("value of child var", "value of child is:" + child)


        val ref =
            FirebaseDatabase.getInstance().getReference("RevisionTopic").child(uid) //.child("11")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {
                    var chapkey = ""
                    var mcqssubject = ""
                    var modelchapter = ""
                    for (b in p0.children) {
                        chapkey = b.child("chapkey").value.toString()
                        mcqssubject = b.child("mcqssubject").value.toString()
                        modelchapter = b.child("modelchapter").value.toString()


///--------------------------------------------------------------------------------------------------------------------------------
                        val reff =
                            FirebaseDatabase.getInstance().getReference("contents").child("books")
                                .child(mcqssubject).child(modelchapter).child("topics")
                                .child(chapkey)

                        Log.d("mcqssubject", "fra1new mcqssubject :" + mcqssubject)
                        Log.d("chapkey", "fra1new chapkey :" + chapkey)
                        Log.d("modelchapter", "fra1new modelchapter :" + modelchapter)
                        var x = 1
                        Log.d("c", "fra1new :" + x++)

                        reff.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p1: DatabaseError) {
//                                TODO("not implemented")
                            }

                            override fun onDataChange(p1: DataSnapshot) {
//                Log.d("data", p0.toString())
                                if (p1!!.exists() && count == 0) {
//                                    count++
                                    editCount.putInt("counter", count++)
                                    editCount.apply()

                                    //---------------------------------------------------------------------------------emd
                                    val name = p1.child("name").value.toString()
                                    val eng = p1.child("urlEng").value.toString()
                                    val urdu = p1.child("urlUrdu").value.toString()

                                    Log.d("testing ", "checking value of name :" + name)
                                    Log.d("testing ", "checking value of eng :" + eng)
                                    Log.d("testing ", "checking value of urdu :" + urdu)

                                    chaptName.text = p1.child("name").value.toString()

//                                 shareName.putString("name", name)
//
//                                    shareName.apply()
//                                   var f =shareprefName.getString("name",name).toString()
//                                    Log.d("name","nam of share "+f)
//                                    Log.d("name","vale of name "+ nameInt)
//                                    if (f != null) {
//                                        Log.d("name", "name is not empty")
//                                        chapName.setText(f)
//                                    } else {
//
//                                        Log.d("name", "name is empty")
//                                    }

                                    //}
                                } else {
                                    Log.d("else", "else of fragonenew child")
                                }


                            }

                        })

                    }
                } else {
                    Log.d("else", "else of fragonenew main")
                }


            }

        })



    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as FragOneNew.OnDataPas
    }

    fun passDataToRev(lang: String) {
        dataPasser.onDataPas(lang)
    }

    interface OnDataPas {
        fun onDataPas(lang: String)
    }

    override fun onItemClick(items: ModelForTopics, position: Int, grade: String) {
        //To change body of created functions use File | Settings | File Templates.
    }



    override fun onItemClickDownload(
        items: ModelForTopics,
        position: Int,
        downloadCallBack1: ModelForTopics,
        downloadCallBack: (Float, Int) -> Unit
    ) {

    }

    override fun DownloadedVid(items: ModelForTopics) {

    }



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {


//            var shareprefName = activity!!.getPreferences(Context.MODE_PRIVATE)
//            var shareName = shareprefName.edit()

//            var nam=shareprefName.getString("name","")

//            Log.d("name","nam of share"+nam)

//            chapName.setText(h)
        } else {

        }
    }

}
