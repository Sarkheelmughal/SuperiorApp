package learningapp.superior.org.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.FacebookSdk.getApplicationContext
import learningapp.superior.org.Models.Model
import learningapp.superior.org.R


class CustomeAdapter(val ctx: Context, var clickListener: OnChapterClick, var chapterlist:ArrayList<Model>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : Model = chapterlist[position]
       holder.textViewName.text=user.name
        holder.textViewChapter.text=user.desc

//        var image = BitmapFactory.decodeFile(user.icon)
//        holder.iconView.setImageBitmap(image).toString()

        val url =user.icon

        Glide.with(getApplicationContext()).load(url).into(holder.iconView)

     holder.initilise(chapterlist.get(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chapterlist.size
         }

}

class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    val textViewName = itemView.findViewById(R.id.textView) as TextView
    val textViewChapter = itemView.findViewById(R.id.textView2) as TextView

    var iconView=itemView.findViewById(R.id.imageView1) as ImageView

    fun initilise(list: Model, action: OnChapterClick){
        textViewName.text=list.name
        textViewChapter.text=list.desc
//        iconView=list.icon

        itemView.setOnClickListener{
            action.onItemClick(list,adapterPosition)
        }

    }
}

interface OnChapterClick {
    fun onItemClick(list: Model, position: Int )

}
