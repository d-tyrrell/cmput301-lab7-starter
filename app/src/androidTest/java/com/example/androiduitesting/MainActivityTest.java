package com.example.androiduitesting;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.widget.ListView;

import java.util.concurrent.atomic.AtomicReference;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);

  @Test public void testAddCity(){
        // Click on Add City button
        onView(withId(R.id.button_add)).perform(click());
        // Type "Edmonton" in the editText
       onView(withId(R.id.editText_name)).perform(typeText("Edmonton"));
       // Click on Confirm
       onView(withId(R.id.button_confirm)).perform(click());
       // Check if text "Edmonton" is matched with any of the text displayed on the screen
       onView(withText("Edmonton")).check(matches(isDisplayed()));
  }

    @Test public void testClearCity(){
      // Add first city to the list
         onView(withId(R.id.button_add)).perform(click());
         onView(withId(R.id.editText_name)).perform(click());

        onView(withId(R.id.editText_name)).perform(typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());
        //Add another city to the list
         onView(withId(R.id.button_add)).perform(click());

        onView(withId(R.id.editText_name)).perform(typeText("Vancouver"));
        onView(withId(R.id.button_confirm)).perform(click());

        //Clear the list
         onView(withId(R.id.button_clear)).perform(click());
         onView(withText("Edmonton")).check(doesNotExist());
         onView(withText("Vancouver")).check(doesNotExist());

    }

    @Test
    public void testListView(){
        // add a city
         onView(withId(R.id.button_add)).perform(click());

        onView(withId(R.id.editText_name)).perform(typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // Check if in the Adapter view (given id of that adapter view), there is a data
        // (which is an instance of String) located at position zero.
        // If this data matches the text we provided then Voila! Our test should pass
        // You can also use anything() in place of is(instanceOf(String.class))
         onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list)).atPosition(0).check(matches((withText("Edmonton"))));

    }

    //  ************** Activity Switch testing:  **************
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsRule intentsRule = new IntentsRule();   // <-- auto init/release

    // above code also works for back button testing

    @Test
    public void testActivitySwitch() {
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(typeText("Edmonton"));
        onView(withId(R.id.button_confirm)).perform(click());

        // finds the first item in the list and clicks it
        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list )).atPosition(0).perform(click());

        intended(hasComponent(ShowActivity.class.getName())); // verify launch
    }

    @Test
    public void testCityNameConsistency(){
        // add the montreal city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(typeText("Montreal"));
        onView(withId(R.id.button_confirm)).perform(click());

        final int position = 0; // montreal should be position 0 in the list now
        final AtomicReference<String> clickedCity = new AtomicReference<>(); // saving the clickedCity name

        // Read the city name from the ListView's adapter BEFORE we click
        activityRule.getScenario().onActivity(activity -> {
            ListView list = activity.findViewById(R.id.city_list);
            Object item = list.getAdapter().getItem(position);
            clickedCity.set(String.valueOf(item));
        });

        // finds the first item in the list and clicks it
        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list )).atPosition(0).perform(click());

        // Assert the displayed title equals the city we clicked
        onView(withId(R.id.showCityText))
                .check(matches(withText(clickedCity.get())));
    }

    //  ************** Back Button testing:  **************
    @Test
    public void testBackButton() {
        // add the montreal city to the list
        onView(withId(R.id.button_add)).perform(click());
        onView(withId(R.id.editText_name)).perform(typeText("Montreal"));
        onView(withId(R.id.button_confirm)).perform(click());

        // finds the Montreal item in the list and clicks it
        onData(is(instanceOf(String.class))).inAdapterView(withId(R.id.city_list )).atPosition(0).perform(click());

        // Verify ShowActivity is displayed
        intended(hasComponent(ShowActivity.class.getName()));

        // Click the back button
        onView(withId(R.id.backButton)).perform(click());

        // Verify the current activity is MainActivity again
        intended(hasComponent(MainActivity.class.getName()));

    }

}
