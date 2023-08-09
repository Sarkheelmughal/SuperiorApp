package learningapp.superior.org.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import learningapp.superior.org.Models.BookMArkModel
import learningapp.superior.org.Models.ModelForTopics
import learningapp.superior.org.R

class BookMarkAdapter (val ctx: Context, var clickListener: OnMarkClick, var items:ArrayList<BookMArkModel>) : RecyclerView.Adapter<ViewHolderBookMark>() {

    var selectedItemIndex = -1

    override fun onBindViewHolder(holder: ViewHolderBookMark, position: Int) {
        val item = items[position]
        holder.textViewName.text = item.VideoName
        if (item.isSelected) {
            holder.textViewName.setTextColor(Color.parseColor("#FFBA5F"))

        } else {
            holder.textViewName.setTextColor(Color.parseColor("#25375F"))
        }
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(item, position)
            item.isSelected = true
            if (selectedItemIndex != -1)
                items[selectedItemIndex].isSelected = false
            selectedItemIndex = position
            notifyDataSetChanged()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBookMark {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topics_row,parent,false)
        return ViewHolderBookMark(v)
    }

    override fun getItemCount() = items.size

}

class ViewHolderBookMark(itemView: View) : RecyclerView.ViewHolder(itemView){

    val textViewName: TextView = itemView.findViewById(R.id.textView)


    fun initilise(items: ModelForTopics, action: OnTopicClick, grade : String) {
        textViewName.text = items.name

        itemView.setOnClickListener {
            action.onItemClick(items, adapterPosition, grade)


        }
    }
}

interface OnMarkClick {
    fun onItemClick(list: BookMArkModel, position: Int )

}
