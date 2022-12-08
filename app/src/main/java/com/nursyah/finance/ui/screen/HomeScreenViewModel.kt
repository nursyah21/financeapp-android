package com.nursyah.finance.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val user = hashMapOf(
  "first" to "Ada",
  "last" to "Lovelace",
  "born" to 1815
)

class HomeScreenViewModel: ViewModel() {
  private val db = Firebase.firestore

  fun sendSpending() = db.collection("users")
    .add(user)
    .addOnSuccessListener { documentReference ->
      Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
    }
    .addOnFailureListener { e ->
      Log.w("TAG", "Error adding document", e)
    }

  fun readSpending() = db.collection("users")
    .get()
    .addOnSuccessListener { result ->
      for (document in result) {
        Log.d("TAG", "${document.id} => ${document.data}")
      }
    }
    .addOnFailureListener { exception ->
      Log.w("TAG", "Error getting documents.", exception)
    }
}