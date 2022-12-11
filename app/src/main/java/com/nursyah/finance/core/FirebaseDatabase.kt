package com.nursyah.finance.ui.components

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nursyah.finance.core.User


class FirebaseDatabaseViewModel(app: Context): ViewModel(){
  private val db = Firebase.firestore
  private val userEmail = User(app as Activity).getUser()
  private val source = Source.CACHE

  fun test(){
    println(userEmail)
  }

  fun getData(collection: String){
//    var result:MutableMap<String, Any> = mutableMapOf(Pair("null","null"))

//    db.collection(collection).document(userEmail).get(source).addOnCompleteListener{
//      if(it.isSuccessful){
//        val document = it.result
//        Log.d("TAG", "${document.data}")
//      }else{
//        Log.d("TAG", "Cache get Failed: ${it.exception}")
//      }
//    }

  }


}