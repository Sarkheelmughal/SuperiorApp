package learningapp.superior.org.Models

import android.os.Parcel
import android.os.Parcelable

class BookMArkModel (val VID:String,val VideoName:String ) :Parcelable {
    constructor(parcel: Parcel) : this(


        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(VID)
        parcel.writeString(VideoName)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookMArkModel> {
        override fun createFromParcel(parcel: Parcel): BookMArkModel {
            return BookMArkModel(parcel)
        }

        override fun newArray(size: Int): Array<BookMArkModel?> {
            return arrayOfNulls(size)
        }
    }
    var isSelected: Boolean = false

}