package com.appdev.epitech.epicture


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
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
class total {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.WRITE_EXTERNAL_STORAGE")

    @Test
    fun total() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(5000)

        val scroller = onView(withId(R.id.grid_view_images))

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
        scroller.perform(ViewActions.swipeDown())
        Thread.sleep(2000)
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val bottomNavigationFixedItemView2 = onView(
                allOf(withId(R.id.action_upload_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                2),
                        isDisplayed()))
        bottomNavigationFixedItemView2.perform(click())

        Thread.sleep(2000)

        val constraintLayout3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.grid_view_images),
                                childAtPosition(
                                        withClassName(`is`("android.widget.LinearLayout")),
                                        0)),
                        0),
                        isDisplayed()))
        constraintLayout3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

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
        Thread.sleep(2000)

        val bottomNavigationFixedItemView3 = onView(
                allOf(withId(R.id.action_home_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                0),
                        isDisplayed()))
        bottomNavigationFixedItemView3.perform(click())

        Thread.sleep(2000)

        val materialSearchBar = onView(
                allOf(withId(R.id.grid_search_bar),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0),
                                0),
                        isDisplayed()))
        materialSearchBar.perform(click())

        Thread.sleep(1000)


        val appCompatEditText = onView(
                allOf(withId(R.id.mt_editText),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("cat"), closeSoftKeyboard())

        Thread.sleep(1000)

        val appCompatEditText2 = onView(
                allOf(withId(R.id.mt_editText), withText("cat"),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                1),
                        isDisplayed()))
        appCompatEditText2.perform(pressImeActionButton())

        scroller.perform(ViewActions.swipeUp())

        Thread.sleep(1500)

        scroller.perform(ViewActions.swipeUp())

        Thread.sleep(1500)

        scroller.perform(ViewActions.swipeUp())

        Thread.sleep(1500)

        scroller.perform(ViewActions.swipeUp())

        Thread.sleep(1500)

        scroller.perform(ViewActions.swipeUp())

        Thread.sleep(1500)

        scroller.perform(ViewActions.swipeDown())

        Thread.sleep(1500)

        val bottomNavigationFixedItemView4 = onView(
                allOf(withId(R.id.action_favorite_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                1),
                        isDisplayed()))
        bottomNavigationFixedItemView4.perform(click())

        Thread.sleep(2000)

        val bottomNavigationFixedItemView5 = onView(
                allOf(withId(R.id.action_home_grid),
                        childAtPosition(
                                allOf(withId(R.id.bbn_layoutManager),
                                        childAtPosition(
                                                withId(R.id.grid_bottom_navigation),
                                                2)),
                                0),
                        isDisplayed()))
        bottomNavigationFixedItemView5.perform(click())

        Thread.sleep(2000)

        val appCompatImageView = onView(
                allOf(withId(R.id.mt_nav),
                        childAtPosition(
                                allOf(withId(R.id.root),
                                        childAtPosition(
                                                withId(R.id.mt_container),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView.perform(click())

        Thread.sleep(2000)

        val appCompatImageView2 = onView(
                allOf(withId(R.id.mt_search),
                        childAtPosition(
                                allOf(withId(R.id.root),
                                        childAtPosition(
                                                withId(R.id.mt_container),
                                                0)),
                                5),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val appCompatEditText3 = onView(
                allOf(withId(R.id.editTag),
                        childAtPosition(
                                allOf(withId(R.id.tagInputLayout),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()))
        appCompatEditText3.perform(replaceText("dog"), closeSoftKeyboard())

        Thread.sleep(2000)

        val appCompatImageButton5 = onView(
                allOf(withId(R.id.addTagButton),
                        childAtPosition(
                                allOf(withId(R.id.tagInputLayout),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                1),
                        isDisplayed()))
        appCompatImageButton5.perform(click())

        Thread.sleep(2000)

        val materialButton = onView(
                allOf(withId(R.id.backButton),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()))
        materialButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val materialSearchBar2 = onView(
                allOf(withId(R.id.grid_search_bar),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                                        0),
                                0),
                        isDisplayed()))
        materialSearchBar2.perform(click())

        Thread.sleep(2000)

        val linearLayout = onView(
                allOf(withId(R.id.requestItem),
                        childAtPosition(
                                allOf(withId(R.id.mt_recycler),
                                        childAtPosition(
                                                withId(R.id.last),
                                                1)),
                                0),
                        isDisplayed()))
        linearLayout.perform(click())

        Thread.sleep(2000)


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

        Thread.sleep(2000)


        val appCompatImageView3 = onView(
                allOf(withId(R.id.mt_clear),
                        childAtPosition(
                                allOf(withId(R.id.inputContainer),
                                        childAtPosition(
                                                withId(R.id.root),
                                                3)),
                                2),
                        isDisplayed()))
        appCompatImageView3.perform(click())

        Thread.sleep(2000)

        val appCompatImageView4 = onView(
                allOf(withId(R.id.mt_nav),
                        childAtPosition(
                                allOf(withId(R.id.root),
                                        childAtPosition(
                                                withId(R.id.mt_container),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageView4.perform(click())

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
