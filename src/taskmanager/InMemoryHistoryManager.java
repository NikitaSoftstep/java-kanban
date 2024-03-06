package taskmanager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> calledTasks = new LinkedList<>();
    public static final int MAX_HISTORY_SIZE = 10;
    @Override
    public void add(Task task) {
        if (calledTasks.size() >= MAX_HISTORY_SIZE) {
            calledTasks.removeFirst();
        }
        calledTasks.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(calledTasks);
    }

    public int getMaxHistorySize() {
        return MAX_HISTORY_SIZE;
    }
}
