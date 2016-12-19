package com.homepass.melbike;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingPolicies;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.homepass.melbike.activity.MapsActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Created by Tony Liu
 */
@RunWith(AndroidJUnit4.class)
public class MelBikeInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.homepass.melbike", appContext.getPackageName());
    }

    private String bikeSiteName;

    @Rule
    public ActivityTestRule<MapsActivity> mActivityRule = new ActivityTestRule<MapsActivity>(MapsActivity.class);

    @Before
    public void initializeInput(){
        bikeSiteName = "Beach st";
    }

    @Test
    // user behavior driven test
    public void userTypeSuggestionBox(){
        //1. user type input
        onView(withId(R.id.autoCompleteTextView)).perform(typeText(bikeSiteName), closeSoftKeyboard());
        IdlingPolicies.setMasterPolicyTimeout(1000 * 2, TimeUnit.MILLISECONDS);

        //2. user choose the suggestion box [should display Beach St - Port Melbourne]
        onData(instanceOf("Beach St - Port Melbourne".getClass())).inRoot(RootMatchers.withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).perform(ViewActions.click());
        IdlingPolicies.setMasterPolicyTimeout(1000 * 2, TimeUnit.MILLISECONDS);

        //3. user click on navi button [button should be clickable]
        onView(withId(R.id.autoCompleteTextView)).check(matches(withText("Beach St - Port Melbourne")));
        onView(withId(R.id.naviBtn)).perform(ViewActions.click());
        IdlingPolicies.setMasterPolicyTimeout(  1000 * 2, TimeUnit.MILLISECONDS);

    }
}
