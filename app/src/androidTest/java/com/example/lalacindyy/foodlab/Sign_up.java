package com.example.lalacindyy.foodlab;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
//****************


//make sure logged out before start
// ******************
public class Sign_up {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sign_up() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.imageButton4),
                        childAtPosition(
                                allOf(withId(R.id.content_frame),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        3),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(358);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.editText),
                        childAtPosition(
                                allOf(withId(R.id.content_frame),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("my"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.editText3),
                        childAtPosition(
                                allOf(withId(R.id.content_frame),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                6),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("my@1.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editText2),
                        childAtPosition(
                                allOf(withId(R.id.content_frame),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("23"), closeSoftKeyboard());

        try {
            Thread.sleep(3508);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView((withId(R.id.button)))
                .perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.imageButton4),
                        childAtPosition(
                                allOf(withId(R.id.content_frame),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton2.perform(click());


        ViewInteraction textView = onView(
                allOf(withId(R.id.textView2), withText("my"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_header_container),
                                        0),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("my")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView3), withText("my@1.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_header_container),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("my@1.com")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
