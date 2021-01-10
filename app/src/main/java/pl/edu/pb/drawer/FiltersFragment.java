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

    double sauvola_ratio_var = 0;
    double sauvola_div_var = 0;

    double niblack_ratio_var = 0;
    double niblack_offsetC_var = 0;

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
                if(current_image != null)
                {
                    // WORKING GREYSCALE
                    for(int x = 0; x < current_image.getWidth(); x++)
                    {
                        for(int y = 0; y < current_image.getHeight(); y++)
                        {
                            int rgb = current_image.getPixel(x, y);
                            int A = Color.alpha(rgb);
                            int R = Color.red(rgb);
                            int G = Color.green(rgb);
                            int B = Color.blue(rgb);
                            B = (R + G + B)/3;
                            G = B;
                            R = G;
                            current_image.setPixel(x, y, Color.argb(A, R, G, B));
                        }
                    }
                }
            }
        });

        median_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_image != null)
                {
                    int[] window = new int[9];
                    Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);
                    // WORKING MEDIAN
                    for(int x = 1; x < current_image.getWidth()-1; x++)
                    {
                        for(int y = 1; y < current_image.getHeight()-1; y++)
                        {
                            int i = 0;
                            for(int xk = 0; xk < 3; xk++)
                            {
                                for(int yk = 0; yk < 3; yk++)
                                {
                                    window[i] = bitmap_copy.getPixel(x + xk - 1, y + yk - 1);
                                    i++;
                                }
                            }
                            Arrays.sort(window);
                            int rgb = window[5]; // median value
                            int A = Color.alpha(rgb);
                            int R = Color.red(rgb);
                            int G = Color.green(rgb);
                            int B = Color.blue(rgb);
                            current_image.setPixel(x, y, Color.argb(A, R, G, B));
                        }
                    }
                }
            }
        });

        sharpen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_image != null)
                {
                    int[][] window = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
                    Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);
                    // WORKING SHARPEN
                    for(int x = 1; x < current_image.getWidth()-1; x++)
                    {
                        for(int y = 1; y < current_image.getHeight()-1; y++)
                        {
                            int newPixelValueA = 0;
                            int newPixelValueR = 0;
                            int newPixelValueG = 0;
                            int newPixelValueB = 0;

                            for(int xk = -1; xk < 2; xk++)
                            {
                                for(int yk = -1; yk < 2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    newPixelValueA += window[xk+1][yk+1] * A;
                                    newPixelValueR += window[xk+1][yk+1] * R;
                                    newPixelValueG += window[xk+1][yk+1] * G;
                                    newPixelValueB += window[xk+1][yk+1] * B;
                                }
                            }

                            if(newPixelValueA > 255) { newPixelValueA = 255; }
                            if(newPixelValueR > 255) { newPixelValueR = 255; }
                            if(newPixelValueG > 255) { newPixelValueG = 255; }
                            if(newPixelValueB > 255) { newPixelValueB = 255; }

                            if(newPixelValueA < 0) { newPixelValueA = 0; }
                            if(newPixelValueR < 0) { newPixelValueR = 0; }
                            if(newPixelValueG < 0) { newPixelValueG = 0; }
                            if(newPixelValueB < 0) { newPixelValueB = 0; }

                            current_image.setPixel(x, y, Color.argb(newPixelValueA, newPixelValueR, newPixelValueG, newPixelValueB));
                        }
                    }
                }
            }
        });

        pixelize_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                if(current_image != null)
                {
                    Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);
                    int block_size = 3;
                    // WORKING PIXELIZE
                    for(int x = block_size/2; x < (current_image.getWidth()-block_size/2); x+=block_size)
                    {
                        for(int y = block_size/2; y < (current_image.getHeight()-block_size/2); y+=block_size)
                        {
                            int newPixelValueA = 0;
                            int newPixelValueR = 0;
                            int newPixelValueG = 0;
                            int newPixelValueB = 0;

                            for(int xk = -block_size/2; xk <= block_size/2; xk++)
                            {
                                for(int yk = -block_size/2; yk <= block_size/2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    newPixelValueA += A;
                                    newPixelValueR += R;
                                    newPixelValueG += G;
                                    newPixelValueB += B;
                                }
                            }

                            newPixelValueA /= block_size*block_size;
                            newPixelValueR /= block_size*block_size;
                            newPixelValueG /= block_size*block_size;
                            newPixelValueB /= block_size*block_size;

                            if(newPixelValueA > 255) { newPixelValueA = 255; }
                            if(newPixelValueR > 255) { newPixelValueR = 255; }
                            if(newPixelValueG > 255) { newPixelValueG = 255; }
                            if(newPixelValueB > 255) { newPixelValueB = 255; }

                            if(newPixelValueA < 0) { newPixelValueA = 0; }
                            if(newPixelValueR < 0) { newPixelValueR = 0; }
                            if(newPixelValueG < 0) { newPixelValueG = 0; }
                            if(newPixelValueB < 0) { newPixelValueB = 0; }

                            for(int xk = -block_size/2; xk <= block_size/2; xk++)
                            {
                                for(int yk = -block_size/2; yk <= block_size/2; yk++)
                                {
                                    current_image.setPixel(x+xk, y+yk, Color.argb(newPixelValueA, newPixelValueR, newPixelValueG, newPixelValueB));
                                }
                            }
                        }
                    }
                }
            }
        });

        niblack_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_image != null)
                {
                    Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);

                    // WORKING NIBLACK
                    for(int x = 1; x < (current_image.getWidth()-1); x++)
                    {
                        for(int y = 1; y < (current_image.getHeight()-1); y++)
                        {
                            double meanPixelValueA = 0.0;
                            double meanPixelValueR = 0.0;
                            double meanPixelValueG = 0.0;
                            double meanPixelValueB = 0.0;

                            for(int xk = -1; xk < 2; xk++)
                            {
                                for(int yk = -1; yk < 2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    meanPixelValueA += A;
                                    meanPixelValueR += R;
                                    meanPixelValueG += G;
                                    meanPixelValueB += B;
                                }
                            }

                            meanPixelValueA /= 9;
                            meanPixelValueR /= 9;
                            meanPixelValueG /= 9;
                            meanPixelValueB /= 9;

                            double stdPixelValueA = 0.0;
                            double stdPixelValueR = 0.0;
                            double stdPixelValueG = 0.0;
                            double stdPixelValueB = 0.0;

                            for(int xk = -1; xk < 2; xk++)
                            {
                                for(int yk = -1; yk < 2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    stdPixelValueA += (A - meanPixelValueA) * (A - meanPixelValueA);
                                    stdPixelValueR += (R - meanPixelValueR) * (R - meanPixelValueR);
                                    stdPixelValueG += (G - meanPixelValueG) * (G - meanPixelValueG);
                                    stdPixelValueB += (B - meanPixelValueB) * (B - meanPixelValueB);
                                }
                            }

                            stdPixelValueA = Math.sqrt(stdPixelValueA/8);
                            stdPixelValueR = Math.sqrt(stdPixelValueR/8);
                            stdPixelValueG = Math.sqrt(stdPixelValueG/8);
                            stdPixelValueB = Math.sqrt(stdPixelValueB/8);

                            double resultPixelValueA = niblack_ratio_var * stdPixelValueA + meanPixelValueA + niblack_offsetC_var;
                            double resultPixelValueR = niblack_ratio_var * stdPixelValueR + meanPixelValueR + niblack_offsetC_var;
                            double resultPixelValueG = niblack_ratio_var * stdPixelValueG + meanPixelValueG + niblack_offsetC_var;
                            double resultPixelValueB = niblack_ratio_var * stdPixelValueB + meanPixelValueB + niblack_offsetC_var;

                            if(resultPixelValueA > 255) { resultPixelValueA = 255; }
                            if(resultPixelValueR > 255) { resultPixelValueR = 255; }
                            if(resultPixelValueG > 255) { resultPixelValueG = 255; }
                            if(resultPixelValueB > 255) { resultPixelValueB = 255; }

                            int rgb = bitmap_copy.getPixel(x, y);
                            int A = Color.alpha(rgb);
                            int R = Color.red(rgb);
                            int G = Color.green(rgb);
                            int B = Color.blue(rgb);

                            int newPixelValueA = 0;
                            int newPixelValueR = 0;
                            int newPixelValueG = 0;
                            int newPixelValueB = 0;

                            if( A >= resultPixelValueA ) { newPixelValueA = 255; }
                            if( R >= resultPixelValueR ) { newPixelValueR = 255; }
                            if( G >= resultPixelValueG ) { newPixelValueG = 255; }
                            if( B >= resultPixelValueB ) { newPixelValueB = 255; }

                            current_image.setPixel(x, y, Color.argb(newPixelValueA, newPixelValueR, newPixelValueG, newPixelValueB));
                        }
                    }
                }
            }
        });

        sauvola_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_image != null)
                {
                    Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);

                    // WORKING SAUVOLA
                    for(int x = 1; x < (current_image.getWidth()-1); x++)
                    {
                        for(int y = 1; y < (current_image.getHeight()-1); y++)
                        {
                            double meanPixelValueA = 0.0;
                            double meanPixelValueR = 0.0;
                            double meanPixelValueG = 0.0;
                            double meanPixelValueB = 0.0;

                            for(int xk = -1; xk < 2; xk++)
                            {
                                for(int yk = -1; yk < 2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    meanPixelValueA += A;
                                    meanPixelValueR += R;
                                    meanPixelValueG += G;
                                    meanPixelValueB += B;
                                }
                            }

                            meanPixelValueA /= 9;
                            meanPixelValueR /= 9;
                            meanPixelValueG /= 9;
                            meanPixelValueB /= 9;

                            double stdPixelValueA = 0.0;
                            double stdPixelValueR = 0.0;
                            double stdPixelValueG = 0.0;
                            double stdPixelValueB = 0.0;

                            for(int xk = -1; xk < 2; xk++)
                            {
                                for(int yk = -1; yk < 2; yk++)
                                {
                                    int rgb = bitmap_copy.getPixel(x+xk, y+yk);
                                    int A = Color.alpha(rgb);
                                    int R = Color.red(rgb);
                                    int G = Color.green(rgb);
                                    int B = Color.blue(rgb);

                                    stdPixelValueA += (A - meanPixelValueA) * (A - meanPixelValueA);
                                    stdPixelValueR += (R - meanPixelValueR) * (R - meanPixelValueR);
                                    stdPixelValueG += (G - meanPixelValueG) * (G - meanPixelValueG);
                                    stdPixelValueB += (B - meanPixelValueB) * (B - meanPixelValueB);
                                }
                            }

                            stdPixelValueA = Math.sqrt(stdPixelValueA/8);
                            stdPixelValueR = Math.sqrt(stdPixelValueR/8);
                            stdPixelValueG = Math.sqrt(stdPixelValueG/8);
                            stdPixelValueB = Math.sqrt(stdPixelValueB/8);

                            double resultPixelValueA = meanPixelValueA * (1 + sauvola_ratio_var * (stdPixelValueA / sauvola_div_var - 1));
                            double resultPixelValueR = meanPixelValueR * (1 + sauvola_ratio_var * (stdPixelValueR / sauvola_div_var - 1));
                            double resultPixelValueG = meanPixelValueG * (1 + sauvola_ratio_var * (stdPixelValueG / sauvola_div_var - 1));
                            double resultPixelValueB = meanPixelValueB * (1 + sauvola_ratio_var * (stdPixelValueB / sauvola_div_var - 1));

                            if(resultPixelValueA > 255) { resultPixelValueA = 255; }
                            if(resultPixelValueR > 255) { resultPixelValueR = 255; }
                            if(resultPixelValueG > 255) { resultPixelValueG = 255; }
                            if(resultPixelValueB > 255) { resultPixelValueB = 255; }

                            int rgb = bitmap_copy.getPixel(x, y);
                            int A = Color.alpha(rgb);
                            int R = Color.red(rgb);
                            int G = Color.green(rgb);
                            int B = Color.blue(rgb);

                            int newPixelValueA = 0;
                            int newPixelValueR = 0;
                            int newPixelValueG = 0;
                            int newPixelValueB = 0;

                            if( A >= resultPixelValueA ) { newPixelValueA = 255; }
                            if( R >= resultPixelValueR ) { newPixelValueR = 255; }
                            if( G >= resultPixelValueG ) { newPixelValueG = 255; }
                            if( B >= resultPixelValueB ) { newPixelValueB = 255; }

                            current_image.setPixel(x, y, Color.argb(newPixelValueA, newPixelValueR, newPixelValueG, newPixelValueB));
                        }
                    }
                }
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

