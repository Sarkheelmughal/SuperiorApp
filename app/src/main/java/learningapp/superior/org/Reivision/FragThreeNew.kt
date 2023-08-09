package learningapp.superior.org.Reivision

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_frag_three_new.view.*
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.Fragmentone


class FragThreeNew : Fragment() {
    lateinit var ref: DatabaseReference
    lateinit var reff: DatabaseReference
    lateinit var recycleView: RecyclerView
    lateinit var items: ArrayList<ModelForTopics>
    lateinit var dataPasser: Fragmentone.OnDataPass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View =  inflater.inflate(R.layout.fragment_frag_three_new, container, false)

        view.started_quiz.setOnClickListener{

            swapFragment()



        }

        return view
    }




    private fun swapFragment() {

        val NAME = Revisionfragment()
        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.fragNew3,NAME)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }


}