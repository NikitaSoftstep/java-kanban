package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task addSimpleTask(Task task);

    Epic addEpicTask(Epic epic);

    Subtask addSubtask(Subtask subtask);

    Task getSimpleTask(int taskID);

    Epic getEpicTask(int taskID);

    Subtask getSubtask(int taskID);

    void deleteSimpleTask(int taskID);

    void deleteEpic(int taskID);

    void deleteSubtask(int taskID);

    Task updateSimpleTask(Task task);

    Epic updateEpicTask(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    ArrayList<Task> getSimpleTasks();

    ArrayList<Epic> getEpicTasks();

    ArrayList<Subtask> getEpicSubtasks(int epicID);

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
