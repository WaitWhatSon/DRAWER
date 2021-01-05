package pl.edu.pb.drawer;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockImagesContainer {
    @SerializedName("photos")
    private List<StockImagesPack> imagesList;

    public List<StockImagesPack> getImagesList() {
        return imagesList;
    }

    public StockImagesPack getImagesPack() {
        return imagesList.get(0);
    }

    public void setImagesList(List<pl.edu.pb.drawer.StockImagesPack> imagesList) {
        this.imagesList = imagesList;
    }
}
