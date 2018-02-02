package club.thatpetbff.android_recipes;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import club.thatpetbff.android_recipes.fragments.RecipeMenuFragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//import club.thatpetbff.android_recipes.fragments.PizzaDetailFragment;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecipeDao recipeDao;
    private IngredientDao ingredientDao;
    private StepDao stepDao;
    private Query<Recipe> recipeQuery;
    private Query<Ingredient> ingredientQuery;
    private Query<Step> stepQuery;
    public List<Recipe> recipes;
    AlertDialog alertDialog;

    RecipeMenuFragment firstFragment;
    //private RecipeAdapter recipeAdapter;
    int fragId = -1;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);

        if (savedInstanceState == null) {
            System.out.println("Print 1");
            //Instance of first fragment
            RecipeMenuFragment firstFragment = new RecipeMenuFragment();
             Bundle args = new Bundle();
            args.putInt("position", 0);
            firstFragment.setArguments(args);          // (1) Communicate with Fragment using Bundle
            // Add Fragment to FrameLayout (flContainer), using FragmentManager
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            //ft.add(R.id.mainContainer, firstFragment);                                // add    Fragment
            ft.add(R.id.mainContainer,firstFragment,"FIRST_FRAGMENT");
            //ft.addToBackStack("FIRST_FRAGMENT");
            ft.commit();

        }

        System.out.println("Print 2");


        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        recipeDao = daoSession.getRecipeDao();
        ingredientDao= daoSession.getIngredientDao();
        stepDao = daoSession.getStepDao();

        recipeQuery = recipeDao.queryBuilder().orderAsc(RecipeDao.Properties.Id).build();

        ingredientQuery = ingredientDao.queryBuilder().orderAsc(IngredientDao.Properties.Ingredient).build();

        stepQuery = stepDao.queryBuilder().orderAsc(StepDao.Properties.Id).build();

        HashMap<Long, Recipe> hmap = new HashMap();

        recipes = recipeQuery.list();

        for(Recipe rec : recipes) {
            hmap.put(rec.getId(), rec);
        }

        for(Ingredient i : ingredientQuery.list()) {
            Recipe tmp = hmap.getOrDefault(i.getRecipeID(), null);
            if(tmp == null) {
                tmp.getIngredients().add(i);
            }
        }

        for(Step s : stepQuery.list()) {
            Recipe tmp = hmap.getOrDefault(s.getRecipeID(), null);
            if(tmp == null) {
                tmp.getSteps().add(s);
            }
        }

        int a = recipes.size();

        System.out.println("Recipes length: " + a);

        if(a == 0) {
            OkHttpHandler ok = new OkHttpHandler();
            ok.execute(getString(R.string.recipeURL));
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //my_button.setBackgroundResource(R.drawable.defaultcard);
                    RecipeMenuFragment fragment = (RecipeMenuFragment) getSupportFragmentManager().findFragmentByTag("FIRST_FRAGMENT");
                    if(fragment != null)
                        fragment.setRecipeList(recipes);
                }
            }, 3000);

        }

        System.out.println("Placeholder!");

        Log.d("DEBUG", getResources().getConfiguration().orientation + "");

    }

    private class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        @Override
        protected String doInBackground(Object... params) {

            Request.Builder builder = new Request.Builder();
            builder.url((String)params[0]);
            Request request = builder.build();

            if(isOnline()) {

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {


                //RecipeMenuFragment fragmentMenu = (RecipeMenuFragment) getSupportFragmentManager().findFragmentByTag("FIRST_FRAGMENT");

                //  if(fragmentMenu != null) {
              //      fragmentMenu.showError();
               // }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object s) {
            String a = (String)s;
            if(a != null && a.length() > 0) {
                super.onPostExecute(a);
               processJSON(a);
            } else {
                //alertDialog.show();


                RecipeMenuFragment fragment = (RecipeMenuFragment) getSupportFragmentManager().findFragmentByTag("FIRST_FRAGMENT");
                fragment.showError();

            }
        }

    }

    public void processJSON(String json) {
        Gson gson = new Gson();
        Recipe[] objs = gson.fromJson(json, Recipe[].class);
        //System.out.println("What ID????? " + objs[0].getName());
        recipes = Arrays.asList(objs);

        for(Recipe obj : objs) {
            recipeDao.insert(obj);
            if(obj != null) {
                for(Ingredient i : obj.getIngredients()) {
                    i.setRecipeID(obj.id);
                    ingredientDao.insert(i);
                }
                for(Step s : obj.getSteps()) {
                    s.setRecipeID(obj.id);
                    stepDao.insert(s);
                }
            }
        }
        System.out.println("ala mode");
        RecipeMenuFragment fragment = (RecipeMenuFragment) getSupportFragmentManager().findFragmentByTag("FIRST_FRAGMENT");
        fragment.setRecipeList(recipes);
    }

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        firstFragment = (RecipeMenuFragment) getSupportFragmentManager().findFragmentByTag("detailFragmentLandscape");


        if(firstFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            //ft.add(R.id.mainContainer, firstFragment);                                // add    Fragment
            ft.add(R.id.mainContainer, firstFragment, "FIRST_FRAGMENT");
            ft.commit();
        }
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
