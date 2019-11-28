package com.enfin.ofabee3;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.enfin.ofabee3.ui.module.login.LoginActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangeTextBehaviorTest {

    private String stringToBetyped;

    @Rule
    public ActivityTestRule<LoginActivity> activityRule
            = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        stringToBetyped = "Espresso";
    }

    @Test
    public void changeText_sameActivity() {
        // Type text and then press the button.
        Espresso.onView(ViewMatchers.withId(R.id.emailEditText))
                .perform(ViewActions.typeText(stringToBetyped), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.continueButton)).perform(ViewActions.click());

        /*// Check that the text was changed.
        Espresso.onView(ViewMatchers.withId(R.id.textToBeChanged))
                .check(matches(withText(stringToBetyped)));*/
    }
}
