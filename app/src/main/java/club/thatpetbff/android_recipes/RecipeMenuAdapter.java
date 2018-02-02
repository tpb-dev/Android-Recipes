package club.thatpetbff.android_recipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randalltom on 1/17/18.
 */

public class RecipeMenuAdapter extends RecyclerView.Adapter<RecipeMenuAdapter.ViewHolder>  {



    public interface OnItemClickListener {
        void onItemClick(Recipe recipe);
        void onHeart(String name, List<Ingredient> ingredients);
    }

    private final OnItemClickListener listener;

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private List<Recipe> recipeList;
    private LayoutInflater mInflater;
    private Context mContext;
    //private ImageView imageView;

    public RecipeMenuAdapter(Context context, OnItemClickListener listener)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.recipeList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_recipe, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeMenuAdapter.ViewHolder holder, int position)
    {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe, listener);
        if(recipe.getImage() != null && !recipe.getImage().equalsIgnoreCase("")) {
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .into(holder.imageView);

        }

        // This is how we use Picasso to load images from the internet.
        //Picasso.with(mContext)
        //        .load("http://image.tmdb.org/t/p/w185/" + recipe.getPoster())
        //        .into(holder.imageView);
        System.out.println("Tried Picasso = " + position + ", " + recipe.getName());
    }

    @Override
    public int getItemCount()
    {
        return (recipeList == null) ? 0 : recipeList.size();
    }

    public void setRecipeList(List<Recipe> recipeList)
    {
        this.recipeList.clear();
        this.recipeList.addAll(recipeList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtView;
        private ImageView imageView;
        private ImageButton imageButtonView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtView = (TextView) itemView.findViewById(R.id.recipeMenuView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageButtonView = (ImageButton) itemView.findViewById(R.id.favorite_button);
        }


        public void bind(final Recipe item, final OnItemClickListener listener) {
            txtView.setText(item.getName());
            if(item.image != null && !item.image.equalsIgnoreCase("")) {
                Picasso.with(itemView.getContext()).load(item.image).into(imageView);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(v.getId() == R.id.favorite_button) {
                        listener.onHeart(item.getName(), item.getIngredients());
                    } else {
                        listener.onItemClick(item);
                    }
                }
            });

            imageButtonView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(v.getId() == R.id.favorite_button) {
                        listener.onHeart(item.getName(), item.getIngredients());
                    } else {
                        listener.onItemClick(item);
                    }
                }
            });

        }

    }
}
