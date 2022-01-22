package com.cellulant.myweatherforcast.ui.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.cellulant.myweatherforcast.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@MediumTest
@RunWith(AndroidJUnit4::class)

class MainActivityTest {

    @Test
    fun searchActionTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val text= Espresso.onView(ViewMatchers.withId(R.id.et_search)).toString().isEmpty()

        //Check if string is empty
        Assert.assertNotEquals("", text)

    }


    @Test
    fun searchActionClickTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        //Using Espresso.
        val action=Espresso.onView(ViewMatchers.withId(R.id.et_search))
            .perform(ViewActions.closeSoftKeyboard())

        //Check if search Clicked closes the keyboard

    }


}