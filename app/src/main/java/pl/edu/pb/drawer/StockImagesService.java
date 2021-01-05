package pl.edu.pb.drawer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface StockImagesService {

    @Headers("Authorization: 563492ad6f917000010000016eaba3b08eed4580bf7181940fcd6401")
    @GET("v1/search")
    Call<StockImagesContainer> findImages(@Query("query") String query);
}
