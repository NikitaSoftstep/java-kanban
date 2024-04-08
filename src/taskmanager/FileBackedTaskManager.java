package taskmanager;

import category.TaskCategory;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskTypes;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    private final File savePath;

    public FileBackedTaskManager(File savePath) throws IOException {
        this.savePath = savePath;
        loadMaps(savePath);
        File historyPath = new File("src/resources/history.csv");
        loadHistory(historyPath);
    }

    private void save() throws IOException {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(savePath))) {
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
        }
    }

    private void historyToString(HistoryManager manager) throws IOException {
        List<Task> tasks = new ArrayList<>(manager.getHistory());
        List<Integer> ids = new ArrayList<>();
        for (Task task : tasks) {
            ids.add(task.getTaskID());
        }

        try (BufferedWriter br1 = new BufferedWriter(new FileWriter("src/resources/history.csv"))) {
            for (Integer id : ids) {
                br1.write(id + "\n");
            }
        }
    }

    private void loadMaps(File file) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file)));

        for (String line : lines) {
            String[] split = line.split(",");
            String id = split[0];
            String type = split[1];
            String title = split[2];
            String category = split[3];
            String description = split[4];
            String epicID = split.length == 6 ? split[5] : "0";

            if (type.equals(TaskTypes.TASK.toString())) {
                Task task = new Task(title, description);
                task.setTaskID(Integer.parseInt(id));
                task.setType(TaskTypes.valueOf(type));
                task.setCategory(TaskCategory.valueOf(category));
                tasks.put(Integer.parseInt(id), task);
            } else if (type.equals(TaskTypes.EPIC.toString())) {
                Epic epic = new Epic(title, description);
                epic.setTaskID(Integer.parseInt(id));
                epic.setType(TaskTypes.valueOf(type));
                epic.setCategory(TaskCategory.valueOf(category));
                epics.put(Integer.parseInt(id), epic);
            } else if (type.equals(TaskTypes.SUBTASK.toString())) {
                Subtask subtask = new Subtask(title, description);
                subtask.setTaskID(Integer.parseInt(id));
                subtask.setType(TaskTypes.valueOf(type));
                subtask.setCategory(TaskCategory.valueOf(category));
                subtask.setEpicID(Integer.parseInt(epicID));
                subtasks.put(Integer.parseInt(id), subtask);
            }
        }
    }

    private void loadHistory(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        List<Integer> ids = new ArrayList<>();
        for (String line : lines) {
            ids.add(Integer.parseInt(line));
        }
        for (Integer id : ids) {
            if (getSimpleTask(id) != null && getSimpleTask(id).getClass() == Task.class) {
                history.add(getSimpleTask(id));
            } else if (getEpicTask(id) != null && getEpicTask(id).getClass() == Epic.class) {
                history.add(getEpicTask(id));
            } else if (getSubtask(id) != null && getSubtask(id).getClass() == Subtask.class) {
                history.add(getSubtask(id));
            }
        }
    }

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
        Task task = super.getSimpleTask(taskID);
        historyToString(history);
        return task;
    }

    @Override
    public Epic getEpicTask(int taskID) throws IOException {
        Epic epic = super.getEpicTask(taskID);
        historyToString(history);
        return epic;
    }

    @Override
    public Subtask getSubtask(int taskID) throws IOException {
        Subtask subtask = super.getSubtask(taskID);
        historyToString(history);
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
