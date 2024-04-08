package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private Path savePath;
    public FileBackedTaskManager(Path savePath) {
        this.savePath = savePath;
    }

    public void save(Task task) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(savePath.toFile(), true))) {
            String taskData = String.format("%s,%s,%s,%s,%s,%s%n",
                    task.getTaskID(),
                    task.getType(),
                    task.getTitle(),
                    task.getCategory(),
                    task.getDescription(),
                    task.getEpicID());
            writer.write(taskData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSimpleTask(Task task) {
        super.addSimpleTask(task);
        save(task);
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(subtask);
    }

    @Override
    public Task getSimpleTask(int taskID) {
        Task task  = super.getSimpleTask(taskID);
        save(task);
        return task;
   }

    @Override
    public Epic getEpicTask(int taskID) {
        Epic epic =  super.getEpicTask(taskID);
        save(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int taskID) {
        Subtask subtask = super.getSubtask(taskID);
        save(subtask);
        return subtask;
    }

    @Override
    public void deleteSimpleTask(int taskID) {
        Task task = getSimpleTasks().get(taskID);
        save(task);
        super.deleteSimpleTask(taskID);
    }

    @Override
    public void deleteEpic(int taskID) {
        Epic epic = getEpicTasks().get(taskID);
        save(epic);
        super.deleteEpic(taskID);
    }

    @Override
    public void deleteSubtask(int taskID) {
        Subtask subtask = getSubtasks().get(taskID);
        save(subtask);
        super.deleteSubtask(taskID);
    }

    @Override
    public void updateSimpleTask(Task task) {
        super.updateSimpleTask(task);
        save(task);
    }

    @Override
    public void updateEpicTask(Epic epic) {
        super.updateEpicTask(epic);
        save(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save(subtask);
    }

    @Override
    public void deleteSimpleTasks() {
        for (Task task : getSimpleTasks()) {
            save(task);
        }
        super.deleteSimpleTasks();
    }

    @Override
    public void deleteEpicTasks() {
        for (Subtask subtask : getSubtasks()) {
            save(subtask);
        }
        for (Epic epic : getEpicTasks()) {
            save(epic);
        }
        super.deleteEpicTasks();
    }

    @Override
    public void deleteSubtasks() {
        for (Subtask subtask : getSubtasks()) {
            save(subtask);
        }
        super.deleteSubtasks();
    }
}
