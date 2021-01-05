package pl.edu.pb.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_images);

        RecyclerView recyclerView = findViewById(R.id.images_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ImageAdapter adapter = new ImageAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        Button open_in_browser = findViewById(R.id.open_in_browser);
        open_in_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.pexels.com/";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                // Verify that the intent will resolve to an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Here we use an intent without a Chooser unlike the next example
                    startActivity(intent);
                }
            }
        });
    }

    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imagePreview;
        private TextView photoAuthor;
        private TextView id;

        private StockImagesPack currentImage;

        public ImageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.image_list_item, parent, false));
            itemView.setOnClickListener(this);

            imagePreview = itemView.findViewById(R.id.image_small_preview);
            photoAuthor = itemView.findViewById(R.id.photographer_text_view);
            photoAuthor = itemView.findViewById(R.id.id);
        }

        public void bind(StockImagesPack image) {
            if (image != null)
            {
                Log.d("HEHE", "IF NIE NULL");
                currentImage = image;
                if (image.getImages().getImageSmall() != null) {
                    Picasso.with(itemView.getContext())
                            .load(image.getImages().getImageSmall())
                            .placeholder(R.drawable.temp_image).into(imagePreview);
                }
            }
            else
                Log.d("HEHE", "SUPRAJS");
        }

        @Override
        public void onClick(View v) {
            if (currentImage.getImages().getImageOriginal() != null) {
                Picasso.with(itemView.getContext())
                        .load(currentImage.getImages().getImageOriginal())
                        .placeholder(R.drawable.temp_image).into(imagePreview);
            }

            if (currentImage.getImages().getImageOriginal() != null) {
                Picasso.with(getApplicationContext()).load(currentImage.getImages().getImageOriginal()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        // path to /data/data/yourapp/app_data/imageDir
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        // Create imageDir
                        File my_path = new File(directory,"image.jpg");

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(my_path);
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                assert fos != null;
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
            }

            Intent intent = new Intent(StockImagesActivity.this, EditActivity.class);
            startActivity(intent);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<StockImagesPack> images;

        @NonNull
        @Override
        public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImageHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            if (images != null) {
                StockImagesPack image = images.get(position);
                holder.bind(image);
            }
            else {
                Log.d("ImagesActivity", "No images");
            }
        }

        @Override
        public int getItemCount() {
            if (images != null)
            {
                return images.size();
            }
            else {
                return 0;
            }
        }

        void setImages(List<StockImagesPack> images) {
            this.images = images;
            notifyDataSetChanged();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        searchItem.setVisible(true);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchImagesData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void setupImagesListView(List<StockImagesPack> images) {
        RecyclerView recyclerView = findViewById(R.id.images_recycler_view);
        final ImageAdapter adapter = new ImageAdapter();
        adapter.setImages(images);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fetchImagesData(String query) {
        String finalQuery = prepareQuery(query);
        StockImagesService imagesService = RetrofitInstance.getRetrofitInstance().create(StockImagesService.class);

        Call<StockImagesContainer> imagesApiCall = imagesService.findImages(finalQuery);
        Log.d("QUERY", finalQuery);

        imagesApiCall.enqueue(new Callback<StockImagesContainer>() {
            @Override
            public void onResponse(@NonNull Call<StockImagesContainer> call, @NonNull Response<StockImagesContainer> response) {
                setupImagesListView(Objects.requireNonNull(response).body().getImagesList());
            }

            @Override
            public void onFailure(@NonNull Call<StockImagesContainer> call, Throwable t) {
                Log.d("EXCEPTION", t.getMessage());
                Snackbar.make(findViewById(R.id.activity_stock_images), "Something went wrong... Please try later!",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        /*Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Response<StockImagesContainer> response = imagesApiCall.execute();
                    Log.d("RESPONSE", response.body().getImagesList().get(0).getImages().getImageOriginal());
                }
                catch (Exception e)
                {
                    Log.d("EXCEPTION", e.getMessage());
                };
            }
        });
        thread.start();*/
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }
}


