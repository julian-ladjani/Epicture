package com.appdev.epitech.epicture


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class favorite {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun favorite() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)
        val constraintLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.grid_view_images),
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        0)),
                        1),
                        isDisplayed()))
        constraintLayout.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val appCompatImageButton = onView(
                allOf(withId(R.id.favoriteButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatImageButton.perform(click())
        Thread.sleep(2000)
        pressBack()

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        val scroller = onView(withId(R.id.grid_view_images))
        scroller.perform(ViewActions.swipeUp())
        Thread.sleep(500)
        scroller.perform(ViewActions.swipeUp())
        Thread.sleep(500)
        scroller.perform(ViewActions.swipeUp())
        Thread.sleep(500)
        scroller.perform(ViewActions.swipeUp())
        Thread.sleep(500)
        scroller.perform(ViewActions.swipeDown())
        Thread.sleep(2000)

        val bottomNavigationFixedItemView = onView(
                allOf(withId(R.id.action_favorite_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                1),
                        isDisplayed()))
        bottomNavigationFixedItemView.perform(click())
        scroller.perform(ViewActions.swipeDown())
        Thread.sleep(2000)
        val constraintLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.grid_view_images),
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        0)),
                        0),
                        isDisplayed()))
        constraintLayout2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val appCompatImageButton2 = onView(
                allOf(withId(R.id.favoriteButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()))
        appCompatImageButton2.perform(click())
        Thread.sleep(2000)
        pressBack()

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
