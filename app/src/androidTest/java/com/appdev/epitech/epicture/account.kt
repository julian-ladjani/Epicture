package com.appdev.epitech.epicture


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
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
class account {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun account() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val scroller = onView(withId(R.id.grid_view_images))
        scroller.perform(swipeDown())
        Thread.sleep(2000)
        val bottomNavigationFixedItemView = onView(
                allOf(withId(R.id.action_upload_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                2),
                        isDisplayed()))
        bottomNavigationFixedItemView.perform(click())

        val constraintLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.grid_view_images),
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        0)),
                        0),
                        isDisplayed()))
        constraintLayout.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val cardView2 = onView(
                allOf(withId(R.id.layout_button_container),
                        childAtPosition(
                                allOf(withId(R.id.deleteButton),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                8)),
                                0),
                        isDisplayed()))
        cardView2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

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
        Thread.sleep(700)

        val appCompatImageButton = onView(
                allOf(withId(R.id.shareButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val appCompatImageButton2 = onView(
                allOf(withId(R.id.downloadButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()))
        appCompatImageButton2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        pressBack()

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(700)

        val bottomNavigationFixedItemView2 = onView(
                allOf(withId(R.id.action_favorite_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                1),
                        isDisplayed()))
        bottomNavigationFixedItemView2.perform(click())
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
