package learningapp.superior.org.Utils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

val UID = FirebaseAuth.getInstance().currentUser!!.uid

class FireStoreUtils {
    val fireStoreDatabase = FirebaseFirestore.getInstance()
    val UID = FirebaseAuth.getInstance().currentUser!!.uid
}

class RealTimeDBUtils {
    val firebaseRTDb = FirebaseDatabase.getInstance()
}