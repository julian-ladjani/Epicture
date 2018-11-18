package com.appdev.epitech.epicture


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class search {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun search() {
        Thread.sleep(5000)

        val materialSearchBar = onView(
                allOf(withId(R.id.grid_search_bar),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0),
                                0),
                        isDisplayed()))
        materialSearchBar.perform(click())

        val appCompatEditText = onView(
                allOf(withId(R.id.mt_editText),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("c"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(1000)

        val appCompatEditText2 = onView(
                allOf(withId(R.id.mt_editText), withText("c"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(replaceText("cat"))

        val appCompatEditText3 = onView(
                allOf(withId(R.id.mt_editText), withText("cat"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText3.perform(closeSoftKeyboard())

        val appCompatEditText4 = onView(
                allOf(withId(R.id.mt_editText), withText("cat"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText4.perform(pressImeActionButton())

        val appCompatEditText5 = onView(
                allOf(withId(R.id.mt_editText), withText("cat"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText5.perform(replaceText("dog"))

        val appCompatEditText6 = onView(
                allOf(withId(R.id.mt_editText), withText("dog"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText6.perform(closeSoftKeyboard())

        val appCompatEditText7 = onView(
                allOf(withId(R.id.mt_editText), withText("dog"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText7.perform(pressImeActionButton())

        val appCompatImageView = onView(
                allOf(withId(R.id.mt_clear),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                2),
                        isDisplayed()))
        appCompatImageView.perform(click())

        val appCompatEditText8 = onView(
                allOf(withId(R.id.mt_editText),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText8.perform(click())

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

        val bottomNavigationFixedItemView2 = onView(
                allOf(withId(R.id.action_home_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                0),
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
