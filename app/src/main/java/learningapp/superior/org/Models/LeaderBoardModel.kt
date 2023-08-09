package learningapp.superior.org.Models

import android.os.Parcel
import android.os.Parcelable


data class LeaderBoardModel (val name:String,val key:String,val score:Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(


        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(key)
        parcel.writeInt(score)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LeaderBoardModel> {
        override fun createFromParcel(parcel: Parcel): LeaderBoardModel {
            return LeaderBoardModel(parcel)
        }

        override fun newArray(size: Int): Array<LeaderBoardModel?> {
            return arrayOfNulls(size)
        }
    }

}