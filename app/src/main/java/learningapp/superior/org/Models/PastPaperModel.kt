package learningapp.superior.org.Models

import android.os.Parcel
import android.os.Parcelable

data class PastPaperModel (val name:String,val url:String) :
    Parcelable {
    constructor(parcel: Parcel) : this(

        parcel.readString()!!,
        parcel.readString()!!

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)

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