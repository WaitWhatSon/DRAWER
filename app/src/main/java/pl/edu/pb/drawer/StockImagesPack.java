package pl.edu.pb.drawer;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static java.lang.Math.min;

public class StockImagesPack {

    @SerializedName("src")
    private StockImage images;

    public StockImage getImages() {
        return images;
    }

}
