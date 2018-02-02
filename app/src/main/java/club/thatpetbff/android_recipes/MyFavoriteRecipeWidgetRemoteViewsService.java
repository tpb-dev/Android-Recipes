package club.thatpetbff.android_recipes;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;


/**
 * Created by rtom on 1/29/18.
 */

public class MyFavoriteRecipeWidgetRemoteViewsService extends RemoteViewsService{

    Gson gson = new Gson();
    String name = "";
    List<Ingredient> ingredients = Arrays.asList(new Ingredient(0L, 0L, 0.0, "", "test"));

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public void onCreate() {
                System.out.println("Created widget view factory");
                String tempName = intent.getStringExtra("name");
                String tempList = intent.getStringExtra("ingredients");

                if(tempName != null && !tempName.equals("")) {
                    name = tempName;
                }
                if(tempList != null && !tempList.equals("")) {
                    ingredients = gson.fromJson(tempList, new TypeToken<List<Ingredient>>(){}.getType());
                }
            }

            @Override
            public int getCount() {
                return ingredients.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.my_favorite_recipe_widget_list_row);
                if(ingredients.get(position).getIngredient().equals("test")) {
                    views.setTextViewText(R.id.widget_ingredient_information, "Please add recipe");
                } else {
                    views.setTextViewText(R.id.widget_ingredient_information, "Ingredient: " + ingredients.get(position).getIngredient() + "\nQty: " + ingredients.get(position).getQuantity() + "\nMeasure: " + ingredients.get(position).getMeasure());
                }
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.my_favorite_recipe_widget_list_row);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}