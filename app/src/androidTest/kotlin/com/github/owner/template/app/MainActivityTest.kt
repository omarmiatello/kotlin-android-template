package com.github.owner.template.app

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun typeANumber_resultIsDisplayed() {
        // onView(withId(R.id.edit_text_factorial)).perform(typeText("1"), closeSoftKeyboard())
        // onView(withId(R.id.button_compute)).perform(click())
        //
        // onView(withId(R.id.text_result)).check(matches(isDisplayed()))
        // onView(withId(R.id.text_result)).check(matches(withText("1")))
    }
}
