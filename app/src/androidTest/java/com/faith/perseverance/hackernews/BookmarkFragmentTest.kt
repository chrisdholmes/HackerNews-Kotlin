package com.faith.perseverance.hackernews

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.faith.perseverance.hackernews.view.BookMarksFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)

class BookmarkFragmentTest {

    private lateinit var scenario: FragmentScenario<BookMarksFragment>

    @Before
    fun setUp()
    {
        scenario = launchFragmentInContainer()
        scenario.moveToState(Lifecycle.State.STARTED)

    }

    @Test
    fun testOpeningBookmarkFragment()
    {

    }
}