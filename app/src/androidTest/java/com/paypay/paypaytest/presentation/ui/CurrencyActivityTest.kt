package com.paypay.paypaytest.presentation.ui

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.textfield.TextInputEditText
import com.paypay.paypaytest.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.core.IsAnything.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyActivityTest {

    @Rule
    @JvmField
    val rule = ActivityScenarioRule(CurrencyActivity::class.java)


    @Before
    fun setUp() {
//        ActivityScenario.launch(CurrencyActivity::class.java)
    }


    @Test
    fun checkUI() {
        Espresso.onView(ViewMatchers.withId(R.id.tv_amt_hint))
        Espresso.onView(ViewMatchers.withId(R.id.til_amount))
        Espresso.onView(ViewMatchers.withId(R.id.actv_currency_name))
        Espresso.onView(ViewMatchers.withId(R.id.rcv_converted_currencies))
        Espresso.onView(ViewMatchers.withId(R.id.progress_bar))
    }

    /*
    * Extra delay added for UI render
    * */
    @Test
    fun checkRecyclerviewScroll() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.rcv_converted_currencies))
        Espresso.onView(ViewMatchers.withId(R.id.et_amount)).perform(typeText("10"))
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(5000))
        Espresso.onView(ViewMatchers.withId(R.id.actv_currency_name))
            .perform(typeText("INR")) //Set text to auto complete textview
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(1000))
        onData(anything())
            .atPosition(0)
            .inRoot(RootMatchers.isPlatformPopup())
            .perform(click())
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(3000))

        val imageRecyclerView =
            getActivity()?.findViewById<RecyclerView>(R.id.rcv_converted_currencies)
        val itemCount = imageRecyclerView?.adapter?.itemCount
        viewInteraction.inRoot(
            RootMatchers.withDecorView(
                Matchers.`is`(
                    getActivity()?.window?.decorView
                )
            )
        )
            .perform(
                RecyclerViewActions.scrollToPosition<RecyclerViewBindingAdapter.ViewHolder>(
                    itemCount!! - 1
                )
            )
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(10000))
    }


    @Test
    fun checkRecyclerviewItemVisibility() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.rcv_converted_currencies))
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(10000))
        viewInteraction.inRoot(
            RootMatchers.withDecorView(
                Matchers.`is`(
                    getActivity()?.window?.decorView
                )
            )
        )
        viewInteraction.check(
            ViewAssertions.matches(
                withViewAtPosition(
                    1, Matchers.allOf(
                        ViewMatchers.withId(R.id.cl_main), ViewMatchers.isDisplayed()
                    )
                )
            )
        )
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(10000))
    }

    @After
    fun tearDown() {

    }


    private fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()
            override fun getDescription(): String = "wait for $delay milliseconds"
            override fun perform(uiController: UiController, v: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }

    private fun getActivity(): Activity? {
        var activity: Activity? = null
        rule.scenario.onActivity {
            activity = it
        }
        return activity
    }

    private fun withViewAtPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

}