package pl.edu.pb.drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FiltersFragment extends Fragment {

    ImageView image_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_fragment, container, false);

        View rootView = inflater.inflate(R.layout.filters_fragment, container, false);
        image_view = rootView.findViewById(R.id.temp);

        image_view.setImageBitmap(EditActivity.current_photo);

        return view;
    }


}

