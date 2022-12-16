package com.nursyah.finance

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nursyah.finance.core.FirebaseDatabase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @Test
  fun useAppContext() {
    // Context of the app under test.
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.nursyah.finance", appContext.packageName)
  }

  @Test
  fun getDataFirebase(){
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    FirebaseDatabase(appContext).test()
//    FirebaseDatabase(appContext).getData("spending")
    //FirebaseDatabaseViewModel(appContext).test()

  }

  @Test
  fun getData(){
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    val data = com.nursyah.finance.core.Data(appContext)
    data.setData("item", "1000")
    val data1 = hashMapOf(
      "item" to "item",
      "value" to "1000"
    )
    assertEquals(data1, data.getData())
    val data2 = hashMapOf(
      "item" to "",
      "value" to ""
    )
    assertEquals(true, (data2["item"] == "" && data2["value"] == ""))
    println(data.getData())
  }
}