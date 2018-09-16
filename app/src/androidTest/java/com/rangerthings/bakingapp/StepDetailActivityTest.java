package com.rangerthings.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {
    private static final String STEP_NAME = "Recipe Introduction";

    @Rule
    public ActivityTestRule<StepDetailActivity> mActivityRule =
            new ActivityTestRule<>(StepDetailActivity.class);

    @Test
    public void clickOnRecyclerViewItem_opensOrReplacesDetailFragment() {

        //check to see if stepDesc is displayed
        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tv_stepDesc))
                .check(matches(withText(STEP_NAME)));
    }

    @Test
    public void clickOnStepWithVideo_showsPlayerView() {

        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(
                        withId(R.id.pv_exoplayer))
                .check(matches(isDisplayed()));
    }
    @Test
    public void clickOnStepWithoutVideo_noPlayerView() {

        onView(withId(R.id.rv_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(
                        withId(R.id.pv_exoplayer))
                .check(matches(not(isDisplayed())));
    }


}
