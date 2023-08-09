package learningapp.superior.org.Reivision

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_frag_three_new.view.*
import learningapp.superior.org.Models.Model
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.Fragmenttwo


class FragThreStartBtn : Fragment() {



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
        val model = requireArguments().getParcelable<Model>("object")

        Log.d("actually ", "actually "+ model)
        val NAME = Fragmenttwo()
        val fragmentTransaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        fragmentTransaction.replace(R.id.fragNew3,NAME)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

}