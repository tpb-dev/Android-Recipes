package club.thatpetbff.android_recipes;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randalltom on 1/17/18.
 */

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder>  {


    public interface OnItemClickListener {
    }

    private final OnItemClickListener listener;

    public List<Step> getStepList() {
        return stepList;
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

    private List<Step> stepList;

    public OnItemClickListener getListener() {
        return listener;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {

        this.ingredientList.clear();
        this.ingredientList.addAll(ingredientList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    private List<Ingredient> ingredientList;
    private LayoutInflater mInflater;
    private Context mContext;

    public RecipeIngredientAdapter(Context context, OnItemClickListener listener)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.stepList = new ArrayList<>();
        this.ingredientList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_step, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeIngredientAdapter.ViewHolder holder, int position)
    {
        Ingredient ingredient = ingredientList.get(position);
        holder.bind(ingredient, listener);


        // This is how we use Picasso to load images from the internet.
        //Picasso.with(mContext)
        //        .load("http://image.tmdb.org/t/p/w185/" + recipe.getPoster())
        //        .into(holder.imageView);
        System.out.println("Tried Picasso = " + position + ", ");
    }

    @Override
    public int getItemCount() {
        return (ingredientList == null) ? 0  : ingredientList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtView = (TextView) itemView.findViewById(R.id.recipeStepView);
        }


        public void bind(final Ingredient item, final OnItemClickListener listener) {
            txtView.setText("Ingredient: " + item.getIngredient() + "\nQuantity: " + item.getQuantity() + "\nMeasure: " + item.getMeasure());

        }

    }
}
