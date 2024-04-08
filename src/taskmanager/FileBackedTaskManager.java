package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private String savePath;
    public FileBackedTaskManager(String savePath) {
        this.savePath = savePath;
    }

    public void save() throws IOException {

        BufferedWriter br = new BufferedWriter(new FileWriter(savePath));
        br.write("id,type,name,status,description,epic" + "\n");
        List<Task> taskList = new ArrayList<>(tasks.values());
        for (Task task : taskList) {
            br.write(task.toString() + "\n");
        }
        List<Epic> epicList = new ArrayList<>(epics.values());
        for (Epic epic : epicList) {
            br.write(epic.toString() + "\n");
        }
        List<Subtask> subtaskList = new ArrayList<>(subtasks.values());
        for (Subtask subtask : subtaskList) {
            br.write(subtask.toString() + "\n");
        }
        br.close();
    }

//    public TaskManager loadFromFile() {
//        // считываем содержимое файла
//        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Paths.get("src/resources/savedManager.csv"));
//        Task task = new Task();
//
//        tasks.put(task.getTaskID(), task);
//        return fileBackedTaskManager;
//    }

    @Override
    public void addSimpleTask(Task task) throws IOException {
        super.addSimpleTask(task);
        save();
    }

    @Override
    public void addEpicTask(Epic epic) throws IOException {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task getSimpleTask(int taskID) throws IOException {
        Task task  = super.getSimpleTask(taskID);
        save();
        return task;
   }

    @Override
    public Epic getEpicTask(int taskID) throws IOException {
        Epic epic =  super.getEpicTask(taskID);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int taskID) throws IOException {
        Subtask subtask = super.getSubtask(taskID);
        save();
        return subtask;
    }

    @Override
    public void deleteSimpleTask(int taskID) throws IOException {
        super.deleteSimpleTask(taskID);
        save();
    }

    @Override
    public void deleteEpic(int taskID) throws IOException {
        super.deleteEpic(taskID);
        save();
    }

    @Override
    public void deleteSubtask(int taskID) throws IOException {
        super.deleteSubtask(taskID);
        save();
    }

    @Override
    public void updateSimpleTask(Task task) throws IOException {
        super.updateSimpleTask(task);
        save();
    }

    @Override
    public void updateEpicTask(Epic epic) throws IOException {
        super.updateEpicTask(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSimpleTasks() throws IOException {
        super.deleteSimpleTasks();
        save();
    }

    @Override
    public void deleteEpicTasks() throws IOException {
        super.deleteEpicTasks();
        save();
    }

    @Override
    public void deleteSubtasks() throws IOException {
        super.deleteSubtasks();
        save();
    }
}
