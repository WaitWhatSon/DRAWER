package pl.edu.pb.drawer;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

public class FiltersLibrary {

    static Bitmap GreyscaleFilter(Bitmap current_image)
    {
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
        return current_image;
    }

    static Bitmap MedianFilter(Bitmap current_image)
    {
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
        return current_image;
    }

    static Bitmap SharpenFilter(Bitmap current_image)
    {
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
        return current_image;
    }

    static Bitmap PixelizeFilter(Bitmap current_image, int block_size)
    {
        if(current_image != null)
        {
            Bitmap bitmap_copy = current_image.copy(current_image.getConfig(), true);
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
        return current_image;
    }

    static Bitmap NiblackFilter(Bitmap current_image, double niblack_ratio_var, double niblack_offsetC_var)
    {
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
        return current_image;
    }

    static Bitmap SauvolaFilter(Bitmap current_image, double sauvola_ratio_var, double sauvola_div_var)
    {
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
        return current_image;
    }
}
