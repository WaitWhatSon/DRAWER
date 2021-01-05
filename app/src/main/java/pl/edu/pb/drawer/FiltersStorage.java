package pl.edu.pb.drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FiltersStorage {

    private static FiltersStorage taskStorage = new FiltersStorage();
    private List<Filter> filters;

    private FiltersStorage() {
        filters = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Filter filter = new Filter();
            filter.setFilterName("Filter #" + i);
            filters.add(filter);
        }
    }

    public static FiltersStorage getInstance() {
        if (taskStorage == null)
        {
            taskStorage = new FiltersStorage();
        }
        return taskStorage;
    }

    public List<Filter> getTasks() {
        return filters;
    }

    public Filter getFilter(UUID id) {

        for (Filter task : filters) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Filter filter) {
        filters.add(filter);
    }

}
