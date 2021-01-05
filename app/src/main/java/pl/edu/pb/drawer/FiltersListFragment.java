package pl.edu.pb.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import static pl.edu.pb.drawer.FilterFragment.newInstance;

public class FiltersListFragment extends Fragment {

    public static final String KEY_EXTRA_FILTER_ID = "extraFilterId";

    private RecyclerView recyclerView;
    private FilterAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filters_list_fragment, container, false);
        recyclerView = view.findViewById(R.id.filters_recycler_view);
        updateView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateView();
    }

    private void updateView() {
        FiltersStorage filtersStorage = FiltersStorage.getInstance();
        List<Filter> filters = filtersStorage.getTasks();

        if (adapter == null)
        {
            adapter = new FilterAdapter(filters);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            adapter.notifyDataSetChanged();
        }
    }

    private class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private Filter filter;

        public FilterHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.filter_single_fragment, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.filter_item_name);
        }

        public void bind(Filter filter) {
            this.filter = filter;
            nameTextView.setText(filter.getFilterName());
        }

        @Override
        public void onClick(View v) {

            FilterFragment filterFragment = newInstance(filter.getId());
            EditActivity.replaceFragment(filterFragment);
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {
        private List<Filter> filters;

        public FilterAdapter(List<Filter> filters) {
            this.filters = filters;
        }

        @NonNull
        @Override
        public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FilterHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return filters.size();
        }

        @Override
        public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
            Filter filter = filters.get(position);
            holder.bind(filter);

        }
    }
}

