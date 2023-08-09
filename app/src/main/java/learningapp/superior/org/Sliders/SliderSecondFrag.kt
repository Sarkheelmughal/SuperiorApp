package learningapp.superior.org.Sliders

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import learningapp.superior.org.Home.Options
import learningapp.superior.org.R

class SliderSecondFrag : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_slider_second, container, false)

        val chemFragBtn=view.findViewById<Button>(R.id.chemFragBtn)
        chemFragBtn.setOnClickListener {
            val intent = Intent(activity, Options::class.java)
            intent.putExtra("mock",4)
            startActivity(intent)

//            Toast.makeText(requireActivity(), "Hello", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}