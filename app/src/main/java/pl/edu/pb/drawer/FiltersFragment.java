package pl.edu.pb.drawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.Arrays;


public class FiltersFragment extends Fragment {

    //ImageView image_view;
    Button greyscale_button;
    Button median_button;
    Button sharpen_button;
    Button pixelize_button;
    Button niblack_button;
    Button sauvola_button;
    SeekBar niblack_ratio;
    SeekBar niblack_offsetC;
    SeekBar sauvola_ratio;
    SeekBar sauvola_div;

    int block_size = 3;

    double niblack_ratio_var = 0;
    double niblack_offsetC_var = 0;

    double sauvola_ratio_var = 0;
    double sauvola_div_var = 0;

    private Bitmap current_image;

    public FiltersFragment(Bitmap bitmap) {
        this.current_image = bitmap;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_fragment, container, false);
        //image_view = view.findViewById(R.id.temp);
        //image_view.setImageBitmap(current_image);

        greyscale_button = view.findViewById(R.id.greyscale_button);
        median_button = view.findViewById(R.id.median_button);
        sharpen_button = view.findViewById(R.id.sharpen_button);
        pixelize_button = view.findViewById(R.id.pixelize_button);
        niblack_button = view.findViewById(R.id.niblack_button);
        sauvola_button = view.findViewById(R.id.sauvola_button);
        niblack_ratio = view.findViewById(R.id.niblack_ratio);
        niblack_offsetC = view.findViewById(R.id.niblack_offsetC);
        sauvola_ratio = view.findViewById(R.id.sauvola_ratio);
        sauvola_div = view.findViewById(R.id.sauvola_div);

        greyscale_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersLibrary.GreyscaleFilter(current_image);
            }
        });

        median_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersLibrary.MedianFilter(current_image);
            }
        });

        sharpen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersLibrary.SharpenFilter(current_image);
            }
        });

        pixelize_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                FiltersLibrary.PixelizeFilter(current_image, block_size);
            }
        });

        niblack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersLibrary.NiblackFilter(current_image, niblack_ratio_var, niblack_offsetC_var);
            }
        });

        sauvola_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FiltersLibrary.SauvolaFilter(current_image, sauvola_ratio_var, sauvola_div_var);
            }
        });

        niblack_ratio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                niblack_ratio_var = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        niblack_offsetC.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                niblack_offsetC_var = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        sauvola_ratio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sauvola_ratio_var = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        sauvola_div.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sauvola_div_var = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

