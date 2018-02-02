package club.thatpetbff.android_recipes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.List;

import club.thatpetbff.android_recipes.fragments.RecipeDetailFragment;
import club.thatpetbff.android_recipes.fragments.RecipeStepFragment;

public class DetailActivity extends AppCompatActivity {

    Recipe recipe;
    private RecipeStepFragment recipeStepFragment;
    private RecipeDetailFragment recipeDetailFragment;
    List<Ingredient> ingredientList;
    private String url;

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(myToolbar);


        Intent intent = getIntent();
        Bundle bd = intent.getExtras();

        Gson gson = new Gson();

        if(bd != null) {
            String json = (String) bd.get("RecipeClass");
            recipe = gson.fromJson(json, Recipe.class);
        }

        Log.d("DEBUG", getResources().getConfiguration().orientation + "");

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        recipeStepFragment =  (RecipeStepFragment)fm.findFragmentByTag("stepFragment");

        recipeDetailFragment =  (RecipeDetailFragment)fm.findFragmentByTag("detailFragmentLandscape");

        if (savedInstanceState == null) {
            // Instance of first fragment
            System.out.println("Inside savedInstanceState");
            RecipeStepFragment firstFragment = new RecipeStepFragment();


            // Add Fragment to FrameLayout (flContainer), using FragmentManager
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft.add(R.id.flContainer, firstFragment, "stepFragment");                                // add    Fragment
            ft.addToBackStack("stepFragment");
            ft.commit();                                                            // commit FragmentTransaction


            /*
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                RecipeDetailFragment secondFragment = new RecipeDetailFragment();

                // Add Fragment to FrameLayout (flContainer), using FragmentManager
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
                ft2.add(R.id.flContainer2, secondFragment, "detailFragmentLandscape");                                // add    Fragment
                ft2.addToBackStack("detailFragmentLandscape");
                ft2.commit();
            }*/

        }
        //else if(recipeDetailFragment == null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
         //   RecipeDetailFragment secondFragment = new RecipeDetailFragment();
/*
            // Add Fragment to FrameLayout (flContainer), using FragmentManager
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft2.add(R.id.flContainer2, secondFragment, "detailFragmentLandscape");                                // add    Fragment
            ft2.addToBackStack("detailFragmentLandscape");
            ft2.commit();*/
        //}


    }
/*
    public void goFullScreen(String url) {
        System.out.println("Going full screen");
        setURL(url);
        setContentView(R.layout.full_screen_container);
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        recipeFullScreenVideoFragment = (RecipeFullScreenVideoFragment) fm.findFragmentByTag("recipeFullScreenVideoFragment");

        if(recipeFullScreenVideoFragment != null && !recipeFullScreenVideoFragment.isAdded()) {
            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft4.replace(R.id.flContainer, recipeFullScreenVideoFragment, "recipeFullScreenVideoFragment");                                // add    Fragment
            ft4.commit();
        } else if(recipeFullScreenVideoFragment != null && recipeFullScreenVideoFragment.isAdded()) {
            System.out.println("Good scenario");
        } else {
            recipeFullScreenVideoFragment = new RecipeFullScreenVideoFragment();
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft2.replace(R.id.flContainer, recipeFullScreenVideoFragment, "recipeFullScreenVideoFragment");                                // add    Fragment
            ft2.commit();
        }
        System.out.println("hhh");
    }
    */

    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           System.out.println("Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("Portrait");
        }
    }

    @Override
    public void onPause() {
        // perform other onPause related actions
        ///...
        // this means that this activity will not be recreated now, user is leaving it
        // or the activity is otherwise finishing
        super.onPause();
        if(isFinishing()) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            // we will not need this fragment anymore, this may also be a good place to signal
            // to the retained fragment object to perform its own cleanup.
            //fm.beginTransaction().remove(recipeStepFragment).commit();
            //fm.beginTransaction().remove(recipeDetailFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {

        recipeStepFragment =  (RecipeStepFragment)getSupportFragmentManager().findFragmentByTag("stepFragment");
        if(recipeStepFragment != null && recipeStepFragment.isVisible()) {
            System.out.println("Stapler!");

            int countDrac = getSupportFragmentManager().getBackStackEntryCount();
            System.out.println("countdrc = " + countDrac);
            if(countDrac == 0) {
                super.onBackPressed();
            }

        }
        super.onBackPressed();
        int countDrac = getSupportFragmentManager().getBackStackEntryCount();
        if(countDrac == 0) {
            super.onBackPressed();
        }

        System.out.println("Hacker!");

    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
