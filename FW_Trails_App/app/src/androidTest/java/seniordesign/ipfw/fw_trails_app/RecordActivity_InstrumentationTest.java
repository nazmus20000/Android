/**
 Copyright (C) 2016 Jared Perry, Jaron Somers, Warren Barnes, Scott Weidenkopf, and Grant Grimm
 Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 and associated documentation files (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies\n
 or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package seniordesign.ipfw.fw_trails_app;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;


/**
 * Created by Jaron on 1/23/2016.
 * Design Doc: https://drive.google.com/drive/u/1/folders/0B46qah_2e5yKbzdMbVNxUDNPeE0
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecordActivity_InstrumentationTest {

   private final String toolbarTitle = "Record Activity";


   // Preferred JUnit 4 mechanism of specifying the activity to be launched before each test
   @Rule
   public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

   // Open Nav Drawer before each test.
   @Before
   public void navDrawerOpen(){
      openDrawer();
      onView(withId(R.id.nav_drawerImage)).check(matches(isDisplayed()));
      onView(withText(R.string.navDrawer_recordActivity)).perform(click());
   }

   // Close Nav Drawer after each test
   @After
   public void navDrawerClose(){
      closeDrawer();
      onView(withId(R.id.nav_drawerImage))
              .check(matches(not(isDisplayed())));
   }

   // Ignore these tests until toolbar and nav drawer is made
   // Make sure the toolbar is displayed.
   @Test
   public void toolbarExists() {
      onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
   }

   // tests the toolbar's title which inherits a text view so we look for anything that
   // is the parent of the toolbar that is also a TextView and see if it matches with our desired
   // title. The title is set in the onCreate method.
   // source: http://www.captechconsulting.com/blogs/introduction-to-espresso
   @Test
   public void recordActivityToolbarTitleVerification() {
      onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
              .check(matches(withText(toolbarTitle)));
   }

   // Opens the Navigation Drawer
   private void openDrawer()
   {
      onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
   }

   // Closes the Navigation Drawer
   private void closeDrawer(){
      onView(withId(R.id.drawer_layout)).perform(DrawerActions.close());
   }
}
