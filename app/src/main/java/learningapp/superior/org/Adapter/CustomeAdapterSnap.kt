package learningapp.superior.org.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import learningapp.superior.org.Models.ModelSnap
import learningapp.superior.org.R


class CustomeAdapterSnap(val ctx: Context, var clickListener: OnSnapClick, var chapterlist:ArrayList<ModelSnap>) : RecyclerView.Adapter<ViewHolderSnap>() {


    override fun onBindViewHolder(holder: ViewHolderSnap, position: Int) {
        val user : ModelSnap = chapterlist[position]
       holder.textViewName.text=user.fileName
        holder.textViewChapter.text=user.date


//        val url =user.icon
//
//        Glide.with(getApplicationContext()).load(url).into(holder.iconView)

     holder.initilise(chapterlist.get(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSnap {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_snap,parent,false)
        return ViewHolderSnap(v)
    }

    override fun getItemCount(): Int {
        return chapterlist.size
         }

}

class ViewHolderSnap(itemView: View) :RecyclerView.ViewHolder(itemView){

    val textViewName = itemView.findViewById(R.id.textView) as TextView
    val textViewChapter = itemView.findViewById(R.id.textView2) as TextView

    var iconView=itemView.findViewById(R.id.imageView1) as ImageView

    fun initilise(list: ModelSnap, action: OnSnapClick){
        textViewName.text=list.fileName
        textViewChapter.text=list.date
//        iconView=list.icon

        itemView.setOnClickListener{
            action.onItemClick(list,adapterPosition)
        }

    }
}

interface OnSnapClick {
    fun onItemClick(list: ModelSnap, position: Int )

}
