package learningapp.superior.org.Reivision

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.activity_result.view.*
import learningapp.superior.org.R
import learningapp.superior.org.VideoPlayerFragments.QuizVideoModel
import java.util.ArrayList


class FragFourNew : Fragment() {

    // Initialize LiveData variables
    private val model: QuizVideoModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.activity_result, container, false)

        // Retrieve LiveData values and update UI
        val correctTxt = view.findViewById<TextView>(R.id.corectTextView)
        val incorectText = view.findViewById<TextView>(R.id.incorectText)
        val unanswered = view.findViewById<TextView>(R.id.unanswered)

        model.falseAns.observe(viewLifecycleOwner) {
            incorectText.text = "${it.toInt()} Incorrect"
        }

        model.trueAns.observe(viewLifecycleOwner) {
            correctTxt.text = "${it.toInt()} Correct"
        }

        model.unanswer.observe(viewLifecycleOwner) {
            unanswered.text = "${it.toInt()} Unanswered"
        }

        model.totalMcqs.observe(viewLifecycleOwner) {
            drawGraph()
        }

        return view
    }

    private fun drawGraph() {
        val pieChart = view?.findViewById(R.id.piechart1) as PieChart

        // Retrieve LiveData values for graph
        val tAns = model.trueAns.value ?: 0f
        val fAns = model.falseAns.value ?: 0f
        val unAns = model.unanswer.value ?: 0f
        val totalMcqs = model.totalMcqs.value ?: 1

        val noOfEmp = ArrayList<PieEntry>()
        noOfEmp.add(PieEntry(tAns))
        noOfEmp.add(PieEntry(fAns))
        noOfEmp.add(PieEntry(unAns))

        val dataSet = PieDataSet(noOfEmp, "Number Of Employees")
        val data = PieData(dataSet)
        pieChart.data = data

        val myColors = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0),
            Color.rgb(255, 156, 0)
        )
        val colors = ArrayList<Int>()
        for (c in myColors) colors.add(c)
        dataSet.colors = colors
        dataSet.setDrawValues(false)
        pieChart.animateXY(2000, 2000)
        pieChart.legend.isEnabled = false
        pieChart.description.isEnabled = false
        pieChart.centerText = "${tAns.toInt()}/$totalMcqs"
        pieChart.setCenterTextColor(Color.rgb(162, 162, 162))

        drawSecondGraph()
    }

    private fun drawSecondGraph() {
        val pieChart2 = view?.findViewById<PieChart>(R.id.piechart2)

        // Retrieve LiveData values for graph
        val tAns = model.trueAns.value ?: 0f
        val fAns = model.falseAns.value ?: 0f
        val unAns = model.unanswer.value ?: 0f
        val totalMcqs = model.totalMcqs.value ?: 1

        val falski = fAns + unAns
        val trueper = tAns

        val percent = (trueper / totalMcqs) * 100

        val noOfEmp2 = ArrayList<PieEntry>()
        noOfEmp2.add(PieEntry(trueper))
        noOfEmp2.add(PieEntry(falski))

        val dataSet2 = PieDataSet(noOfEmp2, "Number Of Employees")
        val data2 = PieData(dataSet2)
        pieChart2?.data = data2

        val myColors2 = intArrayOf(
            Color.rgb(57, 255, 33),
            Color.rgb(255, 0, 0)
        )
        val colors2 = ArrayList<Int>()
        for (b in myColors2) colors2.add(b)

        dataSet2.colors = colors2
        dataSet2.setDrawValues(false)
        pieChart2?.animateXY(2000, 2000)
        pieChart2?.legend?.isEnabled = false
        pieChart2?.description?.isEnabled = false
        pieChart2?.holeRadius = 65f
        pieChart2?.centerText = "${percent.toInt()} %"
        pieChart2?.setCenterTextColor(Color.rgb(162, 162, 162))

        val sharetxt = view?.findViewById<TextView>(R.id.ShareText)
        if (percent.toInt() == 25 || percent.toInt() == 0) {
            sharetxt?.text = "You need more practice."
        } else if (percent.toInt() == 50) {
            sharetxt?.text = "You need to keep it up."
        }

        drawThirdGraph()
    }

    private fun drawThirdGraph() {
        val pieChart3 = view?.findViewById<PieChart>(R.id.piechart3)

        // Retrieve LiveData values for graph
        val screen1 = model.screen1.value ?: 1
        val screen2 = model.screen2.value ?: 1
        val screen3 = model.screen3.value ?: 1
        val screen4 = model.screen4.value ?: 1

        val sumTime = screen1 + screen2 + screen3 + screen4
        val avgTime = sumTime / model.totalMcqs.value!! ?: 1

        val noOfEmp3 = ArrayList<PieEntry>()
        noOfEmp3.add(PieEntry(screen1.toFloat()))
        noOfEmp3.add(PieEntry(screen2.toFloat()))
        noOfEmp3.add(PieEntry(screen3.toFloat()))
        noOfEmp3.add(PieEntry(screen4.toFloat()))

        val dataSet3 = PieDataSet(noOfEmp3, "Number Of Employees")
        val data3 = PieData(dataSet3)
        pieChart3?.data = data3

        val myColors3 = intArrayOf(
            Color.rgb(254, 187, 250),
            Color.rgb(251, 153, 246),
            Color.rgb(253, 114, 246),
            Color.rgb(253, 79, 244)
        )

        val colors3 = ArrayList<Int>()
        for (d in myColors3) colors3.add(d)
        dataSet3.colors = colors3
        dataSet3.setDrawValues(false)

        pieChart3?.animateXY(2000, 2000)
        pieChart3?.legend?.isEnabled = false
        pieChart3?.description?.isEnabled = false
        pieChart3?.holeRadius = 65f
        pieChart3?.centerText = "${avgTime} Second"
        pieChart3?.setCenterTextColor(Color.rgb(162, 162, 162))
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//
//
//        if (getFragmentManager() != null) {
//
//            requireFragmentManager()
//                .beginTransaction()
//                .detach(this)
//                .attach(this)
//                .commit()
//        }
//    }


}
