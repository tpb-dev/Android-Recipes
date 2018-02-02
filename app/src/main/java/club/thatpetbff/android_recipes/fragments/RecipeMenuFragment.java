package club.thatpetbff.android_recipes.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.util.List;

import club.thatpetbff.android_recipes.DetailActivity;
import club.thatpetbff.android_recipes.MyFavoriteRecipeWidgetProvider;
import club.thatpetbff.android_recipes.Ingredient;
import club.thatpetbff.android_recipes.MainActivity;
import club.thatpetbff.android_recipes.R;
import club.thatpetbff.android_recipes.Recipe;
import club.thatpetbff.android_recipes.RecipeMenuAdapter;

/**
 * Created by randalltom on 1/17/18.
 */

public class RecipeMenuFragment extends Fragment {

    private RecyclerView mRecyclerView;
    Context mContext;
    private RecipeMenuAdapter mAdapter;
    Gson gson = new Gson();

    boolean error1 = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_recipes, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        System.out.println("JP needs :" + error1);
        if(error1 == true) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity().getApplicationContext())
                    .title("dddddd")
                    .content("sdfdsfsdfsd")
                    .positiveText("R.string.agree");

            MaterialDialog dialog = builder.build();
            dialog.show();
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
        }
        mAdapter = new RecipeMenuAdapter(mContext, new RecipeMenuAdapter.OnItemClickListener() {
            @Override public void onItemClick(Recipe item) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("RecipeClass", gson.toJson(item));
                startActivity(intent);
                System.out.println("Recipe clicked: " + item.getName());

            }

            @Override public void onHeart(String name, List<Ingredient> ingredients) {
                System.out.println("Ingredient heart clicked  = " + name);

                Gson gson = new Gson();

                Intent intent = new Intent(getContext(), MyFavoriteRecipeWidgetProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra("name", name);
                intent.putExtra("ingredients", gson.toJson(ingredients));
                int[] ids = AppWidgetManager.getInstance(getContext()).getAppWidgetIds(new ComponentName(getContext(), MyFavoriteRecipeWidgetProvider.class));
                if(ids != null && ids.length > 0) {
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    getContext().sendBroadcast(intent);
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        List<Recipe> recs = ((MainActivity)getActivity()).getRecipes();
        if(recs.size() > 0) {
            setRecipeList(recs);
        }

    }

    public void setRecipeList(List<Recipe> recipes) {
        mAdapter.setRecipeList(recipes);
        // The adapter needs to know
    }

    public void showError() {
        //Snackbar.make(mRecyclerView, "Networking error, check your connection", 5);
/*
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity().getApplicationContext())
                .title("dddddd")
                .content("sdfdsfsdfsd")
                .positiveText("R.string.agree");

        MaterialDialog dialog = builder.build();
        dialog.show(); */
        error1 = true;
        System.out.println("LOLOLOL");
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this.getContext())
                .title("Network Connectivity Bad")
                .content("Check Connection")
                .positiveText("OK");

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

}
