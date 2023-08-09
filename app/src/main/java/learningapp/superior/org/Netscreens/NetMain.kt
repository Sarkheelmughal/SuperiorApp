package learningapp.superior.org.Netscreens

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_net_main.*
import kotlinx.android.synthetic.main.chapters.view.*
import learningapp.superior.org.EntryTestInter.McatOptions
import learningapp.superior.org.R

class NetMain : AppCompatActivity() {
    lateinit var mrecyclerView: RecyclerView
    lateinit var mref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_main)

        back_home.setOnClickListener { onBackPressed() }
        lateinit var reff: DatabaseReference
        reff = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
            .child("totalChapters")
        reff.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (h in p0.children) {
                    if (p0!!.exists()) {


                        val chemistry_chap = p0.child("Chemistry").value.toString()
                        val physics_chap = p0.child("Physics").value.toString()
                        val intelligence_chap = p0.child("Intelligence").value.toString()
                        val english_chap = p0.child("English").value.toString()
                        val math_chap = p0.child("Maths").value.toString()

                        var subj = intent.getIntExtra("net", 0)
                        if (subj == 1) {
                            NofMcqs.setText(chemistry_chap + " Chapters")
                        } else if (subj == 2) {
                            NofMcqs.setText(physics_chap + " Chapters")

                        } else if (subj == 3) {
                            NofMcqs.setText(english_chap + " Chapter")
                        } else if (subj == 4) {
                            NofMcqs.setText(intelligence_chap + " Chapter")
                        } else if (subj == 5) {
                            NofMcqs.setText(math_chap + " Chapters")

                        }


                    }
                }
            }

        })

        var subj = intent.getIntExtra("net", 0)
        if (subj == 1) {

            subject_neticon.setImageResource(R.drawable.chemistry_logo)
            sub_netname.setText("Chemistry")

            //Toast.makeText(this, "Hello Chemistry", Toast.LENGTH_SHORT).show()
            mref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("chemistry")

        } else if (subj == 2) {

            subject_neticon.setImageResource(R.drawable.physics_new_logo)
            sub_netname.setText("Physics")
            mref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("physics")
            //Toast.makeText(this, "Hello Physics", Toast.LENGTH_SHORT).show()
        } else if (subj == 3) {

            subject_neticon.setImageResource(R.drawable.eng)
            sub_netname.setText("English")

            mref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("english")
        } else if (subj == 4) {

            subject_neticon.setImageResource(R.drawable.idea)
            sub_netname.setText("Intelligence")
            mref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("intelli")
        } else if (subj == 5) {

            subject_neticon.setImageResource(R.drawable.math_logo)
            sub_netname.setText("Mathematics")
            mref = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("maths")
            // Toast.makeText(this, "Hello Mathematics", Toast.LENGTH_SHORT).show()
        }

        mrecyclerView = findViewById(R.id.recyclerView1)
        mrecyclerView.layoutManager = LinearLayoutManager(this)
        mrecyclerView.setLayoutManager(GridLayoutManager(this, 2))
        val option = FirebaseRecyclerOptions.Builder<ChapterModel>()
            .setQuery(mref, ChapterModel::class.java)
            .build()
        val FirebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<ChapterModel, MyViewHolder>(option) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

                    val layoutInflater =
                        LayoutInflater.from(this@NetMain)
                            .inflate(R.layout.chapters, parent, false)
                    return MyViewHolder(layoutInflater)

                }

                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    position: Int,
                    model: ChapterModel
                ) {


                    // click on chapters
                    holder.itemView.CardViewNet.setOnClickListener {


                        var intent = Intent(applicationContext, McatOptions::class.java)
                        intent.putExtra("mcat", 5)
                        intent.putExtra("net", subj)







                        val refid = getRef(position).key.toString()
                        val sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        val editor = sharedPreferences.edit()
                        editor.putString("id", refid)

                        editor.apply()

                        val chap = holder.topic_name.text
                        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                        val edito = sharedPreference.edit()
                        edito.putString("chap", chap.toString())
                        edito.apply()
                        startActivity(intent)

                    }

                    val refid = getRef(position).key.toString()


                    mref.child(refid).addValueEventListener(object : ValueEventListener {

                        override fun onCancelled(error: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.exists()) {
                                holder.topic_name.setText(model.name.toString())
                                holder.chap_name.setText("Chapter " + model.id.toString())


                                Glide.with(applicationContext).load(model.image).into(holder.images)


                            }
                        }

                    })
                }

            }









        mrecyclerView.adapter = FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter.startListening()

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var topic_name: TextView = itemView!!.findViewById(R.id.textViewName)
        var chap_name: TextView = itemView!!.findViewById(R.id.textViewNet)

        var images: ImageView = itemView!!.findViewById(R.id.imageViewNet)


    }


}

