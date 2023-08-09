package learningapp.superior.org.EntryTestInter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_mdcat.*
import learningapp.superior.org.Netscreens.NetMain
import learningapp.superior.org.R

class Mdcat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mdcat)

        mdcatBackIV.setOnClickListener { onBackPressed() }
        var subject=intent.getStringExtra("subject")
        if (subject=="net"){

            lateinit var reff: DatabaseReference
            reff = FirebaseDatabase.getInstance().getReference("mockTest").child("net")
                .child("numberofMcqs").child("9th")
            reff.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
//                TODO("not implemented")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    for (h in p0.children) {
                        if (p0!!.exists()) {


                            val mcqs_chem=p0.child("Chemistry").value.toString()
                            Nchem.setText(mcqs_chem+" Mcqs")
                            val mcqs_eng=p0.child("English").value.toString()
                            Nmath.setText(mcqs_eng+" Mcqs")
                            val mcqs_intell=p0.child("Intelligence").value.toString()
                            Nbio1.setText(mcqs_intell+" Mcqs")
                            val mcqs_phys=p0.child("Physics").value.toString()
                            NPhy.setText(mcqs_phys+" Mcqs")
                            val mcqs_maths=p0.child("Maths").value.toString()
                            Nmath1.setText(mcqs_maths+" Mcqs")

                        }
                    }
                }

            })



            icHeaderIV.setImageResource(R.drawable.net)
            linearSubjectsmore1.visibility=View.VISIBLE
            Bio_mcat.visibility=View.INVISIBLE
            chem_mcat.setOnClickListener {
                var intent = Intent(this, NetMain::class.java)
                intent.putExtra("net",1)
                startActivity(intent)
            }
            phys_mcat.setOnClickListener {
                var intent = Intent(this, NetMain::class.java)
                intent.putExtra("net",2)
                startActivity(intent)
            }
            eng_mcat.setOnClickListener {
                var intent = Intent(this, NetMain::class.java)
                intent.putExtra("net",3)
                startActivity(intent)
            }
            intelligence.setOnClickListener {
                var intent = Intent(this, NetMain::class.java)
                intent.putExtra("net",4)
                startActivity(intent)
            }
            math_mcat.setOnClickListener {
                var intent = Intent(this, NetMain::class.java)
                intent.putExtra("net",5)
                startActivity(intent)
            }

        }
        else
        {

            phys_mcat.setOnClickListener {
                var intent = Intent(this, McatOptions::class.java)
                intent.putExtra("mcat",1)
                startActivity(intent)
            }

            chem_mcat.setOnClickListener {
                var intent = Intent(this, McatOptions::class.java)
                intent.putExtra("mcat",2)
                startActivity(intent)
            }
            Bio_mcat.setOnClickListener {
                var intent = Intent(this, McatOptions::class.java)
                intent.putExtra("mcat",3)
                startActivity(intent)
            }
            eng_mcat.setOnClickListener {
                var intent = Intent(this, McatOptions::class.java)
                intent.putExtra("mcat",4)
                startActivity(intent)
            }
        }












    }
}