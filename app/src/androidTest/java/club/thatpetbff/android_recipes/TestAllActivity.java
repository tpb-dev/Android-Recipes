package club.thatpetbff.android_recipes;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.core.internal.deps.guava.collect.Lists;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by rtom on 1/23/18.
 */

@RunWith(AndroidJUnit4.class)
public class TestAllActivity {

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<DetailActivity> mDetailActivityTestRule;

    @Test
    public void testSimpleHomeTestMethod() {
        onView(withId(R.id.mainContainer)).check(matches(isDisplayed()));
    }

    @Test
    public void testSeeButtons() {
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void testClickButtonToDetailActivity() {
        goToDetailActivity();
    }

    public void goToDetailActivity() {
        Recipe recipe = new Recipe();
        recipe = new Recipe(1111L, "aaa", 1, "srhserherh");
        List<Step> steps1 = Lists.newArrayList(new Step(44L, 44L, "aa3",
                "descNumberOne", "gaweg", "segerg"), new Step(45L, 45L, "aa11",
                "de33sc", "gaw2eg", "seger2g"), new Step(46L, 46L, "aa2",
                "descNumberOne1", "gaweg1", "segerg1"), new Step(47L, 47L, "aa1",
                "descNumberOne2", "gaweg2", "segerg2"));
        recipe.setSteps(steps1);
        onView(withText("Cheesecake")).perform(click());

        InstrumentationRegistry.getTargetContext();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        Gson gson = new Gson();
        intent.putExtra("RecipeClass",gson.toJson(recipe, Recipe.class));

        mDetailActivityTestRule = new ActivityTestRule<>(DetailActivity.class);

        mDetailActivityTestRule.launchActivity(intent);
    }

    @Test
    public void ClickOnStep() {
        goToDetailActivity();
        onView(withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //onView(withText("aa")).check(matches(isDisplayed()));
    }

    @Test
    public void ClickOnPreviousDetail() {
        ClickOnStep();
        onView(withId(R.id.backButton)).perform(click());
    }

    @Test
    public void BackToSteps() {
        ClickOnPreviousDetail();
        Espresso.pressBack();

        onView(withRecyclerView(R.id.recyclerView).atPosition(0))
                .check(matches(hasDescendant(withText("Ingredients"))));

    }
}