package club.thatpetbff.android_recipes;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by rtom on 1/16/18.
 */

@Entity(indexes = {
        @Index(value = "_id", unique = true)
})

@Keep
public class Ingredient {

    Long recipeID;

    @Id
    Long _id;

    @SerializedName("quantity")
    Double quantity;

    @SerializedName("measure")
    String measure;

    @SerializedName("ingredient")
    String ingredient;

    public Ingredient(Long recipeID, Long _id, Double quantity, String measure, String ingredient) {
        this.recipeID = recipeID;
        this._id = _id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
