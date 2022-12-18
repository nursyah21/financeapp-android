package com.nursyah.finance.benchmark

import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.nursyah.finance.benchmark.Constant.TARGET_PACKAGE
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BaselineProfileBenchmark {
  @get:Rule
  val benchmarkRule = MacrobenchmarkRule()

  @Test
  fun startup() {

    benchmarkRule.measureRepeated(
      packageName = TARGET_PACKAGE,
      metrics = listOf(StartupTimingMetric()),
      iterations = 5,
      setupBlock = {
        // Press home button before each run to ensure the starting activity isn't visible.
        pressHome()
      }
    ) {
      // starts default launch activity
      startActivityAndWait()
    }
  }
}