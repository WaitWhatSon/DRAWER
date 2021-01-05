package pl.edu.pb.drawer;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static java.lang.Math.min;

public class StockImagesPack {

    @SerializedName("src")
    private StockImage images;

    @SerializedName("photographer")
    private String photographer;

    public String getPhotographer() {
        return photographer;
    }

    public StockImage getImages() {
        return images;
    }

}
