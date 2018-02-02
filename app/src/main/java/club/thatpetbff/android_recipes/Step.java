package club.thatpetbff.android_recipes;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by rtom on 1/16/18.
 */


@Entity
public class Step {

    Long recipeID;

    @SerializedName("id")
    Long id;

    @SerializedName("shortDescription")
    String shortDescription;

    @SerializedName("description")
    String description;

    @SerializedName("videoURL")
    String videoURL;

    @SerializedName("thumbnailURL")
    String thumbnailURL;

    @Generated(hash = 422768817)
    public Step(Long recipeID, Long id, String shortDescription, String description,
            String videoURL, String thumbnailURL) {
        this.recipeID = recipeID;
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    @Generated(hash = 561308863)
    public Step() {
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

}
