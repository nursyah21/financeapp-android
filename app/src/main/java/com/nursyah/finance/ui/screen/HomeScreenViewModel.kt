package com.nursyah.finance.ui.screen

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nursyah.finance.core.User
import com.nursyah.finance.core.Utils

val user = hashMapOf(
  "first" to "Ada",
  "last" to "Lovelace",
  "born" to 1815
)

class HomeScreenViewModel(app:Context): ViewModel() {
  private val db = Firebase.firestore
  private val userEmail = User(app as Activity).getUser()
  private val source = Source.CACHE
  private val time = Utils.getTime().toString()

  fun sendSpending(data: HashMap<String, String>) = db.collection("spending").document(userEmail)
    .collection(time).add(data)
    .addOnSuccessListener {
      Log.d("TAG", "Success Adding Data: $data")
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