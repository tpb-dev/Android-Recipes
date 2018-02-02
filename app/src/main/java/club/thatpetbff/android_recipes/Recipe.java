package club.thatpetbff.android_recipes;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by rtom on 1/16/18.
 */

@Entity

public class Recipe {

    @SerializedName("id")
    @Index(unique = true)
    @Id
    Long id;

    @SerializedName("name")
    String name;

    @SerializedName("ingredients")
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "recipeID")
    })
    List<Ingredient> ingredients;

    @SerializedName("steps")
    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "recipeID")
    })
    List<Step> steps;

    @SerializedName("servings")
    Integer servings;

    @SerializedName("image")
    String image;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1947830398)
    private transient RecipeDao myDao;

    @Generated(hash = 819892187)
    public Recipe(Long id, String name, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    @Generated(hash = 829032493)
    public Recipe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 183837919)
    public synchronized void resetIngredients() {
        ingredients = null;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1662411893)
    public synchronized void resetSteps() {
        steps = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1726009037)
    public List<Ingredient> getIngredients() {
        if (ingredients == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            IngredientDao targetDao = daoSession.getIngredientDao();
            List<Ingredient> ingredientsNew = targetDao._queryRecipe_Ingredients(id);
            synchronized (this) {
                if (ingredients == null) {
                    ingredients = ingredientsNew;
                }
            }
        }
        return ingredients;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 590738072)
    public List<Step> getSteps() {
        if (steps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StepDao targetDao = daoSession.getStepDao();
            List<Step> stepsNew = targetDao._queryRecipe_Steps(id);
            synchronized (this) {
                if (steps == null) {
                    steps = stepsNew;
                }
            }
        }
        return steps;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1484851246)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */

}

