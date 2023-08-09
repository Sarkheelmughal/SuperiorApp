package learningapp.superior.org.VideoPlayerFragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizVideoModel: ViewModel() {

   private var _unanswer = MutableLiveData<Float>()
    private var _trueAns = MutableLiveData<Float>()
    private var _falseAns = MutableLiveData<Float>()

    private var _screen1 = MutableLiveData<Int>()
    private var _screen2 = MutableLiveData<Int>()
    private var _screen3 = MutableLiveData<Int>()
    private var _screen4 = MutableLiveData<Int>()
    private var _totalMcqsMLD = MutableLiveData<Int>()

//    private var _screen4 = MutableLiveData<Int>()


    val unanswer :LiveData<Float> = _unanswer
    val trueAns :LiveData<Float> = _trueAns
    val falseAns :LiveData<Float> = _falseAns

    val screen1 :LiveData<Int> = _screen1
    val screen2 :LiveData<Int> = _screen2
    val screen3 :LiveData<Int> = _screen3
    val screen4 :LiveData<Int> = _screen4
     val totalMcqs :LiveData<Int> = _totalMcqsMLD






    fun setMarks(
        totalMcq:Int,
        tAns: Float,
        fAns: Float,
        unans: Float,
        screen1: Int,
        screen2: Int,
        screen3: Int,
        screen4: Int
    ){
        _unanswer.value=unans
        _trueAns.value=tAns
        _falseAns.value=fAns

        _screen1.value=screen1
        _screen2.value=screen2
        _screen3.value=screen3
        _screen4.value=screen4
        _totalMcqsMLD.value=totalMcq


    }
}