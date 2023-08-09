package learningapp.superior.org.VideoPlayerFragments

import android.Manifest
import android.app.NotificationManager
import android.content.*
import android.content.Context.BIND_AUTO_CREATE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import learningapp.superior.org.Adapter.CustomeAdapterForTopics
import learningapp.superior.org.Adapter.OnTopicClick
import learningapp.superior.org.Download.DownloadService
import learningapp.superior.org.Models.BookMArkModel
import learningapp.superior.org.Models.Model
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import android.content.Intent
import android.os.Handler


class Fragmentone : Fragment(), OnTopicClick
//,DownloadBtnClickInterface
{
    private var downloadService: DownloadService? = null


    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downloadService = (service as DownloadService.LocalBinder).serviceInstance
            Log.d("serviceReport", "onServiceConnected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("serviceReport", "onServiceDisconnected")

        }

    }


    private lateinit var serviceIntent: Intent
    lateinit var adapter: CustomeAdapterForTopics

    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var bookMarklist: ArrayList<BookMArkModel>
    var VIDChecker = true
    lateinit var ref: DatabaseReference
    lateinit var reff: DatabaseReference
    lateinit var recycleView: RecyclerView
    lateinit var items: ArrayList<ModelForTopics>
    lateinit var dataPasser: OnDataPass

    var link = "null"
    var linkName = "null"
    lateinit var watch: String
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
//        inflater.inflate(R.layout.main_topics_row, container, false)!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        serviceIntent = Intent(requireActivity(), DownloadService::class.java)
        requireActivity().startService(serviceIntent)

        requireActivity().bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)


        Handler().postDelayed({
            downloadService?.test()
            Log.d("serviceTest", "Handler")
//            downloadService?.downloadTest()

        }, 9000)

        activity?.runOnUiThread {
            Log.d("runOnUiThread ", "activity?")

        }
        VideoNew().runOnUiThread {
            Log.d("runOnUiThread ", "VideoNew()")

        }
        Fragmentone().activity?.runOnUiThread {
            Log.d("runOnUiThread ", "Fragmentone().activity?")

        }


//        return inflater.inflate(R.layout.main_topics_row, container, false)
        val view: View = inflater.inflate(R.layout.main_topics_row, container, false)
        val model = requireArguments().getParcelable<Model>("object")
        val subject = requireArguments().getInt("subject", 0)
        val grade = requireArguments().getString("grade").toString()

        Log.d("grade"," frag1 grade :"+grade.toString())

        val topicName = view.findViewById<TextView>(R.id.textView3)
//        topicName.setText(model!!.name + " (9th-" + model.id + ")")
        topicName.setText(model!!.name + " (Chapter-" + model.id + ")")

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewTopics)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val users = ArrayList<ModelForTopics>()
        items = ArrayList<ModelForTopics>()
        adapter =
            CustomeAdapterForTopics(
                requireActivity().applicationContext,
                this@Fragmentone,
                items,
                grade
                // ,this@Fragmentone

            )

        var totalVideos: String
//        val bookmarkFillIV = view.findViewById<ImageView>(R.id.bookmarkFillIV)
//        val bookmarkEmptyIV = view.findViewById<ImageView>(R.id.bookmarkEmptyIV)

//        bookmarkEmptyIV.setOnClickListener {
//            bookmarkFillIV.visibility = View.VISIBLE
//            bookmarkEmptyIV.visibility = View.GONE
//
//            AddBookMark()
//        }

//        bookmarkFillIV.setOnClickListener {
//            bookmarkFillIV.visibility = View.GONE
//            bookmarkEmptyIV.visibility = View.VISIBLE
//            DeleteBookMark()
//        }

        recycleView = view.findViewById(R.id.recyclerViewTopics)
        items = ArrayList<ModelForTopics>()


        when (subject) {
            1 -> {
                Log.d("Physics clicked ", "Physics clicked frag")
                ref = FirebaseDatabase.getInstance().getReference("contents").child("books").child(grade)
                    .child("subjects-1").child(model.key).child("topics")

                //            ref = FirebaseDatabase.getInstance().getReference("contents").child("books")
                //                .child("subjects-1").child(model.key).child("mtopics")

            }

            2 -> {
                Log.d("Chemis clicked", "Chemis clicked frag")
                ref = FirebaseDatabase.getInstance().getReference("contents").child("books").child(grade)
                    .child("chemistry").child(model.key).child("topics")

                Log.d("model.key checmistry", "model.key chem" + model.key)
            }

            3 -> {
                ref = FirebaseDatabase.getInstance().getReference("contents").child("books").child(grade)
                    .child("biology").child(model.key).child("topics")

            }
        }
//        ref = FirebaseDatabase.getInstance().getReference("contents").child("books")
//            .child("subjects-1").child(model.key).child("topics")


        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                if (p0!!.exists()) {

                    for (h in p0.children) {
                        val chap =
                            ModelForTopics(
                                h.key.toString(),
                                h.child("name").value.toString(),
                                h.child("urlEng").value.toString(),
                                h.child("urlUrdu").value.toString(),
                                h.child("engUrlDownload").value.toString(),
                                h.child("urduUrlDownload").value.toString()
//                                ,
//                                h.child("id").value.toString()

                            )
//                        Log.d("For loop", h.child("name").toString())
                        items.add(chap!!)


                    }
//                    Log.d("outside For loop", "outside from for loop")


                    adapter =
                        CustomeAdapterForTopics(
                            activity!!.applicationContext,
                            this@Fragmentone,
                            items,
                            grade
                            // ,this@Fragmentone

                        )
//

                    recycleView.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }

        })

        return view
    }

    private fun AddBookMark() {
        val refOfBook = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(uid).child(link)
        val userMap = HashMap<Any, Any>()
        userMap["VID"] = link
        userMap["VideoName"] = linkName

        refOfBook.setValue(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                } else {
                    val message = task.exception!!.toString()
                    Toast.makeText(getActivity(), "Something Went Wrong:(", Toast.LENGTH_LONG)
                        .show()
                    //  FirebaseAuth.getInstance().signOut()
                }
            }

    }

    private fun DeleteBookMark() {
        val refOfBook = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(uid).child(link)


        refOfBook.setValue(null)
    }


    override fun onItemClick(items: ModelForTopics, position: Int, grade: String) {
//        val bookmarkLL = view?.findViewById<LinearLayout>(R.id.bookmarkLL)
        Log.d("downloadedVid", items.toString())
        Log.d("onItemClick", "onItemClick grade: "+ grade.toString())

//        val dialogBuilder = AlertDialog.Builder(requireActivity())
//
//        dialogBuilder.setMessage("Please choose a language below.")
//
//
//            .setPositiveButton("URDU", DialogInterface.OnClickListener { dialog, id ->
//                //                Log.d("check", items.key + "")
//
////                if (bookmarkLL != null) {
////                    bookmarkLL.visibility = View.VISIBLE
////                }
////                textView.setTextColor(Color.parseColor("#FFBA5F"))
//
//
//                val frag2 =
//                    Fragmenttwo()
//                val bundle = Bundle()
//                bundle.putString("grade",grade)
//
//                bundle.putString("items.key", items.key)
//                frag2.arguments = bundle
//                val manager = fragmentManager
//                val frag_tran = manager?.beginTransaction()
//                frag_tran?.replace(R.id.container, frag2)
//                Log.d("chapname", "chapname" + items.key)
//                frag_tran?.commit()
//                if (items.isDownloaded) {
//
//                    passData(items.isDownloaded.toString(), items.name, items, "urdu")
//                }
//                //---------------
//                else
//                    passData(items.urlurdu, items.name, items, "urdu")
//                link = items.urlurdu
//                linkName = items.name
//                VIDChecker = true
//
//
//                checkBookMarkBackend()
//
//
//                //--------------
//                val model = requireArguments().getParcelable<Model>("object")
//                val sharepreffrag1 = requireActivity().getPreferences(Context.MODE_PRIVATE)
//                val editfrag1 = sharepreffrag1.edit()
//                if (model?.key == null) {
//                    Log.d("if nothing to change", "if frag2")
//                } else {
//                    editfrag1.putString("model", model?.key)
//                    editfrag1.apply()
//                }
//                var modelchapter = sharepreffrag1.getString("model", "0").toString()
//
//
//                //---------------startsubject and end model.key
//
//                var subject = requireArguments().getInt("subject", 0)
//                var chapsubject: String = "null"
//
//                val shareprefchapfrag1 = requireActivity().getPreferences(Context.MODE_PRIVATE)
//                val editchatpfrag1 = shareprefchapfrag1.edit()
//                if (subject == 0) {
//                    Log.d("if nothing to change", "if frag2")
//                } else {
//                    editchatpfrag1.putInt("subject", subject)
//                    editchatpfrag1.apply()
//                }
//                var subjectt = shareprefchapfrag1.getInt("subject", 0)
//
//                when (subjectt) {
//                    1 -> {
//                        Log.d("Physics clicked ", "Physics clicked frag2")
//                        chapsubject = "physics"
//                    }
//
//                    2 -> {
//                        Log.d("Chemis clicked", "Chemis clicked frag2")
//                        chapsubject = "chemistry"
//                    }
//
//                    3 -> {    //---------------------------------------add after release
//                        Log.d("bio clicked", "bio clicked frag2")
//                        chapsubject = "biology"
//                    }
//                }
//
//                //-------
//                var UID: String = FirebaseAuth.getInstance().currentUser!!.uid
//                val userDataRef: DatabaseReference =
//                    FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
//                        .child(UID).child("books")
//                        .child(chapsubject).child(modelchapter).child("topicWatch").child(items.key)
//                userDataRef.child("watch").setValue(45)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//
//                            // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()
//
//                        } else {
//                            val message = task.exception!!.toString()
//                            Toast.makeText(
//                                getActivity(),
//                                "Something Went Wrong:(",
//                                Toast.LENGTH_LONG
//                            )
//                                .show()
//                            //  FirebaseAuth.getInstance().signOut()
//                        }
//                    }
//
//
//            })
//
//            .setNegativeButton("ENGLISH", DialogInterface.OnClickListener { dialog, id ->

                if (items.isDownloaded) {

                    passData(items.isDownloaded.toString(), items.name, items, "eng")
                } else
                    passData(items.urlEng, items.name, items, "eng")

                link = items.urlEng
                linkName = items.name
                VIDChecker = true

                checkBookMarkBackend()


                val frag2 =
                    Fragmenttwo()
                val bundle = Bundle()
                bundle.putString("grade",grade)

                bundle.putString("items.key", items.key)
                frag2.arguments = bundle
                val manager = fragmentManager
                val frag_tran = manager?.beginTransaction()
                frag_tran?.replace(R.id.container, frag2)
//                Log.d("chapname","chapname"+items.key)
                frag_tran?.commit()
                //----------------
                //--------------
                val model = requireArguments().getParcelable<Model>("object")
                val sharepreffrag1 = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val editfrag1 = sharepreffrag1.edit()
                if (model?.key == null) {
                    Log.d("if nothing to change", "if frag2")
                } else {
                    editfrag1.putString("model", model?.key)
                    editfrag1.apply()
                }
                val modelchapter = sharepreffrag1.getString("model", "0").toString()


                //---------------startsubject and end model.key

                val subject = requireArguments().getInt("subject", 0)
                var chapsubject: String = "null"

                val shareprefchapfrag1 = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val editchatpfrag1 = shareprefchapfrag1.edit()
                if (subject == 0) {
                    Log.d("if nothing to change", "if frag2")
                } else {
                    editchatpfrag1.putInt("subject", subject)
                    editchatpfrag1.apply()
                }
                val subjectt = shareprefchapfrag1.getInt("subject", 0)

                when (subjectt) {
                    1 -> {
                        Log.d("Physics clicked ", "Physics clicked frag2")
                        chapsubject = "physics"
                    }

                    2 -> {
                        Log.d("Chemis clicked", "Chemis clicked frag2")
                        chapsubject = "chemistry"
                    }

                    3 -> {
                        Log.d("bio clicked", "bio clicked frag2")
                        chapsubject = "biology"
                    }
                }

                //-------
                val UID: String = FirebaseAuth.getInstance().currentUser!!.uid
                val userDataRef: DatabaseReference =
                    FirebaseDatabase.getInstance().getReference("contents").child("WatchRecord")
                        .child(UID).child("books")
                        .child(chapsubject).child(modelchapter).child("topicWatch")
                        .child(items.key)//+"Eng")
                userDataRef.child("watch").setValue(45)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // Toast.makeText(this,"Account has been created successfully:)",Toast.LENGTH_SHORT).show()

                        } else {
                            val message = task.exception!!.toString()
                            Toast.makeText(
                                getActivity(),
                                "Something Went Wrong:(",
                                Toast.LENGTH_LONG
                            )
                                .show()
                            //  FirebaseAuth.getInstance().signOut()
                        }
                    }


//            })
//
//        val alert = dialogBuilder.create()
//        alert.show()


    }

    override fun onItemClickDownload(
        items: ModelForTopics,
        position: Int,
        videoData: ModelForTopics,
        callback: (Float, Int) -> Unit
    ) {

        //   Toast.makeText(context,  items.name , Toast.LENGTH_SHORT).show()
        //  downloadService?.downloadTest()
        //  downloadService?.downloadFile(videoData, callback)


//        val downloadCallBack: (Float, Int) -> Unit = { progress, totalLen ->
//           Fragmentone().runOnUiThread {
//
//           CustomeAdapterForTopics.MVH(view: View).downloadIV.visibility = View.GONE
//                holder.progressBar.visibility = View.VISIBLE
//                holder.percentageTxt.visibility = View.VISIBLE
//                val downloadedPercentage = ((progress / totalLen) * 100)
//                //downloadedPercentage = DecimalFormat("0.0").format(downloadedPercentage).toDouble()
//                holder.progressBar.setMax(totalLen)
//                holder.progressBar.setProgress(progress)
//                holder.percentageTxt.text = "${downloadedPercentage.toInt()}%"
//            }
//        }

//       downloadFile( items , downloadCallBack){
//            Log.d("resultCallBackVideo",it.toString())
//
//            Fragmentone().activity?.run {
//                val name = items.name
//                when (it) {
//                    -2 -> {
//                        showToast("Error in Downloading $name!",items.urlDownload)
//                        items.isDownloading = false
//                        hideProgress()
//                    }
//                    -1 -> {
//                        showToast("$name Already Downloaded!",items.urlDownload)
//                        items.isDownloading = false
//                        downloadSuccess()
//                    }
//                    0 -> {
//                        showToast("$name Already Downloading...",items.urlDownload)
//                    }
//                    1 -> {
//                        //showToast("$name Downloaded Successfully")
//                        items.isDownloading = false
//                        downloadSuccess()
//                    }
//                    2 -> {
//                        //showToast("$name Downloading Started")
//                        items.isDownloading = true
//                        showProgress()
//                    }
//                }
//            }
//        }

    }

    override fun DownloadedVid(items: ModelForTopics) {
    }


    private fun showToast(msg: String, urlDownload: String) {
        Log.d("checkClick", msg)
        Log.d("checkClickurl", urlDownload)

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
        activity?.stopService(serviceIntent)
        activity?.unbindService(serviceConnection)
    }

    private fun checkBookMarkBackend() {
//        val bookmarkEmptyIV = view?.findViewById<ImageView>(R.id.bookmarkEmptyIV)
//        bookmarkEmptyIV?.visibility = View.VISIBLE
//
//        val bookmarkFillIV = view?.findViewById<ImageView>(R.id.bookmarkFillIV)
//        bookmarkFillIV?.visibility = View.GONE
//        val bookmarkEmptyIV = view?.findViewById<ImageView>(R.id.bookmarkEmptyIV)
//        val bookmarkFillIV = view?.findViewById<ImageView>(R.id.bookmarkFillIV)

        val refOfBookmark = FirebaseDatabase.getInstance()
            .getReference("BookMarks")
            .child(uid)

        refOfBookmark.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    for (a in snapshot.children) {
                        val detail = BookMArkModel(
                            a.child("VID").value.toString(),
                            a.child("VideoName").value.toString()
                        )

                        Log.d("detailsKiDetail", detail.VID.toString())
                        Log.d("detailsKiLink", link)

                        // bookMarklist.add(detail)
                        if (detail.VID == link) {
//                            bookmarkEmptyIV?.visibility = View.GONE

                            Log.d("detail.VID == link", link)
                            VIDChecker = false
//                            bookmarkFillIV?.visibility = View.VISIBLE
                        } else if (detail.VID != link && VIDChecker == true) {
                            Log.d("detail.VID != link", link)

//                            bookmarkEmptyIV?.visibility = View.VISIBLE
//                            bookmarkFillIV?.visibility = View.GONE
                        }

                    }


                }
            }
        })


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }


    fun passData(data: String, name: String, items: ModelForTopics, lang: String) {
        dataPasser.onDataPass(data, name, items, lang)
    }

    interface OnDataPass {
        fun onDataPass(data: String, name: String, items: ModelForTopics, lang: String)

    }

    fun downloadFile(
        ctx: Context,
        videoData: ModelForTopics,
        callback: (progress: Float, total: Int) -> Unit,
        eventsCallback: (Int) -> Unit
    ) {
        try {
            Log.d("downloadingVideo", " Try")

//         DownloadService().downloadTest()
            DownloadService().downloadFile(ctx, videoData, callback, eventsCallback)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("downloadingVideoError ", e.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 101)
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
    }
}