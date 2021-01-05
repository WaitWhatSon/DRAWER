package pl.edu.pb.drawer;

import com.google.gson.annotations.SerializedName;

public class StockImage {
    @SerializedName("large")
    private String imageSmall;
    @SerializedName("large2x")
    private String imageOriginal;

    public String getImageSmall() {
        return imageSmall;
    }

    public void setImageSmall(String imageSmall) {
        this.imageSmall = imageSmall;
    }

    public String getImageOriginal() {
        return imageOriginal;
    }

    public void setImageOriginal(String imageOriginal) {
        this.imageOriginal = imageOriginal;
    }
}
