package com.mytaxi.android_demo;

import android.Manifest;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.filters.LargeTest;
import com.mytaxi.android_demo.activities.MainActivity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;


import android.support.test.runner.AndroidJUnit4;


import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.not;


/**
 * Class to Test various Login Scenarios for the App
 * Author- Shilpa Sachdeva
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest {

    private String userName= "crazydog335";
    private String password= "venture";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule
            = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    /**
     * This test verifies empty Username scenario
     */

    @Test
    public void test1_emptyUsername() {
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_password)).perform(typeText(password));
        onView(withId(R.id.btn_login)).perform(click());
        onView(isRoot()).perform(SearchDriver.waitFor(1500));

        //Verify error message for blank username is displayed
        onView(withText(R.string.username_empty))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    /**
     * This test verifies empty Password scenario
     */
    @Test
    public void test2_emptyPassword() {
        onView(withId(R.id.edt_username)).perform(typeText(userName));
        onView(withId(R.id.edt_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        onView(isRoot()).perform(SearchDriver.waitFor(1500));

        //Verify error message for blank Password is displayed
        onView(withText(R.string.password_empty))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    /**
     * This test verifies empty Username and Password scenario
     */

    @Test
    public void test3_emptyUsernamePassword() {
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_password)).perform(clearText());
        onView(withId(R.id.btn_login)).perform(click());
        onView(isRoot()).perform(SearchDriver.waitFor(1500));

        //Verify error message for blank username and password is displayed
        onView(withText(R.string.username_password_empty))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }
    /**
     * This test verifies Invalid Username and Password scenario
     */

    @Test
    public void test4_invalidLogin() {
        onView(withId(R.id.edt_username)).perform(typeText("wrongUsername"));
        onView(withId(R.id.edt_password)).perform(typeText("wrongPassword"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(isRoot()).perform(SearchDriver.waitFor(1500));

        //Verify error message for Invalid username and password is displayed
        onView(withText(R.string.message_login_fail))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    /**
     * This test verifies SQL Injection scenario
     */

    @Test
    public void test5_SQLInjection() {
        onView(withId(R.id.edt_username)).perform(typeText("1 OR 1=1"));
        onView(withId(R.id.edt_password)).perform(typeText("wrongPassword"));
        onView(withId(R.id.btn_login)).perform(click());
        onView(isRoot()).perform(SearchDriver.waitFor(1500));

        //Verify error message for Invalid username and password is displayed
        onView(withText(R.string.message_login_fail))
                .inRoot(withDecorView(not(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void test6_successfulLogin() {
        try {
            onView(withId(R.id.edt_username)).perform(typeText(userName));
            onView(withId(R.id.edt_password)).perform(typeText(password));
            onView(withId(R.id.btn_login)).perform(click());
            onView(isRoot()).perform(SearchDriver.waitFor(5000));

            //Verify Driver search screen is displayed
            onView(withId(R.id.textSearch)).check(matches(withHint(containsString("Search driver here"))));


            //Verify Username is displayed
            onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
            onView(withText(equalToIgnoringCase(userName))).check(matches(isDisplayed()));

            // Perform Logout
            onView(withText("Logout")).perform(click());
            onView(withId(R.id.edt_username)).check(matches(isDisplayed()));

        }
        catch (Exception e) {
            fail("Successful Login failed" + e.getMessage());
        }
    }

}
