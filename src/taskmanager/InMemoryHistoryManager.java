package taskmanager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> calledTasks = new ArrayList<>();
    public static final int maxHistorySize = 10;
    @Override
    public void add(Task task) {
        if (calledTasks.size() >= maxHistorySize) {
            calledTasks.remove(0);
        }
        calledTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return calledTasks;
    }

    public int getMaxHistorySize() {
        return maxHistorySize;
    }
}
