package com.nursyah.finance

import com.nursyah.finance.core.Utils
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun `get id`(){
    val uid = UUID.randomUUID()
    println("uid: $uid")
  }

  @Test
  fun `test 1`(){
    val a = Int.MAX_VALUE
    val b = UInt.MAX_VALUE
    val c = Long.MAX_VALUE
    println("max int = $a\nmax uint = $b")
  }

  @Test
  fun `get date today`(){
    println("date today: ${Utils.getTime()}")
  }

  @Test
  fun `get format money`(){
    assertEquals("1,000,000", Utils.formatMoney("1000000"))
    assertEquals("1,000", Utils.formatMoney("1000"))
    assertEquals("10,000", Utils.formatMoney("10000"))
    assertEquals("10", Utils.formatMoney("10"))
    assertEquals("1", Utils.formatMoney("1"))
    assertEquals("0", Utils.formatMoney("0"))
  }
}