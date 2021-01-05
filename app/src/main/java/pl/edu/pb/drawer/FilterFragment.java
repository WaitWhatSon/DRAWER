package pl.edu.pb.drawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

public class FilterFragment extends Fragment {

    public static final String ARG_FILTER_ID = "FILTER_id";
    private Filter filter;

    private TextView nameField;
    private Button apply_button;

    public static FilterFragment newInstance(UUID filterId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FILTER_ID, filterId);
        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setArguments(bundle);
        return filterFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID filterId = (UUID) getArguments().getSerializable(ARG_FILTER_ID);
        filter = FiltersStorage.getInstance().getFilter(filterId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_details_fragment, container, false);

        nameField = view.findViewById(R.id.filter_name);
        nameField.setText(filter.getFilterName());

        apply_button = view.findViewById(R.id.apply_button);
        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.fragmentManager.popBackStackImmediate();
            }
        });

        return view;
    }
}
