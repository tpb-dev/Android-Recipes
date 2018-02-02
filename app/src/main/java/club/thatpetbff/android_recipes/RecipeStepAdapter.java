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

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder>  {


    public interface OnItemClickListener {
        void onItemClick(List<Ingredient> step, int pos);

        void onItemClick(Step step, int pos);
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

    public static int getTypeIngredient() {
        return TYPE_INGREDIENT;
    }

    public static int getTypeStep() {
        return TYPE_STEP;
    }

    private List<Ingredient> ingredientList;
    private LayoutInflater mInflater;
    private Context mContext;

    private static final int TYPE_INGREDIENT = 0;
    private static final int TYPE_STEP = 1;

    public RecipeStepAdapter(Context context, OnItemClickListener listener)
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
    public void onBindViewHolder(RecipeStepAdapter.ViewHolder holder, int position)
    {
        if (position == 0) {
            holder.bind(this.ingredientList, listener, position);
        } else {
            Step step = stepList.get(position - 1);
            holder.bind(step, listener, position - 1);
        }

        // This is how we use Picasso to load images from the internet.
        //Picasso.with(mContext)
        //        .load("http://image.tmdb.org/t/p/w185/" + recipe.getPoster())
        //        .into(holder.imageView);
        System.out.println("Tried Picasso = " + position + ", ");
    }

    @Override
    public int getItemCount() {
        return (stepList == null) ? 0 : stepList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_INGREDIENT : TYPE_STEP;
    }

    public void setStepList(List<Step> stepList, List<Ingredient> ingredientList)
    {
        this.stepList.clear();
        this.stepList.addAll(stepList);

        this.ingredientList.clear();
        this.ingredientList.addAll(ingredientList);

        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtView;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtView = (TextView) itemView.findViewById(R.id.recipeStepView);
        }


        public void bind(final Step item, final OnItemClickListener listener, final int pos) {
            txtView.setText(item.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, pos);
                }
            });
        }

        public void bind(final List<Ingredient> item, final OnItemClickListener listener, final int pos) {
            txtView.setText("Ingredients");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item, pos);
                    System.out.println("Kinda");
                }
            });
        }

    }
}
