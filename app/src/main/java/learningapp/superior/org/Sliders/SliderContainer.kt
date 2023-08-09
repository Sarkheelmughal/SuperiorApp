package learningapp.superior.org.Sliders

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.layout_slider_container.*
import learningapp.superior.org.R

class SliderContainer:AppCompatActivity() {

    var viewPager: ViewPager? =null
    //var indicators: CirclePageIndicator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (supportActionBar != null) {
            supportActionBar?.hide()
        }
        setContentView(R.layout.layout_slider_container)

        viewPager=findViewById(R.id.viewpager)
       // indicators = findViewById(R.id.indicator)

//        intitViews()
        backFromSlider.setOnClickListener {
            onBackPressed()
        }
//        btn_next.setOnClickListener {
////            if (viewPager!!.currentItem <= viewPager!!.childCount) {
////                viewPager!!.setCurrentItem(viewPager!!.currentItem + 1, true)
////            } else {
//                val intent = Intent(this, Options::class.java)
//
//                intent.putExtra("mock",3)
//
//                startActivity(intent)
//                finish()
////            }
//        }

    }

//    private fun intitViews() {
//        val viewPagerAdapter_password = ViewPagerLoadSlidewr(supportFragmentManager)
//        viewPager?.adapter = viewPagerAdapter_password
//
//        indicators!!.setViewPager(viewPager)
//
//        val density = resources.displayMetrics.density
//
////Set circle indicator radius
//        indicators!!.radius = 3 * density
//    }

}