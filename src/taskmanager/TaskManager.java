package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addSimpleTask(Task task);

    void addEpicTask(Epic epic);

    void addSubtask(Subtask subtask);

    Task getSimpleTask(int taskID);

    Epic getEpicTask(int taskID);

    Subtask getSubtask(int taskID);

    void deleteSimpleTask(int taskID);

    void deleteEpic(int taskID);

    void deleteSubtask(int taskID);

    void updateSimpleTask(Task task);

    void updateEpicTask(Epic epic);

    void updateSubtask(Subtask subtask);

    ArrayList<Task> getSimpleTasks();

    ArrayList<Epic> getEpicTasks();

    ArrayList<Subtask> getSubtasks();

    void deleteSimpleTasks();

    void deleteEpicTasks();

    void deleteSubtasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    void removeIfPresentFromPriority(Task task);

    boolean checkTimeAndDuration(Task task);

    boolean isNotOverlapping(Task newTask);
}
