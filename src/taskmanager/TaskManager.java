package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addSimpleTask(Task task) throws IOException;

    void addEpicTask(Epic epic) throws IOException;

    void addSubtask(Subtask subtask) throws IOException;

    Task getSimpleTask(int taskID) throws IOException;

    Epic getEpicTask(int taskID) throws IOException;

    Subtask getSubtask(int taskID) throws IOException;

    void deleteSimpleTask(int taskID) throws IOException;

    void deleteEpic(int taskID) throws IOException;

    void deleteSubtask(int taskID) throws IOException;

    void updateSimpleTask(Task task) throws IOException;

    void updateEpicTask(Epic epic) throws IOException;

    void updateSubtask(Subtask subtask) throws IOException;

    ArrayList<Task> getSimpleTasks();

    ArrayList<Epic> getEpicTasks();

    ArrayList<Subtask> getSubtasks();

    void deleteSimpleTasks() throws IOException;

    void deleteEpicTasks() throws IOException;

    void deleteSubtasks() throws IOException;

    List<Task> getHistory();
}
