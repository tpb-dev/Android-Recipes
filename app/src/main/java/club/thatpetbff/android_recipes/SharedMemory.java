package club.thatpetbff.android_recipes;

/**
 * Created by rtom on 2/1/18.
 */

public class SharedMemory {
    private static SharedMemory instance = null;
    Ingredient ingredient = null;

    protected SharedMemory() {
        // Exists only to defeat instantiation.
    }
    public static SharedMemory getInstance() {
        if(instance == null) {
            instance = new SharedMemory();
        }
        return instance;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
}
