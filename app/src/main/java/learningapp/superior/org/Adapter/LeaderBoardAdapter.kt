package learningapp.superior.org.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import learningapp.superior.org.Models.LeaderBoardModel
import learningapp.superior.org.R

class LeaderBoardAdapter(
    val ctx: Context,
    var namelist: ArrayList<LeaderBoardModel>
) : RecyclerView.Adapter<ViewHolderList>() {


    override fun onBindViewHolder(holder: ViewHolderList, position: Int) {
        val user : LeaderBoardModel = namelist[position]
        holder.nameList.text=user.name
        holder.scoreList.text=user.score.toString()

        holder.initilise(namelist.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderList {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.leader_board_adapter,parent,false)
        return ViewHolderList(v)
    }

    override fun getItemCount(): Int {
        return namelist.size
    }

}

class ViewHolderList(itemView: View) : RecyclerView.ViewHolder(itemView){

    val nameList = itemView.findViewById(R.id.nameOfStudentTV) as TextView
    val scoreList = itemView.findViewById(R.id.scoreOfStudentTV) as TextView
    val numberList = itemView.findViewById(R.id.listNumberTV) as TextView


    @SuppressLint("SetTextI18n")
    fun initilise(list: LeaderBoardModel){


        numberList.text=(adapterPosition+4).toString() + "."
        nameList.text=list.name
        scoreList.text=list.score.toString()



//        itemView.setOnClickListener{
//            action.onItemClick(list,adapterPosition)
//        }

    }
}

//interface OnChapterClickTeacher {
//    fun onItemClick(list: TeacherData, position: Int )
//
//}
