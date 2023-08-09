package learningapp.superior.org.Models

import android.os.Parcel
import android.os.Parcelable

class ModelSnap(val fileName:String,val date:String,val key:String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!

    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(date)
        parcel.writeString(key)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelSnap> {
        override fun createFromParcel(parcel: Parcel): ModelSnap {
            return ModelSnap(parcel)
        }

        override fun newArray(size: Int): Array<ModelSnap?> {
            return arrayOfNulls(size)
        }
    }

}