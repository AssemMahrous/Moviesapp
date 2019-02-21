package com.example.moviesapp.ui.main


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.moviesapp.R
import com.example.moviesapp.ui.details.DetailsActivity
import com.example.moviesapp.utils.EspressoTestingIdlingResource
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class RecyclerMainTest {

    @get:Rule
    var activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.idlingResource)
    }


    @Test
    fun mainActivityTest() {
        onView(withId(R.id.popular_people_list)).perform(click())
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()))
    }


    @Test
    fun backTest() {
        onView(withId(R.id.popular_people_list)).perform(click())

        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

        onView(withId(R.id.popular_people_list)).check(matches(isDisplayed()))
    }


    @Test
    fun searchClickTest() {
        onView(withId(R.id.search_bar)).perform(click())
        onView(withText(R.string.toast_search)).inRoot(withDecorView(not(`is`(activityTestRule.getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun galleryClickTest() {
        onView(withId(R.id.popular_people_list)).perform(click())
        onView(withId(R.id.gallery_recycler)).perform(click())
        onView(withText(R.string.toast_gallery)).inRoot(withDecorView(not(`is`(ActivityTestRule<DetailsActivity>(DetailsActivity::class.java).getActivity().getWindow().getDecorView()))))
            .check(matches(isDisplayed()))
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.idlingResource)
    }



}
