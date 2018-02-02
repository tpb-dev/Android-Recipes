package club.thatpetbff.android_recipes;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rtom on 1/29/18.
 */

public class MyFavoriteRecipeWidgetProvider extends AppWidgetProvider {
    Gson gson = new Gson();
    String name = "Add favorite recipe";
    List<Ingredient> ingredients = Arrays.asList(new Ingredient(0L, 0L, 0.0, "", "test"));

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_favorite_recipe_widget);

        views.setTextViewText(R.id.widget_header_text, name);

        Intent intent = new Intent(context, MyFavoriteRecipeWidgetRemoteViewsService.class);
        intent.putExtra("name", name);
        intent.putExtra("ingredients", gson.toJson(ingredients));
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list, intent);
        views.setEmptyView(R.id.widget_list, R.id.widget_empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, MyFavoriteRecipeWidgetRemoteViewsService.class));

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String tempName = intent.getStringExtra("name");
        String tempList = intent.getStringExtra("ingredients");

        if(tempName != null && !tempName.equals("")) {
            name = tempName;
        }
        if(tempList != null && !tempList.equals("")) {
            ingredients = gson.fromJson(tempList, new TypeToken<List<Ingredient>>(){}.getType());
        }

        super.onReceive(context, intent);
    }


}

