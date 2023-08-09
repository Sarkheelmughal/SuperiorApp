package learningapp.superior.org.Home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import learningapp.superior.org.Adapter.OnPastPaperClick
import learningapp.superior.org.Adapter.PastPaperAdapter
import learningapp.superior.org.Models.PastPaperModel
import learningapp.superior.org.R
import learningapp.superior.org.databinding.ActivityPastPaperSubjectSelectedBinding

class PastPaperSubjectSelected : AppCompatActivity(), OnPastPaperClick {
    lateinit var binding: ActivityPastPaperSubjectSelectedBinding
    lateinit var recycleView: RecyclerView
    lateinit var ref: DatabaseReference
    lateinit var ProgressBar: ProgressBar
    lateinit var subject: String
    lateinit var pastPaperlist: ArrayList<PastPaperModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_past_paper_subject_selected)

        binding.buyNowPPBtn.setOnClickListener { startActivity(Intent(this, BuyNow::class.java)) }

        binding.backPastPaperCv.setOnClickListener { onBackPressed() }
        subject = intent.getStringExtra("subject").toString()
        when (subject) {
            "physics" -> {
                binding.pastPaperSubject.text = "Physics - Past Paper"
            }
            "bio" -> {
                binding.pastPaperSubject.text = "Bio - Past Paper"
            }
            "chemistry" -> {
                binding.pastPaperSubject.text = "Chemistry - Past Paper"
            }
            "english" -> {
                binding.pastPaperSubject.text = "English - Past Paper"
            }
            "urdu" -> {
                binding.pastPaperSubject.text = "Urdu - Past Paper"
            }
            "islamyat" -> {
                binding.pastPaperSubject.text = "Islamyat - Past Paper"
            }
            "maths" -> {
                binding.pastPaperSubject.text = "Math - Past Paper"
            }
            "computer" -> {
                binding.pastPaperSubject.text = "Computer - Past Paper"
            }
            "pak_study" -> {
                binding.pastPaperSubject.text = "Pak-Studies - Past Paper"
            }
        }

        ProgressBar = binding.progressBar
        //callOnePaper()
        val SPforNumber = getSharedPreferences("numberStorage", Context.MODE_PRIVATE)
        val SPforNumberSaved = SPforNumber.getString("number", "null")
        val sharedPreferencesSC: SharedPreferences =
            this.getSharedPreferences("schoolCode", Context.MODE_PRIVATE)
        //val editorSc: SharedPreferences.Editor = sharedPreferencesSC.edit()
        val getSchoolCode = sharedPreferencesSC.getString("schoolCode", "null")

        val refOfPaid = FirebaseDatabase.getInstance()
            .getReference("LockAndUnLockChapter").child(getSchoolCode.toString())
        //.child(SPforNumberSaved.toString())
        refOfPaid.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    //for (h in p0.children) {

                    if (p0.child(SPforNumberSaved.toString())
                            .child(SPforNumberSaved.toString()).value.toString() == "on"
                    ) {
                        Log.d("paidOrNot", p0.child("paid").value.toString())
                        // if (p0.child("paid").value.toString() == "yes") {
                        binding.buyPastPaperCv.visibility = View.GONE

                        binding.recyclerView.visibility = View.VISIBLE
                        callAllPaper()


                    } else {
                        binding.recyclerView.visibility = View.VISIBLE

                        binding.buyPastPaperCv.visibility = View.VISIBLE
                        callOnePaper()
                        // notPaid.visibility = View.VISIBLE
                    }
                    // }

                } else {
                    binding.recyclerView.visibility = View.VISIBLE

                    binding.buyPastPaperCv.visibility = View.VISIBLE
                    callOnePaper()
                    // notPaid.visibility = View.VISIBLE
                }
            }
        })


    }

    private fun callOnePaper() {

        // val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // recyclerView.setLayoutManager(GridLayoutManager(this, 2))
//        recyclerView


        //  recycleView = findViewById(R.id.recyclerView)
        recycleView = binding.recyclerView
        pastPaperlist = ArrayList<PastPaperModel>()

        //chapterlist.remove()

        ref = FirebaseDatabase.getInstance().getReference("contents").child("PastPapers")
            .child("ninth").child(subject)


//
        ref.limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                    if (p0.exists()) {
                        pastPaperlist.clear()
                        //notPaid.visibility = View.GONE
                        ProgressBar.visibility = View.INVISIBLE
                        for (h in p0.children) {
                            val chap = PastPaperModel(
                                h.key!!,
                                h.value.toString()
                            )
//                        Log.d("For loop", h.child("name").toString())
                            Log.d("For loop", chap.toString())
                            pastPaperlist.add(chap!!)

                        }
//                    Log.d("outside For loop", "outside from for loop")


                        val adapter =
                            PastPaperAdapter(
                                applicationContext,
                                this@PastPaperSubjectSelected,
                                pastPaperlist
                            )
                        recycleView.adapter = adapter


                    }
                }

            })

    }
    private fun callAllPaper() {

        // val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // recyclerView.setLayoutManager(GridLayoutManager(this, 2))
//        recyclerView


        //  recycleView = findViewById(R.id.recyclerView)
        recycleView = binding.recyclerView
        pastPaperlist = ArrayList<PastPaperModel>()

        //chapterlist.remove()

        ref = FirebaseDatabase.getInstance().getReference("contents").child("PastPapers")
            .child("ninth").child(subject)


//
        ref
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
//                Log.d("data", p0.toString())
                    if (p0.exists()) {
                        pastPaperlist.clear()
                        //notPaid.visibility = View.GONE
                        ProgressBar.visibility = View.INVISIBLE
                        for (h in p0.children) {
                            val chap = PastPaperModel(
                                h.key!!,
                                h.value.toString()
                            )
//                        Log.d("For loop", h.child("name").toString())
                            Log.d("For loop", chap.toString())
                            pastPaperlist.add(chap!!)

                        }
//                    Log.d("outside For loop", "outside from for loop")


                        val adapter =
                            PastPaperAdapter(
                                applicationContext,
                                this@PastPaperSubjectSelected,
                                pastPaperlist
                            )
                        recycleView.adapter = adapter


                    }
                }

            })

    }

    override fun onItemClick(list: PastPaperModel, position: Int) {
        val intent = Intent(this, PastPaperPDF::class.java)

        intent.putExtra("url", list.url)
        intent.putExtra("name", list.name)
        intent.putExtra("subject", subject)
        // intent.putExtra("subject", subject)
        startActivity(intent)
    }

}