package club.thatpetbff.android_recipes.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import club.thatpetbff.android_recipes.App;
import club.thatpetbff.android_recipes.DetailActivity;
import club.thatpetbff.android_recipes.Ingredient;
import club.thatpetbff.android_recipes.R;
import club.thatpetbff.android_recipes.Recipe;
import club.thatpetbff.android_recipes.RecipeStepAdapter;
import club.thatpetbff.android_recipes.Step;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepFragment extends android.support.v4.app.Fragment {

    Step step;
    private RecyclerView mRecyclerView;
    Context mContext;
    private RecipeStepAdapter mAdapter;
    Gson gson = new Gson();
    RecipeDetailWithStepsFragment recipeDetailWithStepsFragment;
    RecipeDetailFragment secondFragment;

    List<Ingredient> ing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment

        // Get a support ActionBar corresponding to this toolbar

        return inflater.inflate(R.layout.fragment_recipe_steps, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));


        }
        mAdapter = new RecipeStepAdapter(mContext, new RecipeStepAdapter.OnItemClickListener() {
            @Override public void onItemClick(List<Ingredient> item, int pos) {
                //Insert shit here
                //onRecipeStepSelected(item);
                vv1(pos);
                ing = item;

                System.out.println("eeeeeeee");
            }
            @Override public void onItemClick(Step item, int pos) {
                //Insert shit here
                onRecipeStepSelected(item, pos);

                System.out.println("whoa" + item.getDescription());
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        Recipe recipe = ((DetailActivity)getActivity()).getRecipe();
        App app = (App)((DetailActivity)getActivity()).getApplication();
        recipe.__setDaoSession(app.getDaoSession());
        List<Step> steps = recipe.getSteps();
        List<Ingredient> ingredients = recipe.getIngredients();
        if(steps.size() > 0 || ingredients.size() > 0) {
            setRecipeStepList(steps, ingredients);
            step = steps.get(1);
        }
        if(ingredients != null) {
            ((DetailActivity) getActivity()).setIngredientList(ingredients);
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            recipeDetailWithStepsFragment = (RecipeDetailWithStepsFragment) fm.findFragmentByTag("recipeDetailWithStepsFragment");
            if(recipeDetailWithStepsFragment == null) {
               recipeDetailWithStepsFragment = new RecipeDetailWithStepsFragment();
            }
            Bundle args = new Bundle();
            args.putString("step", gson.toJson(step));
            recipeDetailWithStepsFragment.setArguments(args);
            fm.beginTransaction()
                    .replace(R.id.flContainer2, recipeDetailWithStepsFragment,"recipeDetailWithStepsFragment")
                    .commit();
        }
    }

    public void setRecipeStepList(List<Step> steps, List<Ingredient> ingredients) {
        mAdapter.setStepList(steps, ingredients);
        // The adapter needs to know
    }

    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity
        void onRecipeStepSelected(int position);
    }

    public void onRecipeStepSelected(Step step, int pos) {
        boolean isShowing = false;
        //Toast.makeText(this, "Called By Fragment A: position - ", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        // Load Pizza Detail Fragment
        FragmentManager fm = getActivity().getSupportFragmentManager();
        recipeDetailWithStepsFragment = (RecipeDetailWithStepsFragment) fm.findFragmentByTag("recipeDetailWithStepsFragment");

        if(recipeDetailWithStepsFragment != null && recipeDetailWithStepsFragment.isVisible()) {
            isShowing = true;
        }

        secondFragment = (RecipeDetailFragment) fm.findFragmentByTag("recipeDetailFragment");

        if(secondFragment == null) {
            secondFragment = new RecipeDetailFragment();
        }

        Bundle args = new Bundle();
        args.putString("step", gson.toJson(step));
        args.putInt("pos", pos );
        if(secondFragment != null) {
            secondFragment.setArguments(args);          // (1) Communicate with Fragment using Bundle
        }
        if(recipeDetailWithStepsFragment != null) {
            recipeDetailWithStepsFragment.setArguments(args);
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(isShowing) {
                recipeDetailWithStepsFragment.updateFragment(step);
            } else {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer2, recipeDetailWithStepsFragment) // replace flContainer
                        .addToBackStack("recipeDetailWithStepsFragment")
                        .commit();
                System.out.println("Fragment here");
            }
        }else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, secondFragment) // replace flContainer
                    .addToBackStack("recipeDetailFragment")
                    .commit();
        }
    }

    public void vv1(int pos) {
        //Toast.makeText(this, "Called By Fragment A: position - ", Toast.LENGTH_SHORT).show();
        Gson gson = new Gson();
        // Load Pizza Detail Fragment
        RecipeIngredientFragment secondFragment = new RecipeIngredientFragment();

        Bundle args = new Bundle();
        args.putString("ingredients", gson.toJson(ing));
        secondFragment.setArguments(args);          // (1) Communicate with Fragment using Bundle


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer2, secondFragment) // replace flContainer
                    .addToBackStack("ingredientsFragmentLandscape")
                    .commit();
            System.out.println("Fragment here");
        }else{
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, secondFragment) // replace flContainer
                    .addToBackStack("ingredientsFragmentPortrait")
                    .commit();
        }
    }

}