package learningapp.superior.org.Models

import android.os.Parcel
import android.os.Parcelable

data class ModelForTopics(
    val key:String,
    val name:String,val urlEng:String,val urlurdu:String
    ,val engUrlDownload:String ,val urduUrlDownload:String ,
//    val videoID: String,
    var isDownloaded: Boolean = false,
    var isDownloading : Boolean = false
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        //parcel.readString()!!,
      parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeString(key)
        parcel.writeString(name)
        parcel.writeString(urlEng)
        parcel.writeString(urlurdu)
        parcel.writeString(engUrlDownload)
         parcel.writeString(urduUrlDownload)
//        parcel.writeString(videoID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelForTopics> {
        override fun createFromParcel(parcel: Parcel): ModelForTopics {
            return ModelForTopics(parcel)
        }

        override fun newArray(size: Int): Array<ModelForTopics?> {
            return arrayOfNulls(size)
        }
    }

    var isSelected: Boolean = false
}

