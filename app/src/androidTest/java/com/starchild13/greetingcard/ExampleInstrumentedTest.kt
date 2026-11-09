/**
 * Instrumented UI and integration tests for Card Studio.
 *
 * Contains tests executed directly on an Android hardware device or emulator,
 * validating Android framework components, application context, and package configurations.
 */
package com.starchild13.greetingcard

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test suite executing on an Android device or emulator.
 *
 * Validates application target context and package name integrity.
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    /**
     * Verifies that the instrumented application context matches the expected package name.
     */
    @Test
    fun useAppContext() {
        // Retrieve target context from InstrumentationRegistry
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.starchild13.greetingcard", appContext.packageName)
    }
}
