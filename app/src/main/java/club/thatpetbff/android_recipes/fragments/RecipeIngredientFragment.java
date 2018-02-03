package club.thatpetbff.android_recipes.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import club.thatpetbff.android_recipes.DetailActivity;
import club.thatpetbff.android_recipes.Ingredient;
import club.thatpetbff.android_recipes.R;
import club.thatpetbff.android_recipes.RecipeIngredientAdapter;



/**
 * Created by randalltom on 1/22/18.
 */

public class RecipeIngredientFragment extends android.support.v4.app.Fragment  {


    private RecyclerView mRecyclerView;
    Context mContext;
    private RecipeIngredientAdapter mAdapter;
    Gson gson = new Gson();
    List<Ingredient> ingredients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        return inflater.inflate(R.layout.fragment_recipe_ingredient, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ingredientRecyclerView);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            View flContainerView2 = getActivity().findViewById(R.id.flContainer2);
            flContainerView2.setVisibility(View.GONE);
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        }
        mAdapter = new RecipeIngredientAdapter(mContext, new RecipeIngredientAdapter.OnItemClickListener() {

        });
        mRecyclerView.setAdapter(mAdapter);
        if(savedInstanceState != null && savedInstanceState.getString("ingredients") != null) {
            ingredients = gson.fromJson(savedInstanceState.getString("ingredients"), new TypeToken<List<Ingredient>>(){}.getType());
            mAdapter.setIngredientList(ingredients);
        } else {
            ingredients = ((DetailActivity)getActivity()).getIngredientList();
            if(ingredients != null) {
                mAdapter.setIngredientList(ingredients);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        /*
        RecipeStepFragment recipeStepFragment =  (RecipeStepFragment)getFragmentManager().findFragmentByTag("stepFragment");
        if(!recipeStepFragment.isVisible()) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } */

    }

    @Override
    public void onPause() {
        super.onPause();
        /*
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        */
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        if(ingredients != null && ingredients.size() >0 ) {
            outState.putString("ingredients", gson.toJson(ingredients));
        }
    }


}