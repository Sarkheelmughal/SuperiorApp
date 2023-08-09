package learningapp.superior.org.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import learningapp.superior.org.Models.PastPaperModel
import learningapp.superior.org.R

class PastPaperAdapter(val ctx: Context, var clickListener: OnPastPaperClick, var chapterlist:ArrayList<PastPaperModel>) : RecyclerView.Adapter<ViewHolderPastPaper>() {


    override fun onBindViewHolder(holder: ViewHolderPastPaper, position: Int) {
        val user : PastPaperModel = chapterlist[position]
        holder.textViewName.text=user.name


//        var image = BitmapFactory.decodeFile(user.icon)
//        holder.iconView.setImageBitmap(image).toString()


        holder.initilise(chapterlist.get(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPastPaper {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_pastpaper,parent,false)
        return ViewHolderPastPaper(v)
    }

    override fun getItemCount(): Int {
        return chapterlist.size
    }

}

class ViewHolderPastPaper(itemView: View) : RecyclerView.ViewHolder(itemView){

    val textViewName = itemView.findViewById(R.id.textView) as TextView



    fun initilise(list: PastPaperModel, action: OnPastPaperClick){
        textViewName.text=list.name

//        iconView=list.icon

        itemView.setOnClickListener{
            action.onItemClick(list,adapterPosition)
        }

    }
}

interface OnPastPaperClick {
    fun onItemClick(list: PastPaperModel, position: Int )

} 