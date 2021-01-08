package pl.edu.pb.drawer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FiltersFragment extends Fragment {

    ImageView image_view;
    Button greyscale_button;
    Button median_button;
    Button sharpen_button;
    Button pixelize_button;
    Button niblack_button;
    Button sauvola_button;

    private Bitmap current_image;

    public FiltersFragment(Bitmap bitmap) {
        this.current_image = bitmap;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_fragment, container, false);
        image_view = view.findViewById(R.id.temp);
        greyscale_button = view.findViewById(R.id.greyscale_button);
        median_button = view.findViewById(R.id.median_button);
        sharpen_button = view.findViewById(R.id.sharpen_button);
        pixelize_button = view.findViewById(R.id.pixelize_button);
        niblack_button = view.findViewById(R.id.niblack_button);
        sauvola_button = view.findViewById(R.id.sauvola_button);

        image_view.setImageBitmap(current_image);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
}

