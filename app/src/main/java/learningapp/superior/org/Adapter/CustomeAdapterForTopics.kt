package learningapp.superior.org.Adapter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.topics_row.view.*
import learningapp.superior.org.Download.CircleProgressBar
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R
import learningapp.superior.org.Utils.getThumbsDirPath
import learningapp.superior.org.Utils.getVideoDirPath
import learningapp.superior.org.VideoPlayerFragments.Fragmentone
import java.io.File
import java.util.*

class CustomeAdapterForTopics(
    val ctx: Context,
    var clickListener: OnTopicClick,

    val items: ArrayList<ModelForTopics>,
   val grade: String
    //, private var downloadBtnClickInterface:DownloadBtnClickInterface
) : RecyclerView.Adapter<TopicViewHolder>() {

    var selectedItemIndex = -1

    inner class MVH(view: View) : RecyclerView.ViewHolder(view) {
//        val thumb: ImageView = view.findViewById(R.id.thumb)
//        val title: TextView = view.findViewById(R.id.title)
//        val downloadBtn: ImageButton = view.findViewById(R.id.downloadButton)



      //  val progressBar: CircleProgressBar = view.findViewById(R.id.progressBar)
//        val percentageTxt: TextView = view.findViewById(R.id.pTxt)
//
//        val pLay: RelativeLayout = view.findViewById(R.id.pLayout)
//
//        val aDown: ImageView = view.findViewById(R.id.adown)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {


        fun hideProgress() {
            holder.downloadIV.visibility = View.VISIBLE
            holder.progressBar.visibility = View.GONE
           // holder.percentageTxt.visibility = View.GONE
            holder.downloadedIV.visibility = View.GONE

        }

        fun showProgress() {
         holder.downloadIV.visibility = View.GONE
            holder.progressBar.visibility = View.VISIBLE
            //holder.percentageTxt.visibility = View.VISIBLE
            holder.downloadedIV.visibility = View.GONE

        }

        fun downloadSuccess() {
            holder.downloadIV.visibility = View.GONE
            holder.progressBar.visibility = View.GONE
          //  holder.percentageTxt.visibility = View.GONE
            holder.downloadedIV.visibility = View.VISIBLE


        }



        val videoData = items[position]

      //  val thumbUri = items[position].videoThumbUri


        val videoFile =
            File("${getVideoDirPath(ctx).absolutePath}/${videoData.name}_exambites.encvid")
//            File("${getVideoDirPath(ctx).absolutePath}/${videoData.name}###${videoData.videoID}.encvid")

        val videoFileUrdu =
            File("${getVideoDirPath(ctx).absolutePath}/${videoData.name}_UrduExambites.encvid")

        Log.d("pathOfvideoFile",videoFile.toString())
        val thumbFile =
            File("${getThumbsDirPath(ctx).absolutePath}/${videoData.name}_exambites.jpg")

        if ((videoFile.exists() && videoFileUrdu.exists()) || thumbFile.exists())
        { downloadSuccess()
        videoData.isDownloaded=true
        }
        else {
            hideProgress()
            videoData.isDownloaded=false
        }

        if (videoData.isDownloading)
            showProgress()


        val item = items[position]
        holder.textViewName.text = item.name

        if (item.isSelected) {
            holder.textViewName.setTextColor(Color.parseColor("#E84F2E"))

        } else {
            holder.textViewName.setTextColor(Color.parseColor("#25375F"))
        }


        holder.textViewName.setOnClickListener {
            clickListener.onItemClick(item, position, grade)
            item.isSelected = true
            if (selectedItemIndex != -1)
                items[selectedItemIndex].isSelected = false
            selectedItemIndex = position
           notifyDataSetChanged()
        }

        val downloadCallBack: (Float, Int) -> Unit = { progress, totalLen ->
//            Fragmentone().activity?.runOnUiThread {
//            ctx.run {
            Handler(Looper.getMainLooper()).post {
                Log.d("resultdownloadCallBack", Unit.toString())

                holder.downloadIV.visibility = View.GONE
                holder.progressBar.visibility = View.VISIBLE
               // holder.percentageTxt.visibility = View.VISIBLE
                val downloadedPercentage = ((progress / totalLen) * 100)
                //downloadedPercentage = DecimalFormat("0.0").format(downloadedPercentage).toDouble()
                holder.progressBar.setMax(totalLen)
                holder.progressBar.setProgress(progress)
               // holder.percentageTxt.text = "${downloadedPercentage.toInt()}%"
            }
        }
        val savePAidOrNot = ctx.getSharedPreferences("save", Context.MODE_PRIVATE)
        val savePAidOrNotData = savePAidOrNot.getString("save", "null")
        holder.downloadIV.setOnClickListener {
            if (savePAidOrNotData=="paid") {
                showToast(item.name, item.engUrlDownload)
                clickListener.onItemClickDownload(item, position, items[position], downloadCallBack)

                holder.progressBar.apply {
                    setMax(100)
                    setProgress(0f)
                    // holder.percentageTxt.text = "0%"
                    Log.d("holderprogressBar ", "run")

                }

                Fragmentone().downloadFile(ctx, items[position], downloadCallBack) {
                    Log.d("resultCallBackVideo", it.toString())

                    Handler(Looper.getMainLooper()).post {
//


                        Log.d("resultCallBackVideo", "FA runOnUiThread")

                        val name = items[position].name
                        when (it) {
                            -2 -> {
                                showToast("Error in Downloading $name!", item.engUrlDownload)
                                items[position].isDownloading = false
                                hideProgress()
                            }
                            -1 -> {
                                showToast("$name Already Downloaded!", item.engUrlDownload)
                                items[position].isDownloading = false
                                downloadSuccess()
                            }
                            0 -> {
                                showToast("$name Already Downloading...", item.engUrlDownload)
                            }
                            1 -> {
                                //showToast("$name Downloaded Successfully")
                                items[position].isDownloading = false
                                downloadSuccess()
                            }
                            2 -> {
                                //showToast("$name Downloading Started")
                                items[position].isDownloading = true
                                showProgress()
                            }
                        }
                    }
                }
            }
            else {paidToast()}

        }

//
//        holder.downloadedIV.setOnClickListener {
////
//            val intent = Intent(ctx, PlayerActivity::class.java)
//            intent.putExtra("data", items[position])
//            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
//            ctx.startActivity(intent)
//
//        }

        //paidOrNot(ctx)
    }

fun paidOrNot(ctx: Context) {
    val savePAidOrNot = ctx.getSharedPreferences("save", Context.MODE_PRIVATE)
    val savePAidOrNotData = savePAidOrNot.getString("save", "null")

}
    fun paidToast(){
        Toast.makeText(ctx, "This feature is paid!", Toast.LENGTH_SHORT).show()
    }
    private fun showToast(msg: String, urlDownload: String) {
        Log.d("checkClick",msg)
        Log.d("checkClickurl",urlDownload)

        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder =
        with(LayoutInflater.from(parent.context).inflate(R.layout.topics_row, parent, false)) {
            TopicViewHolder(
                this
                // ,downloadBtnClickInterface
            )
        }

    override fun getItemCount() = items.size

}

class TopicViewHolder(
    itemView: View

    //,downloadBtnClickInterface: DownloadBtnClickInterface
) : RecyclerView.ViewHolder(itemView) {
    val textViewName: TextView = itemView.findViewById(R.id.textViewChapName)
    val downloadIV: ImageView = itemView.findViewById(R.id.downloadIV)
    val downloadedIV: ImageView = itemView.findViewById(R.id.downloadedIV)
//    val percentageTxt: TextView = itemView.findViewById(R.id.pTxt) working fine

    val progressBar: CircleProgressBar = itemView.findViewById(R.id.progressBar)
//     var downloadBtnClickInterface:DownloadBtnClickInterface
//    init{
//        this.downloadBtnClickInterface = downloadBtnClickInterface
//
//
//        itemView.download_btn.setOnClickListener {
//
//            if (downloadBtnClickInterface != null) {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    downloadBtnClickInterface.Download(position)
//                   // itemView.download_btn.setBackgroundColor(Color.GREEN)
//                }
//            }
//        }


    //    }


    fun initilise(items: ModelForTopics, action: OnTopicClick, grade: String) {
        textViewName.text = items.name

        itemView.setOnClickListener {
            action.onItemClick(items, adapterPosition, grade)

        }

        itemView.downloadedIV.setOnClickListener {
            action.DownloadedVid(items)
        }


    }

}

interface OnTopicClick {
    fun onItemClick(items: ModelForTopics, position: Int, grade:String)
    fun onItemClickDownload(
        items: ModelForTopics,
        position: Int,
        videoData: ModelForTopics,
        downloadCallBack: (Float, Int) -> Unit
    )
    fun DownloadedVid(items: ModelForTopics)

}


//
//interface DownloadBtnClickInterface {
//    fun Download(position: Int)
//}


