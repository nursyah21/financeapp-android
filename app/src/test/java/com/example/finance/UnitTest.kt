package com.example.finance

import androidx.core.text.isDigitsOnly
import com.example.finance.core.Utils
import com.example.finance.db.model.Data
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import kotlin.reflect.typeOf

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {


  @Test
  fun stringToInt(){
    try{
      val i = "values 234"
      if(i.isDigitsOnly())
        println(i.toLong())
    }catch (e:Exception){
      println("Error: $e")
    }
  }
  @Test
  fun testDate(){
    val calendar = Calendar.getInstance().time
    val time = SimpleDateFormat("HH_mm_ss_dd_MM_yyyy").format(calendar)
    println(time)
  }

  @Test
  fun `testing Accumulated`(){
    val data = listOf(
      Data(category = "Spending", item = "Spending", value = 10000),
      Data(category = "Spending", value = 10000),
      Data(category = "Income", value = 10000),
    )
    assertEquals(20000, Utils.totalData(data, "Spending"))

  }
  @Test
  fun getConvertText(){
    assertEquals("10,000", Utils.convertText("10000"))
    assertEquals("-10,000", Utils.convertText("-10000"))
    assertEquals("1,000", Utils.convertText("1000"))
    assertEquals("-1,000", Utils.convertText("-1000"))
    assertEquals("-100", Utils.convertText("-100"))
    assertEquals("100", Utils.convertText("100"))
    assertEquals("0", Utils.convertText("0"))
  }

  @Test
  fun getId(){
    val long = Long.MAX_VALUE
    println(long.toString().length)
    var i = listOf(
      LocalDate.of(2000,10,1),
      LocalDate.of(2000,10,12),
      LocalDate.of(2000,10,11),
      LocalDate.of(2000,1,1),
      LocalDate.of(2000,12,1)
    )
    i = i.sorted().reversed()
    println(i)
  }


}