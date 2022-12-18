package com.nursyah.finance.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import com.nursyah.finance.benchmark.Constant.TARGET_PACKAGE
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalBaselineProfilesApi::class)
class BaselineProfileGenerator {
  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun startup() = baselineProfileRule.collectBaselineProfile(
    packageName = TARGET_PACKAGE,
    profileBlock = {
      startActivityAndWait()
    }
  )
}