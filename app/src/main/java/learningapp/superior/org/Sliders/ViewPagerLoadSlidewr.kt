package learningapp.superior.org.Sliders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerLoadSlidewr( fm: FragmentManager):FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {


        return when (position) {
            0 -> {
                return SliderFirstFrag()
            }
            1 -> {
                return SliderSecondFrag()
            }
            2 -> {
                return SliderThirdFrag()
            }

            else -> SliderThirdFrag()
        }
    }

    override fun getCount(): Int {

        return 3
    }

}